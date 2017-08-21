package com.example.android.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.Activity.MainActivity;
import com.example.android.Adapter.TaskItemTouchHelperCallback;
import com.example.android.Adapter.TasksAdapter;
import com.example.android.Interface.IFragmentActivityCallback;
import com.example.android.Interface.ITask;
import com.example.android.Model.Database.TaskDb;
import com.example.android.simpletodoapprevised.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.Activity.MainActivity.FILTER_KEY;

//import android.support.annotation.Nullable;


public class TodayFragment extends Fragment implements MainActivity.IFragmentCommunication ,
                                                        TaskItemTouchHelperCallback.TaskItemTouchListener ,
                                                        AddEditDialogFragment.OnAddEditFragmentInteractionListener ,
        TaskItemTouchHelperCallback.ITaskAdapterCallback {

    private static final String TAG = "TodayFragment";
    public static final String KEY_POSITION = "Position";

    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ItemTouchHelper mItemTouchHelper;

    private TasksAdapter mAdapter;
    private View mEmptyView;
    private ActionMode mCurrentActionMode;
    private String mFilter;
    private IFragmentActivityCallback mActivityCallback;
    private boolean mSearchMode = false;

    public TodayFragment( ) {
        // Required empty public constructor
    }

    public static TodayFragment newInstance(String filter ) {
        TodayFragment fragment = new TodayFragment();

        Bundle args = new Bundle();
        args.putString( FILTER_KEY , filter );

        fragment.setArguments( args );
        return fragment;
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
        mAdapter = new TasksAdapter(getActivity(), null , this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        mySearchUpdateOperation();
                    }
                }
        );

        TaskItemTouchHelperCallback taskItemTouchHelperCallback = new TaskItemTouchHelperCallback(  this , getActivity().getApplicationContext());
        mItemTouchHelper = new ItemTouchHelper(taskItemTouchHelperCallback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        mEmptyView = (View) rootView.findViewById(R.id.empty);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String filter = getArguments().getString(FILTER_KEY);
        mFilter = filter;
        if( mAdapter != null)
            mAdapter.setFilter( mFilter );
    }

    @Override
    public void onResume() {
        super.onResume();
        mSearchMode = false;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle( getTitle());
        actionBar.setDisplayHomeAsUpEnabled( true );
        fetchData();
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
    }

    // Will be called by the parent activity when data has been refreshed from other active fragments. Will rarely be used, as we are syncing the adapter OnResume().
    @Override
    public void refreshData(  ) {
        fetchData();
    }

    // will be called by the parent activity when search query has been submitted.
    @Override
    public void searchTasks(String tasksQuery) {
        if(!searchQuery( tasksQuery ) ){
            Toast.makeText(getContext(), getString( R.string.no_tasks), Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(getContext(), getString( R.string.swipe_down), Toast.LENGTH_SHORT).show();
        mSearchMode = true;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        String title = getTitle() + getString( R.string.search_results);
        actionBar.setTitle( title );
        actionBar.setDisplayHomeAsUpEnabled( true );

    }

    // Item -Touch handler interface implementation.
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
        if (source.getItemViewType() != target.getItemViewType()) {
            return false;
        }
        final int sourcePos = source.getAdapterPosition();
        final int destPos = target.getAdapterPosition();
        if( mAdapter.onMove( sourcePos , destPos) ) {
            // update the data model
            mActivityCallback.taskUpdated( mAdapter.getItem( sourcePos ));
            mActivityCallback.taskUpdated( mAdapter.getItem( destPos ));
            checkIfEmpty();
            return true;
        }
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int position = viewHolder.getAdapterPosition();
        if( direction == ItemTouchHelper.RIGHT){
            // launch edit dialog.
           editTask( position );

        } else if ( direction == ItemTouchHelper.LEFT){
            deleteTask( position );
        }
        checkIfEmpty();
    }

    @Override
    public void onFinishAddEditDialog(ITask task, String mode, int position) {
        if( (task == null) || (position == -1) ){
            return;
        }
        if( mode.equalsIgnoreCase( AddEditDialogFragment.MODE_EDIT) ){
            mAdapter.onUpdated( position , task );
            mActivityCallback.taskUpdated( task );
        }
        checkIfEmpty();
    }

    private int mItemPosition = 0;
    // Long clicks on an item will invoke the action Mode.
    @Override
    public boolean onLongClick(View view , int position) {
        if (mCurrentActionMode != null) { return false; }
        mItemPosition = position;
        ((AppCompatActivity) getActivity()).startSupportActionMode(mActionModeCallback);
        view.setSelected(true);
        return true;
    }

    private int getPosition(){
        return mItemPosition;
    }
    private void editTask( int position ){
        TaskDb taskDB = null;
        ITask task = mAdapter.getItem( position );
        if( task instanceof TaskDb){
            taskDB = (TaskDb)task;
        }
        AddEditDialogFragment addEditDialogFragment = AddEditDialogFragment.newInstance( AddEditDialogFragment.MODE_EDIT , taskDB );
        addEditDialogFragment.getArguments().putInt( KEY_POSITION , position);
        addEditDialogFragment.setTargetFragment( TodayFragment.this , 0);
        addEditDialogFragment.show( getActivity().getSupportFragmentManager(), "fragment_edit_task");
    }

    private void deleteTask( int position ){
        // notify adapters of delete
        ITask taskDeleted = mAdapter.getItem( position );
        mAdapter.onDelete( position );
        mActivityCallback.taskDeleted( taskDeleted );
        checkIfEmpty();
    }

    private void fetchData(){
        List<ITask> tasks;
        // Get the latest data.
        if (mActivityCallback != null) {
            tasks = mActivityCallback.getAllTasks( mFilter );
        }
        else
            tasks = new ArrayList<>();

        mAdapter.reloadTasks( tasks );
        mAdapter.notifyDataSetChanged();

        checkIfEmpty();
   }

    private void checkIfEmpty() {
        if (mEmptyView != null && mAdapter != null) {
            int itemCount = mAdapter.getItemCount();
            final boolean emptyViewVisible =
                    ( itemCount == 0);
            if( mSearchMode ){
                mySearchUpdateOperation();
                return;
            }
            mEmptyView.setVisibility(emptyViewVisible ? View.VISIBLE : View.GONE);
            mRecyclerView.setVisibility(emptyViewVisible ? View.GONE : View.VISIBLE);
        }
    }

    private boolean searchQuery( String query){
        if( mAdapter != null )
            return mAdapter.onSearchQuery( query );

        return false;
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

    private void mySearchUpdateOperation(){
        mSearchMode = false;
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setTitle( getTitle());
        actionBar.setDisplayHomeAsUpEnabled( true );
        fetchData();
        mSwipeRefreshLayout.setRefreshing( false );
    }

    // For adapters to implement
    public interface ItemTouchAdapterCallback{
        public boolean onMove(int sourcePos , int destPos );
        public void onDelete(int position);
        public void onUpdated(int position, ITask task);
    }



    // Define the callback when ActionMode is activated
    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            mode.setTitle("Actions");
            mode.getMenuInflater().inflate(R.menu.actions_task_item_row, menu);
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_edit:
                    //Toast.makeText(getContext(), "Editing!", Toast.LENGTH_SHORT).show();
                    editTask( getPosition() );
                    mode.finish(); // Action picked, so close the contextual menu
                    return true;
                case R.id.menu_delete:
                    deleteTask( getPosition() );
                    Toast.makeText(getContext(), getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the contextual menu
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mCurrentActionMode = null; // Clear current action mode
            ((AppCompatActivity)getActivity()).getSupportActionBar().show();

        }

    };


}




