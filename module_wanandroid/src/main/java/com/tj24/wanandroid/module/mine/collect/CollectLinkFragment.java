package com.tj24.wanandroid.module.mine.collect;

import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import androidx.core.content.ContextCompat;
import androidx.core.widget.PopupWindowCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kennyc.view.MultiStateView;
import com.tj24.base.bean.wanandroid.NetUrlBean;
import com.tj24.base.common.recyclerview.interfac.SimpleListener;
import com.tj24.base.utils.MultiStateUtils;
import com.tj24.base.utils.ScreenUtil;
import com.tj24.base.utils.ToastUtil;
import com.tj24.base.utils.ViewUtils;
import com.tj24.wanandroid.R;
import com.tj24.wanandroid.R2;
import com.tj24.wanandroid.common.base.BaseWanAndroidFragment;
import com.tj24.wanandroid.common.event.CollectRefreshEvent;
import com.tj24.wanandroid.common.event.CollectRefreshFinishEvent;
import com.tj24.wanandroid.common.event.EditNetUrlEvent;
import com.tj24.wanandroid.common.http.WanAndroidCallBack;
import com.tj24.wanandroid.common.view.ScrollLinearLayoutManager;
import com.tj24.wanandroid.common.view.UrlLinkEditPopup;
import com.tj24.wanandroid.module.web.WebViewActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;

public class CollectLinkFragment extends BaseWanAndroidFragment {
    @BindView(R2.id.rv_links)
    RecyclerView rvLinks;
    @BindView(R2.id.msv)
    MultiStateView msv;

    private CollectLinkAdapter linkAdapter;
    private ScrollLinearLayoutManager linearLayoutManager;
    private UrlLinkEditPopup editPopup;
    View transView;
    int clickX;
    int clickY;

    int editPosition;
    @Override
    public int getCreateViewLayoutId() {
        return R.layout.wanandroid_fragment_collect_link;
    }

    @Override
    public void init(View view) {
        transView = ((CollectActivity) mActivity).transView;
        ((CollectActivity)mActivity).setCollectLinkListener(new CollectLinkListener() {
            @Override
            public void onActivityClick(float x, float y) {
                clickX = (int) x;
                clickY = (int) y;
            }

            @Override
            public void onTranseViewClick() {
                if(editPopup!=null && editPopup.isShowing()){
                    editPopup.dismiss();
                }
            }

            @Override
            public void onBackPressed() {
                if(editPopup!=null && editPopup.isShowing()){
                    editPopup.dismiss();
                }else {
                    ((CollectActivity)mActivity).onBack();
                }
            }
        });

        linearLayoutManager = new ScrollLinearLayoutManager(mActivity);
        rvLinks.setLayoutManager(linearLayoutManager);
        linkAdapter = new CollectLinkAdapter();
        linkAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NetUrlBean urlBean = linkAdapter.getData().get(position);
                WebViewActivity.actionStart(mActivity,urlBean.getName(),urlBean.getLink());
            }
        });
        linkAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                showEditPopup(view, position);
                return true;
            }
        });
        rvLinks.setAdapter(linkAdapter);
        MultiStateUtils.setEmptyAndErrorClick(msv, new SimpleListener() {
            @Override
            public void onResult() {
                refreshData();
            }
        });
        refreshData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceiveRefresh(CollectRefreshEvent event) {
        if (event.getItem() == 1) {
            if (event.getType() == 0) {
                refreshData();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(editPopup != null){
            editPopup.dismiss();
        }
    }

    public void refreshData() {
        MultiStateUtils.toLoading(msv);
        CollectRequest.getCollectLinks(new WanAndroidCallBack<List<NetUrlBean>>() {
            @Override
            public void onSucces(List<NetUrlBean> urlBeans) {
                if (urlBeans.size() > 0) {
                    MultiStateUtils.toContent(msv);
                } else {
                    MultiStateUtils.toEmpty(msv);
                }
                CollectRefreshFinishEvent.postRefreshFinishEvent();
                linkAdapter.setNewData(urlBeans);
            }

            @Override
            public void onFail(String fail) {
                MultiStateUtils.toError(msv);
                CollectRefreshFinishEvent.postRefreshFinishEvent();
            }
        });
    }
@Subscribe(threadMode = ThreadMode.MAIN)
    public void onReceivedEditNetURlEvent(EditNetUrlEvent event){
        if(event.getType() == CollectLinkActivity.TYPE_CREAT){
            linkAdapter.addData(0,event.getNetUrlBean());
        }else if(event.getType() == CollectLinkActivity.TYPE_EDIT){
            linkAdapter.getData().set(editPosition,event.getNetUrlBean());
            linkAdapter.notifyItemChanged(editPosition);
        }
    }
    private void showEditPopup(View view, int position) {
        NetUrlBean netUrlBean = linkAdapter.getData().get(position);
        if (netUrlBean == null) {
            return;
        }
        transView.setVisibility(View.VISIBLE);
        linearLayoutManager.setCanScrollVertically(false);
        editPopup = new UrlLinkEditPopup(mActivity, netUrlBean);
        View contentView = editPopup.getContentView();
        //需要先测量，PopupWindow还未弹出时，宽高为0
        contentView.measure(ViewUtils.makeDropDownMeasureSpec(editPopup.getWidth()),
                ViewUtils.makeDropDownMeasureSpec(editPopup.getHeight()));
        int popupWidth = contentView.getMeasuredWidth();
        int popupHeight = contentView.getMeasuredHeight();
        //itemView 在屏幕中的位置
        int[] itemLocation = new int[2];
        view.getLocationOnScreen(itemLocation);
        //recyclerview 在屏幕中的位置
        int[] rvLocation = new int[2];
        rvLinks.getLocationOnScreen(rvLocation);

        int overlapValueX = (int) ScreenUtil.dip2px(mActivity, 20);
        int overlapValueY = (int) ScreenUtil.dip2px(mActivity, 14);

        int offX = clickX;
        int offY = 0;
        //计算Y方向偏移量
        if (itemLocation[1] + view.getHeight() + popupHeight > rvLocation[1] + rvLinks.getHeight()) {
            offY = -(view.getHeight() + popupHeight) + overlapValueY;
        } else {
            offY = offY - overlapValueY;
        }

        //计算X方向偏移量 保证左右有margin
        if (clickX < overlapValueX) {
            offX = overlapValueX;
        } else if (clickX + overlapValueX > ScreenUtil.getInstance(mActivity).getScreenWidth()) {
            offX = ScreenUtil.getInstance(mActivity).getScreenWidth() - overlapValueX - popupWidth;
        } else if (clickX + popupWidth > view.getWidth()) {
            offX = clickX - popupWidth;
        }


        PopupWindowCompat.showAsDropDown(editPopup, view, offX, offY, Gravity.START);
        editPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                transView.setVisibility(View.GONE);
                linearLayoutManager.setCanScrollVertically(true);
                view.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.base_white_text));
            }
        });
        editPopup.setUrlLinkEditListener(new UrlLinkEditPopup.UrlLinkEditListener() {
            @Override
            public void onDel() {
                cancleCollectLink(netUrlBean,position);
            }

            @Override
            public void onEdit() {
                editPosition = position;
                CollectLinkActivity.actionStart(mActivity,CollectLinkActivity.TYPE_EDIT,netUrlBean);
            }
        });
        view.setBackgroundColor(ContextCompat.getColor(mActivity,R.color.base_black_eee));
    }

    private void cancleCollectLink(NetUrlBean netUrlBean,int position) {
        CollectRequest.deleteLink(netUrlBean.getId(), new WanAndroidCallBack() {
            @Override
            public void onSucces(Object o) {
                ToastUtil.showShortToast(mActivity,"取消收藏成功");
                linkAdapter.remove(position);
            }

            @Override
            public void onFail(String fail) {
                ToastUtil.showShortToast(mActivity,"取消收藏失败");
            }
        });
    }
}
