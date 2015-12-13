package com.pp.a10dance.document;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.Database;
import com.couchbase.lite.Document;
import com.couchbase.lite.Emitter;
import com.couchbase.lite.Mapper;
import com.couchbase.lite.Query;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.model.ProfClass;

public class ProfClassRepository extends BaseRepository {
    private static final String DOC_TYPE = "prof_class";
    private static final String VIEW_NAME = "prof_class";

    public ProfClassRepository(AndroidContext context) {
        super(context);
    }

    public Query getQuery() {
        com.couchbase.lite.View view = database.getView(VIEW_NAME);
        if (view.getMap() == null) {
            Mapper mapper = new Mapper() {
                public void map(Map<String, Object> document, Emitter emitter) {
                    String type = (String) document.get(TYPE);

                    if (DOC_TYPE.equals(type)) {
                        ProfClass profClass = objectMapper.convertValue(
                                document, ProfClass.class);
                        emitter.emit(profClass.getName(), document);
                    }
                }
            };
            view.setMap(mapper, "1");
        }
        Query query = view.createQuery();
        return query;
    }

    public boolean deleteProfClass(Database database, String classId)
            throws CouchbaseLiteException {
        Document document = database.getDocument(classId);
        String type = (String) document.getProperty(TYPE);
        if (DOC_TYPE.equals(type)) {
            return document.delete();
        }
        return false;
    }

    public ProfClass createNewProfClass(ProfClass profClass)
            throws CouchbaseLiteException {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(DATE_FORMAT);
        Calendar calendar = GregorianCalendar.getInstance();
        String currentTimeString = dateFormatter.format(calendar.getTime());
        profClass.setCreatedAt(currentTimeString);
        Map<String, Object> properties = objectMapper.convertValue(profClass,
                Map.class);
        properties.put(TYPE, DOC_TYPE);
        Document document = database.createDocument();
        document.putProperties(properties);
        profClass.set_id(document.getId());
        profClass.set_rev(document.getCurrentRevisionId());
        return profClass;

    }

}
