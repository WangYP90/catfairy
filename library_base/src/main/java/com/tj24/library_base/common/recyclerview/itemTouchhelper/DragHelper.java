package com.tj24.library_base.common.recyclerview.itemTouchhelper;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;


public class DragHelper extends ItemTouchHelper.Callback{
    private IDragAdapter mAdapter;

    public DragHelper(IDragAdapter adapter){
        mAdapter = adapter;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT|ItemTouchHelper.UP|ItemTouchHelper.DOWN;
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags,swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return mAdapter.onItemMoved(viewHolder.getAdapterPosition(),target.getAdapterPosition());
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        return  false;
    }
}
