package com.fangsf.gankio.ui.fragment;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.fangsf.gankio.bean.ResultBean;
import com.fangsf.gankio.di.component.AppComponent;
import com.fangsf.gankio.di.component.DaggerAndroidComponent;
import com.fangsf.gankio.di.module.AndroidModule;
import com.fangsf.gankio.presenter.AndroidPresenter;
import com.fangsf.gankio.presenter.contract.AndroidContract;
import com.fangsf.gankio.ui.adapter.AndroidAdapter;
import com.fangsf.minddemo.R;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * @author fangsf
 * @date 2018/1/5.
 * @useful:
 */

public class AndroidFragment extends BaseFragment<AndroidPresenter> implements AndroidContract.IAndroidView, BaseQuickAdapter.RequestLoadMoreListener {


    @BindView(R.id.rcView)
    RecyclerView mRcView;
    @BindView(R.id.swRefresh)
    SwipeRefreshLayout mRefreshLayout;

    private AndroidAdapter mAdapter;

    private int count = 10;

    private int pageSize = 1;

    @Override
    protected void init() {

        mPresenter.requestMoreData(10);

        initRecylserView();

        initRefresh();
    }

    private void initRefresh() {
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.getData().clear();
                mPresenter.requestMoreData(10);
            }
        });
    }

    private void initRecylserView() {
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRcView.setLayoutManager(manager);
        mAdapter = new AndroidAdapter(R.layout.item_cardview);
        mRcView.setAdapter(mAdapter);
        mAdapter.setEnableLoadMore(true);
        mAdapter.openLoadAnimation();
        mAdapter.setOnLoadMoreListener(this);
    }

    @Override
    protected int bindLayout() {
        return R.layout.template_recylerview;
    }

    @Override
    protected void setupActivityComponent(AppComponent appComponent) {
        DaggerAndroidComponent.builder().appComponent(appComponent)
                .androidModule(new AndroidModule(this))
                .build().inject(this);
    }


    @Override
    public void showLoading() {
        mRefreshLayout.setRefreshing(true);
    }

    @Override
    public void showError(String mes) {
        toast(mes);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void dismissLoading() {
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showData(ArrayList<ResultBean> resultBeans) {
        mAdapter.addData(resultBeans);
    }

    @Override
    public void loadMore(ArrayList<ResultBean> resultBeans) {
        mAdapter.setNewData(resultBeans);
    }

    @Override
    public void onLoadMoreRequested() {
        count += 10;
        mPresenter.requestMoreData(count);
    }
}