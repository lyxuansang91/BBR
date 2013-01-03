package com.Streaming.New.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;

import com.Streaming.New.Common.NewObject;

public class NewModel {

	public static ArrayList<NewObject> getNewsFromTheServer(String url) {
		ArrayList<NewObject> news = new ArrayList<NewObject>();
		try {
			URL u = new URL(url);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					u.openStream()));
			String str;
			String html = "";
			while ((str = in.readLine()) != null) {
				html += str;
			}
			in.close();
			JSONArray entries = new JSONArray(html);
			for (int i = 0; i < entries.length(); i++) {
				String des = entries.getJSONObject(i).getString(
						"shortDescription");
				String thumb = entries.getJSONObject(i).getString("thumb");
				String id = entries.getJSONObject(i).getString("id");
				String title = entries.getJSONObject(i).getString("title");
				NewObject n = new NewObject(id, des, thumb, title);
				news.add(n);
			}
		} catch (MalformedURLException e) {
			Log.d("TAG", e.getMessage());
		} catch (IOException e) {
			Log.d("TAG", e.getMessage());
		} catch (JSONException e) {
			Log.d("TAG", e.getMessage());
		}
		return news;
	}

}
