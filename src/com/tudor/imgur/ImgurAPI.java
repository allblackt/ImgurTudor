package com.tudor.imgur;

import java.io.IOException;
import java.lang.reflect.Type;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tudor.imgur.entity.Account;
import com.tudor.imgur.entity.ImgurResponse;
import com.tudor.imgur.util.Constants;
import com.tudor.imgur.util.http.MyHttpClientFactory;

public class ImgurAPI {

	private static ImgurAPI instance;
	
	private ImgurAPI(){};
	
	public static ImgurAPI get() {
		if(instance == null){
			instance = new ImgurAPI();
		}
		return instance;
	}
	
	
	public Account getAccount(String OAuth2Key)
		throws HttpException {
		
		System.out.println(OAuth2Key);
		HttpGet request = new HttpGet(Constants.ImgurAPIURL.ACCOUNT_BASE);
		request.addHeader("Authorization", "Bearer " + OAuth2Key);
		
		getRequest.execute(request);
		
		return new Account();
	}
	
	private void onGetAccount(HttpResponse response){
		String responseData;
		try {
			responseData = EntityUtils.toString(response.getEntity());
			Gson gson = new Gson();
			ImgurResponse<Account> accountResponse = new ImgurResponse<Account>();
			Type tipeToken = new TypeToken<ImgurResponse<Account>>(){}.getType();
			accountResponse = gson.fromJson(responseData, tipeToken);
			System.out.println(accountResponse.getData().getUrl());
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private AsyncTask<HttpGet, Void, HttpResponse> getRequest = new AsyncTask<HttpGet, Void, HttpResponse>() {

		@Override
		protected HttpResponse doInBackground(HttpGet... params) {
			HttpClient httpclient = MyHttpClientFactory.getInstance().createHttpClient();
			HttpResponse response = null;
			try {
				response = httpclient.execute(params[0]);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return response;
		}

		@Override
		protected void onPostExecute(HttpResponse result) {
			if(result!= null){
				onGetAccount(result);
			}
		}
	};
	
	private AsyncTask<HttpPost, Void, HttpResponse> postRequest = new AsyncTask<HttpPost, Void, HttpResponse>() {

		@Override
		protected HttpResponse doInBackground(HttpPost... params) {
			// TODO Auto-generated method stub
			return null;
		}
	};
	
	private AsyncTask<HttpPut, Void, HttpResponse> putRequest = new AsyncTask<HttpPut, Void, HttpResponse>() {

		@Override
		protected HttpResponse doInBackground(HttpPut... params) {
			// TODO Auto-generated method stub
			return null;
		}
	};
}
