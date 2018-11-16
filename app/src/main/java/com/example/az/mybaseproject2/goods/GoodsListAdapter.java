package com.example.az.mybaseproject2.goods;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ItemGoodsListBinding;
import com.example.az.mybaseproject2.databinding.ItemMovieListBinding;
import com.example.az.mybaseproject2.goods.bean.GoodsItemBean;
import com.example.az.mybaseproject2.goods.bean.GoodsListBean;
import com.example.resource.base.BaseRecyclerAdapter;

/**
 * Created by wp on 2018/4/10.
 */

public class GoodsListAdapter extends BaseRecyclerAdapter<GoodsListBean> {
	
	private final LayoutInflater inflater;
	
	public GoodsListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	public void updateAdapterInfo(@NonNull GoodsListBean goodsListBean) {
		this.adapterInfo.goodsRecommendResponseList.addAll(goodsListBean.goodsRecommendResponseList);
	}
	
	@Override
	public boolean hasMore() {
		if (this.adapterInfo == null) {
			return false;
		}
		return 100 > this.getItemCount();
	}
	
	@Override
	public int getItemCount() {
		int count = 0;
		if (this.adapterInfo != null && this.adapterInfo.goodsRecommendResponseList != null) {
			count = this.adapterInfo.goodsRecommendResponseList.size();
		}
		return count;
	}
	
	@Override
	public GoodsItemBean getItem(int position) {
		return this.adapterInfo.goodsRecommendResponseList.get(position);
	}
	
	@Override
	public ItemHolder onCreateItemHolder(int viewType) {
		return new ItemHolder() {
			ItemGoodsListBinding dataBinding;
			
			@Override
			protected View onCreateView(ViewGroup parent) {
				this.dataBinding = DataBindingUtil.inflate(inflater, R.layout.item_goods_list, parent, false);
				return this.dataBinding.getRoot();
			}
			
			@Override
			protected void onBindView(int position) {
				this.dataBinding.setIndex(String.valueOf(position + 1));
				this.dataBinding.setGoodsItemBean(getItem(position));
				this.dataBinding.executePendingBindings();
			}
		};
	}
}
