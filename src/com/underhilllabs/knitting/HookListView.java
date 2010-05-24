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

public class HookListView extends ListActivity {
	
	private DbAdapter hdb;
	private Cursor cur;
	private AlertDialog alertDialog;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int VIEW_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int PREFS_ID = Menu.FIRST+1;
    private static final int SORT_SIZE = Menu.FIRST+3;
    private static final int SORT_TYPE = Menu.FIRST+4;

	
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hook_list_activity_view);
        hdb = new DbAdapter(this);
        hdb.open();
        //Toast.makeText(this, "hooks database is open", Toast.LENGTH_LONG);

        ListView lv = getListView();
        registerForContextMenu(lv);
        
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Intent i = new Intent(HookListView.this, HookView.class);
		        i.putExtra("com.underhilllabs.knitting.rowid", id);
		        startActivity(i);
				
			}
         });

        
        fill_data("KEY_SIZE_I");
        
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	hdb.close();
    }
	public void fill_data(String sort_key) {

		cur = hdb.fetchAllHooks_simple(sort_key);
		//cur = hdb.fetchAllHooks();        
       	startManagingCursor(cur);
       	String[] displayf = new String[] { DbAdapter.KEY_SIZE,  DbAdapter.KEY_NOTES,DbAdapter.KEY_MATERIAL};
       	int[] displayv = new int[] { R.id.size_row,R.id.notes_row,R.id.material_row};
       	//setListAdapter(new SimpleCursorAdapter());
       	setListAdapter(new SimpleCursorAdapter(this,R.layout.hook_list_row, cur,displayf, displayv));
 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem addm = menu.add(0, INSERT_ID, 0, R.string.menu_add_hook);
        addm.setIcon(android.R.drawable.ic_menu_add);
        MenuItem sorts = menu.add(0, SORT_SIZE, 1, R.string.sort_size);
        MenuItem sortt = menu.add(0, SORT_TYPE, 2, R.string.sort_type);
        sorts.setIcon(android.R.drawable.ic_menu_sort_by_size);
        sortt.setIcon(android.R.drawable.ic_menu_sort_alphabetically);
        MenuItem homem = menu.add(0, PREFS_ID, 0, R.string.menu_preferences);
        homem.setIcon(android.R.drawable.ic_menu_preferences);
        
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            Intent i = new Intent(this, HookAddActivity.class);
            startActivity(i);
            return true;
        case SORT_SIZE:
        	fill_data("KEY_SIZE_I");
        	return true;
        case SORT_TYPE:
        	fill_data("KEY_TYPE");
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
    	menu.add(0, VIEW_ID, 0, R.string.menu_view);
    	menu.add(0, EDIT_ID, 0, R.string.menu_edit);
    	menu.add(0, DELETE_ID,0,  R.string.menu_delete);
    	//edit_item.setIcon(android.R.drawable.ic_menu_edit); 
    	//del_item.setIcon(android.R.drawable.ic_menu_delete);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case VIEW_ID:
			Intent i = new Intent(this, HookView.class);
	        i.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i);
			return true;
    	case EDIT_ID:
			Intent i2 = new Intent(this, HookEditActivity.class);
	        i2.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i2);
			return true;
		case DELETE_ID:
    		deleteNeedle(info.id);
    		return true;
    	default:
    		return super.onContextItemSelected(item);
    	}
    }
    public boolean deleteNeedle(long nid) {
    	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(HookListView.this);
        builder.setTitle(R.string.delete_dialog_title_hook)
        	.setMessage(R.string.delete_dialog_text_hook)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		hdb.deleteHook(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(HookListView.this,KnittingStashHome.class);
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
