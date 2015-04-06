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

/**
 * Created by saketagarwal on 4/4/15.
 */
public class Student {

    private static final String VIEW_NAME = "student";
    private static final String DOC_TYPE = "student";
    private static final String TAG = LogUtils.getTag(Student.class);


    public static Document createNewStudent(Database database, String name,
                                              String profClassId) throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ProfClass.DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProfClass.TYPE, DOC_TYPE);
        properties.put("name", name);
        properties.put("prof_class_id", profClassId);
        properties.put("created_at", currentTimeString);
        Document document = database.createDocument();
        document.putProperties(properties);
        Log.d(TAG, "Created student doc: %s", document.getId());
        return document;

    }

    public static void deleteTask(Document task) throws CouchbaseLiteException {
        task.delete();
        Log.d(TAG, "Deleted doc: %s", task.getId());
    }

}
