package com.Streaming;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;

import com.Streaming.ToastLoiKhuyen.Controller.ToastController;

public class MusicActivity extends Activity {

	private ToastController toastController;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_player);
		initControls();
	}

	private void initControls() {
		toastController = new ToastController(getApplicationContext());
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			toastController.handleMessage(ToastController.ON_DESTROY);
			System.gc();
			System.exit(0);
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		toastController.handleMessage(ToastController.ON_DESTROY);
		System.gc();
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		toastController.handleMessage(ToastController.ON_PAUSE);
		System.gc();
		super.onPause();
	}

	@Override
	protected void onResume() {
		toastController.handleMessage(ToastController.ON_RESUME);
		System.gc();
		super.onResume();
	}

	@Override
	protected void onStop() {
		toastController.handleMessage(ToastController.ON_STOP);
		System.gc();
		super.onStop();
	}

}