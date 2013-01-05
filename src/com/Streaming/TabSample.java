package com.Streaming;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabSample extends TabActivity {
	/** Called when the activity is first created. */

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		setTabs();
	}

	private void setTabs() {
		
		addTab("", R.drawable.music_cd_3, MusicActivity.class);
		addTab("", R.drawable.news2, NewsActivity.class);

		addTab("", R.drawable.diary_128, NhatkyActivity.class);
		addTab("", R.drawable.hp_fav_star, ShoppingActivity.class);
	}

	private void addTab(String labelId, int drawableId, Class<?> c) {
		@SuppressWarnings("deprecation")
		TabHost tabHost = getTabHost();
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		@SuppressWarnings("deprecation")
		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.tab_indicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);

		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);
	}
}