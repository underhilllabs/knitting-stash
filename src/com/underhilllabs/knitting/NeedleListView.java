package com.underhilllabs.knitting;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;


public class NeedleListView extends ListActivity {
	
	private DbAdapter ndb;
	private Cursor cur;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int VIEW_ID = Menu.FIRST;
    private static final int EDIT_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    private static final int SORT_SIZE = Menu.FIRST+3;
    private static final int SORT_TYPE = Menu.FIRST+4;
    private static final int PREFS_ID = Menu.FIRST+5;
    private static final int SORT_IN_USE = Menu.FIRST+6;
    private Resources r;
    private String[] size_array;
    public static final String PREF_SIZE_OPTION = "NEEDLE_SIZE_OPTION";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.needle_list_activity_view);
        ListView lv = getListView();
        r = this.getResources();
        //lv.setTextFilterEnabled(true);
        registerForContextMenu(lv);
        String size_option = "";
        Context context = getApplicationContext();
 		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
 		size_option = prefs.getString(PREF_SIZE_OPTION, "");
 		if(size_option.contains("us")) {
 			size_array= getResources().getStringArray(R.array.us_size_array);
 		} else if(size_option.contains("metric")) {
 			size_array= getResources().getStringArray(R.array.metric_size_array);
 		} else {
 			size_array= getResources().getStringArray(R.array.size_array);
 		}
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Intent i = new Intent(NeedleListView.this, NeedleView.class);
		        i.putExtra("com.underhilllabs.knitting.rowid", id);
		        startActivity(i);
				
			}
         });

        ndb = new DbAdapter(this);
        ndb.open();
        
        
        fill_data("KEY_SIZE_I");
        
    }

	public void fill_data(String sort_key) {
		if(sort_key.contains("in_use")) {
			cur = ndb.fetchAllAvailableNeedles();
		} else {
			cur = ndb.fetchAllNeedles_simple(sort_key);
		}
       	startManagingCursor(cur);
       	String[] displayFields = new String[] { DbAdapter.KEY_SIZE, DbAdapter.KEY_TYPE, DbAdapter.KEY_LENGTH,DbAdapter.KEY_MATERIAL,DbAdapter.KEY_IN_USE };
       	int[] displayViews = new int[] { R.id.size_row, R.id.type_row,R.id.length_row,R.id.material_row,R.id.in_use_row };
       	SimpleCursorAdapter needles = new SimpleCursorAdapter(this, 
                R.layout.needle_list_row, cur, 
                displayFields, displayViews);
       	needles.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
			
			
			public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
				int nCheckedIndex = cursor.getColumnIndexOrThrow(DbAdapter.KEY_IN_USE);
				int nSizeIndex =  cursor.getColumnIndexOrThrow(DbAdapter.KEY_SIZE);
		        if (columnIndex == nCheckedIndex) {
		          TextView tv = (TextView) view;
		          boolean bChecked = (cursor.getInt(nCheckedIndex) != 0);
		          if (cursor.getInt(nCheckedIndex) != 0) {
		        	  //tv.setTextColor(Color.BLACK);
		        	  //tv.setBackgroundColor(Color.rgb(0, 134, 100));
		        	  tv.setBackgroundColor(r.getColor(R.color.green));
		        	  
		        	  tv.setPadding(5, 0, 5, 0);
		        	  tv.setText("In Use");
		          } else {
		        	  tv.setPadding(0, 0, 0, 0);
		        	  tv.setBackgroundColor(Color.BLACK);
		        	  tv.setText("");
		        	  //return false;
		          }
		          return true;
		          
		        } else if (columnIndex == nSizeIndex) {
			       TextView tv = (TextView) view;
			       int my_size_i = cursor.getInt(2);
			       try {
			    	   tv.setText(size_array[my_size_i]);
			       } catch (IndexOutOfBoundsException ie) {
			    	   Log.d("knittingstash", "index out of bounds on size_array");
			       }
			       return true;
			       
			    }  
		        
		        
				return false;
			}
		}
       			
       	);
       	setListAdapter(needles);
 
    }
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	ndb.close();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean result = super.onCreateOptionsMenu(menu);
        MenuItem addm = menu.add(0, INSERT_ID, 0, R.string.menu_add);
        MenuItem sorts = menu.add(0, SORT_SIZE, 1, R.string.sort_size);
        MenuItem sortt = menu.add(0, SORT_TYPE, 2, R.string.sort_type);
        MenuItem sorti = menu.add(0, SORT_IN_USE, 3, R.string.sort_in_use);
        addm.setIcon(android.R.drawable.ic_menu_add);
        sorts.setIcon(android.R.drawable.ic_menu_sort_by_size);
        sortt.setIcon(android.R.drawable.ic_menu_sort_alphabetically);
        MenuItem homem = menu.add(0, PREFS_ID, 0, "Preferences");
        homem.setIcon(android.R.drawable.ic_menu_preferences);
        return result;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case INSERT_ID:
            Intent i = new Intent(this, NeedleAddActivity.class);
            startActivity(i);
            return true;
        case SORT_SIZE:
        	fill_data("KEY_SIZE_I");
        	return true;
        case SORT_TYPE:
        	fill_data("KEY_TYPE");
        	return true;
        case SORT_IN_USE:
        	fill_data("in_use");
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
    	//edit_item.setIcon(android.R.drawable.ic_menu_edit); 
    	//del_item.setIcon(android.R.drawable.ic_menu_delete);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
    	switch (item.getItemId()) {
    	case VIEW_ID:
			Intent i = new Intent(this, NeedleView.class);
	        i.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i);
			return true;
    	case EDIT_ID:
			Intent i2 = new Intent(this, NeedleEditActivity.class);
	        i2.putExtra("com.underhilllabs.knitting.rowid", info.id);
	        startActivity(i2);
			return true;
		case DELETE_ID:
    			ndb.deleteNeedle(info.id);
    			fill_data("KEY_SIZE_I");
    			return true;
    		default:
    			return super.onContextItemSelected(item);
    	}
    }
}
