WhatClinicChallenge
===================

Objective
-----------
Demo app exercises a sample API to display and edit some simple contact information in a single page App.
Focus is on basic functionality rather than design 

Basic Android conventions were followed: 
- clicking on a contact opens the Edit view,
- validation and feedback messages are displayed as pop up "Toast' messages
- Add contact is a button in the action bar
- contacts can be scrolled through


Please note that on start up the app starts an asynch task that downlaods the contacts from the API specified,
depending on network latency it might take a second. If the list remains empty - I found reopening the app sometimes helps.

I was not able to complete email address format validation or the extra credit phone number /country validation

Requirements
------------

The app is a simple app with 2 primary views 

Contact List View (On startup)
	- This view lists the contacts returned by the contact API
	- Its possible to click on an individual contact to enter the contact detail view in "edit mode"
	- Its possible to click on the "add contact" button to enter the contact detail view in "add mode"

Add/Edit Contact View

	- The user is to capture Name, Phone and Email information
	- I validate these entries with basic validations that: 
		- must have a name
		- must have an email or phone number
		- [email must be valid format] not done
	- clicking save will save the contact details to the API and navigate back to the list view
	- clicking cancel will navigate back without saving
	- clicking reset resets the form to either empty (as for adding) or the state before changes were made (when editing)

	
