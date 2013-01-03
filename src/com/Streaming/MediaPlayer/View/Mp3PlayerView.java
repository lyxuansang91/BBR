package com.Streaming.MediaPlayer.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Streaming.R;
import com.Streaming.MediaPlayer.Controller.Mp3PlayerController;

public class Mp3PlayerView extends LinearLayout {

	private RelativeLayout rlBar;
	private Button btnState;
	private ProgressBar progressBar;
	private ImageView imgDislike;
	private ImageView imgLike;
	private TextView tvCurrent;
	private TextView tvDuration;
	private TextView tvTitle;

	public Mp3PlayerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControls();
	}

	public Mp3PlayerView(Context context) {
		super(context);
		initControls();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (rlBar.getVisibility() == VISIBLE) {
				rlBar.setVisibility(GONE);
			} else
				rlBar.setVisibility(VISIBLE);
		}
		return true;
	}

	private void initControls() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.mp3player_layout, this);
		Typeface font = Typeface.createFromAsset(getContext().getAssets(),
				"SEGOEUI.TTF");
		btnState = (Button) view.findViewById(R.id.btnState);
		progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
		imgDislike = (ImageView) view.findViewById(R.id.imgDislike);
		imgLike = (ImageView) view.findViewById(R.id.imageLike);
		tvCurrent = (TextView) view.findViewById(R.id.tvCurrent);
		tvDuration = (TextView) view.findViewById(R.id.tvDuration);
		tvTitle = (TextView) view.findViewById(R.id.tvTitle);
		tvTitle.setTypeface(font);
		titleEffect(tvTitle);
		rlBar = (RelativeLayout) view.findViewById(R.id.rlBar);
		rlBar.setVisibility(GONE);

		final Mp3PlayerController controller = new Mp3PlayerController(this,
				getContext());
		controller.handleMessage(Mp3PlayerController.UPDATE_STATE_BUTTON_STATE);
		controller.handleMessage(Mp3PlayerController.START_STREAMING);
		btnState.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				controller
						.handleMessage(Mp3PlayerController.BUTTON_STATE_CLICKED);
			}
		});
		imgDislike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				controller.handleMessage(Mp3PlayerController.DISLIKE_CLICK);
			}
		});
		imgLike.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				controller.handleMessage(Mp3PlayerController.LIKE_CLICK);
			}
		});
	}

	private void titleEffect(final TextView tv) {
		final Animation animLeftToRight = AnimationUtils.loadAnimation(
				getContext(), R.anim.left_to_right);
		final Animation animRightToLeft = AnimationUtils.loadAnimation(
				getContext(), R.anim.right_to_left);
		animLeftToRight.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				animRightToLeft.setAnimationListener(new AnimationListener() {

					@Override
					public void onAnimationStart(Animation animation) {
					}

					@Override
					public void onAnimationRepeat(Animation animation) {
					}

					@Override
					public void onAnimationEnd(Animation animation) {
						tv.startAnimation(animLeftToRight);
					}
				});
				tv.startAnimation(animRightToLeft);
			}
		});
		tv.startAnimation(animLeftToRight);
	}

	// Get & Set
	public RelativeLayout getRelativeBar() {
		return rlBar;
	}

	public Button getButtonState() {
		return btnState;
	}

	public ProgressBar getProgressBar() {
		return progressBar;
	}

	public ImageView getImageDisLike() {
		return imgDislike;
	}

	public ImageView getImageLike() {
		return imgLike;
	}

	public TextView getTextViewCurrent() {
		return tvCurrent;
	}

	public TextView getTextViewDuration() {
		return tvDuration;
	}

	public TextView getTextViewTitle() {
		return tvTitle;
	}
}
