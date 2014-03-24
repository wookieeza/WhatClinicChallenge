package org.banananinja.whatclinicchallenge;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.banananinja.whatclinicchallenge.model.Contact;
import org.banananinja.whatclinicchallenge.model.ContactListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ListActivity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	// Add a Contact Request Code
	private static final int ADD_CONTACT_REQUEST = 0;

	private static final String TAG = "WHATCLINIC";

	ContactListAdapter cListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		cListAdapter = new ContactListAdapter(MainActivity.this);
		new HttpGetTask().execute();

		// Put divider between Contact Items and FooterView
		getListView().setFooterDividersEnabled(true);

		// Inflate footerView for footer_view.xml file
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		TextView footerView = (TextView) layoutInflater.inflate(
				R.layout.footer_view, null);

		// Add footerView to ListView
		getListView().addFooterView(footerView);

		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i(TAG, "Entered footerView.OnClickListener.onClick()");

				// Attach Listener to FooterView. Implement onClick().
				Intent intent = new Intent(MainActivity.this,
						AddContactActivity.class);
				startActivityForResult(intent, ADD_CONTACT_REQUEST);

			}
		});

		setListAdapter(cListAdapter);

		ListView lv = getListView();
		lv.setTextFilterEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (ADD_CONTACT_REQUEST == requestCode && RESULT_OK == resultCode) {
			Contact contact = new Contact(data);
			new HttpPostTask().execute(contact);
			cListAdapter.add(contact);
		}
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<Contact>> {

		private static final String TAG = "HttpGetTask";

		private static final String URL = "http://hidden-oasis-1864.herokuapp.com/contacts";

		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<Contact> doInBackground(Void... params) {
			HttpGet get = new HttpGet(URL);
			JSONContactsResponseHandler responseHandler = new JSONContactsResponseHandler();
			try {
				return androidHttpClient.execute(get, responseHandler);
			} catch (ClientProtocolException e) {
				Log.e(TAG, e.getMessage());
			} catch (IOException e) {
				Log.e(TAG, e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Contact> results) {
			if (null != androidHttpClient) {
				androidHttpClient.close();
			}
			// make sure we got something back from the server
			if (null != results) { 
				((ContactListAdapter) getListAdapter()).addAll(results);
			}
			
		}
    }
	
	private class HttpPostTask extends AsyncTask<Contact, Void, Contact> {

		private static final String TAG = "HttpPostTask";

		private static final String URL = "http://hidden-oasis-1864.herokuapp.com/contacts";

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
			// make sure we got something back from the server
//			if (null != result) {
//				((ContactListAdapter) getListAdapter()).add(result);
//			}
//			
		}
    }
	
	private class JSONContactsResponseHandler implements ResponseHandler<List<Contact>> {

		private static final String CREATED_TAG = "created_at";
		private static final String EMAIL_TAG = "email";
		private static final String ID_TAG = "id";
		private static final String NAME_TAG = "name";
		private static final String PHONE_TAG = "phone";
		private static final String UPDATED_TAG = "updated_at";
		
		@Override
		public List<Contact> handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			List<Contact> result = new ArrayList<Contact>();

			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);

			try {
				JSONArray contacts = (JSONArray) new JSONTokener(JSONResponse)
						.nextValue();

				for (int idx = 0; idx < contacts.length(); idx++) {
					
					JSONObject contact = (JSONObject) contacts.get(idx);
					result.add(new Contact(contact.getString(NAME_TAG),
							contact.getString(PHONE_TAG),
							contact.getString(NAME_TAG),
							contact.getString(EMAIL_TAG)));

				}
			} catch (JSONException e) {
				Log.e(JSONResponse, e.getMessage());
			}

			return result;
		}
		
	}
	
	private class JSONContactResponseHandler implements ResponseHandler<Contact> {

		private static final String CREATED_TAG = "created_at";
		private static final String EMAIL_TAG = "email";
		private static final String ID_TAG = "id";
		private static final String NAME_TAG = "name";
		private static final String PHONE_TAG = "phone";
		private static final String UPDATED_TAG = "updated_at";
		
		@Override
		public Contact handleResponse(HttpResponse response)
				throws ClientProtocolException, IOException {
			Contact result = null;

			String JSONResponse = new BasicResponseHandler()
					.handleResponse(response);

			try {
				JSONObject contact = (JSONObject) new JSONTokener(JSONResponse)
						.nextValue();

				result = new Contact(contact.getString(NAME_TAG),
						contact.getString(PHONE_TAG),
						contact.getString(NAME_TAG),
						contact.getString(EMAIL_TAG));

			} catch (JSONException e) {
				Log.e(JSONResponse, e.getMessage());
			}

			return result;
		}
		
	}

}
