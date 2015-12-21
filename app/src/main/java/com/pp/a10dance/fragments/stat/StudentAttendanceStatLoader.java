package com.pp.a10dance.fragments.stat;

import java.util.ArrayList;
import java.util.List;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.QueryRow;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.document.ProfClassRepository;
import com.pp.a10dance.document.StudentAttendanceRepository;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.model.Student;
import com.pp.a10dance.model.StudentAttendance;

public class StudentAttendanceStatLoader extends
        AsyncTaskLoader<List<StudentAttendanceStatViewModel>> {

    private final StudentAttendanceRepository studentAttendanceRepository;
    private final StudentRepository studentRepository;
    private final ProfClassRepository profClassRepository;
    private Context mContext;
    private String mProfClassId;

    public StudentAttendanceStatLoader(Context context, String profClassId) {
        super(context);
        mContext = context;
        mProfClassId = profClassId;
        studentAttendanceRepository = new StudentAttendanceRepository(
                new AndroidContext(context));
        studentRepository = new StudentRepository(new AndroidContext(context));
        profClassRepository = new ProfClassRepository(new AndroidContext(
                context));
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
        cancelLoad();
    }

    @Override
    public List<StudentAttendanceStatViewModel> loadInBackground() {
        try {
            QueryEnumerator studentsInClassQueryEnumerator = studentRepository
                    .getStudentInClassQuery(mProfClassId).run();
            List<StudentAttendanceStatViewModel> studentAttendanceStatViewModelList = new ArrayList<>();
            while (studentsInClassQueryEnumerator.hasNext()) {
                QueryRow studentRow = studentsInClassQueryEnumerator.next();
                Student student = studentRepository.documentToObject(
                        studentRow.getDocument(), Student.class);
                StudentAttendanceStatViewModel studentAttendanceStatViewModel = new StudentAttendanceStatViewModel();
                studentAttendanceStatViewModel.name = student.getName();
                studentAttendanceStatViewModel.studentId = student.get_id();
                studentAttendanceStatViewModel.email = student.getEmail();
                studentAttendanceStatViewModel.phone = student.getPhone();
                studentAttendanceStatViewModel.roll = student.getRoll();
                studentAttendanceStatViewModel.createdAt = student
                        .getCreatedAt();

                Query query = studentAttendanceRepository
                        .getByClassIdStudentIdQuery(student.get_id(),
                                mProfClassId);
                List<StudentAttendance> studentAttendanceArrayList = new ArrayList<>();
                QueryEnumerator queryEnumerator = query.run();
                if (queryEnumerator != null) {
                    while (queryEnumerator.hasNext()) {
                        studentAttendanceArrayList
                                .add(studentAttendanceRepository
                                        .documentToObject(queryEnumerator
                                                .next().getDocument(),
                                                StudentAttendance.class));

                    }
                }
                studentAttendanceStatViewModel.attendanceRecord = studentAttendanceArrayList;
                studentAttendanceStatViewModelList
                        .add(studentAttendanceStatViewModel);
            }
            return studentAttendanceStatViewModelList;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        return null;
    }
}
