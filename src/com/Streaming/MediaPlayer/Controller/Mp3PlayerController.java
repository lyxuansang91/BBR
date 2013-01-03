package com.Streaming.MediaPlayer.Controller;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.util.Log;

import com.Streaming.Configuration;
import com.Streaming.R;
import com.Streaming.MediaPlayer.Common.SongObject;
import com.Streaming.MediaPlayer.Model.Mp3PlayerModel;
import com.Streaming.MediaPlayer.View.Mp3PlayerView;
import com.ThreadQueue.RecursiveThreadOp;
import com.ThreadQueue.SBThread;
import com.ThreadQueue.ThreadQueue;

public class Mp3PlayerController implements OnBufferingUpdateListener,
		OnCompletionListener {
	// /////////////////////////////////////////////////////////////////////
	// Fields
	// /////////////////////////////////////////////////////////////////////
	private Mp3PlayerView mp3View;
	private MediaPlayer mp3Player; // MediaPlayer để phát nhạc
	private Handler mHandler = new Handler(); // Handler quản lý vòng lặp
	private Mp3PlayerModel mMp3PlayerModel; // Model truy cập csdl
	private Context mContext; // Context của app
	private SongObject songCurrent; // Bài hát hiện tại đang phát
	private SongObject songPrev; // Bài hát cũ
	private SBThread mThreadUnLike = null; // Thread Unlike
	private SBThread mThreadLike = null; // Thread Like
	private SBThread mThreadGetMusicFromServer; // Thread lấy nhạc
	private SBThread mThreadStreaming = null; // Thread streaming
	private String mDeviceId; // ID Thiết bị
	
	private boolean mIsClickLike = true;//Đã nhấn like chưa?
	private boolean mIsClickUnlike = true;//Đã nhấn unlike chưa?

	private ThreadQueue mThreadQueue = new ThreadQueue();
	private RecursiveThreadOp op = new RecursiveThreadOp();
	// /////////////////////////////////////////////////////////////////////
	// Constraint
	// /////////////////////////////////////////////////////////////////////
	private final String LINK_MP3 = Configuration.IPSONG; // Ip nhạc
	private final String PAGE = "page";
	private final String ZERO_TIME = "00 : 00 : 00";
	private final String UNLIKE = "Unlike";
	private final String LIKE = "Like";
	private final String GETMUSIC_FROM_THESERVER_THREAD = "Get music from the server";
	private final String STARTSTREAMING_THREAD = "Start Streaming";
	

	public static final String BUTTON_STATE_CLICKED = "button state clicked";
	public static final String UPDATE_STATE_BUTTON_STATE = "Update state button state";
	public static final String START_STREAMING = "start streaming";
	public static final String DISLIKE_CLICK = "dislike click";
	public static final String LIKE_CLICK = "like click";

	public Mp3PlayerController(Mp3PlayerView view, Context context) {
		mp3View = view;
		mThreadQueue.setMaxNumberOfConcurrentThreads(5);
		mThreadQueue.addToQueue(op);
		mp3View.getProgressBar().setEnabled(false);
		mContext = context;
		mp3View.getTextViewCurrent().setText(ZERO_TIME);
		mp3View.getTextViewDuration().setText(ZERO_TIME);
		mMp3PlayerModel = new Mp3PlayerModel(mContext);
		mDeviceId = Secure.getString(mContext.getContentResolver(),
				Secure.ANDROID_ID);
	}

	/**
	 * đưa yêu cầu từ view lên controller qua hàm này
	 * 
	 * @param what
	 *            : yêu cầu là ?
	 * @return thành công
	 */
	public boolean handleMessage(String what) {
		if (what.equals(BUTTON_STATE_CLICKED))
			return buttonStateClicked();
		else if (what.equals(UPDATE_STATE_BUTTON_STATE)) {
			mHandler.removeCallbacks(updateState);
			updateStateButtonState();
		} else if (what.equals(START_STREAMING)) {
			startStreaming();
			updateTimeTask();
		} else if (what.equals(DISLIKE_CLICK))
			disLikeClick();
		else if (what.equals(LIKE_CLICK))
			likeClick();
		return true;
	}

	private void like(final String idLink) {
		if (mThreadLike == null) {
			mThreadLike = new SBThread(LIKE) {

				@Override
				public void runAsync() {
					// 1 ký hiệu là like
					String url = Configuration.IPLIKE + mDeviceId + "/"
							+ "android" + "/1/" + idLink;
					boolean i = mMp3PlayerModel.isRequestLike(url);
					if (i)
						mIsClickLike = false;
					Log.d("TAG", "Đã like  "
							+ (i ? " thành công " : " thất bại "));
					mThreadLike = null;
				}
			};
			mThreadQueue.addToQueue(mThreadLike);
		}
	}

	private void unlike(final String idLink) {
		if (mThreadUnLike == null) {
			mThreadUnLike = new SBThread(UNLIKE) {

				@Override
				public void runAsync() {
					// -1 ký hiệu là unlike
					String url = Configuration.IPLIKE + mDeviceId + "/"
							+ "android" + "/-1/" + idLink;
					boolean i = mMp3PlayerModel.isRequestLike(url);
					if (i) {
						mIsClickUnlike = false;
					}
					Log.d("TAG", "Đã unlike  "
							+ (i ? "thành công " : "Thất bại"));
					mThreadUnLike = null;
				}
			};
			mThreadQueue.addToQueue(mThreadUnLike);
		}
	}

	private void disLikeClick() {
		if (songCurrent != null) {
			next();
			unlike(songCurrent.getIdSong());
		}
	}

	private void likeClick() {
		if (songCurrent != null) {
			back();
			like(songCurrent.getIdSong());
		}
	}

	/**
	 * thay đổi trạng thái của nút button state
	 */
	private void updateStateButtonState() {
		mHandler.post(updateState);
	}

	Runnable updateState = new Runnable() {

		@Override
		public void run() {
			mp3View.getButtonState().setBackgroundDrawable(null);
			try {
				if (mp3Player != null) {
					if (mp3Player.isPlaying()) {
						mp3View.getButtonState().setBackgroundResource(
								R.drawable.pausedisabled);
						mp3View.getTextViewCurrent().setText(
								String.valueOf(renderTime(mp3Player
										.getCurrentPosition())));
						mp3View.getTextViewDuration().setText(
								String.valueOf(renderTime(mp3Player
										.getDuration())));
						mp3View.getTextViewTitle().setText(
								songCurrent.getTitle());

					} else
						mp3View.getButtonState().setBackgroundResource(
								R.drawable.playdisabled);
				} else
					mp3View.getButtonState().setBackgroundResource(
							R.drawable.playdisabled);
			} catch (Exception ex) {
				mp3View.getButtonState().setBackgroundResource(
						R.drawable.playdisabled);
			}
			updateStateButtonState();
		}
	};

	/**
	 * sự kiện khi nút state đc nhấn
	 * 
	 * @return thành công
	 */
	private boolean buttonStateClicked() {
		try {
			if (mp3Player != null) {
				if (mp3Player.isPlaying())
					pauseStreaming();
				else
					startStreaming();
			} else
				startStreaming();
		} catch (Exception ex) {
		}
		return true;
	}

	/**
	 * tiến hành streaming
	 * 
	 * @param url
	 *            : đường dẫn url cần streaming
	 */
	private void startStreaming(final SongObject s) {
		if (mThreadStreaming == null) {
			Log.d("TAG", "Bắt đầu tiến hành streaming url : " + s.getLink());
			mThreadStreaming = new SBThread(STARTSTREAMING_THREAD) {

				@Override
				public void runAsync() {
					try {
						if (mp3Player == null) {
							mp3Player = new MediaPlayer();
							mp3Player
									.setOnBufferingUpdateListener(Mp3PlayerController.this);
							mp3Player
									.setOnCompletionListener(Mp3PlayerController.this);
							mp3Player.reset();
							mp3Player.setDataSource(s.getLink());
							mp3Player
									.setAudioStreamType(AudioManager.STREAM_MUSIC);
							mp3Player.prepare();
							mp3Player.start();
							mMp3PlayerModel.delete(s.getID());
							mIsClickLike = true;
							mIsClickUnlike = true;
						} else {
							Log.d("TAG",
									"Streaming đang chạy , bị pause từ ng dùng , vì vậy sẽ tiếp tục bài hát");
							mp3Player.start();
						}
						mThreadStreaming = null;
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (IllegalStateException e) {
						Log.d("TAG",
								"Streaming bị lỗi , Streaming sẽ được khởi tạo lại");
						destroyPlayer();
						mMp3PlayerModel.delete(s.getID());
						mThreadStreaming = null;
						startStreaming();
					} catch (IOException e) {
						Log.d("TAG",
								"Streaming bị lỗi , Streaming sẽ được khởi tạo lại");
						destroyPlayer();
						mMp3PlayerModel.delete(s.getID());
						mThreadStreaming = null;
						startStreaming();
					}
					Log.d("TAG", "Tiến trình streaming đã kết thúc");
				}
			};
			mThreadQueue.addToQueue(mThreadStreaming);
		}
	}

	/**
	 * tiến hành streaming , có lấy dữ liệu
	 */
	public void startStreaming() {
		ArrayList<SongObject> glstSong = mMp3PlayerModel.getAllSong();
		Log.d("TAG",
				"Số lượng bài hát trong sqlite còn lại là : " + glstSong.size());
		if (glstSong.size() > 0) {
			SongObject s = glstSong.get(0);
			songPrev = songCurrent;
			songCurrent = s;
			startStreaming(s);
		} else {
			if (mThreadGetMusicFromServer == null) {
				mThreadGetMusicFromServer = new SBThread(
						GETMUSIC_FROM_THESERVER_THREAD) {

					@Override
					public void runAsync() {
						SharedPreferences preferences = PreferenceManager
								.getDefaultSharedPreferences(mContext);
						SharedPreferences.Editor editor = preferences.edit();
						String linknhac = LINK_MP3
								+ (preferences.getInt(PAGE, 0) + 1);
						Log.d("TAG", "Lấy dữ liệu nhạc từ đường dẫn : "
								+ linknhac);
						try {
							ArrayList<SongObject> glst = mMp3PlayerModel
									.LayNhac(linknhac + "/json");

							if (glst.size() > 0) {
								editor.putInt(PAGE,
										preferences.getInt(PAGE, 0) + 1);
								editor.commit();
								for (SongObject s : glst) {
									mMp3PlayerModel.insert(s.getLink(),
											s.getIdSong(), s.getTitle());
								}
							}
						} catch (Exception e) {
							if (e.getMessage().equals(Mp3PlayerModel.EX_MAX)) {
								editor.putInt(PAGE, 0);
								editor.commit();
								Log.d("TAG",
										"Trang nhạc ứng dụng đang lấy là = "
												+ preferences.getInt(PAGE, 0));
							}
						}
						Log.d("TAG", "Đã lấy xong dữ liệu nhạc");
						mThreadGetMusicFromServer = null;
						startStreaming();
					}
				};
				mThreadQueue.addToQueue(mThreadGetMusicFromServer);
			}
		}
	}

	private void next() {
		destroyPlayer();
		startStreaming();
	}

	private void back() {
		destroyPlayer();
		if (songPrev != null && songCurrent != null) {
			SongObject tmp = new SongObject(songCurrent.getID(),
					songCurrent.getLink(), songCurrent.getIdSong(),
					songCurrent.getTitle());
			if (!songCurrent.equals(songPrev)) {
				songCurrent = new SongObject(songPrev.getID(),
						songPrev.getLink(), songPrev.getIdSong(),
						songPrev.getTitle());
				songPrev = tmp;
			}
			startStreaming(songPrev);
		} else
			startStreaming();
	}

	/**
	 * tạm dừng streaming
	 */
	private void pauseStreaming() {
		mp3Player.pause();
	}

	/**
	 * hủy player
	 */
	private void destroyPlayer() {
		if (mp3Player != null && mThreadStreaming == null) {
			try {
				mp3Player.stop();
				mp3View.getProgressBar().setSecondaryProgress(0);
			} catch (Exception e) {
			}
			mp3Player.release();
			try {
			} catch (Exception e) {
			}
			mp3Player = null;
		}
	}

	/**
	 * di chuyển seekbar
	 */
	private void updateTimeTask() {
		mHandler.post(updateSeekBar);
	}

	private Runnable updateSeekBar = new Runnable() {

		@Override
		public void run() {
			try {
				if (mp3Player != null) {
					if (mp3Player.isPlaying()) {
						mp3View.getImageLike().setEnabled(mIsClickLike);
						mp3View.getImageDisLike().setEnabled(mIsClickUnlike);
						mp3View.getProgressBar()
								.setMax(mp3Player.getDuration());
						int currentSeekBar = mp3Player.getCurrentPosition();
						mp3View.getProgressBar().setProgress(currentSeekBar);
					}
				}
			} catch (Exception ex) {
			}
			mHandler.postDelayed(this, 50);
		}
	};

	/**
	 * Render Time
	 * @param time
	 * @return
	 */
	private String renderTime(int time) {
		int hours = time / (1000 * 60 * 60);
		int min = (time % (1000 * 60 * 60)) / (1000 * 60);
		int sec = (time % (1000 * 60 * 60)) % (1000 * 60) / 1000;
		if (hours <= 24 && hours >= 0 && min <= 60 && min >= 0 && sec <= 60
				&& sec >= 0)
			return (hours == 0 ? "00 : " : String.valueOf(hours) + " : ")
					+ (min > 9 ? String.valueOf(min) : "0"
							+ String.valueOf(min))
					+ " : "
					+ (sec > 9 ? String.valueOf(sec) : "0"
							+ String.valueOf(sec));
		return (ZERO_TIME);
	}

	@Override
	public void onBufferingUpdate(MediaPlayer mp, int percent) {
		if (mp3Player != null) {
			if (percent <= 100) {
				mp3View.getProgressBar().setSecondaryProgress(
						mp3Player.getDuration() * percent / 100);
			}
		} else {
			mp3View.getProgressBar().setSecondaryProgress(0);
		}
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		mp3View.getProgressBar().setProgress(0);
		destroyPlayer();
		startStreaming();
	}
}
