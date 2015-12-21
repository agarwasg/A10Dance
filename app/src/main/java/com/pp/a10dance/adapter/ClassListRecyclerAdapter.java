package com.pp.a10dance.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.couchbase.lite.QueryEnumerator;
import com.couchbase.lite.android.AndroidContext;
import com.pp.a10dance.R;
import com.pp.a10dance.document.ProfClassRepository;
import com.pp.a10dance.model.ProfClass;

public class ClassListRecyclerAdapter extends
        RecyclerView.Adapter<ClassListRecyclerAdapter.ViewHolder> implements
        View.OnClickListener {

    private Context context;
    private LiveQuery query;
    private QueryEnumerator enumerator;
    private ProfClassRepository repository;

    private OnItemClickListener mListener;

    public static interface OnItemClickListener {
        public void onItemClick(String listId);
    }

    public ClassListRecyclerAdapter(Context context, LiveQuery query,
            OnItemClickListener listener) {
        this.context = context;
        this.query = query;
        this.mListener = listener;
        repository = new ProfClassRepository(new AndroidContext(context));

        query.addChangeListener(new LiveQuery.ChangeListener() {
            @Override
            public void changed(final LiveQuery.ChangeEvent event) {
                ((AppCompatActivity) ClassListRecyclerAdapter.this.context)
                        .runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                enumerator = event.getRows();
                                notifyDataSetChanged();
                            }
                        });
            }
        });
        query.start();
    }

    public Object getItem(int i) {
        return enumerator != null ? enumerator.getRow(i).getDocument() : null;
    }

    @Override
    public ClassListRecyclerAdapter.ViewHolder onCreateViewHolder(
            ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(context).inflate(
                R.layout.recycler_view_item_row, viewGroup, false);
        ViewHolder holder = new ViewHolder(view, i);
        holder.textView.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(
            ClassListRecyclerAdapter.ViewHolder viewHolder, int i) {
        final Document task = (Document) getItem(i);
        ProfClass profClass = repository
                .documentToObject(task, ProfClass.class);
        viewHolder.textView.setText(profClass.getName());
        viewHolder.textView.setTag(viewHolder);
    }

    @Override
    public int getItemCount() {
        return enumerator != null ? enumerator.getCount() : 0;
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();
        if (viewId == R.id.listRowText) {
            ViewHolder holder = (ViewHolder) v.getTag();
            Document document = (Document) getItem(holder.getPosition());
            mListener.onItemClick((String) document.getProperty("_id"));
        }
    }

    // Creating a ViewHolder class which extends the RecyclerView.ViewHolder
    // ViewHolder are used to store the inflated views in order to recycle them
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.listRowText);
        }

    }
}
