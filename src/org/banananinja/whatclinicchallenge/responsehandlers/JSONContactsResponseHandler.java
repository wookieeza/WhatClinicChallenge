package org.banananinja.whatclinicchallenge.responsehandlers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.banananinja.whatclinicchallenge.model.Contact;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class JSONContactsResponseHandler implements ResponseHandler<List<Contact>> {

	private static final String EMAIL_TAG = "email";
	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PHONE_TAG = "phone";
	
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