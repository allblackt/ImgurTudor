package com.tudor.imgur.APICalls;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.tudor.imgur.Main;
import com.tudor.imgur.R;
import com.tudor.imgur.util.Constants;
import com.tudor.imgur.util.Constants.TradeItemType;
import com.tudor.imgur.util.http.MyHttpClientFactory;

public class ImgurLogin {

	View RootLayout;
	Context context;
	private final String TAG = this.getClass().getName();

	public ImgurLogin(View rootLayout, Context context) {
		this.RootLayout = rootLayout;
		this.context = context;
	}

	public void getPin() {
		String URL = "https://api.imgur.com/oauth2/authorize?client_id="
				+ Constants.CLIENT_ID + "&response_type=pin&state=no_state";

		WebView imgurLogin = (WebView) RootLayout.findViewById(R.id.imgurLogin);
		imgurLogin.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d(TAG, "Page finished loading. URL: " + url);
				super.onPageFinished(view, url);
				String loadedURL = view.getUrl();
				if (loadedURL.startsWith(Constants.IMUGUR_PIN_URL)) {
					String kvps[] = loadedURL.split("\\?")[1].split("&");
					for (String kvp : kvps) {
						if (kvp.startsWith("pin")) {
							String PIN = kvp.split("=")[1];
							((Main) context).setPIN(PIN);
							((Main) context).onPinLoaded();
							((RelativeLayout) RootLayout).removeView(view);
						}
					}
				}
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// TODO Auto-generated method stub
				super.onReceivedError(view, errorCode, description, failingUrl);
			}
		});
		imgurLogin.loadUrl(URL);
		// System.out.println(imgurLogin.getTitle());
	}

	public void getOAuth2Token(String trade_item, final TradeItemType type) {
		
		Log.d(TAG, "Started getting Oauth2 token...");
		new AsyncTask<String, Void, BasicNameValuePair>() {
			protected void onPreExecute() {
				((Main)context).showLoading();
			}

			protected BasicNameValuePair doInBackground(String... aParams) {
				
				Log.d(TAG, "Entered doInBackground...");
				HttpResponse response = null;
				BasicNameValuePair responseMap = null;
				
				try {
					HttpClient httpclient = MyHttpClientFactory.getInstance().createHttpClient();
					//HttpClient httpclient = new DefaultHttpClient();
					HttpPost httppost = new HttpPost(Constants.IMUGUR_TOKEN_URL);
					
					// Add your data
					List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
					nameValuePairs.add(new BasicNameValuePair("client_id", Constants.CLIENT_ID));
					nameValuePairs.add(new BasicNameValuePair("client_secret", Constants.CLIENT_SECRET));
					
					if(type == TradeItemType.PIN){
						nameValuePairs.add(new BasicNameValuePair("grant_type", "pin"));
						nameValuePairs.add(new BasicNameValuePair("pin",aParams[0]));
					} else if (type == TradeItemType.REFRESH_TOKEN) {
						nameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
						nameValuePairs.add(new BasicNameValuePair("refresh_token",aParams[0]));
					}
					for(NameValuePair bnvp : nameValuePairs){
						Log.d(TAG, bnvp.getName()+"="+bnvp.getValue());
					}

					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

					// Execute HTTP Post Request
					response = httpclient.execute(httppost);
					responseMap = new BasicNameValuePair(Integer.toString(response.getStatusLine().getStatusCode()), EntityUtils.toString(response.getEntity()));
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				}
				return responseMap;
			}

			protected void onPostExecute(BasicNameValuePair response) {
				JSONObject imgurResponse;
				try {
					if(response == null){
						Toast toast = Toast.makeText(context, "Null response.", Toast.LENGTH_SHORT);
						toast.show();
						return;
					}
					
					imgurResponse = new JSONObject(response.getValue());
					if(Integer.parseInt(response.getName()) == 200){
							SharedPreferences settings = ((Main)context).getSharedPreferences(Constants.PREFERENCES_FILE_NAME, 0);
						    SharedPreferences.Editor editor = settings.edit();
						    editor.putString(Constants.SettingsMap.REFRESH_TOKEN, (String) imgurResponse.get("refresh_token"));
						    editor.putString(Constants.SettingsMap.ACCESS_TOKEN, (String) imgurResponse.get("access_token"));
						    editor.putLong(Constants.SettingsMap.ACCESS_TOKEN_VALIDITY,(System.currentTimeMillis()/1000 + 3600));
						    editor.commit();
						    ((Main)context).onLoginCompleted();
						} else {
							Log.d(TAG, "Some error occured: "+ response.getName() + " - " +  response.getValue());
							Toast toast = Toast.makeText(context, (CharSequence) imgurResponse.getJSONObject("data").get("error"), Toast.LENGTH_LONG);
							toast.show();
							((Main)context).onLoginFailed();
						}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (ParseException e) {
					e.printStackTrace();
				} 
				((Main)context).hideLoading();
				
			}
		}.execute(trade_item);
	}
}
