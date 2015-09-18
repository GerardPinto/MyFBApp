package com.example.myfbapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.myfbapp.data.MyFbAppConstants;
import com.example.myfbapp.service.UserService;

/**
 * The Main Activity is used for connecting to FB oauth and retrieve and display
 * list of users.
 * 
 * @author Gerard
 */
public class MainActivity extends Activity
{
	private TextView userFriends    = null;
	private WebView  fbWebViewLogin = null;
	
	/*
	 * (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Button authButton = (Button) findViewById(R.id.authenticate);
		userFriends = (TextView) findViewById(R.id.userFriends);
		
		authButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String authURL = MyFbAppConstants.FB_OAUTH_URL
				        + "client_id="
				        + MyFbAppConstants.client_id
				        + "&redirect_uri="
				        + MyFbAppConstants.redirect_uri
				        + "&response_type=token&scope=user_friends,read_custom_friendlists";
				
				fbWebViewLogin = (WebView) findViewById(R.id.fbWebViewLogin);
				fbWebViewLogin.loadUrl(authURL);
				fbWebViewLogin.setVisibility(View.VISIBLE);
				
				initWebClient(fbWebViewLogin);
			}
		});
	}
	
	/**
	 * Initializes a web client to get response from web view opened in an
	 * activity.
	 * 
	 * @param webView
	 *            {@link WebView}
	 */
	private void initWebClient(WebView webView)
	{
		webView.setWebViewClient(new WebViewClient()
		{
			boolean authComplete = false;
			
			/*
			 * (non-Javadoc)
			 * @see
			 * android.webkit.WebViewClient#shouldOverrideUrlLoading(android
			 * .webkit.WebView, java.lang.String)
			 */
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url)
			{
				view.loadUrl(url);
				return true;
			}
			
			@SuppressLint("NewApi")
			@Override
			public void onPageFinished(WebView view, String url)
			{
				super.onPageFinished(view, url);
				
				if (url.contains(MyFbAppConstants.ACCESS_TOKEN)
				        && authComplete != true)
				{
					String url1 = url.replace("#", "?");
					Uri uri = Uri.parse(url1);
					String accessToken = uri
					        .getQueryParameter(MyFbAppConstants.ACCESS_TOKEN);
					
					if (accessToken != null && !accessToken.isEmpty())
					{
						authComplete = true;
						SharedPreferences sharedpreferences = getSharedPreferences(
						        MyFbAppConstants.SHARED_PREF_KEY,
						        Context.MODE_PRIVATE);
						
						Editor editor = sharedpreferences.edit();
						editor.putString(MyFbAppConstants.ACCESS_TOKEN,
						        accessToken);
						editor.commit();
						
						fbWebViewLogin.setVisibility(View.GONE);
						getUserFriendList();
					}
				}
			}
		});
	}
	
	/**
	 * Retrieves users friend list.
	 */
	private void getUserFriendList()
	{
		new UserService(this).execute(MyFbAppConstants.USER_ID_API);
		new UserService(this).execute(MyFbAppConstants.USER_FRIENDS_API);
	}
}
