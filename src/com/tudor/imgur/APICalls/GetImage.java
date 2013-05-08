package com.tudor.imgur.APICalls;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import static com.tudor.imgur.util.Constants.TAG;

public class GetImage extends AsyncTask<String, Void, Drawable> {

	private ImageView imb;
	
	public GetImage(ImageView imb)
	{
		this.imb = imb;
	}
	
	@Override
	protected Drawable doInBackground(String... params) {
		Log.i("GetImage.doInBackground", "entered");
		InputStream is = null;
		try {
			is = (InputStream) this.fetch(params[0].toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Drawable d = Drawable.createFromStream(is, "src");
        Log.i("doInBackground", "exited");
        return d;
	}
	
	
	@Override
	protected void onPostExecute(Drawable result) {
		Log.d(TAG, "Attempting to set the image in place...");
		BitmapDrawable bd = (BitmapDrawable) result;
		int height=bd.getBitmap().getHeight();
		int width=bd.getBitmap().getWidth();
		Log.d(TAG, String.format("Actual size:[w=%d][h=%d]",width, height));
		
		if(height*width == 0)
		{
			Log.d(TAG, "Something is wrong with the image size...");
			return;
		}
		
		if ( height > 1280 || width > 720 ) {
			Log.d(TAG, "Image is larger than the screen, resizing...");
			int newHeight, newWidth;
			if (height > width)
			{
				Log.d(TAG, "H>W");
				newWidth = (int) ((1280f / height) * width);
				newHeight = 1280;
			} else {
				Log.d(TAG, "W>H");
				newHeight = (int) ((720f / width) * height);
				newWidth = 720;
			}
			Log.d(TAG, String.format("New size:[w=%d][h=%d]",newWidth, newHeight));
			Bitmap scaled = Bitmap.createScaledBitmap(bd.getBitmap(), newWidth, newHeight, false);
			imb.setImageBitmap(scaled);
		} else {
			imb.setImageDrawable(result);
		}
	}


	private Object fetch(String address) throws MalformedURLException, IOException {
		Log.i("GetImage.fetch", "entered");
		Log.i("GetImage.fetch", "URL=" + address);
        URL url = new URL(address);
        Object content = url.getContent();
		Log.i("GetImage.fetch", "exited");
        return content;

    } 
}
