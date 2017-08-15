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
import com.example.android.Helper.TaskParser;
import com.example.android.Model.Database.TaskDb;
import com.example.android.Model.GoogleTasksAPI.TaskListsGoogle;
import com.example.android.Model.ITask;
import com.example.android.simpletodoapprevised.MainActivity;
import com.example.android.simpletodoapprevised.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.android.simpletodoapprevised.MainActivity.FILTER_KEY;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayFragment extends Fragment implements MainActivity.IFragmentCommunication , TaskItemTouchHelperCallback.TaskItemTouchListener , AddEditDialogFragment.OnAddEditFragmentInteractionListener {

    private static final String TAG = "TodayFragment";
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;

    private TasksAdapter mAdapter;
    private List<ITask> mTasks;
    private String mFilter;
    private IFragmentActivityCallback mActivityCallback;
    //private ActionModeCallback actionModeCallback;
    //private ActionMode actionMode;

    public TodayFragment( ) {
        // Required empty public constructor

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

    public static TodayFragment newInstance( String filter ) {
        TodayFragment fragment = new TodayFragment();

        Bundle args = new Bundle();
        args.putString( FILTER_KEY , filter );

        fragment.setArguments( args );
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String filter = getArguments().getString(FILTER_KEY);
        mFilter = filter;

        if (mActivityCallback != null) {
            mTasks = mActivityCallback.getAllTasks( mFilter );
        }
        else
            mTasks = new ArrayList<>();


    }

    @Override
    public void onResume() {
        super.onResume();
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle( getTitle());
        actionBar.setDisplayHomeAsUpEnabled( true );

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

    @Override
    public void onDataRefresh( List<ITask> newData ) {
         mTasks = newData;
         mAdapter.reloadTasks( mTasks );
         mAdapter.notifyDataSetChanged();
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
        mAdapter = new TasksAdapter(getActivity(), mTasks);
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

    private void myUpdateOperation(){
        mAdapter.reloadTasks( mTasks );
        mSwipeRefreshLayout.setRefreshing( false );

    }

    protected static TodayFragment newInstance( String param1, String param2 ) {

        Bundle args = new Bundle();

        TodayFragment fragment = new TodayFragment();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);

        fragment.setArguments(args);
        return fragment;
    }

    private static TaskDb createTask(String id , String title , String priority , String taskListID , String status , Date lastUpdated ){
        TaskDb taskDb = new TaskDb();
        taskDb.id = id;
        taskDb.title = title;
        taskDb.priority = priority;
        taskDb.taskListID = taskListID;
        taskDb.status = status;
        taskDb.lastUpdated = lastUpdated;

        return taskDb;
    }

    private static Date getTodayPlusDays(int daysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, daysAgo);
        return calendar.getTime();
    }



    private List<ITask> getTasksfromJSON(){
        TaskParser parser = new TaskParser("tasklists.json", "taskdata.json", this.getContext());
        TaskListsGoogle lists = parser.getTaskLists();
        return lists.getITasks();
    }

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
            ITask task = mTasks.get( position );
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


