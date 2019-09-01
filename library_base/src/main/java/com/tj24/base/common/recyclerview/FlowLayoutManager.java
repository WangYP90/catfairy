package com.tj24.base.common.recyclerview;

import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by energy on 2018/8/14.
 */

public class FlowLayoutManager extends RecyclerView.LayoutManager {
    private static final String TAG = FlowLayoutManager.class.getSimpleName();
    private SparseArray<Rect> allItemFrames = new SparseArray();
    private int height;
    private int left;
    private List<Row> lineRows = new ArrayList();
    private int right;
    private Row row = new Row();
    final FlowLayoutManager self = this;
    private int top;
    private int totalHeight = 0;
    private int usedMaxWidth;
    private int verticalScrollOffset = 0;
    private int width;

    public class Item {
        Rect rect;
        int useHeight;
        View view;

        public void setRect(Rect rect) {
            this.rect = rect;
        }

        public Item(int useHeight, View view, Rect rect) {
            this.useHeight = useHeight;
            this.view = view;
            this.rect = rect;
        }
    }

    public class Row {
        float cuTop;
        float maxHeight;
        List<Item> views = new ArrayList();

        public void setCuTop(float cuTop) {
            this.cuTop = cuTop;
        }

        public void setMaxHeight(float maxHeight) {
            this.maxHeight = maxHeight;
        }

        public void addViews(Item view) {
            this.views.add(view);
        }
    }

    public int getTotalHeight() {
        return this.totalHeight;
    }

    public FlowLayoutManager() {
        setAutoMeasureEnabled(true);
    }

    public RecyclerView.LayoutParams generateDefaultLayoutParams() {
        return new RecyclerView.LayoutParams(-2, -2);
    }

    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d(TAG, "onLayoutChildren");
        if (getItemCount() == 0) {
            detachAndScrapAttachedViews(recycler);
            this.verticalScrollOffset = 0;
        } else if (getChildCount() != 0 || !state.isPreLayout()) {
            detachAndScrapAttachedViews(recycler);
            if (getChildCount() == 0) {
                this.width = getWidth();
                this.height = getHeight();
                this.left = getPaddingLeft();
                this.right = getPaddingRight();
                this.top = getPaddingTop();
                this.usedMaxWidth = (this.width - this.left) - this.right;
            }
            this.totalHeight = 0;
            int cuLineTop = this.top;
            int cuLineWidth = 0;
            int maxHeightItem = 0;
            this.row = new Row();
            this.lineRows.clear();
            this.allItemFrames.clear();
            removeAllViews();
            for (int i = 0; i < getItemCount(); i++) {
                Log.d(TAG, "index:" + i);
                View childAt = recycler.getViewForPosition(i);
                if (View.GONE != childAt.getVisibility()) {
                    MarginLayoutParams lp = (MarginLayoutParams) childAt.getLayoutParams();
                    int leftMargin = lp.leftMargin;
                    int rightMargin = lp.rightMargin;
                    int topMargin = lp.topMargin;
                    int bottomMargin = lp.bottomMargin;
                    measureChildWithMargins(childAt, 0, 0);
                    int childWidth = (getDecoratedMeasuredWidth(childAt) + leftMargin) + rightMargin;
                    int childHeight = (getDecoratedMeasuredHeight(childAt) + bottomMargin) + topMargin;
                    int childUseWidth = childWidth;
                    int childUseHeight = childHeight;
                    int itemLeft;
                    int itemTop;
                    Rect frame;
                    if (cuLineWidth + childUseWidth <= this.usedMaxWidth) {
                        itemLeft = this.left + cuLineWidth;
                        itemTop = cuLineTop;
                        frame = (Rect) this.allItemFrames.get(i);
                        if (frame == null) {
                            frame = new Rect();
                        }
                        frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                        this.allItemFrames.put(i, frame);
                        cuLineWidth += childUseWidth;
                        maxHeightItem = Math.max(maxHeightItem, childUseHeight);
                        this.row.addViews(new Item(childUseHeight, childAt, frame));
                        this.row.setCuTop((float) cuLineTop);
                        this.row.setMaxHeight((float) maxHeightItem);
                    } else {
                        formatAboveRow();
                        cuLineTop += maxHeightItem;
                        this.totalHeight += maxHeightItem;
                        itemTop = cuLineTop;
                        itemLeft = this.left;
                        frame = (Rect) this.allItemFrames.get(i);
                        if (frame == null) {
                            frame = new Rect();
                        }
                        frame.set(itemLeft, itemTop, itemLeft + childWidth, itemTop + childHeight);
                        this.allItemFrames.put(i, frame);
                        cuLineWidth = childUseWidth;
                        maxHeightItem = childUseHeight;
                        this.row.addViews(new Item(childUseHeight, childAt, frame));
                        this.row.setCuTop((float) cuLineTop);
                        this.row.setMaxHeight((float) maxHeightItem);
                    }
                    if (i == getItemCount() - 1) {
                        formatAboveRow();
                        this.totalHeight += maxHeightItem;
                    }
                }
            }
            this.totalHeight = Math.max(this.totalHeight, getVerticalSpace());
            fillLayout(recycler, state);
        }
    }

    private void fillLayout(RecyclerView.Recycler recycler, RecyclerView.State state) {
        if (!state.isPreLayout()) {
            Rect displayFrame = new Rect(getPaddingLeft(), getPaddingTop() + this.verticalScrollOffset, getWidth() - getPaddingRight(), this.verticalScrollOffset + (getHeight() - getPaddingBottom()));
            for (int j = 0; j < this.lineRows.size(); j++) {
                Row row = (Row) this.lineRows.get(j);
                float lineTop = row.cuTop;
                float lineBottom = lineTop + row.maxHeight;
                List<Item> views;
                int i;
                if (lineTop >= ((float) displayFrame.bottom) || ((float) displayFrame.top) >= lineBottom) {
                    views = row.views;
                    for (i = 0; i < views.size(); i++) {
                        removeAndRecycleView(((Item) views.get(i)).view, recycler);
                    }
                } else {
                    views = row.views;
                    for (i = 0; i < views.size(); i++) {
                        View scrap = ((Item) views.get(i)).view;
                        measureChildWithMargins(scrap, 0, 0);
                        addView(scrap);
                        Rect frame = ((Item) views.get(i)).rect;
                        layoutDecoratedWithMargins(scrap, frame.left, frame.top - this.verticalScrollOffset, frame.right, frame.bottom - this.verticalScrollOffset);
                    }
                }
            }
        }
    }

    private void formatAboveRow() {
        List<Item> views = this.row.views;
        for (int i = 0; i < views.size(); i++) {
            Item item = (Item) views.get(i);
            View view = item.view;
            int position = getPosition(view);
            if (((float) ((Rect) this.allItemFrames.get(position)).top) < ((this.row.maxHeight - ((float) ((Item) views.get(i)).useHeight)) / 2.0f) + this.row.cuTop) {
                Rect frame = (Rect) this.allItemFrames.get(position);
                if (frame == null) {
                    frame = new Rect();
                }
                frame.set(((Rect) this.allItemFrames.get(position)).left, (int) (((this.row.maxHeight - ((float) ((Item) views.get(i)).useHeight)) / 2.0f) + this.row.cuTop), ((Rect) this.allItemFrames.get(position)).right, (int) ((((this.row.maxHeight - ((float) ((Item) views.get(i)).useHeight)) / 2.0f) + this.row.cuTop) + ((float) getDecoratedMeasuredHeight(view))));
                this.allItemFrames.put(position, frame);
                item.setRect(frame);
                views.set(i, item);
            }
        }
        this.row.views = views;
        this.lineRows.add(this.row);
        this.row = new Row();
    }

    public boolean canScrollVertically() {
        return true;
    }

    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        Log.d("TAG", "totalHeight:" + this.totalHeight);
        int travel = dy;
        if (this.verticalScrollOffset + dy < 0) {
            travel = -this.verticalScrollOffset;
        } else if (this.verticalScrollOffset + dy > this.totalHeight - getVerticalSpace()) {
            travel = (this.totalHeight - getVerticalSpace()) - this.verticalScrollOffset;
        }
        this.verticalScrollOffset += travel;
        offsetChildrenVertical(-travel);
        fillLayout(recycler, state);
        return travel;
    }

    private int getVerticalSpace() {
        return (this.self.getHeight() - this.self.getPaddingBottom()) - this.self.getPaddingTop();
    }

    public int getHorizontalSpace() {
        return (this.self.getWidth() - this.self.getPaddingLeft()) - this.self.getPaddingRight();
    }

}
