package com.pp.a10dance.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.couchbase.lite.Document;
import com.couchbase.lite.LiveQuery;
import com.pp.a10dance.R;
import com.pp.a10dance.document.StudentRepository;
import com.pp.a10dance.helper.Utils;

/**
 * Created by saketagarwal on 4/11/15.
 */

public class StudentAdapter extends LiveQueryBaseAdapter {

    private final Context mContext;
    private final LiveQuery liveQuery;
    private final ColorGenerator mGenerator;

    public StudentAdapter(Context context, LiveQuery liveQuery) {
        super(context, liveQuery);
        this.mContext = context;
        this.liveQuery = liveQuery;
        mGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(mContext).inflate(
                    R.layout.student_list_item, parent, false);
        }
        final Document student = (Document) getItem(position);
        if (student == null || student.getCurrentRevision() == null) {
            return convertView;
        }
        TextView textView = (TextView) itemView
                .findViewById(R.id.student_name_textView);
        ImageView imageView = (ImageView) itemView
                .findViewById(R.id.student_imageView);

        String studentName = (String) student
                .getProperty(StudentRepository.NAME);
        if (!Utils.StringUtils.isBlank(studentName)) {
            int color = mGenerator.getColor(studentName);
            String firstLetter = studentName.substring(0, 1);
            TextDrawable studentNameDrawable = TextDrawable.builder()
                    .buildRound(firstLetter, color); // radius in px
            imageView.setImageDrawable(studentNameDrawable);
        }
        textView.setText(studentName);
        return itemView;
    }
}
