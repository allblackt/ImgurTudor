package com.tudor.imgur;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Window;
import android.widget.RelativeLayout;

import com.tudor.imgur.util.Constants;
import com.tudor.imgur.util.ImgurLogin;
import com.tudor.imgur.util.Constants.TradeItemType;

public class Main extends Activity {

	private static final String TAG = "MainActivity";
	
	private String PIN;
	private ProgressDialog mDialog;
	private SharedPreferences settings;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Constants.ref_currentActivity = this;
		settings = getSharedPreferences(Constants.PREFERENCES_FILE_NAME, 0);
		RelativeLayout root = (RelativeLayout) findViewById(R.id.root);

		String auth = settings.getString(Constants.SettingsMap.ACCESS_TOKEN, null);
		String refresh = settings.getString(Constants.SettingsMap.REFRESH_TOKEN, null);
		Long token_exp = settings.getLong(Constants.SettingsMap.ACCESS_TOKEN_VALIDITY, System.currentTimeMillis()/1000);
		/* Force refresh */
		token_exp = System.currentTimeMillis()/1000;
		if(refresh == null){
			/* First application run */
			Log.d(TAG, "Refresh token is null, getting a new one...");
			ImgurLogin login = new ImgurLogin(root, this);
			login.getPin();
		} else if ((token_exp - System.currentTimeMillis()/1000) < 30) {
			/* Get new authorization token */
			Log.d(TAG, "Token expired, getting a new one...");
			ImgurLogin login = new ImgurLogin(root, this);
			//login.getOAuth2Token(refresh, TradeItemType.REFRESH_TOKEN);
		} else {
			/* Trigger onLoginComplete */
			Log.d(TAG, "Seems like everything is ready to start making calls...");
			this.onLoginCompleted();
		}

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	
	
	@Override
	protected void onResume() {
		super.onResume();
		Constants.ref_currentActivity = this;
	}

	public void onPinLoaded() {
		System.out.println(PIN);
		ImgurLogin login = new ImgurLogin(null, this);
		login.getOAuth2Token(PIN, TradeItemType.PIN);
	}

	public String getPIN() {
		return PIN;
	}

	public void setPIN(String PIN) {
		this.PIN = PIN;
	}

	public void showLoading() {
		mDialog = new ProgressDialog(this);
		mDialog.setMessage("Loading...");
		mDialog.setCancelable(false);
		mDialog.show();
	}

	public void hideLoading(){
		if (mDialog != null){
			mDialog.dismiss();
		}
	}

	public void onLoginCompleted() {
		/* Insert logic for login completed */
		Log.d(TAG, "Login was completed");
		/* Transition to the gridview which will hold the images */
		Intent viewAllImages = new Intent(this,ImageGridView.class);
        this.startActivity(viewAllImages); 
	}
}
