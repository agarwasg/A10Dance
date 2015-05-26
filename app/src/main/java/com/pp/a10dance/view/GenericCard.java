package com.pp.a10dance.view;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.View;

import com.pp.a10dance.R;

/**
 * Created by saketagarwal on 4/5/15.
 */
public class GenericCard extends CardView {

    private Context mContext;
    private View mBaseView;

    public GenericCard(Context context) {
        this(context, null);
    }

    public GenericCard(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GenericCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        setRadius(getResources().getDimension(R.dimen.card_corner_radius));
        setCardElevation(getResources().getDimension(R.dimen.card_elevation));
        mBaseView = inflate(mContext, R.layout.generic_card, this);

    }
}
