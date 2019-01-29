package com.example.az.mybaseproject2.goods;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.az.mybaseproject2.R;
import com.example.az.mybaseproject2.databinding.ItemGoodsListBinding;
import com.example.az.mybaseproject2.goods.bean.GoodsItemBean;
import com.example.resource.base.BaseRecyclerAdapter;
import com.example.resource.utils.LogUtils;

import java.util.List;

/**
 * Created by wp on 2018/4/10.
 */

public class GoodsListAdapter extends BaseRecyclerAdapter<List<GoodsItemBean>> {
	
	private final LayoutInflater inflater;
	
	public GoodsListAdapter(Context context) {
		this.inflater = LayoutInflater.from(context);
	}
	
	@Override
	protected void updateAdapterInfo(@NonNull List<GoodsItemBean> goodsItemBeans) {
		// this.adapterInfo.addAll(goodsItemBeans);
		this.adapterInfo = goodsItemBeans;
	}
	
	@Override
	public boolean hasMore() {
		return false;
	}
	
	@Override
	public int getItemCount() {
		return this.adapterInfo != null ? this.adapterInfo.size() : 0;
	}
	
	@Override
	public GoodsItemBean getItem(int position) {
		return this.adapterInfo.get(position);
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
				// LogUtils.d("-----" + getItem(position));
				this.dataBinding.setIndex(String.valueOf(position + 1));
				this.dataBinding.setGoodsItemBean(getItem(position));
				this.dataBinding.executePendingBindings();
			}
		};
	}
}
