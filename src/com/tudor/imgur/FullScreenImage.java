package com.tudor.imgur;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.tudor.imgur.APICalls.GetImage;

public class FullScreenImage extends Activity {
	
	private final String TAG = this.getClass().getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.full_image);
		ImageView imageView = (ImageView) findViewById(R.id.fullImage);

		
		Bundle extras = getIntent().getExtras(); 
		String url = null;

		
		
//		imageView.setLayoutParams(new ViewGroup.LayoutParams(
//				WindowManager.LayoutParams.MATCH_PARENT,
//				WindowManager.LayoutParams.MATCH_PARENT));

		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
		
		if (extras != null) {
			url = extras.getString("url");
			Log.d(TAG, url);
			new GetImage(imageView).execute(url);
		}
		

	}
}
