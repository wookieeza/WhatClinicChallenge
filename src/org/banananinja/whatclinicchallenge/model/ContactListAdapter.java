package org.banananinja.whatclinicchallenge.model;



import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ContactListAdapter extends BaseAdapter {
	
	private Context context;
	
	// List of ToDoItems
	private final List<Contact> contactList = new ArrayList<Contact>();
		
	
	public ContactListAdapter (Context context){
		this.context = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return null;
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
