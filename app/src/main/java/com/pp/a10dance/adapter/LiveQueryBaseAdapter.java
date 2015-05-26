package com.pp.a10dance.adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.BaseAdapter;

import com.couchbase.lite.Database;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryEnumerator;

/**
 * Created by saketagarwal on 4/11/15.
 */
public abstract class LiveQueryBaseAdapter extends BaseAdapter {

    private final Context mContext;
    private final LiveQuery mLiveQuery;
    private QueryEnumerator mQueryEnumerator;

    public LiveQueryBaseAdapter(Context context, LiveQuery liveQuery) {
        this.mContext = context;
        this.mLiveQuery = liveQuery;

        mLiveQuery.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(final LiveQuery.ChangeEvent event) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mQueryEnumerator = event.getRows();
                        notifyDataSetChanged();
                    }
                });
            }
        });
        mLiveQuery.start();
    }

    @Override
    public int getCount() {
        return mQueryEnumerator != null ? mQueryEnumerator.getCount() : 0;
    }

    @Override
    public Object getItem(int position) {
        return mQueryEnumerator != null ? mQueryEnumerator.getRow(position)
                .getDocument() : null;
    }

    @Override
    public long getItemId(int position) {
        return mQueryEnumerator.getRow(position).getSequenceNumber();
    }

    public void invalidate() {
        if (mLiveQuery != null)
            mLiveQuery.stop();
    }

    /*
     * Method called in the database change listener when a new change is
     * detected. Because live queries do not trigger a change event when non
     * current revisions are saved or pulled from a remote database.
     */
    public void updateQueryToShowConflictingRevisions(
            final Database.ChangeEvent event) {

        ((Activity) LiveQueryBaseAdapter.this.mContext)
                .runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLiveQuery.stop();
                        mQueryEnumerator = mLiveQuery.getRows();
                        notifyDataSetChanged();
                    }
                });

    }

}
