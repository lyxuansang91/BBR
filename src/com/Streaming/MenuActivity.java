package com.Streaming;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends Activity {

	private Button btnNhacChoBe;
	private Button btnNhacChoMe;
	private Button btnMuaSam;
	private Button btnThongTin;
	private Button btnNhatKy;
	private Button btnUocNguyen;

	private TextView tvNhacChobe;
	private TextView tvNhacChoMe;
	private TextView tvMuaSam;
	private TextView tvThongTin;
	private TextView tvNhatKy;
	private TextView tvUocNguyen;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		initControls();
	}

	private void initControls() {
		btnNhacChoBe = (Button) findViewById(R.id.btnNhacChoBe);
		btnNhacChoMe = (Button) findViewById(R.id.btnNhacChoMe);
		btnMuaSam = (Button) findViewById(R.id.btnMuaSam);
		btnThongTin = (Button) findViewById(R.id.btnThongTin);
		btnNhatKy = (Button) findViewById(R.id.btnNhatKy);
		btnUocNguyen = (Button) findViewById(R.id.btnUocNguyen);

		tvNhacChobe = (TextView) findViewById(R.id.tvNhacChoBe);
		tvNhacChoMe = (TextView) findViewById(R.id.tvNhacChoMe);
		tvMuaSam = (TextView) findViewById(R.id.tvMuaSam);
		tvThongTin = (TextView) findViewById(R.id.tvThongTin);
		tvNhatKy = (TextView) findViewById(R.id.tvNhatKy);
		tvUocNguyen = (TextView) findViewById(R.id.tvUocNguyen);

		btnNhacChoBe.setOnClickListener(nhacChoBe_OnClick);
		btnNhacChoMe.setOnClickListener(nhacChoMe_OnClick);
		btnMuaSam.setOnClickListener(shopping_OnClick);
		btnThongTin.setOnClickListener(thongTin_OnClick);
		btnNhatKy.setOnClickListener(nhatKy_OnClick);
		btnUocNguyen.setOnClickListener(uocNguyen_OnClick);

	}

	OnClickListener nhacChoMe_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					MusicActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener nhacChoBe_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					MusicActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener shopping_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					ShoppingActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener nhatKy_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					NhatkyActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener thongTin_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getApplicationContext(),
					NewsActivity.class);
			startActivity(intent);
		}
	};

	OnClickListener uocNguyen_OnClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
		}
	};
}
