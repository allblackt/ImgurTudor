package com.tudor.imgur.util;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.tudor.imgur.FullScreenImage;
import com.tudor.imgur.APICalls.GetImage;
import com.tudor.imgur.entity.ImgurImage;

public class ImageAdapter extends BaseAdapter implements ListAdapter {
	
	private Context context;
	private List<ImgurImage> imgurImages;
	private List<ImageButton> cache;

	public ImageAdapter(Context context, List<ImgurImage> imgurImages) {
		this.context = context;
		this.imgurImages = imgurImages;
		cacheImages();
	}

	/* Caches the images for fast display; Eager loading */
	private void cacheImages() {
		cache = imgurImages.size() > 0 ? new ArrayList<ImageButton>() : null;
		for (ImgurImage image : imgurImages) {
			
			ImageButton img = new ImageButton(context);
			img.setContentDescription(image.getLink());
			img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			img.setLayoutParams(new GridView.LayoutParams(160, 160));
			img.setBackgroundDrawable(null);
			img.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					String url =(String) ((ImageButton)v).getContentDescription();
					Intent i = new Intent(context, FullScreenImage.class);
					i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					i.putExtra("url", url);
					context.startActivity(i);
				}
			});
			new GetImage(img).execute(image.getLink().replace(image.getId(), image.getId()+ "b") );
			cache.add(img);
		}
	}
	
	@Override
	public int getCount() {
		return imgurImages.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return cache.get(position);
	}

}
