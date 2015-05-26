package com.pp.a10dance.document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Document;
import com.couchbase.lite.android.AndroidContext;
import com.couchbase.lite.util.Log;
import com.pp.a10dance.helper.LogUtils;
import com.pp.a10dance.model.Attendance;

/**
 * Created by saketagarwal on 5/23/15.
 */
public class AttendanceRepository extends BaseRepository {

    private static final String DOC_TYPE = "attendance";
    private static final String TAG = LogUtils
            .getTag(AttendanceRepository.class);

    public AttendanceRepository(AndroidContext context) {
        super(context);
    }

    public Attendance save(Attendance attendance) {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        attendance.setDateTime(currentTimeString);
        Map<String, Object> objectMap = objectMapper.convertValue(attendance,
                Map.class);
        objectMap.put(TYPE, DOC_TYPE);
        Document document = database.createDocument();
        try {
            document.putProperties(objectMap);
            attendance.set_id(document.getId());
            attendance.set_rev(document.getCurrentRevisionId());
            Log.d(TAG, "Created student doc: %s", document.getId());
            return attendance;
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
            Log.d(TAG, "error while saving student");
        }
        return attendance;
    }

    /**
     * Gets a attendance with id
     *
     * @param id
     * @return Attendance with given id or null if attendace does not exist
     */
    public Attendance get(String id) {
        Document document = database.getExistingDocument(id);
        if (document == null) {
            return null;
        }
        Attendance attendance = objectMapper.convertValue(
                document.getProperties(), Attendance.class);
        return attendance;
    }

    public boolean delete(Attendance s) {
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

}
