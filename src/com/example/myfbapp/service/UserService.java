package com.example.myfbapp.service;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.TextView;

import com.example.myfbapp.R;
import com.example.myfbapp.data.MyFbAppConstants;

/**
 * @author Gerard
 */
public class UserService extends AsyncTask<String, String, JSONObject>
{
	private final Activity activity;
	private String         type;
	
	public UserService(Activity activity)
	{
		this.activity = activity;
	}
	
	/*
	 * (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected JSONObject doInBackground(String... params)
	{
		JSONObject responseJSON = new JSONObject();
		String url = MyFbAppConstants.FB_API_URL;
		type = params[0];
		SharedPreferences sharedPreferences = this.activity
		        .getSharedPreferences(MyFbAppConstants.SHARED_PREF_KEY,
		                Context.MODE_PRIVATE);
		String accessToken = "&"
		        + MyFbAppConstants.ACCESS_TOKEN
		        + "="
		        + sharedPreferences
		                .getString(MyFbAppConstants.ACCESS_TOKEN, "");
		String userId = sharedPreferences.getString(
		        MyFbAppConstants.FB_USER_ID, "");
		
		if (MyFbAppConstants.USER_ID_API.equals(type))
		{
			url = url + MyFbAppConstants.USER_ID_API + accessToken;
		}
		else
		{
			if (MyFbAppConstants.USER_FRIENDS_API.equals(type))
			{
				url = url
				        + MyFbAppConstants.USER_FRIENDS_API.replace(
				                "{user-id}", userId)
				        + "?"
				        + MyFbAppConstants.ACCESS_TOKEN
				        + "="
				        + sharedPreferences.getString(
				                MyFbAppConstants.ACCESS_TOKEN, "");
			}
		}
		
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(url);
		try
		{
			HttpResponse response = client.execute(request);
			String responseData = IOUtils.toString(response.getEntity()
			        .getContent());
			if (MyFbAppConstants.USER_ID_API.equals(type))
			{
				responseJSON = new JSONObject(responseData);
				parseAndStoreUserId(responseJSON);
			}
			else
			{
				responseJSON = new JSONObject(responseData);
				TextView userFriends = (TextView) activity
				        .findViewById(R.id.userFriends);
				userFriends.setText(responseJSON.toString());
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return responseJSON;
	}
	
	private void parseAndStoreUserId(JSONObject responseJSON)
	        throws JSONException
	{
		SharedPreferences sharedpreferences = activity.getSharedPreferences(
		        MyFbAppConstants.SHARED_PREF_KEY, Context.MODE_PRIVATE);
		
		Editor editor = sharedpreferences.edit();
		editor.putString(MyFbAppConstants.FB_USER_ID,
		        (String) responseJSON.get("id"));
		editor.commit();
	}
}
