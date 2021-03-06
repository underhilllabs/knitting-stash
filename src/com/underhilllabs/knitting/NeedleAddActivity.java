package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class NeedleAddActivity extends Activity {
	/** Called when the activity is first created. */

	private OnClickListener radio_listener = new OnClickListener() {
		public void onClick(View v) {
			// Perform action on clicks
			switch (rg_type.getCheckedRadioButtonId()) {
			case R.id.radio_dpns:
				spinner_length.setAdapter(adapter_length_dpns);
				break;
			case R.id.radio_straight:
				spinner_length.setAdapter(adapter_length_straight);
				break;
			default:
				spinner_length.setAdapter(adapter_length_circ);
				break;
			}
			// spinner_size.setAdapter(adapter_length_circ);
			// Toast.makeText(KnittingInventory.this, "id: "
			// +rb.getId()+" text: "+rb.getText(), Toast.LENGTH_SHORT).show();
		}
	};
	private DbAdapter ndb;
	private RadioButton radio_circular;
	private RadioButton radio_dpns;
	private RadioButton radio_straight;
	private RadioButton radio_bamboo;
	private RadioButton radio_plastic;

	private CheckBox cb_in_use;
	private RadioGroup rg_type;
	private RadioGroup rg_material;
	private Spinner spinner_length;
	private Spinner spinner_size;
	private ArrayAdapter<CharSequence> adapter_length_straight;
	private ArrayAdapter<CharSequence> adapter_length_dpns;
	private ArrayAdapter<CharSequence> adapter_length_circ;
	SharedPreferences prefs;
	public static final String PREF_SIZE = "NEEDLE_SIZE_OPTION";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.needle_add);
		ndb = new DbAdapter(this);
		ndb.open();

		rg_material = (RadioGroup) findViewById(R.id.rg_material);
		rg_type = (RadioGroup) findViewById(R.id.rg_type);
		radio_circular = (RadioButton) findViewById(R.id.radio_circular);
		radio_straight = (RadioButton) findViewById(R.id.radio_straight);
		radio_dpns = (RadioButton) findViewById(R.id.radio_dpns);
		radio_bamboo = (RadioButton) findViewById(R.id.radio_bamboo);
		radio_plastic = (RadioButton) findViewById(R.id.radio_plastic);
		final Button add_button = (Button) findViewById(R.id.add_button);
		cb_in_use = (CheckBox) findViewById(R.id.cb_in_use);

		// get preferences for metric or us needle sizing
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String size_option = prefs.getString(PREF_SIZE, "both");
		int size_array;
		if (size_option.contains("metric")) {
			size_array = R.array.metric_size_array;
		} else if (size_option.contains("us")) {
			size_array = R.array.us_size_array;
		} else {
			size_array = R.array.size_array;
		}

		radio_circular.setOnClickListener(radio_listener);
		radio_straight.setOnClickListener(radio_listener);
		radio_dpns.setOnClickListener(radio_listener);

		spinner_length = (Spinner) findViewById(R.id.spinner_length);
		adapter_length_circ = ArrayAdapter
				.createFromResource(this, R.array.length_array_circ,
						android.R.layout.simple_spinner_item);
		adapter_length_circ
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_length.setAdapter(adapter_length_circ);

		// different adapters for spinner_length
		adapter_length_dpns = ArrayAdapter
				.createFromResource(this, R.array.length_array_dpns,
						android.R.layout.simple_spinner_item);
		adapter_length_dpns
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		adapter_length_straight = ArrayAdapter.createFromResource(this,
				R.array.length_array_straight,
				android.R.layout.simple_spinner_item);
		adapter_length_straight
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner_size = (Spinner) findViewById(R.id.spinner_size);
		ArrayAdapter<CharSequence> adapter_size = ArrayAdapter
				.createFromResource(this, size_array,
						android.R.layout.simple_spinner_item);
		adapter_size
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_size.setAdapter(adapter_size);

		// set default selected radio button
		radio_circular.setChecked(true);
		radio_bamboo.setChecked(true);

		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				// BWL don't open twice
				// ndb.open();
				String size = "";
				String type = "";
				String material = "";
				String length = "";
				int is_metric = 0;
				int is_crochet = 0;
				int size_i = 0;
				int length_i = 0;
				String notes = "";
				switch (rg_type.getCheckedRadioButtonId()) {
				case R.id.radio_dpns:
					type = "dpns";
					break;
				case R.id.radio_straight:
					type = "straight";
					break;
				default:
					type = "circular";
					break;
				}
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
				default:
					material = "bamboo";
					break;
				}
				int in_use = 0;
				if (cb_in_use.isChecked()) {
					in_use = 1;
				}
				size = spinner_size.getSelectedItem().toString();
				size_i = spinner_size.getSelectedItemPosition();

				length = spinner_length.getSelectedItem().toString();
				length_i = spinner_length.getSelectedItemPosition();

				ndb.createNeedle(size, size_i, type, material, length,
						length_i, is_metric, is_crochet, notes, in_use);
				Intent i = new Intent(NeedleAddActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 0);
				startActivity(i);
			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		try {
			ndb.close();
		} catch (NullPointerException npe) {
			Log.d("knittingstash", "caught null pointer excpetion");
			npe.printStackTrace();
		}
	}
}
