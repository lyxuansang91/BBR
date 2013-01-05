package com.Streaming.New.View;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.Streaming.Configuration;
import com.Streaming.R;

public class TintucChitietActivity extends Activity {
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tintucchitiet);
		initControls();
		if (getLinkData() != null) {
			loadData(getLinkData());
		}
	}

	private void initControls() {
		webView = (WebView) findViewById(R.id.webView);
	}

	private void loadData(String url) {
		webView.setWebViewClient(new WebViewClient());
		
		webView.loadUrl(url);
	}

	private String getLinkData() {
		Bundle action = this.getIntent().getExtras();
		if (action != null) {
			String str = action.getString(Configuration.LINK);
			return str;
		}
		return null;
	}
}
