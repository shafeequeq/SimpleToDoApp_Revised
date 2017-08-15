package com.example.android.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.Fragment.TodayFragment;
import com.example.android.Helper.DatabaseInitializer;
import com.example.android.Model.ITask;
import com.example.android.simpletodoapprevised.R;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by shafe on 8/7/2017.
 */

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.MyViewHolder> implements TodayFragment.ItemTouchAdapterCallback{
    private Context mContext;
    private List<ITask> mTasks;

    private static final Date START_OF_DAY = DatabaseInitializer.GetTodayStartofDay();

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        public TextView taskTitle, taskNotes , taskDueDesc , taskDueDate;
        //public LinearLayout taskContainer;

        public MyViewHolder(View view) {
            super(view);
            taskTitle = (TextView) view.findViewById(R.id.txt_tasktitle);
            taskNotes = (TextView) view.findViewById(R.id.txt_notes);
            //taskDueDesc = (TextView) view.findViewById(R.id.txt_due_string);
            taskDueDate = (TextView) view.findViewById(R.id.txt_due_date);

            //taskContainer = (LinearLayout) view.findViewById(R.id.task_container);
          //  view.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View view) {
            //listener.onRowLongClicked(getAdapterPosition());
            return true;
        }
    }



    public TasksAdapter(Context mContext, List<ITask> tasks ) {
        this.mContext = mContext;
        this.mTasks = tasks;


    }

    private Context getContext(){
        return mContext;
    }

    public void reloadTasks( List<ITask> tasks ){
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
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        ITask task = mTasks.get( position );

        // set all text views.
        holder.taskTitle.setText( task.getTitle());
        holder.taskNotes.setText( task.getNotes());


        Date dueDate = task.getDue();
        if( dueDate != null ) {
            String dueString;
            if( dueDate.compareTo( START_OF_DAY ) < 0 ){
                // Mark OverDue
                dueString = getContext().getString(R.string.overdue);
                holder.taskDueDate.setTextColor(Color.RED);
            }
            else {
                // Show time of day when this is due.
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                dueString = getContext().getString(R.string.due_date)
                            + " : " + sdf.format(dueDate);
            }
            holder.taskDueDate.setText( dueString );
        }

        // apply click events
       // applyClickEvents(holder, position);
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



}

