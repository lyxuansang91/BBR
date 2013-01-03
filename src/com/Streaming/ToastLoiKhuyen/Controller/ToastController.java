package com.Streaming.ToastLoiKhuyen.Controller;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;

import com.Streaming.ToastLoiKhuyen.Model.ToastModel;
import com.Streaming.ToastLoiKhuyen.View.CustomToast;

public class ToastController {
	// Constraint
	public static final String ON_RESUME = "onResume";
	public static final String ON_STOP = "onStop";
	public static final String ON_DESTROY = "onDestroy";
	public static final String ON_PAUSE = "onPause";

	//Fields
	private CustomToast mToast;
	private Context mContext;

	
	//Constructor
	public ToastController(Context context) {
		this.mContext = context;
		mToast = new CustomToast(mContext);
	}

	public void handleMessage(String what) {
		if (what.equals(ON_RESUME)) {
			resume();
		} else if (what.equals(ON_STOP)) {
			stop();
		} else if (what.equals(ON_DESTROY)) {
			stop();
		} else if (what.equals(ON_PAUSE)) {
			stop();
		}
	}

	private void resume() {
		mToast.cancel();
		showAdvice();
	}

	private void stop() {
		mToast.cancel();
		mHandler.removeCallbacks(updateAdvice);
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		return cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isConnectedOrConnecting();
	}

	private Handler mHandler = new Handler();

	private void showAdvice() {
		mHandler.post(updateAdvice);
	}

	Runnable updateAdvice = new Runnable() {

		@Override
		public void run() {
			new Thread(new Runnable() {

				@Override
				public void run() {
					String content = ToastModel.getAdviceFromServer();
					mToast.show(content);
				}
			}).start();
			mHandler.postDelayed(updateAdvice, 30000);
		}
	};

}
