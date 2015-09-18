package com.example.myfbapp.data;

/**
 * Constants used in the project
 * 
 * @author Gerard
 */
public class MyFbAppConstants
{
	public static final String FB_OAUTH_URL     = "https://www.facebook.com/dialog/oauth?";
	public static final String client_id        = "930856970318224";
	public static final String redirect_uri     = "https://www.facebook.com/connect/login_success.html";
	public static final String ACCESS_TOKEN     = "access_token";
	public static final String SHARED_PREF_KEY  = "com.example.myfbapp";
	public static final String FB_API_URL       = "https://graph.facebook.com/v2.4/";
	public static final String USER_ID_API      = "me?fields=id";
	public static final String USER_FRIENDS_API = "{user-id}/friendlists";
	public static final String FB_USER_ID       = "user-id";
}
