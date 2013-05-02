package com.tudor.imgur.util;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.tudor.imgur.FullScreenImage;
import com.tudor.imgur.R;
import com.tudor.imgur.APICalls.GetImage;
import com.tudor.imgur.entity.ImgurImage;

public class ImageAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private final List<ImgurImage> imgurImages;

	public ImageAdapter(Context context, List<ImgurImage> imgurImages) {
		this.context = context;
		this.imgurImages = imgurImages;
	}

	@Override
	public int getCount() {
		return imgurImages.size();
	}

	@Override
	public Object getItem(int position) {
		return imgurImages.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageButton imgbnew = null;
		
		if (convertView == null){
			ImgurImage image = imgurImages.get(position);
			imgbnew = new ImageButton(context);
			imgbnew.setContentDescription(image.getLink());
			imgbnew.setScaleType(ImageView.ScaleType.CENTER_CROP);
			imgbnew.setLayoutParams(new GridView.LayoutParams(200, 200));
			imgbnew.setBackgroundDrawable(null);
	    	imgbnew.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url =(String) ((ImageButton)v).getContentDescription();
//					Intent i = new Intent(Intent.ACTION_VIEW);
//					i.setData(Uri.parse(url));
//					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					Intent i = new Intent(context, FullScreenImage.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("url", url);
					context.startActivity(i);
				}
			});
	        
	        new GetImage(((ImageView)imgbnew)).execute(image.getLink());
		} else {
			imgbnew = (ImageButton) convertView;
		}
		
        return imgbnew;
	}

}
