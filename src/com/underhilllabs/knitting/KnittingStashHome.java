package com.underhilllabs.knitting;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class KnittingStashHome extends TabActivity {
	private int tabid;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// mAlreadyAgreedToEula = Eula.show(this);
		tabid = 0;
		setContentView(R.layout.knitting_stash_tabs);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			tabid = extras.getInt("com.underhilllabs.knitting.tabid");
		}
		setupTabs();
	}

	public void setupTabs() {
		Resources res = this.getResources();
		TabHost tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec("needles_tab").setIndicator(
				"Needles", res.getDrawable(R.drawable.ic_tab_needle))
				.setContent(needlesIntent()));
		tabHost.addTab(tabHost.newTabSpec("hooks_tab").setIndicator("Hooks",
				res.getDrawable(R.drawable.ic_tab_hook)).setContent(
				hooksIntent()));
		tabHost.addTab(tabHost.newTabSpec("projects_tab").setIndicator(
				"Projects", res.getDrawable(R.drawable.ic_tab_project))
				.setContent(projectsIntent()));
		tabHost.addTab(tabHost.newTabSpec("counters_tab").setIndicator(
				"Counters", res.getDrawable(R.drawable.ic_tab_counter))
				.setContent(countersIntent()));

		tabHost.setCurrentTab(tabid);
	}

	public Intent needlesIntent() {
		Intent intent = new Intent(KnittingStashHome.this, NeedleListView.class);
		// intent.setClassName("com.underhilllabs.knitting",
		// "com.underhilllabs.knitting.knittingstash.NeedlesView");

		// Bundle extras = new Bundle();
		// extras.putLong("rowid", rowid);

		// intent.putExtras(extras);
		return intent;
	}

	public Intent hooksIntent() {
		Intent intent = new Intent(KnittingStashHome.this, HookListView.class);
		// intent.setClassName("com.underhilllabs.knitting",
		// "com.underhilllabs.knitting.knittingstash.HookView");

		// Bundle extras = new Bundle();
		// extras.putLong("rowid", rowid);

		// intent.putExtras(extras);
		return intent;
	}

	public Intent projectsIntent() {
		Intent intent = new Intent(KnittingStashHome.this,
				ProjectListView.class);
		// intent.setClassName("com.underhilllabs.knitting",
		// "com.underhilllabs.knitting.knittingstash.ProjectView");

		// Bundle extras = new Bundle();
		// extras.putLong("rowid", rowid);

		// intent.putExtras(extras);
		return intent;
	}

	public Intent countersIntent() {
		Intent intent = new Intent(KnittingStashHome.this,
				CounterListView.class);
		// intent.setClassName("com.underhilllabs.knitting",
		// "com.underhilllabs.knitting.knittingstash.CounterView");

		// Bundle extras = new Bundle();
		// extras.putLong("rowid", rowid);

		// intent.putExtras(extras);
		return intent;
	}

}
