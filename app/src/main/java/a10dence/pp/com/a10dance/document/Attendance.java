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
public class Attendance {

    private static final String VIEW_NAME = "attendance";
    private static final String DOC_TYPE = "attendance";
    private static final String TAG = LogUtils.getTag(Attendance.class);


    public static Document createNewAttendance(Database database,
                                            String profClassId,String studentId) throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(ProfClass.DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(ProfClass.TYPE, DOC_TYPE);
        properties.put("prof_class_id", profClassId);
        properties.put("student_id", studentId);
        properties.put("created_at", currentTimeString);
        Document document = database.createDocument();
        document.putProperties(properties);
        Log.d(TAG, "Created attendance doc: %s", document.getId());
        return document;

    }

    public static void deleteAttendance(Document attendance) throws CouchbaseLiteException {
        attendance.delete();
        Log.d(TAG, "Deleted attendance: %s", attendance.getId());
    }

}
