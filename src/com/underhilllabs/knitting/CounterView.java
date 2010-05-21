package com.underhilllabs.knitting;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

	
	
public class CounterView extends Activity {
	private TextView tvName;
	private TextView tvNotes;
	private TextView tvMode;
	private Button counter_button;
	private DbAdapter pdb;
	private long rowid;
	private int cur_val;
	private Cursor cur;
	private int toggle_int;
	private String toggle_str;
	private AlertDialog alertDialog;

	
	private static final int HOME_ID = Menu.FIRST+2;
	private static final int EDIT_ID = Menu.FIRST;
	private static final int DEL_ID = Menu.FIRST+1;
	private static final int TOGGLE_ID = Menu.FIRST+3;
	private static final int RESET_ID = Menu.FIRST+4;
	
	private MenuItem togm;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.counter_view);
        tvNotes = (TextView) findViewById(R.id.notes);
        tvName = (TextView) findViewById(R.id.name);
        tvMode = (TextView) findViewById(R.id.mode_text);
        tvMode.setText("increase");
        counter_button = (Button)findViewById(R.id.counter_button);

        // get rowid from Intent Bundle
        Bundle extras = getIntent().getExtras();
        rowid = extras.getLong("com.underhilllabs.knitting.rowid"); 
 
        // toggle from positive adder to negative adder
        toggle_str = "Switch to decrement";
 		toggle_int = 1;
        
        pdb = new DbAdapter(this);
        pdb.open();
        fill_data();
        
		counter_button.setOnClickListener(new Button.OnClickListener() {
 			public void onClick(View v) {
 				// Perform action on clicks
 				cur_val = cur_val + toggle_int;
 				counter_button.setText(""+cur_val);
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
        MenuItem editm = menu.add(0, EDIT_ID, 0, "Edit Counter");
        editm.setIcon(android.R.drawable.ic_menu_edit);
        // does it make sense to delete counter here?  
        //    No, just on long click
        MenuItem delm = menu.add(0, DEL_ID, 0, "Delete Counter");
        delm.setIcon(android.R.drawable.ic_menu_delete);
        MenuItem homem = menu.add(0, HOME_ID, 0, "Home");
        homem.setIcon(R.drawable.ic_menu_home);
        togm = menu.add(0, TOGGLE_ID, 0, toggle_str);
        togm.setIcon(android.R.drawable.ic_menu_revert);
        MenuItem resm = menu.add(0, RESET_ID, 0, "Reset Counter");
        resm.setIcon(android.R.drawable.ic_menu_revert);
        
        return result;
    }
    @Override
    public void onPause(){
    	super.onPause();
    	//boolean result = super.
    	pdb.setCounter(rowid,cur_val);
	}	
    /*
    @Override
    public void onResume(){
    	//boolean result = super.
    	Cursor cur = pdb.fetchCounter(rowid);
    	cur_val = Integer.parseInt(cur.getString(2));
    	counter_button.setText(""+cur_val);
	}
	*/
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case EDIT_ID:
            Intent i = new Intent(this, CounterEditActivity.class);
            i.putExtra("com.underhilllabs.knitting.rowid", rowid);
            startActivity(i);
            return true;
        case DEL_ID:
			deleteCounter(rowid);
        	//Intent i2 = new Intent(this, CounterListView.class);
        	//startActivity(i2);
        	return true;
        case HOME_ID:
			Intent i3 = new Intent(this, KnittingStashHome.class);
			i3.putExtra("com.underhilllabs.knitting.tabid",3);
        	startActivity(i3);
        	return true;
        case RESET_ID:
        	cur_val = 0;
            counter_button.setText("0");
            return true;
        case TOGGLE_ID:
        	if(toggle_int==1) {
        		toggle_int = -1;
        		toggle_str = "Switch to increment";
        		tvMode.setText("decrease");
        		togm.setTitle(toggle_str);
        	} else {
        		toggle_int = 1;
        		toggle_str = "Switch to decrement";
        		tvMode.setText("increase");
        		togm.setTitle(toggle_str);
        	}
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void fill_data() {
    	cur = pdb.fetchCounter(rowid);
        startManagingCursor(cur);
        tvName.setText(cur.getString(1));
        tvNotes.setText(cur.getString(8));
        cur_val = Integer.parseInt(cur.getString(2));
        counter_button.setText(cur.getString(2));
			
    }
    public boolean deleteCounter(long nid) {
    	final Long needleId = nid;
    	AlertDialog.Builder builder = new AlertDialog.Builder(CounterView.this);
        builder.setTitle(R.string.delete_dialog_title_counter)
        	.setMessage(R.string.delete_dialog_text_counter)
        	.setPositiveButton(R.string.dialog_button_delete, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
              		pdb.deleteCounter(needleId);
              		//Intent i2 = new Intent(HookView.this, HookListView.class);
                    Intent i2 = new Intent(CounterView.this,KnittingStashHome.class);
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
