package com.underhilllabs.knitting;

import android.app.Activity;
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
        MenuItem editm = menu.add(0, EDIT_ID, 0, "Edit Hook");
        editm.setIcon(android.R.drawable.ic_menu_edit);
        MenuItem delm = menu.add(0, DEL_ID, 0, "Delete Hook");
        delm.setIcon(android.R.drawable.ic_menu_delete);
        MenuItem homem = menu.add(0, HOME_ID, 0, "Home");
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
			hdb.deleteHook(rowid);
        	Intent i2 = new Intent(this, HookListView.class);
        	startActivity(i2);
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
        material.setText(cur.getString(3));
        notes.setText(cur.getString(4));
        size.setText(cur.getString(1));
        
    }
	
}
