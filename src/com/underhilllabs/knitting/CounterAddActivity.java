package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CounterAddActivity extends Activity {

	private DbAdapter pdb;
	private EditText name_field;
	private EditText notes_field;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.counter_add);
		pdb = new DbAdapter(this);
		pdb.open();

		final Button add_button = (Button) findViewById(R.id.add_counter_button);
		name_field = (EditText) findViewById(R.id.name_field);
		notes_field = (EditText) findViewById(R.id.notes_field);

		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				// pdb.open();
				String name = name_field.getEditableText().toString();
				String notes = notes_field.getEditableText().toString();
				// name,cur_val,proj_id,proj_name,int_up_down,str_type,int_multiple,notes
				int cur_val = 1;
				int proj_id = 0;
				String proj_name = "";
				int up_or_down = 0;
				String type = "increase";
				int multiple = 1;
				// get date string
				// ateFormat dateFormat = new
				// SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				// java.util.Date date = new java.util.Date();
				// String cdatetime = dateFormat.format(date);
				// ProjectAddActivity.this.timeStampFormat.Format(new Date());
				pdb.createCounter(name, cur_val, proj_id, proj_name,
						up_or_down, type, multiple, notes);
				Intent i = new Intent(CounterAddActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 3);
				startActivity(i);

				// Toast.makeText(KnittingInventory.this,
				// "Added to Project Inventory", Toast.LENGTH_SHORT).show();

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (pdb != null) {
			pdb.close();
		}
	}
}
