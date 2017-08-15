package com.example.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.android.Model.Database.TaskDb;
import com.example.android.Model.ITask;
import com.example.android.simpletodoapprevised.R;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;


public class AddEditDialogFragment extends DialogFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODE = "Mode";
    private static final String ARG_TASK_OBJECT = "param2";

    public static final String MODE_NEW = "New";
    public static final String MODE_EDIT = "Edit";


    private String mModeStr;
    private ITask mTask = null;

    // Controls
    private EditText mEditTextTitle;
    private Spinner mSpinnerPriority;
    private DatePicker mPickerDueDate;
    private CheckBox mCheckBoxIsComplete;
    private Button mBtnSave;

    private OnAddEditFragmentInteractionListener mListener;

    public AddEditDialogFragment() {
        // Required empty public constructor
    }

    public static AddEditDialogFragment newInstance(String mode, TaskDb taskObject) {
        AddEditDialogFragment fragment = new AddEditDialogFragment();
        Bundle args = new Bundle();
        args.putString(ARG_MODE, mode );
        if( taskObject != null )
            args.putSerializable(ARG_TASK_OBJECT, taskObject);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mModeStr = getArguments().getString(ARG_MODE);
            Serializable taskDb = getArguments().getSerializable(ARG_TASK_OBJECT);
            if( ( taskDb != null) && ( taskDb instanceof ITask)){
                mTask = (ITask) taskDb;
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditTextTitle = (EditText)view.findViewById( R.id.title);
        mSpinnerPriority = (Spinner) view.findViewById( R.id.priority);
        mPickerDueDate = (DatePicker) view.findViewById( R.id.duedate);
        mCheckBoxIsComplete = (CheckBox)view.findViewById( R.id.is_complete);
        mBtnSave = (Button)view.findViewById( R.id.save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Dialog Fragment.
                onSave();

            }
        });

        mEditTextTitle.requestFocus();
        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        // Load all data in the Add/Edit dialog.
        if ( ( mModeStr.equalsIgnoreCase( AddEditDialogFragment.MODE_EDIT)) && (mTask != null)){
            // load all data from task object
            mEditTextTitle.setText( mTask.getTitle());
            if( mTask.getPriority() != null){
                switch( mTask.getPriority()){
                    case "Low":
                        mSpinnerPriority.setSelection(0);
                        break;
                    case "High":
                        mSpinnerPriority.setSelection(2);
                        break;
                    default:
                        mSpinnerPriority.setSelection(1);
                        break;
                }
            }
            else
                mSpinnerPriority.setSelection(1); // default when null.

            setDueDate( mTask.getDue());
        }
        else {
            initControls();
        }


    }

    private void initControls(){
        initDueDate();
        // set priority to Normal
        mSpinnerPriority.setSelection(1);
    }

    private void initDueDate(){
        // set DatePicker to current date
        Date today = new Date( System.currentTimeMillis());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( today );
        mPickerDueDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDueDate( Date dueDate){
        if( dueDate != null) {
            //convert to a time stamp and update the DatePicker
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dueDate);
            mPickerDueDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        else
            initDueDate();
    }

    // TODO: Rename method, update argument and hook method into UI event
    private void  onSave(  ) {
        //TaskDb task = new TaskDb();
        if (mListener != null) {
            TaskDb taskDb = new TaskDb();
            taskDb.setTitle( mEditTextTitle.getText().toString());
            taskDb.setPriority( mSpinnerPriority.getSelectedItem().toString() ); ;
            //Set date in milliseconds
            Calendar dueDate = Calendar.getInstance();

            dueDate.set(Calendar.DAY_OF_MONTH, mPickerDueDate.getDayOfMonth());
            dueDate.set(Calendar.MONTH, mPickerDueDate.getMonth());
            dueDate.set(Calendar.YEAR, mPickerDueDate.getYear());

            taskDb.setDueDate( dueDate.getTime() );

            mListener.onFinishAddEditDialog( (ITask) taskDb );
        }
        dismiss();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddEditFragmentInteractionListener) {
            mListener = (OnAddEditFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnAddEditFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /* This interface must be implemented by activities / fragments that
     * expect to receive data/ completion status from this fragment after invocation.
     */
    public interface OnAddEditFragmentInteractionListener {
        void onFinishAddEditDialog(ITask task);
    }
}
