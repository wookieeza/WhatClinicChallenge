package org.banananinja.whatclinicchallenge;


import org.banananinja.whatclinicchallenge.model.Contact;
import org.banananinja.whatclinicchallenge.model.ContactListAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
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
	    
		// Put divider between Contact Items and FooterView
		getListView().setFooterDividersEnabled(true);

		//Inflate footerView for footer_view.xml file
		LayoutInflater layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

		TextView footerView = (TextView) layoutInflater.inflate(R.layout.footer_view, null);

		//Add footerView to ListView
		getListView().addFooterView(footerView);
		
		footerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Log.i(TAG, "Entered footerView.OnClickListener.onClick()");

				//Attach Listener to FooterView. Implement onClick().
				Intent intent = new Intent(MainActivity.this,AddContactActivity.class);
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
			cListAdapter.add(contact);
		}
	}

	

}
