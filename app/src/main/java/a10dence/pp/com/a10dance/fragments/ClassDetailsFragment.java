package a10dence.pp.com.a10dance.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import a10dence.pp.com.a10dance.R;
import a10dence.pp.com.a10dance.activity.StudentListActivity;
import a10dence.pp.com.a10dance.view.GenericCard;

/**
 * Created by saketagarwal on 4/5/15.
 */
public class ClassDetailsFragment extends Fragment {

    private static final String CLASS_ID_ARGS = "class_id";
    private GenericCard mStudentCard;
    private String mClassId;


    public static ClassDetailsFragment createInstance(String classId) {
        ClassDetailsFragment classDetailsFragment = new ClassDetailsFragment();
        Bundle args = new Bundle();
        args.putString(CLASS_ID_ARGS, classId);
        classDetailsFragment.setArguments(args);
        return classDetailsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mClassId = getArguments().getString(CLASS_ID_ARGS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.class_details, container,
                false);
        mStudentCard = (GenericCard) view.findViewById(R.id.student_container);
        Button studentCallToAction = (Button) mStudentCard.findViewById(R.id.call_to_action_button);
        studentCallToAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = StudentListActivity.createInstance(getActivity(), mClassId);
                startActivity(intent);
            }
        });

        return view;
    }
}
