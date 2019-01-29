package com.example.az.mybaseproject2.goods;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ActivityGoodsListBinding;
import com.example.az.mybaseproject2.goods.bean.GoodsListBean;
import com.example.resource.base.SimpleBaseSwipeBackActivity;
import com.example.resource.component.NormalItemDecoration;
import com.example.resource.network.ServiceFactory;
import com.example.resource.network.SimpleTaskObserver;
import com.example.resource.utils.LogUtils;

import cn.shyman.library.refresh.OnTaskListener;
import cn.shyman.library.refresh.RecyclerAdapter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class GoodsListActivity extends SimpleBaseSwipeBackActivity {
	private ActivityGoodsListBinding dataBinding;
	private GoodsFloorListAdapter goodsListAdapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_goods_list);
		
		// getSwipeBackLayout().setEdgeTrackingEnabled(0);
		
		observerContent();
		
		this.dataBinding.refreshLayout.swipeRefresh();
	}
	
	private void observerContent() {
		this.dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
		this.dataBinding.recyclerView.addItemDecoration(new NormalItemDecoration(this));
		
		this.goodsListAdapter = new GoodsFloorListAdapter(this);
		this.goodsListAdapter.setRefreshLayout(this.dataBinding.refreshLayout);
		this.goodsListAdapter.setRecyclerView(this.dataBinding.recyclerView);
		this.goodsListAdapter.setOnTaskListener(new OnTaskListener<Disposable>() {
			@Override
			public Disposable onTask() {
				return listGoods(goodsListAdapter.getCurrentPage());
			}
			
			@Override
			public void onCancel(Disposable disposable) {
				disposable.dispose();
			}
		});
		
		goodsListAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(RecyclerAdapter.ItemHolder itemHolder) {
				// MovieItemBean itemBean = testMovieListAdapter.getItem(itemHolder.getAdapterPosition());
				// HashMap<String, Object> params = new HashMap<>();
				// params.put(BaseConst.ID, itemBean.id);
				// params.put(BaseConst.TITLE, itemBean.title);
				// LaunchUtil.launchActivity(mContext, MovieInfoActivity.class, params);
			}
		});
	}
	
	private void swipeResult(GoodsListBean goodsListBean) {
		goodsListAdapter.swipeResult(goodsListBean);
	}
	
	private Disposable listGoods(int currentPage) {
		//https://app.52momopig.com/index/index/queryGoodsRecommendByPosition?token=8899daaa4c8e50882bbfa48a68901706&page=%7B%22pageIndex%22%3A1%2C%22pageSize%22%3A20%7D&positionCode=syhybd
		String pageInfo = String.format(String.format("{\"pageIndex\":%s,\"pageSize\":%s}", currentPage, goodsListAdapter.getDefaultPageSize()));
		return ServiceFactory.createService("https://app.idouyao.com/", GoodsService.class)
				.listGoods("68d1e22a4549c907cd0e73903e558566", "588189232397012992", pageInfo)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new SimpleTaskObserver<GoodsListBean>(GoodsListActivity.this) {
					
					@Override
					public void taskStart() {
						// super.taskStart();
					}
					
					@Override
					public void taskSuccess(GoodsListBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskSuccess()");
						super.taskSuccess(basicBean);
						swipeResult(basicBean);
					}
					
					@Override
					public void taskFailure(GoodsListBean basicBean) {
						LogUtils.d("TestMoviePresenter", "taskFailure()");
						super.taskFailure(basicBean);
						swipeResult(basicBean);
					}
					
					@Override
					public void taskError(Throwable throwable) {
						LogUtils.d("TestMoviePresenter", "taskError()");
						super.taskError(throwable);
						swipeResult(null);
					}
				});
	}
}
