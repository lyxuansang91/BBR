package com.Streaming.New.Controller;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Streaming.R;
import com.Streaming.New.Common.NewObject;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class NewAdapter extends ArrayAdapter<NewObject> {

	private ArrayList<NewObject> glstNews;
	private Context mContext;
	private Typeface fontSegoe;
	private Typeface fontSego;

	public NewAdapter(Context context, int textViewResourceId,
			ArrayList<NewObject> objects) {
		super(context, textViewResourceId, objects);
		this.mContext = context;
		this.glstNews = objects;
		fontSegoe = Typeface.createFromAsset(mContext.getAssets(), "segoe.ttf");
		fontSego = Typeface.createFromAsset(mContext.getAssets(), "SEGOEUI.TTF");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final NewObject news = (NewObject) getItem(position);
		if (news != null) {
			if (convertView == null) {
				LayoutInflater inf = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inf.inflate(R.layout.news_item, null);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.imgThumb);
			TextView title = (TextView) convertView.findViewById(R.id.tvTitle);
			title.setTypeface(fontSego);
			TextView des = (TextView) convertView.findViewById(R.id.tvDes);
			des.setTypeface(fontSegoe);
			UrlImageViewHelper.setUrlDrawable(img, news.getThumb());
			title.setText(news.getTitle());
			des.setText(news.getDescription());
		}
		return convertView;
	}

	@Override
	public NewObject getItem(int position) {
		return glstNews.get(position);
	}
}
