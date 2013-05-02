package com.tudor.imgur;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class FullScreenImage extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.full_image);
		ImageView imageView = (ImageView) findViewById(R.id.fullImage);

//		imageView.setLayoutParams(new ViewGroup.LayoutParams(
//				WindowManager.LayoutParams.MATCH_PARENT,
//				WindowManager.LayoutParams.MATCH_PARENT));

		imageView.setImageResource(R.drawable.ic_launcher);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);

	}
}
