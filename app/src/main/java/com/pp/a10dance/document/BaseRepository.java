package com.pp.a10dance.document;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;

import com.couchbase.lite.Database;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.A10danceDB;

/**
 * Created by saketagarwal on 5/23/15.
 */
public abstract class BaseRepository {
    public static final String TYPE = "type";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    public static final String CREATED_AT = "createdAt";
    protected final AndroidContext context;
    protected final Database database;

    ObjectMapper objectMapper = new ObjectMapper();
    {
        objectMapper
                .configure(
                        DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
                        false);

    }

    BaseRepository(AndroidContext context) {
        this.context = context;
        database = A10danceDB.getInstance(context).getDatabase();
    }
}
