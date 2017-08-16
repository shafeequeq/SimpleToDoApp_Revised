package com.example.android.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.Fragment.TodayFragment;
import com.example.android.Model.Database.DatabaseInitializer;
import com.example.android.Model.ITask;
import com.example.android.simpletodoapprevised.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by shafe on 8/7/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> implements TodayFragment.ItemTouchAdapterCallback {
    private Context mContext;
    private List<ITask> mTasks;

    private static final String PRIORITY_HIGH = "High";
    private static final String PRIORITY_NORMAL = "Normal";
    private static final String PRIORITY_LOW = "Low";

    private static final Date START_OF_DAY = DatabaseInitializer.GetTodayStartofDay();
    private static final Date END_OF_DAY = DatabaseInitializer.GetTodayEndofDay();
    private TaskItemTouchHelperCallback.ITaskAdapterCallback mTaskAdapterCallback = null;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView mTaskTitle, mTaskDueDate;
        public ImageView mImgView;
        //public LinearLayout taskContainer;

        public MyViewHolder(View view) {
            super(view);
            mTaskTitle = (TextView) view.findViewById(R.id.txt_tasktitle);
           // mTaskNotes = (TextView) view.findViewById(R.id.txt_notes);
            //taskDueDesc = (TextView) view.findViewById(R.id.txt_due_string);
            mTaskDueDate = (TextView) view.findViewById(R.id.txt_due_date);
            mImgView = (ImageView) view.findViewById(R.id.priority);

            //taskContainer = (LinearLayout) view.findViewById(R.id.task_container);
            //  view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            //listener.onRowLongClicked(getAdapterPosition());
            if( mTaskAdapterCallback != null){
                mTaskAdapterCallback.onLongClick( view , getAdapterPosition() );

                int color = getContext().getResources().getColor( R.color.dark_grey );
                view.setBackgroundColor( color );
            }
            return true;
        }
    }


    public TasksAdapter(Context mContext, List<ITask> tasks , TaskItemTouchHelperCallback.ITaskAdapterCallback taskAdapterCallback) {
        this.mContext = mContext;
        this.mTasks = tasks;
        this.mTaskAdapterCallback = taskAdapterCallback;
    }

    public void setFilter(String filter) {
    }

    private Context getContext() {
        return mContext;
    }

    public void reloadTasks(List<ITask> tasks) {
        mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View taskView = inflater.inflate(R.layout.task_list_row, parent, false);

        // Return a new holder instance
        MyViewHolder viewHolder = new MyViewHolder(taskView);
        taskView.setOnLongClickListener( viewHolder );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ITask task = mTasks.get(position);

        // set all text views.
        holder.mTaskTitle.setText(task.getTitle());
       // holder.mTaskNotes.setText(task.getNotes());


        Date dueDate = task.getDue();
        Date compareDate = START_OF_DAY;


        if (dueDate != null) {
            String dueString;
            if (dueDate.compareTo(START_OF_DAY) < 0) {
                // Mark OverDue
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
                dueString = getContext().getString(R.string.due_on)
                        + " : " + sdf.format(dueDate);
                holder.mTaskDueDate.setTextColor(Color.RED);
            } else if (dueDate.compareTo(END_OF_DAY) <= 0) {
                // Show time of day when this is due.
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                dueString = getContext().getString(R.string.due_today)
                        + " : " + sdf.format(dueDate);
                holder.mTaskDueDate.setTextColor(Color.BLUE);
            } else {
                // Show time of day when this is due.
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d, ''yy");
                dueString = getContext().getString(R.string.due_on)
                        + " : " + sdf.format(dueDate);
                holder.mTaskDueDate.setTextColor(Color.BLUE);
            }
            holder.mTaskDueDate.setText(dueString);
        }

        holder.mImgView.setImageResource(R.drawable.ic_priority_normal);
        String priority = task.getPriority();
        if( priority != null) {
            if (priority.equalsIgnoreCase(PRIORITY_HIGH)) {
                holder.mImgView.setImageResource(R.drawable.ic_priority_high);
            } else if (priority.equalsIgnoreCase(PRIORITY_LOW)) {
                holder.mImgView.setImageResource(R.drawable.ic_priority_high);
            }
        }

    }
    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public ITask getItem( int position){
        if (mTasks != null)
            return mTasks.get( position );
        return null;
    }

    @Override
    public boolean onMove(int sourcePos , int destPos) {
        Collections.swap( mTasks , sourcePos , destPos);
        notifyItemMoved( sourcePos , destPos );
        return true;
    }

    @Override
    public void onSwiped(int position, int direction) {
        // TODO when swipe RIGHT edit Item.

        mTasks.remove( position );
        notifyItemRemoved( position );
    }

    public void onUpdated(int position, ITask task){
        mTasks.set( position , task);
        notifyItemChanged( position );
    }

    public boolean onSearchQuery( String query ){
        List<ITask> searchList = searchTasks( query );
        if(  ( searchList != null )&& ( searchList.size() > 0)) {
            mTasks = searchList;
            notifyDataSetChanged();
            return true;
        }
        return false;
    }

    private List<ITask> searchTasks( String searchTitle ){
        ArrayList<ITask> list = new ArrayList<>();
        searchTitle = searchTitle.toLowerCase();
        for( ITask task : mTasks){
            String title = task.getTitle().toLowerCase();
            if (( title != null )&&( title.contains( searchTitle ))){
                list.add( task );
            }
        }
        return list;
    }


}

