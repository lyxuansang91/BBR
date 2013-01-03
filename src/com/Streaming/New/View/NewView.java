package com.Streaming.New.View;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.Streaming.R;
import com.Streaming.New.Controller.NewController;

public class NewView extends LinearLayout {

	private ListView lstView;
	private ProgressBar progress;
	private Activity mActivity;

	public NewView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControls();
	}

	public NewView(Context context) {
		super(context);
		initControls();
	}

	private void initControls() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.news_layout, this);
		lstView = (ListView) view.findViewById(R.id.lstNew);
		progress = (ProgressBar) view.findViewById(R.id.progressBar);
		NewController control = new NewController(this);
		control.handleMessage(NewController.LOAD_DATA);
	}

	public ListView getListView() {
		return lstView;
	}

	public Activity getActivity() {
		return mActivity;
	}

	public void setActivity(Activity activity) {
		mActivity = activity;
	}

	public ProgressBar getProgress() {
		return progress;
	}
}
