package com.tudor.imgur.util;

import android.os.AsyncTask;
import org.apache.http.HttpResponse;
import org.apache.http.HttpRequest;

public class AsyncTaskCb extends AsyncTask<HttpRequest, Void, HttpResponse> {

	private AsyncTaskCompleteListener<HttpRequest> callback;
	
	@Override
	protected HttpResponse doInBackground(HttpRequest... arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
