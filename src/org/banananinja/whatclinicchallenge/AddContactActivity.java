package org.banananinja.whatclinicchallenge;

import org.banananinja.whatclinicchallenge.model.Contact;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddContactActivity extends Activity{
	
	Button cancelButton;
	Button resetButton;
	Button submitButton;
	EditText nameText;
	EditText phoneNumberText;
	EditText countryText;
	EditText emailText;
	EditText idText;
	public static final String EMPTY = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ActionBar actionBar = getActionBar();
		actionBar.setHomeButtonEnabled(true);
		
		setContentView(R.layout.add_contact);
		
	    cancelButton = (Button) findViewById(R.id.cancelButton);
	    resetButton = (Button) findViewById(R.id.resetButton);
	    submitButton = (Button) findViewById(R.id.submitButton);
	    
	    nameText = (EditText) findViewById(R.id.name);
	    phoneNumberText = (EditText) findViewById(R.id.phone);
	    countryText = (EditText) findViewById(R.id.country);
	    emailText = (EditText) findViewById(R.id.email);
	    idText = (EditText) findViewById(R.id.contact_id);
	    
	    final Intent intent = getIntent();
		if (intent.hasExtra(Contact.CONTACT_ID)) {
			nameText.setText(intent.getStringExtra(Contact.NAME));
			phoneNumberText.setText(intent.getStringExtra(Contact.PHONE));
			countryText.setText(intent.getStringExtra(Contact.COUNTRY));
			emailText.setText(intent.getStringExtra(Contact.EMAIL));
			idText.setText(intent.getStringExtra(Contact.CONTACT_ID));
		}
		
	    
	    cancelButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	    
	    resetButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// reset defaults
				if (intent.hasExtra(Contact.CONTACT_ID)) {
					nameText.setText(intent.getStringExtra(Contact.NAME));
					phoneNumberText.setText(intent
							.getStringExtra(Contact.PHONE));
					countryText.setText(intent.getStringExtra(Contact.COUNTRY));
					emailText.setText(intent.getStringExtra(Contact.EMAIL));
					idText.setText(intent.getStringExtra(Contact.CONTACT_ID));
				} else {
					nameText.setText(EMPTY);
					phoneNumberText.setText(EMPTY);
					emailText.setText(EMPTY);
					countryText.setText(EMPTY);
				}
			}
		});
	    
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (nameText.getText().toString().isEmpty()) {
					// check that name was supplied
					Toast.makeText(
							AddContactActivity.this,
							getResources().getString(
									R.string.validation_empty_name),
							Toast.LENGTH_SHORT).show();
					
				} else if (phoneNumberText.getText().toString().isEmpty()
						&& emailText.getText().toString().isEmpty()) {
					// check that either email or phone number was supplied
					Toast.makeText(
							AddContactActivity.this,
							getResources().getString(
									R.string.validation_empty_phone_or_email),
							Toast.LENGTH_SHORT).show();
					
				} else 
				{
					// if all went well, pack data to save it
					Intent data = new Intent();
					Contact.packageAsIntent(data, idText.getText().toString(),
							nameText.getText().toString(), phoneNumberText
									.getText().toString(), countryText
									.getText().toString(), emailText.getText()
									.toString());
					data.putExtra(Contact.POSITION,
							getIntent().getIntExtra(Contact.POSITION, 0));
					setResult(RESULT_OK, data);
					finish();
				}
			}
		});

	}

	
}
