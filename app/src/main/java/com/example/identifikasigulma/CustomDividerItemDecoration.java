package com.example.identifikasigulma;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class CustomDividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;
    private final int marginStart;
    private final int marginEnd;

    public CustomDividerItemDecoration(Context context, int resId, int marginStart, int marginEnd) {
        mDivider = ContextCompat.getDrawable(context, resId);
        this.marginStart = marginStart;
        this.marginEnd = marginEnd;
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int left = parent.getPaddingLeft() + marginStart;
        int right = parent.getWidth() - parent.getPaddingRight() - marginEnd;

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + mDivider.getIntrinsicHeight();

            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.bottom = mDivider.getIntrinsicHeight();
    }
}

