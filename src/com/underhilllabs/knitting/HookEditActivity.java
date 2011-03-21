package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

public class HookEditActivity extends Activity {

	/** Called when the activity is first created. */

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on clicks
			// RadioButton rb = (RadioButton) v;
			// Toast.makeText(HookEditActivity.this, "rowid is "+rowid,
			// Toast.LENGTH_SHORT).show();
		}
	};
	private DbAdapter hdb;
	private RadioButton radio_wood;
	private RadioButton radio_steel;
	private RadioButton radio_bamboo;
	private RadioButton radio_plastic;
	private RadioButton radio_metal;
	private RadioGroup rg_material;
	private Spinner spinner_size;
	private EditText notes_field;
	private long rowid;
	private Cursor cur;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hook_edit);
		hdb = new DbAdapter(this);
		hdb.open();
		// get rowid from Intent Bundle
		Bundle extras = getIntent().getExtras();
		rowid = extras.getLong("com.underhilllabs.knitting.rowid");

		rg_material = (RadioGroup) findViewById(R.id.rg_material);
		radio_wood = (RadioButton) findViewById(R.id.radio_wood);
		radio_bamboo = (RadioButton) findViewById(R.id.radio_bamboo);
		radio_steel = (RadioButton) findViewById(R.id.radio_steel);
		radio_plastic = (RadioButton) findViewById(R.id.radio_plastic);
		radio_metal = (RadioButton) findViewById(R.id.radio_metal);
		notes_field = (EditText) findViewById(R.id.edit_notes);

		final Button add_button = (Button) findViewById(R.id.add_button);

		radio_wood.setOnClickListener(radio_listener);
		radio_bamboo.setOnClickListener(radio_listener);
		radio_steel.setOnClickListener(radio_listener);
		radio_plastic.setOnClickListener(radio_listener);
		radio_metal.setOnClickListener(radio_listener);

		spinner_size = (Spinner) findViewById(R.id.spinner_size);
		ArrayAdapter<CharSequence> adapter_size = ArrayAdapter
				.createFromResource(this, R.array.hook_array,
						android.R.layout.simple_spinner_item);
		adapter_size
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_size.setAdapter(adapter_size);

		setWidgets(rowid);

		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				// BWL don't open twice
				// hdb.open();
				String size = "";
				String material = "";
				int size_i;
				String notes = "";
				switch (rg_material.getCheckedRadioButtonId()) {
				case R.id.radio_steel:
					material = "steel";
					break;
				case R.id.radio_wood:
					material = "wood";
					break;
				case R.id.radio_plastic:
					material = "plastic";
					break;
				case R.id.radio_metal:
					material = "metal";
					break;
				default:
					material = "bamboo";
					break;
				}
				size = spinner_size.getSelectedItem().toString();
				size_i = spinner_size.getSelectedItemPosition();
				notes = notes_field.getText().toString();
				hdb.updateHook(rowid, size, size_i, material, notes);
				Intent i = new Intent(HookEditActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 1);
				startActivity(i);
				// Toast.makeText(HookEditActivity.this,
				// "Added to Hook Inventory", Toast.LENGTH_SHORT).show();

			}
		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hdb.close();
	}

	private boolean setWidgets(long row_id) {
		cur = hdb.fetchHook(row_id);
		startManagingCursor(cur);
		// 1 = size, 2 size_i
		// 3 = material,
		// 4 = notes
		if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals(
				"wood")) {
			radio_wood.setChecked(true);
		} else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL))
				.equals("bamboo")) {
			radio_bamboo.setChecked(true);
		} else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL))
				.equals("plastic")) {
			radio_plastic.setChecked(true);
		} else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL))
				.equals("metal")) {
			radio_metal.setChecked(true);
		} else {
			radio_steel.setChecked(true);
		}
		// Toast.makeText(HookEditActivity.this, " 1:"+cur.getString(6) ,
		// Toast.LENGTH_LONG).show();
		// spinner_size.setSelection(cur.getInt(2));
		int size_i = cur.getInt(cur.getColumnIndex(DbAdapter.KEY_SIZE_I));
		spinner_size.setSelection(size_i);
		notes_field.setText(cur.getString(cur
				.getColumnIndex(DbAdapter.KEY_NOTES)));

		return true;
	}
}
