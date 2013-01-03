package com.Streaming.ToastLoiKhuyen.View;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.Streaming.R;

public class CustomToast extends Toast {

	public CustomToast(Context context) {
		super(context);
		this.mContext = context;
		init();
	}

	private Context mContext;
	private View mView;
	private TextView msg;

	private static ArrayList<Integer> glstImageView = new ArrayList<Integer>();
	private ImageView imageView;

	private void init() {
		Typeface font = Typeface.createFromAsset(mContext.getAssets(),
				"segoe.ttf");
		mView = View.inflate(mContext, R.layout.customize_toast_layout, null);
		msg = (TextView) mView.findViewById(R.id.msg);
		msg.setTextColor(Color.BLACK);
		msg.setTypeface(font);
		imageView = (ImageView) mView.findViewById(R.id.imageView);
		mView.setFocusable(true);
		glstImageView.add(R.drawable.img0);
		glstImageView.add(R.drawable.img1);
		glstImageView.add(R.drawable.img2);
		glstImageView.add(R.drawable.img3);
		glstImageView.add(R.drawable.img4);
		glstImageView.add(R.drawable.img5);
		glstImageView.add(R.drawable.img6);
		glstImageView.add(R.drawable.img7);
		glstImageView.add(R.drawable.img8);
	}

	public void show(String text) {
		int randImage = (new Random()).nextInt(9);
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inDither = false; // Disable Dithering mode
		opts.inPurgeable = true;
		opts.inSampleSize = 8;
		Bitmap bm = BitmapFactory.decodeResource(mContext.getResources(),
				glstImageView.get(randImage), opts);
		imageView.setImageBitmap(null);
		imageView.setImageBitmap(bm);
		msg.setText(text);
		setView(mView);
		setDuration(Toast.LENGTH_LONG);
		setGravity(Gravity.CENTER, 0, 0);
		toastHackTime(10);
	}

	int count;

	private void toastHackTime(final int secHack) {
		mHandler.post(update);
	}

	public void cancel() {
		count = 0;
		mHandler.removeCallbacks(update);
		super.cancel();
	}

	Runnable update = new Runnable() {
		@Override
		public void run() {
			if (count <= 10) {
				show();
				count++;
				mHandler.postDelayed(update, 1000);
			} else {
				cancel();
			}
		}
	};

	private Handler mHandler = new Handler();

}
