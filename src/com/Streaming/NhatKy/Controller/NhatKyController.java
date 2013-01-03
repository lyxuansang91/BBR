package com.Streaming.NhatKy.Controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.Streaming.R;
import com.Streaming.NhatKy.Common.NhatKyObject;
import com.Streaming.NhatKy.Model.NhatKyModel;
import com.Streaming.NhatKy.View.FolderLayout;
import com.Streaming.NhatKy.View.NhatKyView;

public class NhatKyController {
	private Context mContext;
	private NhatKyView nhatkyView;

	private ArrayList<NhatKyObject> glstNhatky;
	private NhatKyAdapter adapterNhatky;
	private NhatKyModel modelNhatky;

	public NhatKyController(Context context, NhatKyView view) {
		nhatkyView = view;
		mContext = context;
		nhatkyView.getListView().setFocusable(true);
		nhatkyView.getListView().setOnItemLongClickListener(
				new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int arg2, long arg3) {
						final NhatKyObject nk = modelNhatky
								.getNhatKyByID(adapterNhatky.getItem(arg2)
										.getID());
						if (nk != null) {
							LayoutInflater inf = (LayoutInflater) mContext
									.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
							final Dialog dl = new Dialog(mContext);
							final View v = inf.inflate(R.layout.dialog_layout,
									null);

							Button btnOK = (Button) v.findViewById(R.id.btnOK);
							Button btnCancel = (Button) v
									.findViewById(R.id.btnCancle);
							btnOK.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									boolean del = modelNhatky.delete(nk.getID());
									if (del) {
										adapterNhatky.remove(nk);
										new File(nk.getImagePath()).delete();
										adapterNhatky.notifyDataSetChanged();
									}
									loadData();
									dl.dismiss();
								}
							});
							btnCancel.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									dl.dismiss();
								}
							});
							dl.setContentView(v);
							dl.setTitle("Thông báo");
							dl.show();
						}
						return false;
					}
				});
		modelNhatky = new NhatKyModel(mContext);
		glstNhatky = new ArrayList<NhatKyObject>();
		adapterNhatky = new NhatKyAdapter(mContext,
				android.R.layout.simple_list_item_1, glstNhatky);
		nhatkyView.getListView().setStackFromBottom(true);
		nhatkyView.getListView().setAdapter(adapterNhatky);
	}

	public static final String WRITE_MESSAGE = "write message";
	public static final String GET_DATA = "get data";
	public static final String OPEN_DIALOG_FILE = "open dialog file";

	public void handMessage(String what) {
		if (what.equals(GET_DATA)) {
			loadData();
		} else if (what.equals(WRITE_MESSAGE)) {
			writeMessage();
		} else if (what.equals(OPEN_DIALOG_FILE)) {
			openDialogFile();
		}
	}

	private String pathImage = "";

	private void openDialogFile() {
		Dialog dialog = new Dialog(mContext);
		FolderLayout localFolders = new FolderLayout(mContext);
		localFolders.setIFolderItemListener(new IFolderItemListener() {

			@Override
			public void OnFileClicked(final File file) {
				if (checkFileType(file.getName())) {
					createFolderInSdCard();
					pathImage = file.getPath();
					Toast.makeText(mContext, "Bạn đã chọn ảnh " + pathImage,
							Toast.LENGTH_LONG).show();
				}
			}

			@Override
			public void OnCannotFileRead(File file) {
				new AlertDialog.Builder(mContext)
						.setIcon(R.drawable.ic_launcher)
						.setTitle(
								"[" + file.getName()
										+ "] thư mục này không thể mở")
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
			}
		});
		dialog.setContentView(localFolders);
		dialog.setTitle("Local");
		dialog.show();
		localFolders.setDir("./sys");

	}

	private boolean checkFileType(String fileName) {
		int dotPosition = fileName.lastIndexOf(".");
		if (dotPosition >= 0) {
			String ext = fileName.substring(dotPosition + 1, fileName.length());
			if (ext.toLowerCase().equals("png")
					|| ext.toLowerCase().equals("jpg")
					|| ext.toLowerCase().equals("gif")) {
				return true;
			}
		}
		return false;
	}

	private String getFileType(String fileName) {
		int dotPosition = fileName.lastIndexOf(".");
		if (dotPosition >= 0)
			return fileName.substring(dotPosition + 1, fileName.length());
		return null;
	}

	String pathSDCard = Environment.getExternalStorageDirectory() + "/Baby";

	private boolean createFolderInSdCard() {
		File folder = new File(pathSDCard);
		boolean success = false;
		if (!folder.exists()) {
			success = folder.mkdir();
		}
		return success;
	}

	private void loadData() {
		glstNhatky.clear();
		glstNhatky.addAll(modelNhatky.getAllNhatKy());
		adapterNhatky.notifyDataSetChanged();
		nhatkyView.getListView().setSelection(adapterNhatky.getCount());
	}

	private void writeMessage() {
		long id = 0;
		if (pathImage.length() > 0) {
			id = modelNhatky.insertHasImage(nhatkyView.getEditTitle().getText()
					.toString(),
					nhatkyView.getEditInput().getText().toString(), new Date(),
					pathImage);
			if (id > 0) {
				if (pathImage.length() > 0) {
					File filesrc = new File(pathImage);
					File filedst = new File(pathSDCard + "/" + id + "."
							+ getFileType(filesrc.getName()));
					if (filesrc.exists()) {
						boolean is = copyFile(filesrc, filedst);
						if (is)
							modelNhatky.updateImage(id, filedst.getPath());
					}
				}
				pathImage = "";
			}
		} else {
			id = modelNhatky.insertNotHasImage(nhatkyView.getEditTitle()
					.getText().toString(), nhatkyView.getEditInput().getText()
					.toString(), new Date());
		}
		nhatkyView.getEditInput().setText("");
		nhatkyView.getEditTitle().setText("");
		loadData();
	}

	public boolean copyFile(File source, File dest) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;

		try {
			bis = new BufferedInputStream(new FileInputStream(source));
			bos = new BufferedOutputStream(new FileOutputStream(dest, false));

			byte[] buf = new byte[1024];
			bis.read(buf);

			do {
				bos.write(buf);
			} while (bis.read(buf) != -1);
		} catch (IOException e) {
			return false;
		} finally {
			try {
				if (bis != null)
					bis.close();
				if (bos != null)
					bos.close();
			} catch (IOException e) {
				return false;
			}
		}

		return true;
	}
}
