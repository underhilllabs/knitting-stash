package com.underhilllabs.knitting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

	
	
public class HookView extends Activity {
	private TextView material;
	private TextView size;
	private TextView notes;
	private DbAdapter hdb;
	private long rowid;
	private Cursor cur;
	private AlertDialog alertDialog;
	private static final int HOME_ID = Menu.FIRST+2;
	private static final int EDIT_ID = Menu.FIRST;
	private static final int DEL_ID = Menu.FIRST+1;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hook_view);
        notes = (TextView) findViewById(R.id.notes);
        size = (TextView) findViewById(R.id.size);
        material = (TextView) findViewById(R.id.material);
        
        
        // get rowid from Intent Bundle
        Bundle extras = getIntent().getExtras();
        rowid = extras.getLong("com.underhilllabs.knitting.rowid"); 
 		
        
        hdb = new DbAdapter(this);
        hdb.open();
        fill_data();
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	hdb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem editm = menu.add(0, EDIT_ID, 0, R.string.menu_edit_hook);
        editm.setIcon(android.R.drawable.ic_menu_edit);
        MenuItem delm = menu.add(0, DEL_ID, 0, R.string.menu_delete_hook);
        delm.setIcon(android.R.drawable.ic_menu_delete);
        MenuItem homem = menu.add(0, HOME_ID, 0, R.string.menu_home);
        homem.setIcon(R.drawable.ic_menu_home);
        return result;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EDIT_ID:
            Intent i = new Intent(this, HookEditActivity.class);
            i.putExtra("com.underhilllabs.knitting.rowid", rowid);
            startActivity(i);
            return true;
        case DEL_ID:
			deleteNeedle(rowid);
        	return true;
        case HOME_ID:
			Intent i3 = new Intent(this, KnittingStashHome.class);
			i3.putExtra("com.underhilllabs.knitting.tabid",1);
        	startActivity(i3);
        	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void fill_data() {
    	cur = hdb.fetchHook(rowid);
        startManagingCursor(cur);
        // need to display translation 
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
        //material.setText(cur.getString(3));
        notes.setText(cur.getString(cur.getColumnIndex(DbAdapter.KEY_NOTES)));
        size.setText(cur.getString(cur.getColumnIndex(DbAdapter.KEY_SIZE)));
        
    }
    public boolean deleteNeedle(long nid) {
    	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(HookView.this);
        builder.setTitle(R.string.delete_dialog_title_hook)
        	.setMessage(R.string.delete_dialog_text_hook)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		hdb.deleteHook(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(HookView.this,KnittingStashHome.class);
                    i2.putExtra("com.underhilllabs.knitting.tabid",1);
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
