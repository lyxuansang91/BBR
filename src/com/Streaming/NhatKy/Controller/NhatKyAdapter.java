package com.Streaming.NhatKy.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.Streaming.R;
import com.Streaming.NhatKy.Common.NhatKyObject;

public class NhatKyAdapter extends ArrayAdapter<NhatKyObject> {
	private ArrayList<NhatKyObject> glstNhatKy;
	private Context mContext;

	public NhatKyAdapter(Context context, int textViewResourceId,
			ArrayList<NhatKyObject> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.glstNhatKy = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final NhatKyObject nhatky = (NhatKyObject) getItem(position);
		if (nhatky != null) {
			if (convertView == null) {
				LayoutInflater inf = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.listitem_discuss, null);
			}
			LinearLayout llContent = (LinearLayout) convertView
					.findViewById(R.id.llContent);

			llContent.removeAllViews();
			TextView tvComment = new TextView(mContext);
			tvComment.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			tvComment.setTextColor(Color.BLUE);
			tvComment.setText(nhatky.getMessage());
			llContent.addView(tvComment);
			if (nhatky.getIsImage()) {
				ImageView img = new ImageView(mContext);
				BitmapFactory.Options opt = new BitmapFactory.Options();
				opt.inSampleSize = 8;
				Bitmap bm = BitmapFactory
						.decodeFile(nhatky.getImagePath(), opt);
				Log.d("TAG", nhatky.getImagePath());
				if (bm != null) {
					img.setLayoutParams(new LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT));
					img.setImageBitmap(bm);
					llContent.addView(img);
				}
			}

			Date d = nhatky.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			String str = sdf.format(d);
			TextView time = (TextView) convertView.findViewById(R.id.tvTime);
			time.setText(str);
			TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
			title.setText(nhatky.getTitle());
		}
		return convertView;
	}

	@Override
	public NhatKyObject getItem(int position) {
		return glstNhatKy.get(position);
	}
}
