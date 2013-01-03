package com.Streaming.NhatKy.View;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.Streaming.R;
import com.Streaming.NhatKy.Controller.NhatKyController;

public class NhatKyView extends LinearLayout {

	private EditText txtInput;
	private EditText txtTitle;
	private ListView listView;
	private Button btnVietNhatKy;
	private Button btnAttach;
	private EditText txtSwitch;
	private NhatKyController controller;

	public NhatKyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initControls();
	}

	public NhatKyView(Context context) {
		super(context);
		initControls();
	}

	private void initControls() {
		LayoutInflater layoutInflater = (LayoutInflater) getContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.nhat_ky_layout, this);

		Typeface fontSegoeui = Typeface.createFromAsset(getContext()
				.getAssets(), "SEGOEUI.TTF");
		txtInput = (EditText) view.findViewById(R.id.txtInput);
		txtInput.setTypeface(fontSegoeui);
		txtTitle = (EditText) view.findViewById(R.id.txtTitle);
		txtTitle.setTypeface(fontSegoeui);
		txtSwitch = (EditText) view.findViewById(R.id.txtSwitch);
		txtSwitch.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				txtInput.setFocusable(true);
				return true;
			}
		});
		listView = (ListView) view.findViewById(R.id.listView);
		btnVietNhatKy = (Button) view.findViewById(R.id.btnVietNhatKy);
		btnAttach = (Button) view.findViewById(R.id.btnAttach);
		btnAttach.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				controller.handMessage(NhatKyController.OPEN_DIALOG_FILE);
			}
		});
		btnVietNhatKy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				controller.handMessage(NhatKyController.WRITE_MESSAGE);
			}
		});
		controller = new NhatKyController(getContext(), this);
		controller.handMessage(NhatKyController.GET_DATA);
	}

	// Get

	public EditText getEditSwitch() {
		return txtSwitch;
	}

	public EditText getEditInput() {
		return txtInput;
	}

	public EditText getEditTitle() {
		return txtTitle;
	}

	public ListView getListView() {
		return listView;
	}

	public Button getButtonVietNhatKy() {
		return btnVietNhatKy;
	}

	public Button getButtonAttach() {
		return btnAttach;
	}

}
