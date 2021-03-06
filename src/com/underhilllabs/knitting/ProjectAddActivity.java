package com.underhilllabs.knitting;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ProjectAddActivity extends Activity {

	private DbAdapter pdb;
	private EditText name_field;
	private EditText notes_field;
	private EditText shopping_field;
	private Spinner spinner_status;
	private ArrayAdapter<CharSequence> adapter_status;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_add);
		pdb = new DbAdapter(this);
		pdb.open();

		final Button add_button = (Button) findViewById(R.id.add_project_button);
		name_field = (EditText) findViewById(R.id.name_field);
		notes_field = (EditText) findViewById(R.id.notes_field);
		shopping_field = (EditText) findViewById(R.id.shopping_field);
		spinner_status = (Spinner) findViewById(R.id.spinner_status);

		adapter_status = ArrayAdapter.createFromResource(this,
				R.array.status_array, android.R.layout.simple_spinner_item);
		adapter_status
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_status.setAdapter(adapter_status);

		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				// pdb.open();
				String name = name_field.getEditableText().toString();
				String notes = notes_field.getEditableText().toString();
				String status = spinner_status.getSelectedItem().toString();
				int status_i = spinner_status.getSelectedItemPosition();
				String needed_shopping = shopping_field.getEditableText()
						.toString();
                // get date string
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date date = new java.util.Date();
				String cdatetime = dateFormat.format(date);
				// ProjectAddActivity.this.timeStampFormat.Format(new Date());
				pdb.createProject(name, cdatetime, cdatetime, status, status_i,
						notes, needed_shopping);
				Intent i = new Intent(ProjectAddActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 2);
				startActivity(i);
				// Toast.makeText(KnittingInventory.this,
				// "Added to Project Inventory", Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pdb.close();
	}
}