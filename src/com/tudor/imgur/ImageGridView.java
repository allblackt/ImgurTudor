package com.tudor.imgur;

import java.lang.reflect.Type;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.GridView;
import android.widget.ImageButton;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tudor.imgur.APICalls.GetImage;
import com.tudor.imgur.entity.ImgurImage;
import com.tudor.imgur.entity.ImgurResponse;

public class ImageGridView extends Activity{
	
	private final String TAG = this.getClass().getName();

	private String dummyImgurResponse = "{\"data\":[{\"id\":\"6MJf5T4\",\"title\":null,\"description\":null,\"datetime\":1362603508,\"type\":\"image/png\",\"animated\":false,\"width\":96,\"height\":96,\"size\":7020,\"views\":0,\"bandwidth\":0,\"deletehash\":\"EnAyw1pk4TYFTzW\",\"link\":\"http://i.imgur.com/6MJf5T4.png\"},{\"id\":\"zjKoLBn\",\"title\":null,\"description\":null,\"datetime\":1362598656,\"type\":\"image/png\",\"animated\":false,\"width\":48,\"height\":48,\"size\":5605,\"views\":1,\"bandwidth\":5605,\"deletehash\":\"XHjf4lHD12x1Ync\",\"link\":\"http://i.imgur.com/zjKoLBn.png\"},{\"id\":\"rh0aIjZ\",\"title\":null,\"description\":null,\"datetime\":1362597359,\"type\":\"image/png\",\"animated\":false,\"width\":96,\"height\":96,\"size\":7020,\"views\":1,\"bandwidth\":7020,\"deletehash\":\"a4mnG3ZoPoPyfDi\",\"link\":\"http://i.imgur.com/rh0aIjZ.png\"},{\"id\":\"3ZSlx\",\"title\":null,\"description\":null,\"datetime\":1353613196,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":5031,\"views\":226,\"bandwidth\":1137006,\"deletehash\":\"1TllbugINc2GGv9\",\"link\":\"http://i.imgur.com/3ZSlx.jpg\"},{\"id\":\"RqW0n\",\"title\":null,\"description\":null,\"datetime\":1353613195,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":5765,\"views\":225,\"bandwidth\":1297125,\"deletehash\":\"ff3Yqll39Z0BOsY\",\"link\":\"http://i.imgur.com/RqW0n.jpg\"},{\"id\":\"dtxzF\",\"title\":null,\"description\":null,\"datetime\":1353613195,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":9700,\"views\":3,\"bandwidth\":29100,\"deletehash\":\"q60rwQs0YmiQAmt\",\"link\":\"http://i.imgur.com/dtxzF.jpg\"},{\"id\":\"Mvc7s\",\"title\":null,\"description\":null,\"datetime\":1353613194,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":14682,\"views\":226,\"bandwidth\":3318132,\"deletehash\":\"N4AIezeuJz5Roru\",\"link\":\"http://i.imgur.com/Mvc7s.jpg\"},{\"id\":\"FYVyC\",\"title\":null,\"description\":null,\"datetime\":1353613194,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":4258,\"views\":226,\"bandwidth\":962308,\"deletehash\":\"Cm4AxB2OxJsGU2C\",\"link\":\"http://i.imgur.com/FYVyC.jpg\"},{\"id\":\"k8ZBT\",\"title\":null,\"description\":null,\"datetime\":1353613193,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":6519,\"views\":226,\"bandwidth\":1473294,\"deletehash\":\"Vq9mOyt6Q1QzQDW\",\"link\":\"http://i.imgur.com/k8ZBT.jpg\"},{\"id\":\"zSY6l\",\"title\":null,\"description\":null,\"datetime\":1353613192,\"type\":\"image/jpeg\",\"animated\":false,\"width\":224,\"height\":224,\"size\":4821,\"views\":225,\"bandwidth\":1084725,\"deletehash\":\"sIxTyji9t3H0J0W\",\"link\":\"http://i.imgur.com/zSY6l.jpg\"},{\"id\":\"Pvi1M\",\"title\":null,\"description\":null,\"datetime\":1353613191,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":10664,\"views\":226,\"bandwidth\":2410064,\"deletehash\":\"OljYh1b4rNqGtIG\",\"link\":\"http://i.imgur.com/Pvi1M.jpg\"},{\"id\":\"Q2sbe\",\"title\":null,\"description\":null,\"datetime\":1353613191,\"type\":\"image/jpeg\",\"animated\":false,\"width\":225,\"height\":225,\"size\":5704,\"views\":228,\"bandwidth\":1300512,\"deletehash\":\"GrzJIZuNiKjVg01\",\"link\":\"http://i.imgur.com/Q2sbe.jpg\"},{\"id\":\"EcsQD\",\"title\":null,\"description\":null,\"datetime\":1353613190,\"type\":\"image/jpeg\",\"animated\":false,\"width\":259,\"height\":194,\"size\":8623,\"views\":233,\"bandwidth\":2009159,\"deletehash\":\"BWwcnO4IA2Vja3G\",\"link\":\"http://i.imgur.com/EcsQD.jpg\"}],\"success\":true,\"status\":200}";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagegridview);
		Log.d(TAG, "HERE");
		onGotImageList();
		
	
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	public void onGotImageList(){
		try {
			Gson gson = new Gson();
			ImgurResponse<List<ImgurImage>> response = new ImgurResponse<List<ImgurImage>>();
			Type tipeToken = new TypeToken<ImgurResponse<List<ImgurImage>>>(){}.getType();
			response = gson.fromJson(dummyImgurResponse, tipeToken);
			
			
			for(ImgurImage image : response.getData()){
				
				if(this == null)
					return;
				
				ImageButton imgbnew = new ImageButton(this);
				
				imgbnew.setContentDescription(image.getLink());
            	
            	imgbnew.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String url =(String) ((ImageButton)v).getContentDescription();
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setData(Uri.parse(url));
						startActivity(i);
					}
				});
				
        		((GridView)findViewById(R.id.imageGrid)).addView(imgbnew);
            	
            	new GetImage(imgbnew).execute(image.getLink());
			}

		} catch (JsonSyntaxException e) {
			e.printStackTrace();
		}
	}

}
