package com.Streaming;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.Streaming.New.View.NewView;

public class NewsActivity extends Activity {

	LinearLayout rootActivityNews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news);
		rootActivityNews = (LinearLayout) findViewById(R.id.rootActivityNews);
		NewView n = new NewView(this);
		n.setActivity(this);
		rootActivityNews.addView(n);
	}
}
