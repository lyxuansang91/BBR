package com.Streaming.New.Controller;

import java.util.ArrayList;
import java.util.Iterator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ProgressBar;

import com.Streaming.Configuration;
import com.Streaming.New.Common.NewObject;
import com.Streaming.New.Model.NewModel;
import com.Streaming.New.View.NewView;
import com.Streaming.New.View.TintucChitietActivity;

public class NewController {

	private NewView newView;

	private int page = 0;
	private int mItemCount = 0;

	private Thread mThreadLoadData = null;
	private int maxPage = 20;

	private ArrayList<NewObject> news = new ArrayList<NewObject>();
	private NewAdapter adapter;

	public static final String LOAD_DATA = "Load data";

	public NewController(NewView n) {
		newView = n;
		adapter = new NewAdapter(newView.getContext(),
				android.R.layout.simple_list_item_1, news);
		newView.getListView().setAdapter(adapter);
		n.getListView().setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
			}

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				boolean loadMore = arg1 + arg2 >= arg3 - 1;
				if (loadMore && adapter.getCount() >= mItemCount
						&& adapter.getCount() > 0) {
					loadData();
				}
			}
		});

		n.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				NewObject n = (NewObject) adapter.getItem(arg2);
				if (n != null) {
					Intent intent = new Intent(newView.getContext(),
							TintucChitietActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString(Configuration.LINK,
							Configuration.IPNEWSCHITIET + n.getId());
					intent.putExtras(bundle);
					newView.getContext().startActivity(intent);
				}
			}
		});
	}

	public void handleMessage(String what) {
		if (what.equals(LOAD_DATA)) {
			loadData();
		}
	}

	private void loadData() {
		final Runnable updateAdapter = new Runnable() {

			@Override
			public void run() {
				Iterator<NewObject> itr = news.iterator();
				while (itr.hasNext())
					adapter.add(itr.next());
				adapter.notifyDataSetChanged();
				mItemCount = adapter.getCount();
				newView.getProgress().setVisibility(ProgressBar.GONE);
			}
		};
		Runnable loadItem = new Runnable() {

			@Override
			public void run() {
				if (page == 0) {
					page = page + 1;// load trang 1
				} else if(page <= maxPage){
					page = page + 1;// ++
				}
				news = NewModel.getNewsFromTheServer(Configuration.IPNEWS
						+ page);
				newView.getActivity().runOnUiThread(updateAdapter);
				mThreadLoadData = null;
			}
		};
		if (mThreadLoadData == null) {
			newView.getProgress().setVisibility(ProgressBar.VISIBLE);
			mThreadLoadData = new Thread(loadItem);
			mThreadLoadData.start();
		}
	}
}
