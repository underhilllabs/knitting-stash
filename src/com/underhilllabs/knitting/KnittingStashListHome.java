package com.underhilllabs.knitting;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class KnittingStashListHome extends ListActivity {
	private Resources r;
	private String[] appnames={"Needles","Hooks","Projects","Counters"};
	private Drawable[] icons=null;
	@Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.knitting_stash_home_list);
        ListView lv = getListView();
        r = this.getResources();
        registerForContextMenu(lv);
        lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Intent i = null;
				if(position == 0)
					i = new Intent (KnittingStashListHome.this,NeedleListView.class);
				else if (position == 1)
					i = new Intent (KnittingStashListHome.this,HookListView.class);
				else if (position == 2)
					i = new Intent (KnittingStashListHome.this,ProjectListView.class);
				else if (position == 3)
					i = new Intent (KnittingStashListHome.this,CounterListView.class);
				
				//Intent i = new Intent(KnittingStashListHome.this, myClass);
		        startActivity(i);
				
			}
         });
		Drawable nicon = r.getDrawable(R.drawable.ic_tab_needle);
		Drawable hicon = r.getDrawable(R.drawable.ic_tab_hook);
		Drawable picon = r.getDrawable(R.drawable.ic_tab_project);
		Drawable cicon = r.getDrawable(R.drawable.ic_tab_counter);
		icons = new Drawable[] {nicon,hicon,picon,cicon};
		setListAdapter(new IconicAdapter(this));
        
    }
	
	private class IconicAdapter extends ArrayAdapter { 
        Activity context; 
 
        IconicAdapter(Activity context) { 
            super(context, R.layout.knitting_stash_home_row, appnames); 
 
            this.context=context; 
        } 
 
        public View getView(int position, View convertView, ViewGroup parent) { 
            LayoutInflater inflater=getLayoutInflater(); 
            View row=inflater.inflate(R.layout.knitting_stash_home_row, parent, false); 
            TextView label=(TextView)row.findViewById(R.id.appname_row); 
            label.setText(appnames[position]); 
            ImageView icon=(ImageView)row.findViewById(R.id.appicon_row);
            icon.setImageDrawable(icons[position]);
            return(row); 
        }  
    }
}
