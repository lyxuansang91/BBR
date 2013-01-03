package com.Streaming.ToastLoiKhuyen.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.Streaming.Configuration;

public class ToastModel {
	public static String getAdviceFromServer() {
		try {
			// Read all the text returned by the server
			URL url = new URL(Configuration.IPCONTENT);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					url.openStream()));
			String str;
			String html = "";
			while ((str = in.readLine()) != null) {
				html += str;
			}
			in.close();
			JSONArray entries = new JSONArray(html);
			JSONObject post = entries.getJSONObject(0);
			String content = post.getString("content");
			return content;
		} catch (MalformedURLException e) {
		} catch (IOException e) {
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}
}
