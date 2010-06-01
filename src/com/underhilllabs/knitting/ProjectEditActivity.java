package com.underhilllabs.knitting;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

public class ProjectEditActivity extends Activity {


/** Called when the activity is first created. */

 	private DbAdapter pdb;
 	private EditText notes_field;
 	private EditText shopping_field;
 	private EditText name_field;
 	private Spinner spinner_status;
 	private long rowid;
 	private String img_path;
 	private static int TAKE_PICTURE=1;
 	//private Uri outputFileUri;
 	private ImageView pictureHolder;
	private static final int PHOTO_ID = Menu.FIRST;
	private String photo_menu_str;
	private ArrayAdapter<CharSequence>  adapter_status;
	private Cursor cur;
	private String f_uri;
	
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
 				String notes=notes_field.getEditableText().toString();
 				String name=name_field.getEditableText().toString();
 				DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                java.util.Date date = new java.util.Date();
                String lastmod = dateFormat.format(date);
 				String status = spinner_status.getSelectedItem().toString();
 				int status_i = spinner_status.getSelectedItemPosition();

 				notes = notes_field.getText().toString();
 				String needed_shopping = shopping_field.getText().toString();
 				pdb.updateProject(rowid,name,lastmod,status,status_i,f_uri,notes,needed_shopping);
 				
 				Intent i = new Intent(ProjectEditActivity.this,KnittingStashHome.class);
 				i.putExtra("com.underhilllabs.knitting.tabid",2);
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
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
	  if (resultCode == Activity.RESULT_OK && requestCode == SELECT_SHOT) {
	    //pic_uri = data.toURI();
	    Uri chosenImageUri = data.getData();
	    pic_uri = chosenImageUri.getSchemeSpecificPart();
	    
		//Toast.makeText(ProjectEditActivity.this, "uri: "+pic_uri, Toast.LENGTH_LONG);
	  }
	} 
	*/	
	private boolean setWidgets(long row_id) {
		cur = pdb.fetchProject(row_id);
		startManagingCursor(cur);
		//1 = name, 2 start
		//3 = lastmod, 
		// 4 = notes
		//Toast.makeText(ProjectEditActivity.this, " 1:"+cur.getString(6) , Toast.LENGTH_LONG).show();
		//spinner_size.setSelection(cur.getInt(2));
		name_field.setText(cur.getString(1));
		notes_field.setText(cur.getString(7));
		shopping_field.setText(cur.getString(8));
        adapter_status = ArrayAdapter.createFromResource(
                this, R.array.status_array, android.R.layout.simple_spinner_item);
        adapter_status.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_status.setAdapter(adapter_status);
		int status_i= cur.getInt(5);
		spinner_status.setSelection(status_i);
		String img_str = cur.getString(6);
		Bitmap bm = null;
		if(img_str!=null) {
			Uri myUri = Uri.parse(img_str);
			bm = BitmapFactory.decodeFile(myUri.getSchemeSpecificPart());
		} else {
			bm = BitmapFactory.decodeFile("/sdcard/pic"+rowid+".png");
		}
		if(bm != null) {
			photo_menu_str = "Change Project Photo";
			pictureHolder.setImageBitmap(bm);   
		} else {
			photo_menu_str = "Take Project Photo";	
		}
		return true;
	}

	/*
	 * take a picture and save full image to sdcard/ external storage
	 * 
	 */
	/*
	private void saveFullImage() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		File pic_file = new File(Environment.getExternalStorageDirectory(),"pic"+rowid+".jpg");
		outputFileUri = Uri.fromFile(pic_file);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		startActivityForResult(intent, TAKE_PICTURE);
	}
	*/
	
	/*
	 * take a picture and save thumb image to sdcard/ external storage
	 * 
	 */
	
	private void getThumbnailPicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent,TAKE_PICTURE);
	}
	
	@Override
	public void onActivityResult (int requestCode, int resultCode, Intent data) {
	  if (requestCode == TAKE_PICTURE) {
		  if(data != null  && data.hasExtra("data")) {
			  Bundle b = data.getExtras(); // Kept as a Bundle to check for other things in my actual code
			  Bitmap pic = (Bitmap) b.get("data");
			  pictureHolder.setImageBitmap(pic);
			  //use the getExternalStorage.. instead
			  //String filename = "/sdcard/pic"+rowid+".png";
			  try {
				  // create a File object for the parent directory http://docs.google.com/Doc?docid=0AX7RnSwRegJfYWg3eG5za2t4bnFmXzEyNGZkdDV0d2R6&hl=en
				  File imgDir = new File(Environment.getExternalStorageDirectory(),"/knittingstash/");
				  // have the object build the directory structure, if needed.
				  imgDir.mkdirs();
				  File myfile = new File(imgDir, "pic"+rowid+".png");
				  FileOutputStream out = new FileOutputStream(myfile);
				  f_uri = myfile.toURI().toString();
				  //writing to 
				  //FileOutputStream out = new FileOutputStream(filename);
                      pic.compress(Bitmap.CompressFormat.PNG, 90, out);
                      out.flush();
                      out.close();
              	} catch (Exception e) {
                      e.printStackTrace();
              	}
		  }
 

	  }
		
	  
	} 	

} 