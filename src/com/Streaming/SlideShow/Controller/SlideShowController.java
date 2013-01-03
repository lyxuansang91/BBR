package com.Streaming.SlideShow.Controller;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;

import com.Streaming.R;
import com.Streaming.SlideShow.Model.SlideShowModel;
import com.Streaming.SlideShow.View.SlideShowView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class SlideShowController {
	private Context mContext;
	private SlideShowView mSlide;
	private int mDisplayWidth = 0; // Chiều rộng màn hình
	private int mDisplayHeight = 0; // Chiều cao màn hình
	public static final String SHOWSLIDE = "show slide";
	private final int THE_TIME_LEFT_CHANGE_SLIDE = 5000;
	private final String PREF_NAME = "pref_name";

	private final String LINKIMAGE_FROMCACHE = "linkImage_fromcache";
	
	private final String LINKIMAGE_EXCEPTION = "linkImage_Ex";
	private Handler mHandler = new Handler();
	private ArrayList<String> glstLinkImage = new ArrayList<String>();
	private boolean mIsDirectionLeftToRight;
	private Drawable mDefaultDrawable;

	private String loadLinkImageFromCache() {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		return sharedPreferences.getString(LINKIMAGE_FROMCACHE,
				LINKIMAGE_EXCEPTION);
	}

	private void save(String link) {
		SharedPreferences sharedPreferences = mContext.getSharedPreferences(
				PREF_NAME, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(LINKIMAGE_FROMCACHE, link);
		editor.commit();
	}

	public SlideShowController(Context context, SlideShowView sl) {
		mContext = context;
		mSlide = sl;
		Display display = ((WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		mDisplayWidth = display.getWidth();
		mDisplayHeight = display.getHeight();
		UrlImageViewHelper.MAX_DISPLAY_SIZE = mDisplayHeight + 30;
		if (!loadLinkImageFromCache().equals(LINKIMAGE_EXCEPTION))
			UrlImageViewHelper.setUrlDrawable(mSlide.getImageView(),
					loadLinkImageFromCache(), mDisplayHeight * mDisplayWidth,
					new UrlImageViewCallback() {

						@Override
						public void onLoaded(ImageView imageView,
								Drawable loadedDrawable, String url,
								boolean loadedFromCache) {
							mDefaultDrawable = loadedDrawable;
						}
					});
		else
			mDefaultDrawable = mContext.getResources().getDrawable(
					R.drawable.img7);
		imgWidth = mDefaultDrawable.getIntrinsicWidth();
		imgHeight = mDefaultDrawable.getIntrinsicHeight();
	}

	public void handleMessage(String what) {
		if (what.equals(SHOWSLIDE)) {
			showSlide();
		}
	}

	private void startingRunningSlide() {
		mHandler.post(updateSlideRunnable);
	}

	Runnable updateSlideRunnable = new Runnable() {

		@Override
		public void run() {
			if (mCurrentIndexLinkImage >= glstLinkImage.size() - 1) {
				if (mThreadGetDataFromServer == null) {
					mThreadGetDataFromServer = new Thread(
							getDataImageFromServerRunnable);
					mThreadGetDataFromServer.start();
				}
			}
			if (glstLinkImage.size() > 0) {
				next();
			}
			mHandler.postDelayed(updateSlideRunnable,
					THE_TIME_LEFT_CHANGE_SLIDE);
		}
	};

	int mCurrentIndexLinkImage = 0;

	Thread mThreadGetDataFromServer = null;
	Runnable getDataImageFromServerRunnable = new Runnable() {

		@Override
		public void run() {
			glstLinkImage.clear();
			glstLinkImage.addAll(SlideShowModel.getDataImageFromServer());
			mCurrentIndexLinkImage = 0;
			mThreadGetDataFromServer = null;
			Log.d("TAG", "Đã lấy xong ảnh từ server " + glstLinkImage.size());
		}
	};

	private void showSlide() {
		startingRunningSlide();
		scrollingImage();
	}

	private void next() {
		if (mCurrentIndexLinkImage + 1 < glstLinkImage.size()) {
			mCurrentIndexLinkImage++;
			UrlImageViewHelper.setUrlDrawable(mSlide.getImageView(),
					glstLinkImage.get(mCurrentIndexLinkImage),
					mDefaultDrawable, mDisplayHeight * mDisplayWidth,
					new UrlImageViewCallback() {

						@Override
						public void onLoaded(ImageView imageView,
								Drawable loadedDrawable, String url,
								boolean loadedFromCache) {
							if (loadedDrawable != null) {
								mDefaultDrawable = loadedDrawable;
								save(url);
								imgWidth = loadedDrawable.getIntrinsicWidth();
								imgHeight = loadedDrawable.getIntrinsicHeight();
								mScrollX = 0;
								mScrollY = 0;
							}
						}
					});
		}
	}

	private void scrollingImage() {
		mHandler.post(updateEffectBitmapRunnable);
	}

	int mScrollX = 0;
	int mScrollY = 0;
	int imgWidth = 0;
	int imgHeight = 0;

	Runnable updateEffectBitmapRunnable = new Runnable() {
		@Override
		public void run() {
			if ((imgWidth - mSlide.getImageView().getScrollX() > mDisplayWidth)
					&& (imgHeight - mSlide.getImageView().getScrollY() > mDisplayHeight)
					&& !mIsDirectionLeftToRight) {
				mSlide.getImageView().scrollTo(mScrollX++, mScrollY++);
			} else if (mScrollX <= 0 || mScrollY <= 0)
				mIsDirectionLeftToRight = false;
			else {
				mIsDirectionLeftToRight = true;
				mSlide.getImageView().scrollTo(mScrollX--, mScrollY--);
			}

			scrollingImage();
		}
	};
}