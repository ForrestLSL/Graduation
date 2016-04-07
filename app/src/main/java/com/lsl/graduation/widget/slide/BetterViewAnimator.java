package com.lsl.graduation.widget.slide;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewAnimator;

/**
 * Created by Forrest on 16/4/7.
 */
public class BetterViewAnimator extends ViewAnimator{

    public BetterViewAnimator(Context context) {
        super(context);
    }

    public BetterViewAnimator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDisplayedChildId(int id) {
        if (getDisplayedChildId() == id) {
            return;
        }
        for (int i = 0, count = getChildCount(); i < count; i++) {
            if (getChildAt(i).getId() == id) {
                setDisplayedChild(i);
                return;
            }
        }
        throw new IllegalArgumentException("No view with ID " + id);
    }

    public int getDisplayedChildId() {
        return getChildAt(getDisplayedChild()).getId();
    }
}
