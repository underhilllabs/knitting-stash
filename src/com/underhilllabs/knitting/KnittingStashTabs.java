package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class KnittingStashTabs extends Activity {
	private static final int PREF_ID = Menu.FIRST;
	private static final int TABS_ID = Menu.FIRST + 1;
	private static final int LIST_ID = Menu.FIRST + 2;

	// private boolean mAlreadyAgreedToEula = false;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mAlreadyAgreedToEula = Eula.show(this);

		setContentView(R.layout.knitting_stash_home);

		findViewById(R.id.crochet_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i = new Intent(KnittingStashTabs.this,
								HookListView.class);
						startActivity(i);
					}
				});
		findViewById(R.id.knit_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i2 = new Intent(KnittingStashTabs.this,
								NeedleListView.class);
						startActivity(i2);
					}
				});
		findViewById(R.id.counter_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i3 = new Intent(KnittingStashTabs.this,
								CounterListView.class);
						startActivity(i3);
					}
				});
		findViewById(R.id.project_button).setOnClickListener(
				new View.OnClickListener() {
					public void onClick(View v) {
						Intent i4 = new Intent(KnittingStashTabs.this,
								ProjectListView.class);
						startActivity(i4);
					}
				});
	}

	/** {@inheritDoc} */
	public void onEulaAgreedTo() {
		//
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem prefm = menu.add(0, PREF_ID, 0, "Preferences");
		MenuItem tabsm = menu.add(0, TABS_ID, 0, "Tabs View");
		MenuItem listm = menu.add(0, LIST_ID, 0, "List View");

		// prefm.setIcon(R.drawable.ic_menu_preferences);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case PREF_ID:
			Intent i = new Intent(KnittingStashTabs.this,
					myPreferencesActivity.class);
			startActivity(i);
			return true;
		case TABS_ID:
			Intent i2 = new Intent(KnittingStashTabs.this,
					KnittingStashTabs.class);
			startActivity(i2);
			return true;
		case LIST_ID:
			Intent i3 = new Intent(KnittingStashTabs.this,
					KnittingStashTabs.class);
			startActivity(i3);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

}
