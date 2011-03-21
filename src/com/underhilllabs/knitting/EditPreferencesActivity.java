package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

public class EditPreferencesActivity extends Activity {
	private CheckBox cb_metric;
	private Button update_button;
	private TextView tv_message;
	public static final String PREF_SIZE = "NEEDLE_SIZE_METRIC";
	SharedPreferences prefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.preferences2);
		Context context = getApplicationContext();
		prefs = PreferenceManager.getDefaultSharedPreferences(context);

		cb_metric = (CheckBox) findViewById(R.id.cb_metric);
		String[] flist = getApplication().fileList();

		update_button = (Button) findViewById(R.id.update_button);
		tv_message = (TextView) findViewById(R.id.tv_message);
		update_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				boolean is_metric = cb_metric.isChecked();
				Editor editor = prefs.edit();
				editor.putBoolean(PREF_SIZE, is_metric);
				editor.commit();
				EditPreferencesActivity.this.setResult(RESULT_OK);
				finish();
				// Intent i = new
				// Intent(EditPreferencesActivity.this,KnittingStashHome.class);
				// startActivity(i);

			}
		});

	}

}
