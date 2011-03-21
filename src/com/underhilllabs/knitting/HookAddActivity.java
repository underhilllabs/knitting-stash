package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class HookAddActivity extends Activity {
	/** Called when the activity is first created. */

	private DbAdapter hdb;
	private RadioButton radio_wood;
	private RadioButton radio_steel;
	private RadioButton radio_bamboo;
	private RadioButton radio_plastic;
	private RadioButton radio_metal;
	private RadioGroup rg_material;
	private Spinner spinner_size;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.hook_add);
		hdb = new DbAdapter(this);
		hdb.open();

		rg_material = (RadioGroup) findViewById(R.id.rg_material);
		radio_wood = (RadioButton) findViewById(R.id.radio_wood);
		radio_bamboo = (RadioButton) findViewById(R.id.radio_bamboo);
		radio_steel = (RadioButton) findViewById(R.id.radio_steel);
		radio_plastic = (RadioButton) findViewById(R.id.radio_plastic);
		radio_metal = (RadioButton) findViewById(R.id.radio_metal);

		// set default checked radio button
		radio_metal.setChecked(true);
		final Button add_button = (Button) findViewById(R.id.add_hook_button);

		spinner_size = (Spinner) findViewById(R.id.spinner_size);
		ArrayAdapter<CharSequence> adapter_size = ArrayAdapter
				.createFromResource(this, R.array.hook_array,
						android.R.layout.simple_spinner_item);
		adapter_size
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_size.setAdapter(adapter_size);

		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				// hdb.open();
				String size = "";
				String material = "";
				int size_i = 0;
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

				hdb.createHook(size, size_i, material, notes);
				Intent i = new Intent(HookAddActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 1);
				startActivity(i);

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		hdb.close();
	}
}
