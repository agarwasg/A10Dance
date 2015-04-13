package a10dence.pp.com.a10dance.document;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.View;
import com.couchbase.lite.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a10dence.pp.com.a10dance.helper.LogUtils;

/**
 * Created by saketagarwal on 4/4/15.
 */
public class Student {

    private static final String VIEW_NAME = "student";
    private static final String DOC_TYPE = "student";
    private static final String TAG = LogUtils.getTag(Student.class);
    public static final String NAME = "name";
    public static final String PROF_CLASS_ID = "prof_class_id";
    public static final String CREATED_AT = "created_at";
    public static final String PHONE = "student_phone";
    public static final String EMAIL = "student_email";
    public static final String ROLL = "student_roll_number";


    public static Document createNewStudent(Database database,
                                            String profClassId, String name, String rollNumber, String email, String phoneNumber) throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ProfClass.DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProfClass.TYPE, DOC_TYPE);
        properties.put(NAME, name);
        properties.put(PROF_CLASS_ID, profClassId);
        properties.put(CREATED_AT, currentTimeString);
        properties.put(ROLL, rollNumber);
        properties.put(EMAIL, email);
        properties.put(PHONE, phoneNumber);
        Document document = database.createDocument();
        document.putProperties(properties);
        Log.d(TAG, "Created student doc: %s", document.getId());
        return document;

    }

    public static void deleteTask(Document task) throws CouchbaseLiteException {
        task.delete();
        Log.d(TAG, "Deleted doc: %s", task.getId());
    }

    public static Query getQuery(Database database, String profClassId) {
        View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper map = new Mapper() {

                @Override
                public void map(Map<String, Object> document, Emitter emitter) {
                    if (DOC_TYPE.equals(document.get(ProfClass.TYPE))) {
                        List<Object> keys = new ArrayList<>();
                        keys.add(document.get(PROF_CLASS_ID));
                        keys.add(document.get(CREATED_AT));
                        emitter.emit(keys, document);
                    }
                }
            };
            view.setMap(map, "1");
        }
        //only return student for current class
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
