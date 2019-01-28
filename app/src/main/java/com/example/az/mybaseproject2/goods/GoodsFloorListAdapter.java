package com.example.az.mybaseproject2.goods;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ItemGoodsFloorBinding;
import com.example.az.mybaseproject2.goods.bean.GoodsInfoItemBean;
import com.example.az.mybaseproject2.goods.bean.GoodsListBean;
import com.example.resource.base.BaseRecyclerAdapter;
import com.example.resource.network.StatusInfo;
import com.example.resource.utils.LogUtils;
import com.example.resource.widget.PictureView;

/**
 * Created by wp on 2019/1/28.
 */
public class GoodsFloorListAdapter extends BaseRecyclerAdapter<GoodsListBean> {
	private final LayoutInflater inflater;
	
	public GoodsFloorListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getItemCount() {
		return this.adapterInfo != null && this.adapterInfo.list != null ?
				this.adapterInfo.list.size() : 0;
	}
	
	@Override
	public GoodsInfoItemBean getItem(int position) {
		return adapterInfo.list.get(position);
	}
	
	@Override
	public RecyclerHolder onCreateItemHolder(int viewType) {
		return new ItemHolder() {
			ItemGoodsFloorBinding dataBinding;
			GoodsListAdapter goodsListAdapter;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_goods_floor, parent, false);
				return dataBinding.getRoot();
			}
			
			@Override
			protected void onViewCreated(View view) {
				super.onViewCreated(view);
				goodsListAdapter = new GoodsListAdapter(inflater.getContext());
				dataBinding.recyclerView.setLayoutManager(new LinearLayoutManager(inflater.getContext()));
				goodsListAdapter.setRecyclerView(dataBinding.recyclerView);
				
			}
			
			@Override
			protected void onBindView(int position) {
				PictureView.loadPicture(dataBinding.pictureView, getItem(position).imgUrl);
				dataBinding.tvTitle.setText(getItem(position).homeName);
				// dataBinding.setGoodsInfoItemBean(getItem(position));
				LogUtils.d("-----" + getItem(position).goodsRecommendResponseList.size());
				goodsListAdapter.swipeResult(getItem(position).goodsRecommendResponseList);
				goodsListAdapter.swipeStatus(new StatusInfo(StatusInfo.STATUS_SUCCESS));
			}
		};
	}
	
	@Override
	protected void updateAdapterInfo(@NonNull GoodsListBean goodsListBean) {
		this.adapterInfo.list.addAll(goodsListBean.list);
	}
	
	@Override
	public boolean hasMore() {
		return this.adapterInfo != null && getCurrentPage() <= this.adapterInfo.pageCount;
	}
}
