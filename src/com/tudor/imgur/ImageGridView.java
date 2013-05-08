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
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tudor.imgur.APICalls.ImgurAPI;
import com.tudor.imgur.entity.ImgurImage;
import com.tudor.imgur.entity.ImgurResponse;
import com.tudor.imgur.util.Constants;
import com.tudor.imgur.util.Constants.ResultType;
import com.tudor.imgur.util.ImageAdapter;

public class ImageGridView extends Activity{
	
	private SharedPreferences settings;
	private static final int SELECT_PICTURE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagegridview);
		Constants.ref_currentActivity = this;
		settings = getSharedPreferences(Constants.PREFERENCES_FILE_NAME, 0);
		getImageList();
			
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
			case R.id.btnExit:
				setResult(-1);
				finish();
				break;
			case R.id.btnLogOut:
				SharedPreferences.Editor editor = settings.edit();
			    editor.remove(Constants.SettingsMap.REFRESH_TOKEN);
			    editor.commit();
			    setResult(-2);
			    finish();
				break;
			case R.id.btnUpload:
				Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
				break;
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
	    if ((keyCode == KeyEvent.KEYCODE_BACK))
	    {
	    	setResult(-1);
	        finish();
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                uploadImage(getRealPathFromURI(selectedImageUri));
            }
        }
    }

	/* Returns the absolute path of an Uri */
	public String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
   	
	private void onGotImageList(String imgurResponseString){
		try {
			Gson gson = new Gson();
			ImgurResponse<List<ImgurImage>> response = new ImgurResponse<List<ImgurImage>>();
			Type tipeToken = new TypeToken<ImgurResponse<List<ImgurImage>>>(){}.getType();
			response = gson.fromJson(imgurResponseString, tipeToken);
			
			Log.d(TAG, "Going to display " + response.getData().size() + " images");
			GridView gv = (GridView)findViewById(R.id.imageGrid);
			gv.setAdapter(new ImageAdapter(this, response.getData()));

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
						interpretResult(result, ResultType.GET_ALL_IMAGES);
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
	
	private void uploadImage(final String localPath) {
		ImgurAPI imgurapi = new ImgurAPI() {
			@Override
			public void onTaskComplete(BasicNameValuePair result) {
				interpretResult(result, ResultType.UPLOAD_IMAGE);
			}
			
			@Override
			protected void onPreExecute() {
				setUrl(Constants.ImgurAPIURL.IMAGE_POST);
				this.setRequestType(Constants.RequestType.POST);
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("Authorization", "Bearer " + settings.getString(Constants.SettingsMap.ACCESS_TOKEN, null)));
				this.setHeaders(nameValuePairs);
			}
		};
		
		imgurapi.execute(localPath);
	}
	
	private void interpretResult(BasicNameValuePair result, ResultType type) {
		Log.d(TAG, "Result is present, request was completed successfully!!!");
		try {
			if(Integer.parseInt(result.getName()) == 200) {
				if (type == ResultType.GET_ALL_IMAGES) {
					onGotImageList(result.getValue());
				} else if (type == ResultType.UPLOAD_IMAGE) {
					/* Refresh the images list */
					getImageList();
				}
			} else {
				Toast.makeText(getApplicationContext(), (CharSequence) new JSONObject(result.getValue()).getJSONObject("data").get("error"), Toast.LENGTH_LONG).show();
			}
			
		} catch (NumberFormatException ex) {
			ex.printStackTrace();			
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
