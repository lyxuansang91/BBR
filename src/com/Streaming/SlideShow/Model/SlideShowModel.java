package com.Streaming.SlideShow.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.Streaming.Configuration;

public class SlideShowModel {
	public static ArrayList<String> getDataImageFromServer() {
		ArrayList<String> glstAnh = new ArrayList<String>();
		try {
			// Read all the text returned by the server
			URL url = new URL(Configuration.IPPICTURE);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			String html = "";
			while ((str = in.readLine()) != null) {
				html += str;
			}
			in.close();
			JSONArray entries = new JSONArray(html);
			for (int i = 0; i < entries.length(); i++) {
				String imagePath = entries.getString(i);
				glstAnh.add(imagePath);
			}
		} catch (MalformedURLException e) {
			Log.d("TAG",e.getMessage());
		} catch (IOException e) {
			Log.d("TAG",e.getMessage());
		} catch (JSONException e) {
			Log.d("TAG",e.getMessage());
		}
		Log.d("TAG", "Số lượng ảnh lấy được từ server là " + glstAnh.size());
		return glstAnh;
	}
}
