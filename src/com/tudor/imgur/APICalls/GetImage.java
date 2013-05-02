package com.tudor.imgur.APICalls;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

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
		imb.setImageDrawable(result);
	}


	private Object fetch(String address) throws MalformedURLException, IOException {
		Log.i("GetImage.fetch", "entered");
        URL url = new URL(address);
        Object content = url.getContent();
		Log.i("GetImage.fetch", "exited");
        return content;

    } 
}
