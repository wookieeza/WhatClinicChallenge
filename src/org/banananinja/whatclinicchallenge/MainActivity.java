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
import org.apache.http.client.methods.HttpPut;
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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ListActivity {

	// Add a Contact Request Code
	private static final int ADD_CONTACT_REQUEST = 0;
	private static final int EDIT_CONTACT_REQUEST = 1;
	
	ContactListAdapter cListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//create list, and fetch contacts
		cListAdapter = new ContactListAdapter(MainActivity.this);
		setListAdapter(cListAdapter);
		new HttpGetTask().execute();
		getListView().setTextFilterEnabled(true);

		//when a contact is clicked, start an edit contact activity
		getListView().setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Contact toBeEdited = (Contact) getListAdapter().getItem(position);
				
				Intent editIntent = new Intent(MainActivity.this, AddContactActivity.class);
				editIntent.putExtra(Contact.CONTACT_ID, toBeEdited.getId());
				editIntent.putExtra(Contact.NAME, toBeEdited.getName());
				editIntent.putExtra(Contact.COUNTRY, toBeEdited.getCountry());
				editIntent.putExtra(Contact.EMAIL, toBeEdited.getEmail());
				editIntent.putExtra(Contact.PHONE, toBeEdited.getPhone());
				editIntent.putExtra(Contact.POSITION, position);
				startActivityForResult(editIntent, EDIT_CONTACT_REQUEST);
			}
		});
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.add_contact) {
			Intent intent = new Intent(MainActivity.this,
					AddContactActivity.class);
			startActivityForResult(intent, ADD_CONTACT_REQUEST);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (ADD_CONTACT_REQUEST == requestCode && RESULT_OK == resultCode) {
			Contact contact = new Contact(data);
			new HttpPostTask().execute(contact);
			cListAdapter.add(contact);
			StringBuilder builder = new StringBuilder("new contact ");
			builder.append(contact.getName());
			builder.append(" added");
			Toast.makeText(getApplicationContext(), builder.toString() , Toast.LENGTH_SHORT).show();
		} else if (EDIT_CONTACT_REQUEST == requestCode && RESULT_OK == resultCode) {
			Contact contact = new Contact(data);
			//update on API
			new HttpPutTask().execute(contact);
			//update in list adapter
			cListAdapter.updateContactAtLocation(data.getIntExtra(Contact.POSITION,0), contact);
			StringBuilder builder = new StringBuilder(contact.getName());
			builder.append(" edited");
			Toast.makeText(getApplicationContext(), builder.toString(), Toast.LENGTH_SHORT).show();
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
			// no need to add contact here, as we called startActivityForResult
			//and contact will be added to adapter in onActivityResult

		}
    }
	
	private class HttpPutTask extends AsyncTask<Contact, Void, Contact> {

		private static final String TAG = "HttpPutTask";

		private static final String URL = "http://hidden-oasis-1864.herokuapp.com/contacts/";

		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");

		@Override
		protected Contact doInBackground(Contact... param) {
			HttpPut put = new HttpPut(URL+param[0].getId());
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair(Contact.NAME, param[0].getName()));
			nameValuePairs.add(new BasicNameValuePair(Contact.PHONE, param[0].getPhone()));
			nameValuePairs.add(new BasicNameValuePair(Contact.EMAIL, param[0].getEmail()));
			
			JSONContactResponseHandler responseHandler = new JSONContactResponseHandler();
			try {
				put.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				return androidHttpClient.execute(put, responseHandler);
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
					result.add(new Contact(Double.valueOf(contact.getDouble(ID_TAG)).intValue()+"",
							contact.getString(NAME_TAG),
							contact.getString(PHONE_TAG),
							null,
							contact.getString(EMAIL_TAG)));

				}
			} catch (JSONException e) {
				Log.e("JSONResponse", e.getMessage());
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

				result = new Contact(contact.getString(ID_TAG),
						contact.getString(NAME_TAG),
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
