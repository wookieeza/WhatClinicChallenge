package org.banananinja.whatclinicchallenge.net;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.banananinja.whatclinicchallenge.model.Contact;
import org.banananinja.whatclinicchallenge.responsehandlers.JSONContactResponseHandler;

import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class HttpPostTask extends AsyncTask<Contact, Void, Contact> {

	private static final String TAG = "HttpPostTask";

	private String URL;

	AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");

	@Override
	protected Contact doInBackground(Contact... param) {
		HttpPost post = new HttpPost(URL);
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
		nameValuePairs.add(new BasicNameValuePair(Contact.NAME, param[0].getName()));
		nameValuePairs.add(new BasicNameValuePair(Contact.PHONE, param[0].getPhone()));
		nameValuePairs.add(new BasicNameValuePair(Contact.EMAIL, param[0].getEmail()));
		
		JSONContactResponseHandler responseHandler = new JSONContactResponseHandler();
		try {
			post.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			return androidHttpClient.execute(post, responseHandler);
		} catch (ClientProtocolException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Contact result) {
		if (null != androidHttpClient) {
			androidHttpClient.close();
		}
		// no need to add contact here, as we called startActivityForResult
		//and contact will be added to adapter in onActivityResult
	}

	public String getUrl() {
		return URL;
	}

	public void setUrl(String uRL) {
		URL = uRL;
	}
	
	
}
