package com.underhilllabs.knitting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

	
	
public class NeedleView extends Activity {
	private TextView material;
	private TextView length;
	private TextView size;
	private TextView type;
	private TextView notes;
	private TextView tv_in_use;
	private DbAdapter ndb;
	private long rowid;
	private Cursor cur;
	private int in_use;
	private AlertDialog alertDialog;
	private static final int EDIT_ID = Menu.FIRST;
	private static final int DEL_ID = Menu.FIRST+1;
	private static final int HOME_ID = Menu.FIRST+2;
 	public static final String PREF_SIZE = "NEEDLE_SIZE_OPTION";
 	private String[] size_array;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_needle);
        notes = (TextView) findViewById(R.id.notes);
        size = (TextView) findViewById(R.id.size);
        type = (TextView) findViewById(R.id.type);
        tv_in_use = (TextView) findViewById(R.id.tv_in_use);
        length = (TextView) findViewById(R.id.length);
        material = (TextView) findViewById(R.id.material);
 		// get prefs for metric or us needle sizing
        Context context = getApplicationContext();
 		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
 		String size_option = prefs.getString(PREF_SIZE, "both");
 		size_array = null;
 		if(size_option.contains("us")) {
 			size_array= getResources().getStringArray(R.array.us_size_array);
 		} else if(size_option.contains("metric")) {
 			size_array= getResources().getStringArray(R.array.metric_size_array);
 		} else {
 			size_array= getResources().getStringArray(R.array.size_array);
 		}
        
        // get rowid from Intent Bundle
        Bundle extras = getIntent().getExtras();
        rowid = extras.getLong("com.underhilllabs.knitting.rowid"); 
 		
        
        ndb = new DbAdapter(this);
        ndb.open();
        fill_data();
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	ndb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem editm = menu.add(0, EDIT_ID, 0, R.string.edit_app_name);
        editm.setIcon(android.R.drawable.ic_menu_edit);
        MenuItem delm = menu.add(0, DEL_ID, 0, R.string.del_app_name);
        delm.setIcon(android.R.drawable.ic_menu_delete);
        MenuItem homem = menu.add(0, HOME_ID, 0, R.string.menu_home);
        homem.setIcon(R.drawable.ic_menu_home);
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EDIT_ID:
            Intent i = new Intent(this, NeedleEditActivity.class);
            i.putExtra("com.underhilllabs.knitting.rowid", rowid);
            startActivity(i);
            return true;
        case DEL_ID:
			deleteNeedle(rowid);
        	return true;
        case HOME_ID:
        	Intent i3 = new Intent(this, KnittingStashHome.class);
        	i3.putExtra("com.underhilllabs.knitting.tabid",0);
            startActivity(i3);
                
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void fill_data() {
    	cur = ndb.fetchNeedle(rowid);
        startManagingCursor(cur);
        //type.setText(cur.getString(5) );
        if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_TYPE)).equals("dpns")) {
        	type.setText(R.string.dpns);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_TYPE)).equals("straight")) {
        	type.setText(R.string.straight);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_TYPE)).equals("circular")) {
        	type.setText(R.string.circular);
        }
        //material.setText(cur.getString(6));
        if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals("wood")) {
        	material.setText(R.string.wood);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals("plastic")) {
        	material.setText(R.string.plastic);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals("metal")) {
        	material.setText(R.string.metal);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals("steel")) {
        	material.setText(R.string.steel);
        } else if (cur.getString(cur.getColumnIndex(DbAdapter.KEY_MATERIAL)).equals("bamboo")) {
        	material.setText(R.string.bamboo);
        } else {
        	//material.setText(R.string.Steel);
        }
        length.setText(cur.getString(cur.getColumnIndex(DbAdapter.KEY_LENGTH)));
        notes.setText(cur.getString(cur.getColumnIndex(DbAdapter.KEY_NOTES)));
        //size.setText(cur.getString(1));
        int size_i = cur.getInt(cur.getColumnIndex(DbAdapter.KEY_SIZE_I));
        size.setText(size_array[size_i]);
        in_use=cur.getInt(cur.getColumnIndex(DbAdapter.KEY_IN_USE));
        if(in_use>0) {
        	Resources r  = NeedleView.this.getResources();
        	tv_in_use.setBackgroundColor(r.getColor(R.color.green));
      	    tv_in_use.setPadding(5, 0, 5, 0);
        	tv_in_use.setText(R.string.menu_in_use);
        	
        }
    }
    public boolean deleteNeedle(long nid) {
    	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(NeedleView.this);
        builder.setTitle(R.string.delete_dialog_title_needle)
        	.setMessage(R.string.delete_dialog_text_needle)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		ndb.deleteNeedle(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(NeedleView.this,KnittingStashHome.class);
                    i2.putExtra("com.underhilllabs.knitting.tabid",0);
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
