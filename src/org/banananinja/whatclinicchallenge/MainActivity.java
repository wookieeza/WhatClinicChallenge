package org.banananinja.whatclinicchallenge;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.banananinja.whatclinicchallenge.model.Contact;
import org.banananinja.whatclinicchallenge.model.ContactListAdapter;
import org.banananinja.whatclinicchallenge.net.HttpPostTask;
import org.banananinja.whatclinicchallenge.net.HttpPutTask;
import org.banananinja.whatclinicchallenge.responsehandlers.JSONContactsResponseHandler;

import android.app.ListActivity;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

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
				Intent editIntent = buildEditContactIntent(position, toBeEdited);
				startActivityForResult(editIntent, EDIT_CONTACT_REQUEST);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

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
			//update on API
			executePost(contact);
			//update in list adapter
			cListAdapter.add(contact);
			Toast.makeText(getApplicationContext(), buildContactAddedNotificationString(contact) , Toast.LENGTH_SHORT).show();
			
		} else if (EDIT_CONTACT_REQUEST == requestCode && RESULT_OK == resultCode) {
			
			Contact contact = new Contact(data);
			//update on API
			executePut(contact);
			//update in list adapter
			cListAdapter.updateContactAtLocation(data.getIntExtra(Contact.POSITION,0), contact);
			Toast.makeText(getApplicationContext(), buildEditedContactNotificationString(contact), Toast.LENGTH_SHORT).show();
		}
	}

	private void executePut(Contact contact) {
		HttpPutTask put = new HttpPutTask();
		put.setUrl(getResources().getString(R.string.url));
		put.execute(contact);
	}

	private void executePost(Contact contact) {
		HttpPostTask post = new HttpPostTask();
		post.setUrl(getResources().getString(R.string.url));
		post.execute(contact);
	}
	
	// build the edit intent
	private Intent buildEditContactIntent(int position, Contact toBeEdited) {
		Intent editIntent = new Intent(MainActivity.this,
				AddContactActivity.class);
		editIntent.putExtra(Contact.CONTACT_ID, toBeEdited.getId());
		editIntent.putExtra(Contact.NAME, toBeEdited.getName());
		editIntent.putExtra(Contact.COUNTRY, toBeEdited.getCountry());
		editIntent.putExtra(Contact.EMAIL, toBeEdited.getEmail());
		editIntent.putExtra(Contact.PHONE, toBeEdited.getPhone());
		editIntent.putExtra(Contact.POSITION, position);
		return editIntent;
	}
		
	private String buildEditedContactNotificationString(Contact contact) {
		StringBuilder builder = new StringBuilder(contact.getName());
		builder.append(" edited");
		return builder.toString();
	}

	private String buildContactAddedNotificationString(Contact contact) {
		StringBuilder builder = new StringBuilder("new contact ");
		builder.append(contact.getName());
		builder.append(" added");
		return builder.toString();
	}

	private class HttpGetTask extends AsyncTask<Void, Void, List<Contact>> {

		private static final String TAG = "HttpGetTask";

		AndroidHttpClient androidHttpClient = AndroidHttpClient.newInstance("");

		@Override
		protected List<Contact> doInBackground(Void... params) {
			HttpGet get = new HttpGet(getResources().getString(R.string.url));
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
	
}
