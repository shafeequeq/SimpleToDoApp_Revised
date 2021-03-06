package com.example.android.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.codetroopers.betterpickers.calendardatepicker.CalendarDatePickerDialogFragment;
import com.codetroopers.betterpickers.radialtimepicker.RadialTimePickerDialogFragment;
import com.example.android.Interface.ITask;
import com.example.android.Model.Database.TaskDb;
import com.example.android.simpletodoapprevised.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import static com.example.android.Fragment.TodayFragment.KEY_POSITION;

public class AddEditDialogFragment extends DialogFragment implements CalendarDatePickerDialogFragment.OnDateSetListener , RadialTimePickerDialogFragment.OnTimeSetListener {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_MODE = "Mode";
    private static final String ARG_TASK_OBJECT = "param2";

    public static final String MODE_NEW = "New";
    public static final String MODE_EDIT = "Edit";
    private static final String FRAG_TAG_DATE_PICKER = "fragment_date_picker_name";
    private static final String FRAG_TAG_TIME_PICKER = "timePickerDialogFragment";


    private String mModeStr;
    private int mPosition; // position in adapter initiating this.
    private ITask mTask = null;

    // Controls
    private EditText mEditTextTitle;
    private Spinner mSpinnerPriority;
    private View mImageButtonContainer;
    private TextView mTextViewDate;

    private Calendar mDueDate = null;

    private Button mBtnSave;
    private TextInputLayout mTaskTitleLabel = null;

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

            mPosition = getArguments().getInt( KEY_POSITION );

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
        mTextViewDate = (TextView)view.findViewById( R.id.textview_date);
        mSpinnerPriority = (Spinner) view.findViewById( R.id.priority);
        mImageButtonContainer = (View)view.findViewById( R.id.imageButtonContainer);
        mImageButtonContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditDateTime();
            }
        });
        mTaskTitleLabel = (TextInputLayout) view.findViewById(R.id.title_layout);
        setupTaskTitleLabelError();
        mBtnSave = (Button)view.findViewById( R.id.save);
        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launch Dialog Fragment.
                onSave();

            }
        });

        ArrayAdapter adapter = ArrayAdapter.createFromResource( getContext() , R.array.priority , R.layout.spinner_dropdown_item );
        mSpinnerPriority.setAdapter( adapter );

        mEditTextTitle.requestFocus();
        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        // Based on the mode of invocation load data ( Edit or New )
       loadData();

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

    @Override
    public void onDateSet(CalendarDatePickerDialogFragment dialog, int year, int monthOfYear, int dayOfMonth) {
        if( mDueDate == null)
            mDueDate = Calendar.getInstance();

        mDueDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        mDueDate.set(Calendar.MONTH, monthOfYear);
        mDueDate.set(Calendar.YEAR, year);
        updateTextViewDate();
        // launch time fragment.
        RadialTimePickerDialogFragment rtpd = new RadialTimePickerDialogFragment()
                .setOnTimeSetListener( this );
        if ( mDueDate == null ) initDueDate();
        rtpd.setStartTime( mDueDate.get( Calendar.HOUR_OF_DAY ),
                            mDueDate.get( Calendar.MINUTE) );
        rtpd.show( getActivity().getSupportFragmentManager(), FRAG_TAG_TIME_PICKER);
    }

    @Override
    public void onTimeSet(RadialTimePickerDialogFragment dialog, int hourOfDay, int minute) {
        if( mDueDate == null)
            mDueDate = Calendar.getInstance();

        mDueDate.set(Calendar.HOUR_OF_DAY, hourOfDay);
        mDueDate.set(Calendar.MINUTE, minute);
        updateTextViewDate();
    }

    private void onEditDateTime(){
        if( mDueDate == null) initDueDate();

        CalendarDatePickerDialogFragment cdp = new CalendarDatePickerDialogFragment()
                .setOnDateSetListener( this );
        cdp.setPreselectedDate( mDueDate.get( Calendar.YEAR ) ,
                mDueDate.get( Calendar.MONTH),
                mDueDate.get( Calendar.DAY_OF_MONTH));
        cdp.show( getActivity().getSupportFragmentManager(), FRAG_TAG_DATE_PICKER);
    }

    private void loadData(){
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



    private void setupTaskTitleLabelError() {
        if( mTaskTitleLabel != null){
            mTaskTitleLabel.getEditText().addTextChangedListener(new TextWatcher() {
                // ...
                @Override
                public void onTextChanged(CharSequence text, int start, int count, int after) {
                    if (text.length() > 0 ) {
                        mTaskTitleLabel.setHintEnabled(false);
                        mTaskTitleLabel.setErrorEnabled(false);
                    } else {
                        mTaskTitleLabel.setError(getString(R.string.title_required));
                        mTaskTitleLabel.setErrorEnabled(true);
                    }
                }
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }

    }

    private void initControls(){
        initDueDate();
        // set priority to Normal
        mSpinnerPriority.setSelection(1);
    }

    private void initDueDate(){
        // set DatePicker to current date
        mDueDate = Calendar.getInstance();
        updateTextViewDate();
    }

    private void setDueDate( Date dueDate){
        if( mDueDate == null){
            mDueDate = Calendar.getInstance();
        }
        if( dueDate != null)
            mDueDate.setTime( dueDate);
        updateTextViewDate();
    }

    private void updateTextViewDate(){
        if( mTextViewDate != null){
            SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d , h:mm a");

            mTextViewDate.setText( sdf.format( mDueDate.getTime() ).toString() );
        }
    }

    private boolean validateEntries(){
        String taskTitle = null;
        if( mEditTextTitle != null){
            taskTitle = mEditTextTitle.getText().toString();
            if (( taskTitle == null )||( taskTitle.isEmpty())){
                if ( mTaskTitleLabel != null ){
                    mTaskTitleLabel.setError(getString(R.string.title_required));
                    mTaskTitleLabel.setErrorEnabled(true);
                }
            }
            else
                return true;
        }
        return false;
    }

    private void  onSave(  ) {
       if( ! validateEntries())
           return;

        if( mTask == null){
            mTask = new TaskDb();
        }
        mTask.setTitle( mEditTextTitle.getText().toString());
        mTask.setPriority( mSpinnerPriority.getSelectedItem().toString() ); ;
        mTask.setDueDate( mDueDate.getTime() );
        if ( (mModeStr.equalsIgnoreCase(MODE_NEW)) && (mListener != null) ) {
            mTask.setID( UUID.randomUUID().toString() );
            // TODO : setTaskList ID.
            mTask.setTaskListID( "MDQxOTI0MzIxNTAzNjYwMDY5NDQ6MDow"); // currently hard-coded.
            mListener.onFinishAddEditDialog( (ITask) mTask , mModeStr , mPosition);
        }
        else if (mModeStr.equalsIgnoreCase(MODE_EDIT)){ // Item was right slided for update from the fragment.
            // send result to fragment directly.
            OnAddEditFragmentInteractionListener listener = (OnAddEditFragmentInteractionListener)getTargetFragment();
            if( listener != null){
                listener.onFinishAddEditDialog( (ITask) mTask , mModeStr , mPosition);
            }
        }
        dismiss();
    }

    /* This interface must be implemented by activities / fragments that
         * expect to receive data/ completion status from this fragment after invocation.
         */
    public interface OnAddEditFragmentInteractionListener {
        void onFinishAddEditDialog(ITask task , String mode , int position);
    }
}
