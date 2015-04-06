package a10dence.pp.com.a10dance.document;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by saketagarwal on 4/4/15.
 */
public class ProfClass {
    private static final String DOC_TYPE = "prof_class";
    private static final String VIEW_NAME = "prof_class";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String TYPE = "type";
    private static final String TITLE = "title";


    public static Document createNewProfClass(Database database, String title,
            String description) throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put(TYPE, DOC_TYPE);
        properties.put(TITLE, title);
        properties.put("description", description);
        properties.put("created_at", currentTimeString);
        Document document = database.createDocument();
        document.putProperties(properties);
        return document;

    }

    public static Query getQuery(Database database){
        com.couchbase.lite.View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    String type = (String)document.get(TYPE);
                    if (DOC_TYPE.equals(type)) {
                        emitter.emit(document.get(TITLE), document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }
        Query query = view.createQuery();
        return query;
    }

    public static boolean deleteProfClass(Database database,String classId) throws CouchbaseLiteException {
        Document document = database.getDocument(classId);
        String type = (String)document.getProperty(TYPE);
        if(DOC_TYPE.equals(type)){
          return   document.delete();
        }
        return false;
    }

}
