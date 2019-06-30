package com.smartdev.fishfarm.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ItemDecoration;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.support.v7.widget.RecyclerView.State;
import android.view.View;

public class ItemDivider extends ItemDecoration {
    private final Drawable divider;

    public ItemDivider(Context context) {
        this.divider = context.obtainStyledAttributes(new int[]{16843284}).getDrawable(0);
    }

    public void onDrawOver(Canvas c, RecyclerView parent, State state) {
        super.onDrawOver(c, parent, state);
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View item = parent.getChildAt(i);
            int top = item.getBottom() + ((LayoutParams) item.getLayoutParams()).bottomMargin;
            this.divider.setBounds(left, top, right, this.divider.getIntrinsicHeight() + top);
            this.divider.draw(c);
        }
    }
}
