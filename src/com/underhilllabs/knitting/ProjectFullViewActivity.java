package com.underhilllabs.knitting;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;

public class ProjectFullViewActivity extends Activity {
	private ImageView iv;
	private long rowid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_project_view);
		iv = (ImageView) findViewById(R.id.iv);
		Bundle extras = getIntent().getExtras();
		rowid = extras.getLong("com.underhilllabs.knitting.rowid");
		//String img_file = "/sdcard/pic" + rowid + ".png";
        String img_file = Environment.getExternalStorageDirectory() + "/knittingstash/" + rowid + ".png";
		Bitmap bm = BitmapFactory.decodeFile(img_file);
		// iv.setMinimumWidth();
		// iv.setMinimumWidth(bm.getWidth());
		// iv.setMinimumHeight(bm.getHeight());
		iv.setImageBitmap(bm);

	}

}
