package com.tudor.imgur.APICalls;

import static com.tudor.imgur.util.Constants.TAG;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

import com.tudor.imgur.util.AsyncTaskCompleteListener;
import com.tudor.imgur.util.Constants.RequestType;
import com.tudor.imgur.util.http.MyHttpClientFactory;

public abstract class ImgurAPI extends AsyncTask<String, Void, BasicNameValuePair> implements AsyncTaskCompleteListener<BasicNameValuePair> {

	private List<NameValuePair> headers;
	private List<NameValuePair> params;
	private String url;
	private RequestType requestType;
	
	@Override
	abstract protected void onPreExecute() ;

	@Override
	protected BasicNameValuePair doInBackground(String... params) {
		
		Log.d(TAG, "Doing stuff in background...");
		HttpClient httpclient = MyHttpClientFactory.getInstance().createHttpClient();
		HttpRequestBase request = null;
		switch(requestType)
		{
			case GET:
				Log.d(TAG, "GET request to process...");
				request = new HttpGet(url);
				if(headers != null && headers.size() > 0) {
					for (NameValuePair header : headers) {
						Log.d(TAG, String.format("HTTP HEADER [%s=%s]", header.getName(), header.getValue()));
						request.addHeader(header.getName(), header.getValue());
					}
				}
				break;
			default:
				Log.w(TAG, "Invalid RequestType");
				break;
		
		}
		
		HttpResponse response = null;
		try {
			Log.d(TAG, "Attempting to execute the request...");
			response = httpclient.execute(request);
			return new BasicNameValuePair(Integer.toString(response.getStatusLine().getStatusCode()), EntityUtils.toString(response.getEntity()));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	

	@Override
	protected void onPostExecute(BasicNameValuePair result) {
		onTaskComplete(result);
	}


	@Override
	abstract public void onTaskComplete(BasicNameValuePair result);

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public RequestType getRequestType() {
		return requestType;
	}

	public void setRequestType(RequestType requestType) {
		this.requestType = requestType;
	}

	public List<NameValuePair> getParams() {
		return params;
	}

	public void setParams(List<NameValuePair> params) {
		this.params = params;
	}

	public List<NameValuePair> getHeaders() {
		return headers;
	}

	public void setHeaders(List<NameValuePair> headers) {
		this.headers = headers;
	}
	
	
	
}
