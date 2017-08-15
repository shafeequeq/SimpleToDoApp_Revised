package com.example.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.Adapter.TaskItemTouchHelperCallback;
import com.example.android.Adapter.TasksAdapter;
import com.example.android.Model.Database.TaskDb;
import com.example.android.Model.ITask;
import com.example.android.Activity.MainActivity;
import com.example.android.simpletodoapprevised.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.Activity.MainActivity.FILTER_KEY;



public class TodayFragment extends Fragment implements MainActivity.IFragmentCommunication , TaskItemTouchHelperCallback.TaskItemTouchListener , AddEditDialogFragment.OnAddEditFragmentInteractionListener {

    private static final String TAG = "TodayFragment";
    private String mParam1;
    private String mParam2;


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;

    private TasksAdapter mAdapter;

    private List<ITask> mInsertedTasks;
    private List<ITask> mDeletedTasks;
    private List<ITask> mUpdatedTasks;

    //private List<ITask> mTasks;
    private String mFilter;
    private IFragmentActivityCallback mActivityCallback;

    public TodayFragment( ) {
        // Required empty public constructor
        mInsertedTasks = new ArrayList<>();
        mDeletedTasks = new ArrayList<>();
        mUpdatedTasks = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_today, container, false);
        rootView.setTag(TAG);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view_today);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager( mLayoutManager );
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mAdapter = new TasksAdapter(getActivity(), null);
        mRecyclerView.setAdapter(mAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        myUpdateOperation();
                    }
                }
        );

        //actionModeCallback = new ActionModeCallback();
        TaskItemTouchHelperCallback taskItemTouchHelperCallback = new TaskItemTouchHelperCallback(  this );
        mItemTouchHelper = new ItemTouchHelper(taskItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filter = getArguments().getString(FILTER_KEY);
        mFilter = filter;
    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle( getTitle());
        actionBar.setDisplayHomeAsUpEnabled( true );
        List<ITask> tasks;

        refreshData();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentActivityCallback){
            this.mActivityCallback = (IFragmentActivityCallback) context;
        }
        else{
            throw new ClassCastException(context.toString()
                    + " must implement IFragmentActivityCallback");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivityCallback = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        // TODO persist data to database.
    }

    public static TodayFragment newInstance(String filter ) {
        TodayFragment fragment = new TodayFragment();

        Bundle args = new Bundle();
        args.putString( FILTER_KEY , filter );

        fragment.setArguments( args );
        return fragment;
    }

    private void refreshData(){
        List<ITask> tasks;
        // Get the latest data.
        if (mActivityCallback != null) {
            tasks = mActivityCallback.getAllTasks( mFilter );
        }
        else
            tasks = new ArrayList<>();

        mAdapter.reloadTasks( tasks );
        mAdapter.notifyDataSetChanged();

        clearPendingChanges();

    }

    private void clearPendingChanges(){
        mInsertedTasks.clear();
        mUpdatedTasks.clear();
        mDeletedTasks.clear();
    }
    private String getTitle(){
        String title = getString( R.string.inbox);
        if( mFilter != null ){
            if( mFilter.equalsIgnoreCase(MainActivity.FilterData.FILTER_TODAY)){
                title = getString( R.string.today);
            }
            else if ( mFilter.equalsIgnoreCase(MainActivity.FilterData.FILTER_NEXT_SEVEN_DAYS)){
                title = getString( R.string.sevenDays);
            }
        }
        return title;
    }

    // Will be called by the parent activity when data has been refreshed from other active fragments. Will rarely be used, as we are syncing the adapter OnResume().
    @Override
    public void onDataRefresh( List<ITask> newData ) {
         mAdapter.reloadTasks( newData );
         mAdapter.notifyDataSetChanged();
    }

    private void myUpdateOperation(){
        refreshData();
        mSwipeRefreshLayout.setRefreshing( false );
    }

    // Item -Touch handler interface implementation.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        final int sourcePos = source.getAdapterPosition();
        final int destPos = target.getAdapterPosition();
        return mAdapter.onMove( sourcePos , destPos);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if( direction == ItemTouchHelper.RIGHT){
            // launch edit dialog.
            TaskDb taskDB = null;
            ITask task = mAdapter.getItem( position );
            if( task instanceof TaskDb){
                taskDB = (TaskDb)task;
            }
            AddEditDialogFragment addEditDialogFragment = AddEditDialogFragment.newInstance( AddEditDialogFragment.MODE_EDIT , taskDB );
            addEditDialogFragment.show( getActivity().getSupportFragmentManager(), "fragment_edit_task");

        } else if ( direction == ItemTouchHelper.LEFT){
            // notify adapters of delete
            mAdapter.onSwiped( position , direction);
        }


    }

    // Called after the Add/Edit dialog has finished. Returns with the updated task.
    @Override
    public void onFinishAddEditDialog(ITask task) {
        // Updat
    }

    // For adapters to implement
    public interface ItemTouchAdapterCallback{
        public boolean onMove(int sourcePos , int destPos );
        public void onSwiped(int position, int direction);
    }

}


