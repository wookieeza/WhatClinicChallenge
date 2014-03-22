package org.banananinja.whatclinicchallenge.model;

import java.util.ArrayList;
import java.util.List;

import org.banananinja.whatclinicchallenge.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ContactListAdapter extends BaseAdapter {
	
	private Context context;
	
	// List of ToDoItems
	private final List<Contact> contactList = new ArrayList<Contact>();
		
	
	public ContactListAdapter (Context context){
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final Contact contact = (Contact) getItem(position);
		
        View itemLayout = convertView;
		
		if (itemLayout == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			itemLayout = inflater.inflate(R.layout.list_item, null);
		}

		//Inflate the View for this Contact
		// from list_item.xml.
		
		// Fill in specific Contact data
		final TextView nameView = (TextView) itemLayout.findViewById(R.id.nameView);
		nameView.setText(contact.getName());
		
		final TextView emailView = (TextView) itemLayout.findViewById(R.id.emailView);
		emailView.setText(contact.getEmail());
		
		final TextView phoneView = (TextView) itemLayout.findViewById(R.id.phoneView);
		phoneView.setText(contact.getPhone());
		
		final TextView countryView = (TextView) itemLayout.findViewById(R.id.countryView);
		countryView.setText(contact.getCountry());
		
		// Return the View you just created
		return itemLayout;
	}

	@Override
	public int getCount() {
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		return contactList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void add (Contact contact){
		contactList.add(contact);
		notifyDataSetChanged();
	}
	
	
}
