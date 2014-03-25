package org.banananinja.whatclinicchallenge.responsehandlers;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.banananinja.whatclinicchallenge.model.Contact;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.util.Log;

public class JSONContactResponseHandler implements ResponseHandler<Contact> {

	private static final String EMAIL_TAG = "email";
	private static final String ID_TAG = "id";
	private static final String NAME_TAG = "name";
	private static final String PHONE_TAG = "phone";
	
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