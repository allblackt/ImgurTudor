package com.tudor.imgur;

import static com.tudor.imgur.util.Constants.TAG;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tudor.imgur.APICalls.ImgurAPI;
import com.tudor.imgur.entity.ImgurImage;
import com.tudor.imgur.entity.ImgurResponse;
import com.tudor.imgur.util.Constants;
import com.tudor.imgur.util.ImageAdapter;

public class ImageGridView extends Activity{
	
	private SharedPreferences settings;
	
//	private String dummyImgurResponse = "{\"data\":[{\"id\":\"6MJf5T4\",\"title\":null,\"description\":null,\"datetime\":1362603508,\"type\":\"image/png\",\"animated\":false,\"width\":96,\"height\":96,\"size\":7020,\"views\":0,\"bandwidth\":0,\"deletehash\":\"EnAyw1pk4TYFTzW\",\"link\":\"http://i.imgur.com/6MJf5T4.png\"},{\"id\":\"zjKoLBn\",\"title\":null,\"description\":null,\"datetime\":1362598656,\"type\":\"image/png\",\"animated\":false,\"width\":48,\"height\":48,\"size\":5605,\"views\":1,\"bandwidth\":5605,\"deletehash\":\"XHjf4lHD12x1Ync\",\"link\":\"http://i.imgur.com/zjKoLBn.png\"},{\"id\":\"rh0aIjZ\",\"title\":null,\"description\":null,\"datetime\":1362597359,\"type\":\"image/png\",\"animated\":false,\"width\":96,\"height\":96,\"size\":7020,\"views\":1,\"bandwidth\":7020,\"deletehash\":\"a4mnG3ZoPoPyfDi\",\"link\":\"http://i.imgur.com/rh0aIjZ.png\"},{\"id\":\"3ZSlx\",\"title\":null,\"description\":null,\"datetime\":1353613196,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":5031,\"views\":226,\"bandwidth\":1137006,\"deletehash\":\"1TllbugINc2GGv9\",\"link\":\"http://i.imgur.com/3ZSlx.jpg\"},{\"id\":\"RqW0n\",\"title\":null,\"description\":null,\"datetime\":1353613195,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":5765,\"views\":225,\"bandwidth\":1297125,\"deletehash\":\"ff3Yqll39Z0BOsY\",\"link\":\"http://i.imgur.com/RqW0n.jpg\"},{\"id\":\"dtxzF\",\"title\":null,\"description\":null,\"datetime\":1353613195,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":9700,\"views\":3,\"bandwidth\":29100,\"deletehash\":\"q60rwQs0YmiQAmt\",\"link\":\"http://i.imgur.com/dtxzF.jpg\"},{\"id\":\"Mvc7s\",\"title\":null,\"description\":null,\"datetime\":1353613194,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":14682,\"views\":226,\"bandwidth\":3318132,\"deletehash\":\"N4AIezeuJz5Roru\",\"link\":\"http://i.imgur.com/Mvc7s.jpg\"},{\"id\":\"FYVyC\",\"title\":null,\"description\":null,\"datetime\":1353613194,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":4258,\"views\":226,\"bandwidth\":962308,\"deletehash\":\"Cm4AxB2OxJsGU2C\",\"link\":\"http://i.imgur.com/FYVyC.jpg\"},{\"id\":\"k8ZBT\",\"title\":null,\"description\":null,\"datetime\":1353613193,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":6519,\"views\":226,\"bandwidth\":1473294,\"deletehash\":\"Vq9mOyt6Q1QzQDW\",\"link\":\"http://i.imgur.com/k8ZBT.jpg\"},{\"id\":\"zSY6l\",\"title\":null,\"description\":null,\"datetime\":1353613192,\"type\":\"image/jpeg\",\"animated\":false,\"width\":224,\"height\":224,\"size\":4821,\"views\":225,\"bandwidth\":1084725,\"deletehash\":\"sIxTyji9t3H0J0W\",\"link\":\"http://i.imgur.com/zSY6l.jpg\"},{\"id\":\"Pvi1M\",\"title\":null,\"description\":null,\"datetime\":1353613191,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":10664,\"views\":226,\"bandwidth\":2410064,\"deletehash\":\"OljYh1b4rNqGtIG\",\"link\":\"http://i.imgur.com/Pvi1M.jpg\"},{\"id\":\"Q2sbe\",\"title\":null,\"description\":null,\"datetime\":1353613191,\"type\":\"image/jpeg\",\"animated\":false,\"width\":225,\"height\":225,\"size\":5704,\"views\":228,\"bandwidth\":1300512,\"deletehash\":\"GrzJIZuNiKjVg01\",\"link\":\"http://i.imgur.com/Q2sbe.jpg\"},{\"id\":\"EcsQD\",\"title\":null,\"description\":null,\"datetime\":1353613190,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":8623,\"views\":233,\"bandwidth\":2009159,\"deletehash\":\"BWwcnO4IA2Vja3G\",\"link\":\"http://i.imgur.com/EcsQD.jpg\"}],\"success\":true,\"status\":200}";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagegridview);
		Log.d(TAG, "HERE");
		settings = getSharedPreferences(Constants.PREFERENCES_FILE_NAME, 0);
		
		getImageList();
			
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.imagegridview, menu);
		return true;
	}
	
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Log.d(TAG, "You selected " + item.getItemId());
		switch(item.getItemId())
		{
			case R.id.btnRefresh:
				Log.d(TAG, "Refresh was called...");
				getImageList();
				break;
		}
		return true;
	}

	public void onGotImageList(String imgurResponseString){
		try {
			Gson gson = new Gson();
			ImgurResponse<List<ImgurImage>> response = new ImgurResponse<List<ImgurImage>>();
			Type tipeToken = new TypeToken<ImgurResponse<List<ImgurImage>>>(){}.getType();
			response = gson.fromJson(imgurResponseString, tipeToken);
			
			GridView gv = (GridView)findViewById(R.id.imageGrid);
			gv.setAdapter(new ImageAdapter(getApplicationContext(), response.getData()));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void getImageList()
	{
		ImgurAPI imgurapi = new ImgurAPI() {
			
			@Override
			public void onTaskComplete(BasicNameValuePair result) {
					if(result != null) {
						
						interpretResult(result);
					} else {
						Log.d(TAG, "Result is null, BOOHOO, see what happened...");
					}
			}
			
			@Override
			protected void onPreExecute() {
				this.setUrl(Constants.ImgurAPIURL.ACCOUNT_IMAGES);
				this.setRequestType(Constants.RequestType.GET);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + settings.getString(Constants.SettingsMap.ACCESS_TOKEN, null)));
				this.setHeaders(nameValuePairs);
			}
		};
		
		imgurapi.execute();
	}
	
	private void interpretResult(BasicNameValuePair result) {
		Log.d(TAG, "Result is present, request was completed successfully!!!");
		Log.d(TAG, result.getName());
		Log.d(TAG, result.getValue());
		try {
			if(Integer.parseInt(result.getName()) == 200) {
				onGotImageList(result.getValue());
			} else {
				Toast.makeText(getApplicationContext(), (CharSequence) new JSONObject(result.getValue()).getJSONObject("data").get("error"), Toast.LENGTH_LONG);
			}
			
		} catch (NumberFormatException ex) {
			ex.printStackTrace();			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
