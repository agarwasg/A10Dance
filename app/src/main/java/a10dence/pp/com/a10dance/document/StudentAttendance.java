package a10dence.pp.com.a10dance.document;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import a10dence.pp.com.a10dance.helper.LogUtils;

public class StudentAttendance {
    private static final String VIEW_NAME = "student_attendance";
    private static final String DOC_TYPE = "student_attendance";
    private static final String TAG = LogUtils.getTag(StudentAttendance.class);


    public static Document createNewStudentAttendance(Database database,
                                               String profClassId,String studentId,String attendanceId,boolean isPresent) throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ProfClass.DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProfClass.TYPE, DOC_TYPE);
        properties.put("prof_class_id", profClassId);
        properties.put("student_id", studentId);
        properties.put("created_at", currentTimeString);
        properties.put("attendance_id",attendanceId);
        Document document = database.createDocument();
        document.putProperties(properties);
        Log.d(TAG, "Created new student attendance doc: %s", document.getId());
        return document;

    }



}
