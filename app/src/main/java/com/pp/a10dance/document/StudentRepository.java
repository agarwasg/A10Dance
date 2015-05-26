package com.pp.a10dance.document;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.View;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.model.Student;

/**
 * Created by saketagarwal on 4/4/15.
 */
public class StudentRepository extends BaseRepository {

    private static final String VIEW_NAME = "student";
    private static final String DOC_TYPE = "student";
    private static final String TAG = LogUtils.getTag(StudentRepository.class);
    public static final String NAME = "name";
    public static final String PROF_CLASS_ID = "profClassId";

    public static final String PHONE = "studentPhone";
    public static final String EMAIL = "studentEmail";
    public static final String ROLL = "student_roll_number";
    private AndroidContext context;

    public StudentRepository(AndroidContext context) {
        super(context);
    }

    /**
     * Save new student
     * 
     * @param student
     * @return the saved Student with its ID
     */
    public Student save(Student student) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(
                ProfClass.DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        student.setCreatedAt(currentTimeString);
        Map<String, Object> objectMap = objectMapper.convertValue(student,
                Map.class);
        objectMap.put(TYPE, DOC_TYPE);
        Document document = database.createDocument();
        try {
            document.putProperties(objectMap);
            student.set_id(document.getId());
            student.set_rev(document.getCurrentRevisionId());
            Log.d(TAG, "Created student doc: %s", document.getId());
            return student;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "error while saving student");
        }
        return student;
    }

    /**
     * Update a student with new fields
     * 
     * @param student
     * @return The updated student or null if the student does not exist
     */
    public Student update(Student student) {
        Document document = database.getExistingDocument(student.get_id());
        if (document == null) {
            return null;
        }
        Map<String, Object> updatedProperties = new HashMap<String, Object>();
        updatedProperties.putAll(document.getProperties());
        Map<String, Object> objectMap = objectMapper.convertValue(student,
                Map.class);
        updatedProperties.putAll(objectMap);
        try {
            document.putProperties(updatedProperties);
            student.set_rev(document.getCurrentRevisionId());
        } catch (CouchbaseLiteException e) {
            android.util.Log.d("CBL", "error updating event", e);
            e.printStackTrace();
        }
        return student;

    }

    /**
     * Gets a student with prticular id
     *
     * @param id
     * @return Student with given id or null if student does not exist
     */
    public Student get(String id) {
        Document document = database.getExistingDocument(id);
        if (document == null) {
            return null;
        }
        Log.d("Saket", "document is *********** "
                + document.getProperties().toString());
        Student student = objectMapper.convertValue(document.getProperties(),
                Student.class);
        return student;
    }

    /**
     * Checks if an student exists with given id
     * 
     * @param id
     * @return true if exits false if not
     */
    public boolean exists(String id) {
        Document document = database.getExistingDocument(id);
        if (document == null) {
            return false;
        }
        return true;
    }

    /**
     * Delete a student
     * 
     * @param s
     * @return True if successful
     */
    public boolean delete(Student s) {
        Document document = database.getExistingDocument(s.get_id());
        if (document == null) {
            return false;
        }
        try {
            return document.delete();
        } catch (CouchbaseLiteException e) {
            android.util.Log.e("CBL", "error deleting document", e);
            e.printStackTrace();
            return false;
        }
    }

    public static Query getQuery(Database database, String profClassId) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {

                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get(TYPE))) {
                        List<Object> keys = new ArrayList<>();
                        keys.add(document.get(PROF_CLASS_ID));
                        keys.add(document.get(CREATED_AT));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(map, "1");
        }
        // only return student for current class
        Query query = view.createQuery();
        query.setDescending(true);
        java.util.List<Object> startKeys = new ArrayList<Object>();
        startKeys.add(profClassId);
        startKeys.add(new HashMap<String, Object>());

        java.util.List<Object> endKeys = new ArrayList<Object>();
        endKeys.add(profClassId);

        query.setStartKey(startKeys);
        query.setEndKey(endKeys);
        return query;
    }
}
