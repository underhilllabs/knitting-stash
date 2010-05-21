package com.underhilllabs.knitting;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;


public class CounterListView extends ListActivity {
	
	private DbAdapter pdb;
	private Cursor cur;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int VIEW_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int PREFS_ID = Menu.FIRST + 3;
    private AlertDialog alertDialog;
	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_list_activity_view);
        pdb = new DbAdapter(this);
        pdb.open();
        
        ListView lv = getListView();
        registerForContextMenu(lv);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Intent i = new Intent(CounterListView.this, CounterView.class);
		        i.putExtra("com.underhilllabs.knitting.rowid", id);
		        startActivity(i);
				
			}
         });

        
        fill_data();
        
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	pdb.close();
    }
	public void fill_data() {
    	cur = pdb.fetchAllCounters();        
    	//Toast.makeText(CounterListView.this, "current val"+cur.getShort(2), Toast.LENGTH_SHORT);
       	startManagingCursor(cur);
       	String[] displayFields = new String[] { DbAdapter.KEY_NAME,DbAdapter.KEY_NOTES};
       	int[] displayViews = new int[] { R.id.name_row,R.id.notes_row};
       	setListAdapter(new SimpleCursorAdapter(this,R.layout.counter_list_row, cur,displayFields, displayViews));
 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem addm = menu.add(0, INSERT_ID, 0, "Add Counter");
        addm.setIcon(android.R.drawable.ic_menu_add);
        MenuItem homem = menu.add(0, PREFS_ID, 0, "Preferences");
        homem.setIcon(android.R.drawable.ic_menu_preferences);
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            Intent i = new Intent(this, CounterAddActivity.class);
            startActivity(i);
            return true;
    	case PREFS_ID:
    		Intent i3 = new Intent(this, myPreferencesActivity.class);
    	    startActivity(i3);
    	    return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
    	super.onCreateContextMenu(menu, v, menuInfo);
    	menu.add(0, VIEW_ID, 0, "View");
    	menu.add(0, EDIT_ID, 0, "Edit");
    	menu.add(0, DELETE_ID,0,  "Delete");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case VIEW_ID:
			Intent i = new Intent(this, CounterView.class);
	        i.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i);
			return true;
    	case EDIT_ID:
			Intent i2 = new Intent(this, CounterEditActivity.class);
	        i2.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i2);
			return true;
    	case DELETE_ID:
    			deleteCounter(info.id);
    			//fill_data();
    			return true;
    		default:
    			return super.onContextItemSelected(item);
    	}
    }
    public boolean deleteCounter(long nid) {
       	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(CounterListView.this);
        builder.setTitle(R.string.delete_dialog_title_counter)
        	.setMessage(R.string.delete_dialog_text_counter)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		pdb.deleteCounter(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(CounterListView.this,KnittingStashHome.class);
                    i2.putExtra("com.underhilllabs.knitting.tabid",3);
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
