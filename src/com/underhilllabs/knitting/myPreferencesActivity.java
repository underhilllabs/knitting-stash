package com.underhilllabs.knitting;

import android.os.Bundle;
import android.preference.PreferenceActivity;

public class myPreferencesActivity extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);

	}
}
