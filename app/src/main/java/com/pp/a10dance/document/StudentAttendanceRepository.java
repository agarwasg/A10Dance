package com.pp.a10dance.document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.helper.Utils;
import com.pp.a10dance.model.StudentAttendance;

public class StudentAttendanceRepository extends BaseRepository {

    public static final String DOC_TYPE = "studentAttendance";
    public static final String VIEW_NAME = "studentAttendance_view";
    public static final String TAG = LogUtils
            .getTag(StudentAttendanceRepository.class);
    public static final String ATTENDANCE_ID = "attendanceId";
    public static final String STUDENT_ID = "studentId";

    public StudentAttendanceRepository(AndroidContext context) {
        super(context);
    }

    public StudentAttendance save(StudentAttendance studentAttendance) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        studentAttendance.setCreatedAt(currentTimeString);
        Map<String, Object> objectMap = objectMapper.convertValue(
                studentAttendance, Map.class);
        objectMap.put(TYPE, DOC_TYPE);
        Document document = database.createDocument();
        try {
            document.putProperties(objectMap);
            studentAttendance.set_id(document.getId());
            studentAttendance.set_rev(document.getCurrentRevisionId());
            Log.d(TAG, "Created student doc: %s", document.getId());
            return studentAttendance;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "error while saving student");
        }
        return studentAttendance;

    }

    public void save(List<StudentAttendance> studentAttendanceList) {
        for (StudentAttendance studentAttendance : studentAttendanceList) {
            save(studentAttendance);
        }
    }

    public StudentAttendance update(StudentAttendance studentAttendance) {
        Document document = database.getExistingDocument(studentAttendance
                .get_id());
        if (document == null) {
            return null;
        }
        Map<String, Object> updatedProperties = new HashMap<String, Object>();
        updatedProperties.putAll(document.getProperties());
        Map<String, Object> objectMap = objectMapper.convertValue(
                studentAttendance, Map.class);
        updatedProperties.putAll(objectMap);
        try {
            document.putProperties(updatedProperties);
            studentAttendance.set_rev(document.getCurrentRevisionId());
        } catch (CouchbaseLiteException e) {
            android.util.Log.d("CBL", "error updating event", e);
            e.printStackTrace();
        }
        return studentAttendance;

    }

    public StudentAttendance updateOrCreate(StudentAttendance studentAttendance) {
        StudentAttendance studentAttendance1 = update(studentAttendance);
        if (studentAttendance1 == null) {
            studentAttendance1 = save(studentAttendance);
        }
        return studentAttendance1;
    }

    /**
     * Gets a studentAttendance with id
     *
     * @param id
     * @return Student Attendance with given id or null if attendace does not
     *         exist
     */
    public StudentAttendance get(String id) {
        Document document = database.getExistingDocument(id);
        if (document == null) {
            return null;
        }
        StudentAttendance studentAttendance = objectMapper.convertValue(
                document.getProperties(), StudentAttendance.class);
        return studentAttendance;
    }

    public Query getByAttendanceIdStudentIdQuery(String attendanceId,
            String studentId) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {

                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get(TYPE))) {
                        List<Object> keys = new ArrayList<>();
                        keys.add(document.get(ATTENDANCE_ID));
                        keys.add(document.get(STUDENT_ID));
                        keys.add(document.get(CREATED_AT));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(map, "1");
        }
        // only return student for current class
        com.couchbase.lite.Query query = view.createQuery();
        query.setDescending(true);
        java.util.List<Object> startKeys = new ArrayList<Object>();
        startKeys.add(attendanceId);
        startKeys.add(studentId);
        startKeys.add(new HashMap<String, Object>());

        java.util.List<Object> endKeys = new ArrayList<Object>();
        endKeys.add(attendanceId);
        endKeys.add(studentId);

        query.setStartKey(startKeys);
        query.setEndKey(endKeys);
        return query;

    }

    public Query getByClassIdStudentIdQuery(String classId, String studentId) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {

                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get(TYPE))) {
                        StudentAttendance studentAttendance = objectMapper
                                .convertValue(document, StudentAttendance.class);
                        List<Object> keys = new ArrayList<>();
                        keys.add(studentAttendance.getStudentId());
                        keys.add(studentAttendance.getClassId());
                        keys.add(studentAttendance.getCreatedAt());
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(map, "1");
        }
        // only return student for current class
        com.couchbase.lite.Query query = view.createQuery();
        query.setDescending(true);
        java.util.List<Object> startKeys = new ArrayList<Object>();
        startKeys.add(classId);
        startKeys.add(studentId);
        startKeys.add(new HashMap<String, Object>());

        java.util.List<Object> endKeys = new ArrayList<Object>();
        endKeys.add(classId);
        endKeys.add(studentId);

        query.setStartKey(startKeys);
        query.setEndKey(endKeys);
        return query;

    }

    public boolean getCurrentAttendanceState(String studentId,
            String attendanceId) {
        if (Utils.StringUtils.isBlank(attendanceId)) {
            return true;
        }

        Query query = getByAttendanceIdStudentIdQuery(attendanceId, studentId);
        query.setLimit(1);
        try {
            QueryEnumerator result = query.run();
            StudentAttendance studentAttendance = get(result.getRow(0)
                    .getDocumentId());
            android.util.Log.d(TAG,
                    "Student Fragment is " + studentAttendance.toString());
            if (studentAttendance == null) {
                return true;
            }
            return studentAttendance.isPresent();
        } catch (CouchbaseLiteException e) {
            android.util.Log.e(TAG, e.getMessage(), e);
        }
        return true;

    }

}
