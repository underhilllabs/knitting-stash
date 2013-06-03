package com.underhilllabs.knitting;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

public class ProjectEditActivity extends Activity {

	/** Called when the activity is first created. */

	private DbAdapter pdb;
	private EditText notes_field;
	private EditText shopping_field;
	private EditText name_field;
	private Spinner spinner_status;
	private long rowid;
	private String img_path;
	private static int TAKE_PICTURE = 1;
	// private Uri outputFileUri;
	private ImageView pictureHolder;
	private static final int PHOTO_ID = Menu.FIRST;
	private String photo_menu_str;
	private ArrayAdapter<CharSequence> adapter_status;
	private Cursor cur;
	private String f_uri;

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private Uri fileUri;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_edit);
		pdb = new DbAdapter(this);
		pdb.open();
		// get rowid from Intent Bundle
		Bundle extras = getIntent().getExtras();
		rowid = extras.getLong("com.underhilllabs.knitting.rowid");
		photo_menu_str = "";
		name_field = (EditText) findViewById(R.id.name);
		notes_field = (EditText) findViewById(R.id.notes);
		shopping_field = (EditText) findViewById(R.id.shopping);
		spinner_status = (Spinner) findViewById(R.id.spinner_status);
		pictureHolder = (ImageView) this.findViewById(R.id.imagev);
		f_uri = "";

		final Button add_button = (Button) findViewById(R.id.add_button);
		final Button photo_button = (Button) findViewById(R.id.photo_button);

		setWidgets(rowid);

		photo_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				getThumbnailPicture();
			}
		});
		add_button.setOnClickListener(new Button.OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks
				String notes = notes_field.getEditableText().toString();
				String name = name_field.getEditableText().toString();
				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
				java.util.Date date = new java.util.Date();
				String lastmod = dateFormat.format(date);
				String status = spinner_status.getSelectedItem().toString();
				int status_i = spinner_status.getSelectedItemPosition();

				notes = notes_field.getText().toString();
				String needed_shopping = shopping_field.getText().toString();
				pdb.updateProject(rowid, name, lastmod, status, status_i,
						f_uri, notes, needed_shopping);

				Intent i = new Intent(ProjectEditActivity.this,
						KnittingStashHome.class);
				i.putExtra("com.underhilllabs.knitting.tabid", 2);
				startActivity(i);

			}
		});

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		pdb.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem photom = menu.add(0, PHOTO_ID, 0, photo_menu_str);
		photom.setIcon(R.drawable.ic_menu_camera);
		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case PHOTO_ID:
			getThumbnailPicture();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * @Override public void onActivityResult (int requestCode, int resultCode,
	 * Intent data) { if (resultCode == Activity.RESULT_OK && requestCode ==
	 * SELECT_SHOT) { //pic_uri = data.toURI(); Uri chosenImageUri =
	 * data.getData(); pic_uri = chosenImageUri.getSchemeSpecificPart();
	 * 
	 * //Toast.makeText(ProjectEditActivity.this, "uri: "+pic_uri,
	 * Toast.LENGTH_LONG); } }
	 */
	private boolean setWidgets(long row_id) {
		cur = pdb.fetchProject(row_id);
		startManagingCursor(cur);
		// 1 = name, 2 start
		// 3 = lastmod,
		// 4 = notes
		// Toast.makeText(ProjectEditActivity.this, " 1:"+cur.getString(6) ,
		// Toast.LENGTH_LONG).show();
		// spinner_size.setSelection(cur.getInt(2));
		name_field.setText(cur
				.getString(cur.getColumnIndex(DbAdapter.KEY_NAME)));
		notes_field.setText(cur.getString(cur
				.getColumnIndex(DbAdapter.KEY_NOTES)));
		shopping_field.setText(cur.getString(cur
				.getColumnIndex(DbAdapter.KEY_NEEDED_SHOPPING)));
		adapter_status = ArrayAdapter.createFromResource(this,
				R.array.status_array, android.R.layout.simple_spinner_item);
		adapter_status
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_status.setAdapter(adapter_status);
		int status_i = cur.getInt(5);
		spinner_status.setSelection(status_i);
		String img_str = cur.getString(cur
				.getColumnIndex(DbAdapter.KEY_PICTURE));
		Bitmap bm = null;
		if (img_str != null) {
			Uri myUri = Uri.parse(img_str);
			bm = BitmapFactory.decodeFile(myUri.getSchemeSpecificPart());
		} else {
			bm = BitmapFactory.decodeFile("/sdcard/pic" + rowid + ".png");
		}
		if (bm != null) {
			photo_menu_str = "Change Project Photo";
			pictureHolder.setImageBitmap(bm);
		} else {
			photo_menu_str = "Take Project Photo";
		}
		return true;
	}

	/*
	 * take a picture and save full image to sdcard/ external storage
	 */
	/*
	 * private void saveFullImage() { Intent intent = new
	 * Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	 * 
	 * File pic_file = new
	 * File(Environment.getExternalStorageDirectory(),"pic"+rowid+".jpg");
	 * outputFileUri = Uri.fromFile(pic_file);
	 * intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
	 * startActivityForResult(intent, TAKE_PICTURE); }
	 */

	/*
	 * take a picture and save thumb image to sdcard/ external storage
	 */

	private void getThumbnailPicture() {
        // create Intent to take a picture and return control to the calling application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
        f_uri = fileUri.getPath();
        Log.d("KnittingStash","fileUri" + fileUri);
        Log.d("KnittingStash","f_uri is " + f_uri);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
        // Start the image capture Intent
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if(data == null) {
                    Log.d("KnittingStash","data is null! WTF");
                } else {
                    Log.d("KnittingStash","data ain't null" + data.getDataString());
                }
                // Image captured and saved to fileUri specified in the Intent
                Toast.makeText(this, "Image saved to:\n" +
                        fileUri, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            } else {
                // Image capture failed, advise user
            }

		}

	}

    /** Create a file Uri for saving an image or video */
    private static Uri getOutputMediaFileUri(int type){
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private static File getOutputMediaFile(int type){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "knittingstash");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){
            if (! mediaStorageDir.mkdirs()){
                Log.d("KnittingStash", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE){
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        } else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

}