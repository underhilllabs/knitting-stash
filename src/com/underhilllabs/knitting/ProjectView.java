package com.underhilllabs.knitting;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

	
	
public class ProjectView extends Activity {
	//private TextView tvLastmod;
	private TextView tvName;
	private TextView tvNotes;
	private TextView tvShopping;
	//private TextView tvPicuri;
	private ImageView imagev;
	private DbAdapter pdb;
	private long rowid;
	private Cursor cur;
	private String f_uri;
	private String name;
	private String notes;
	private String needed_shopping;
	private String status;
	private String lastmod;
	private int status_i;
	private AlertDialog alertDialog;

	
	private static final int HOME_ID = Menu.FIRST+2;
	private static final int EDIT_ID = Menu.FIRST;
	private static final int DEL_ID = Menu.FIRST+1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_view);
        tvNotes = (TextView) findViewById(R.id.notes);
        tvName = (TextView) findViewById(R.id.name);
        tvShopping = (TextView) findViewById(R.id.shopping);
        //tvLastmod = (TextView) findViewById(R.id.lastmod);
        imagev = (ImageView) findViewById(R.id.imagev);

        // get rowid from Intent Bundle
        Bundle extras = getIntent().getExtras();
        rowid = extras.getLong("com.underhilllabs.knitting.rowid"); 
 		
        
        imagev.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //Intent i = new Intent(ProjectView.this, ProjectFullViewActivity.class);
                //i.putExtra("com.underhilllabs.knitting.rowid", rowid);
                //startActivity(i);
            	
                if(f_uri != null) {
                	Uri myUri = Uri.parse(f_uri);
                	Intent intent = new Intent();
                	intent.setAction(android.content.Intent.ACTION_VIEW);
                	intent.setDataAndType(myUri, "image/png");
                	startActivity(intent);
                } else {
                	String path = "/sdcard/pic"+rowid+".png";
                	Intent intent = new Intent();
                	intent.setAction(android.content.Intent.ACTION_VIEW);
                	intent.setDataAndType(Uri.fromFile(new File(path)), "image/png");
                	startActivity(intent);
                }
            	
            	
            }
        });


        
        
        pdb = new DbAdapter(this);
        pdb.open();
        fill_data();
        
        //check if there is no f_uri,
        // if so upgrade image path
        if(f_uri == null)  {
        	File img_f = new File("/sdcard/pic"+rowid+".png");
        	String new_f = "/sdcard/knittingstash/pic"+rowid+".png";
        	//make sure the knittingstash dir is created
        	File imgDir = new File(Environment.getExternalStorageDirectory(),"/knittingstash/");
        	imgDir.mkdir();
        	
        	if(img_f.exists()) {
        		try {
        			//String new_f = "/sdcard/knittingstash/"+rowid+".png";
        			copyFile(img_f,new File(new_f));
        		} catch (IOException ioe) {
        			Log.d("knittingstash","caught ioe");
        			
        		}
        		
        	}
        	pdb.updateProject(rowid, name, lastmod, status, status_i, "file:"+new_f, notes,needed_shopping);
        }
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	pdb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        //menu.setHeaderTitle("");
        MenuItem editm = menu.add(0, EDIT_ID, 0, "Edit Project");
        editm.setIcon(android.R.drawable.ic_menu_edit);
        MenuItem delm = menu.add(0, DEL_ID, 0, "Delete Project");
        delm.setIcon(android.R.drawable.ic_menu_delete);
        MenuItem homem = menu.add(0, HOME_ID, 0, "Home");
        homem.setIcon(R.drawable.ic_menu_home);
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EDIT_ID:
            Intent i = new Intent(this, ProjectEditActivity.class);
            i.putExtra("com.underhilllabs.knitting.rowid", rowid);
            startActivity(i);
            return true;
        case DEL_ID:
			deleteProject(rowid);
        	//Intent i2 = new Intent(this, ProjectListView.class);
        	//startActivity(i2);
        	return true;
        case HOME_ID:
			Intent i3 = new Intent(this, KnittingStashHome.class);
			i3.putExtra("com.underhilllabs.knitting.tabid",2);
        	startActivity(i3);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void fill_data() {
    	cur = pdb.fetchProject(rowid);
        startManagingCursor(cur);
        name = cur.getString(1);
        notes = cur.getString(7);
        needed_shopping = cur.getString(8);
        lastmod = cur.getString(3);
        status = cur.getString(4);
        status_i =  cur.getInt(5);
        
        tvName.setText(cur.getString(1));
        tvNotes.setText(cur.getString(7));
        tvShopping.setText(cur.getString(8));
        f_uri = cur.getString(6);  
        if(f_uri != null) {
        	Uri myUri = Uri.parse(f_uri);
        	Bitmap bm = BitmapFactory.decodeFile(myUri.getSchemeSpecificPart());
        	imagev.setImageBitmap(bm);
        } else {
        	//Bitmap bm = BitmapFactory.decodeFile("/sdcard/pic"+rowid+".png");
        	//imagev.setImageBitmap(bm);
        	
        }
    }
    public void copyFile(File sourceFile, File destFile) throws IOException {
    	 if(!destFile.exists()) {
    	  destFile.createNewFile();
    	 }

    	 FileChannel source = null;
    	 FileChannel destination = null;
    	 try {
    	  source = new FileInputStream(sourceFile).getChannel();
    	  destination = new FileOutputStream(destFile).getChannel();
    	  destination.transferFrom(source, 0, source.size());
    	 }
    	 finally {
    	  if(source != null) {
    	   source.close();
    	  }
    	  if(destination != null) {
    	   destination.close();
    	  }
    	}
 	
    }
    
    public boolean deleteProject(long nid) {
    	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(ProjectView.this);
        builder.setTitle(R.string.delete_dialog_title_project)
        	.setMessage(R.string.delete_dialog_text_project)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		pdb.deleteProject(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(ProjectView.this,KnittingStashHome.class);
                    i2.putExtra("com.underhilllabs.knitting.tabid",2);
                	startActivity(i2);
            		//fill_data("KEY_SIZE_I");    			
                    return;
                } })
             .setNegativeButton(R.string.dialog_button_cancel, new DialogInterface.OnClickListener() {
                 public void onClick(DialogInterface dialog, int which) {
                     return;
                 }});

        alertDialog = builder.create();
        alertDialog.show();


    	return true;
    }
}
