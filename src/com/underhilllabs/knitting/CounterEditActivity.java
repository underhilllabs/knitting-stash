package com.underhilllabs.knitting;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CounterEditActivity extends Activity {


/** Called when the activity is first created. */

 	private DbAdapter pdb;
 	private Cursor cur;
 	private EditText notes_field;
 	private EditText name_field;
 	private EditText curval;
 	private long rowid;
 	@Override
 	public void onCreate(Bundle savedInstanceState) {
 		super.onCreate(savedInstanceState);
 		setContentView(R.layout.counter_edit);
 		pdb = new DbAdapter(this);
 		pdb.open();
 		// get rowid from Intent Bundle
        Bundle extras = getIntent().getExtras();
        rowid = extras.getLong("com.underhilllabs.knitting.rowid"); 
        name_field = (EditText) findViewById(R.id.name);
 		notes_field = (EditText) findViewById(R.id.notes);
 	    curval = (EditText) findViewById(R.id.curval);
 		final Button add_button = (Button) findViewById(R.id.add_button);
		 	
 		setWidgets(rowid);

 		add_button.setOnClickListener(new Button.OnClickListener() {
 			public void onClick(View v) {
 				// Perform action on clicks
 				String notes=notes_field.getEditableText().toString();
 				String name=name_field.getEditableText().toString();
                //name,cur_val,proj_id,proj_name,int_up_down,str_type,int_multiple,notes
 				int cur_val = 0;
 				try {
 					cur_val = Integer.parseInt(curval.getEditableText().toString());
 				} catch (NumberFormatException e){
 					Toast.makeText(CounterEditActivity.this, R.string.please_enter_number, Toast.LENGTH_LONG);
 				}
                int proj_id = 0;
                String proj_name = "";
                int up_or_down = 0;
                String type = "increase";
                int multiple = 1;
 				
 				pdb.updateCounter(rowid,name,cur_val,proj_id,proj_name,up_or_down,type,multiple,notes);
 				
 				Intent i = new Intent(CounterEditActivity.this,KnittingStashHome.class);
 				i.putExtra("com.underhilllabs.knitting.tabid",3);
 				startActivity(i);

 			}
 		});
 		

 	}
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	pdb.close();
    }
	private boolean setWidgets(long row_id) {
		cur = pdb.fetchCounter(row_id);
		startManagingCursor(cur);
		//1 = name, 2 start
		//3 = lastmod, 
		// 4 = notes
		name_field.setText(cur.getString(1));
		notes_field.setText(cur.getString(8));
		curval.setText(cur.getString(2));
		return true;
	}
} 
