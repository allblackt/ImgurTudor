package com.tudor.imgur.util;

import android.app.Activity;

public class Constants {
	
	public static Activity ref_currentActivity = null;
	
	public static final String CLIENT_ID = "YOUR_CLIENT_ID";
	public static final String CLIENT_SECRET = "YOUR_CLIENT_SECRET";
	public static final String IMUGUR_PIN_URL = "https://api.imgur.com/oauth2/pin?";
	public static final String IMUGUR_TOKEN_URL ="https://api.imgur.com/oauth2/token";
	public static final String PREFERENCES_FILE_NAME = "tudor_imgur_prefs";

	public static final String TAG = "ImgurTudor";
	
	
	public static final class SettingsMap {
		public static final String REFRESH_TOKEN         = "REFRESH_TOKEN";
		public static final String ACCESS_TOKEN          = "ACCESS_TOKEN";
		public static final String ACCESS_TOKEN_VALIDITY = "ACCESS_TOKEN_VALIDITY";
	}

	public static enum TradeItemType {
		PIN,
		REFRESH_TOKEN;
	};
	
	public static enum RequestType {
		GET,
		POST,
		PUT,
		DELETE
	}
	
	public static final class ImgurAPIURL{
		public static final String ACCOUNT_BASE   = "https://api.imgur.com/3/account/me";
		public static final String ACCOUNT_IMAGES = "https://api.imgur.com/3/account/me/images";
	}
	
}
