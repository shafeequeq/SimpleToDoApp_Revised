package com.example.android.Activity;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.android.Fragment.AddEditDialogFragment;
import com.example.android.Interface.IFragmentActivityCallback;
import com.example.android.Fragment.TodayFragment;
import com.example.android.Model.Database.DatabaseInitializer;
import com.example.android.Model.Database.TaskDatabase;
import com.example.android.Model.Database.TaskDb;
import com.example.android.Interface.ITask;
import com.example.android.simpletodoapprevised.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , IFragmentActivityCallback , AddEditDialogFragment.OnAddEditFragmentInteractionListener {
    private RecyclerView recyclerView;
    private DrawerLayout mDrawerLayout;
    private TaskDatabase mDB;
    public static final String FILTER_KEY = "FILTER";
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // avoid scrolling of toolbar.
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(0);  // clear all scroll flags
        toolbar.setLayoutParams( params );


        // Create Navigation drawer and inlfate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            VectorDrawableCompat indicator
                    = VectorDrawableCompat.create(getResources(), R.drawable.ic_menu, getTheme());
            indicator.setTint(ResourcesCompat.getColor(getResources(),R.color.white,getTheme()));
            supportActionBar.setHomeAsUpIndicator(indicator);
            supportActionBar.setDisplayHomeAsUpEnabled(true);

        }

        // Set behavior of Navigation drawer
        navigationView.setNavigationItemSelectedListener( this ) ;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Dialog Fragment.
                launchAddEditDialogFragment();

            }
        });

        // setup initial fragment
        if (findViewById(R.id.fragment_container) != null) {

            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            initFragment();
        }
        dbSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        final SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Notify the fragments about the search query.
                notifySearchQuery(query);
                // Reset SearchView
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Set item in checked state
        menuItem.setChecked(true);
        int id = menuItem.getItemId();

        OnFragmentSelected( id );

        // Closing drawer on item click
        mDrawerLayout.closeDrawers();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == R.id.menu_refresh ){
            // TODO : show warning message to user, that his current data will be deleted. Take consent before loading.
            if( mDB != null ){
                DatabaseInitializer.populateSyncFromJSON( mDB , this );

                notifyDataRefresh(  );
            }
            // Reload test data from DB.
            return true;
        }
        else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public List<ITask> getAllTasks(String filter) {
        List<ITask> tasks = new ArrayList<>();

        if( ( filter != null) &&( mDB != null)){
            if( filter.equalsIgnoreCase( FilterData.FILTER_TODAY ) ){
                Date before = DatabaseInitializer.GetTodayEndofDay(  );
                List<TaskDb> list  = mDB.taskModel().loadTasksDueBy( before );
                tasks.addAll( list );
            }
            else if( filter.equalsIgnoreCase( FilterData.FILTER_ALL)){
                List<TaskDb> list  = mDB.taskModel().loadAllTasks();
                tasks.addAll( list );
            }
            else if( filter.equalsIgnoreCase( FilterData.FILTER_NEXT_SEVEN_DAYS )){
                Date start = DatabaseInitializer.GetTodayStartofDay(  );
                Date end = DatabaseInitializer.GetTodayPlusDays( 7 );
                List<TaskDb> list  = mDB.taskModel().loadTasksDueBetween( start , end );
                tasks.addAll( list );
            }

        }

        return tasks;
    }

    @Override
    public void onFinishAddEditDialog(ITask task, String mode, int position) {
        if( ( task != null)&&( mode.equalsIgnoreCase( AddEditDialogFragment.MODE_NEW))){
            taskAdded( task );
            notifyDataRefresh();
        }
    }

     /*
    Data Model Add / Update methods. can later be moved to a specific data model class.
     */

    @Override
    public void taskAdded(ITask task) {
        if( mDB != null){
            if( task instanceof TaskDb ){
                try {
                    mDB.taskModel().insertTask((TaskDb) task);
                }
                catch (Exception e){
                    Log.e(TAG , e.getMessage());
                }
            }
        }
    }

    @Override
    public void taskUpdated(ITask task) {
        if( mDB != null){
            if( task instanceof TaskDb ){
                try {
                    mDB.taskModel().updateTasks((TaskDb) task);

                }
                catch (Exception e){
                    Log.e(TAG , e.getMessage());
                }
                finally {
                }
            }
        }
    }

    @Override
    public void taskDeleted(ITask task) {
        if( mDB != null){
            if( task instanceof TaskDb ){
                try {
                    mDB.taskModel().deleteTask((TaskDb) task);
                }
                catch (Exception e){
                    Log.e(TAG , e.getMessage());
                }
            }
        }
    }

    private void initFragment(){

        TodayFragment firstFragment = TodayFragment.newInstance( FilterData.FILTER_TODAY );

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, firstFragment).commit();
    }

    private void OnFragmentSelected( int id){
        String filter = FilterData.FILTER_ALL;
        Fragment fragment = null;
        //Class fragmentClass = null;
        if (id == R.id.nav_view_today) {
            filter = FilterData.FILTER_TODAY;
        } else if (id == R.id.nav_view_inbox) {
            filter = FilterData.FILTER_ALL;
        } else if (id == R.id.nav_view_week) {
            filter = FilterData.FILTER_NEXT_SEVEN_DAYS;
        }
        try {
            //Class fragmentClass = TodayFragment.class;
            fragment = new TodayFragment();
            Bundle args = new Bundle();
            args.putString( FILTER_KEY , filter);
            fragment.setArguments( args );
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment);
        // add to back stack
        transaction.addToBackStack( null );
        transaction.commit();
   }

    private void launchAddEditDialogFragment(){
        AddEditDialogFragment addEditDialogFragment = AddEditDialogFragment.newInstance( AddEditDialogFragment.MODE_NEW , null );
        try {
            addEditDialogFragment.show(getSupportFragmentManager(), "fragment_add_task");
        }
        catch(Exception e){
            Log.e( TAG , e.getMessage());
        }
    }

    private void notifyDataRefresh(  ){

        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for( Fragment fragment : fragments){
            if ((fragment.isVisible()) && ( fragment instanceof MainActivity.IFragmentCommunication)){

                ((IFragmentCommunication)fragment).refreshData(  );
            }
        }
    }

    private void notifySearchQuery( String searchQuery){
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for( Fragment fragment : fragments){
            if ((fragment.isVisible()) && ( fragment instanceof MainActivity.IFragmentCommunication)){

                ((IFragmentCommunication)fragment).searchTasks( searchQuery );
            }
        }
    }

    private void dbSetup(){
        if( mDB == null){
            mDB = TaskDatabase.getDatabase( this );
        }
    }

    public interface IFragmentCommunication {
        public void refreshData();
        public void searchTasks( String tasksQuery );
    }

    public class FilterData {
        public static final String FILTER_TODAY = "Today";
        public static final String FILTER_ALL = "All";
        public static final String FILTER_NEXT_SEVEN_DAYS = "NEXT_SEVEN_DAYS";

        public final String mFilterString;

        public FilterData(String filterString) {
            this.mFilterString = filterString;
        }
    }


}