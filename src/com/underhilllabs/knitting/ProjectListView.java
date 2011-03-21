package com.underhilllabs.knitting;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
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

public class ProjectListView extends ListActivity {

	private DbAdapter pdb;
	private static final int INSERT_ID = Menu.FIRST;
	private static final int VIEW_ID = Menu.FIRST;
	private static final int EDIT_ID = Menu.FIRST + 1;
	private static final int DELETE_ID = Menu.FIRST + 2;
	private static final int PREFS_ID = Menu.FIRST + 3;
	private static final int WIP_ID = Menu.FIRST + 4;
	private static final int ALL_ID = Menu.FIRST + 5;
	private static final int SHOP_ID = Menu.FIRST + 6;

	private AlertDialog alertDialog;
	private Cursor cur;
	private Resources r;

	// private String selectedWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.project_list_activity_view);
		pdb = new DbAdapter(this);
		pdb.open();
		r = this.getResources();
		ListView lv = getListView();
		registerForContextMenu(lv);

		lv.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> av, View v, int position,
					long id) {
				Intent i = new Intent(ProjectListView.this, ProjectView.class);
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
		cur = pdb.fetchAllProjects();
		startManagingCursor(cur);
		String[] displayFields = new String[] { DbAdapter.KEY_NAME,
				DbAdapter.KEY_NEEDED_SHOPPING, DbAdapter.KEY_NOTES };
		int[] displayViews = new int[] { R.id.name_row, R.id.shopping_row,
				R.id.notes_row };
		// setListAdapter(new
		// SimpleCursorAdapter(this,R.layout.project_list_row,
		// cur,displayFields, displayViews));
		// cur.close();
		SimpleCursorAdapter projects = new SimpleCursorAdapter(this,
				R.layout.project_list_row, cur, displayFields, displayViews);
		projects.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int nCheckedIndex = cursor
						.getColumnIndexOrThrow(DbAdapter.KEY_NEEDED_SHOPPING);
				// int nSizeIndex =
				// cursor.getColumnIndexOrThrow(DbAdapter.KEY_SIZE);
				if (columnIndex == nCheckedIndex) {
					// Log.d("KnittingStash","in custom view binder");
					TextView tv = (TextView) view;
					String nShopping = cursor.getString(nCheckedIndex);
					if (nShopping != null && nShopping.length() > 0) {
						// tv.setTextColor(Color.BLACK);
						// tv.setBackgroundColor(Color.rgb(0, 134, 100));
						tv.setBackgroundColor(r.getColor(R.color.green));

						tv.setPadding(5, 0, 5, 0);
						tv.setText(getString(R.string.menu_shopping) + " "
								+ nShopping);
					} else {
						tv.setPadding(0, 0, 0, 0);
						tv.setBackgroundColor(Color.BLACK);
						tv.setText("");
						// return false;
					}
					return true;

				}
				/*
				 * else if (columnIndex == nSizeIndex) { TextView tv =
				 * (TextView) view; int my_size_i = cursor.getInt(2); try {
				 * tv.setText(size_array[my_size_i]); } catch
				 * (IndexOutOfBoundsException ie) { Log.d("knittingstash",
				 * "index out of bounds on size_array"); } return true;
				 * 
				 * }
				 */

				return false;
			}
		}

		);
		setListAdapter(projects);

	}

	public void fill_data_wip() {
		cur = pdb.fetchAllWipProjects();
		startManagingCursor(cur);
		String[] displayFields = new String[] { DbAdapter.KEY_NAME,
				DbAdapter.KEY_NEEDED_SHOPPING, DbAdapter.KEY_NOTES };
		int[] displayViews = new int[] { R.id.name_row, R.id.shopping_row,
				R.id.notes_row };
		// setListAdapter(new
		// SimpleCursorAdapter(this,R.layout.project_list_row,
		// cur,displayFields, displayViews));
		SimpleCursorAdapter projects = new SimpleCursorAdapter(this,
				R.layout.project_list_row, cur, displayFields, displayViews);
		projects.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int nCheckedIndex = cursor
						.getColumnIndexOrThrow(DbAdapter.KEY_NEEDED_SHOPPING);
				// int nSizeIndex =
				// cursor.getColumnIndexOrThrow(DbAdapter.KEY_SIZE);
				if (columnIndex == nCheckedIndex) {
					Log.d("KnittingStash", "in custom view binder");
					TextView tv = (TextView) view;
					String nShopping = (cursor.getString(nCheckedIndex));
					if (nShopping != null && nShopping.length() > 0) {
						// tv.setTextColor(Color.BLACK);
						tv.setBackgroundColor(Color.rgb(0, 134, 100));
						// tv.setBackgroundColor(r.getColor(R.color.green));

						tv.setPadding(5, 0, 5, 0);
						tv.setText(getString(R.string.menu_shopping) + " "
								+ nShopping);
					} else {
						tv.setPadding(0, 0, 0, 0);
						tv.setBackgroundColor(Color.BLACK);
						tv.setText("");
						// return false;
					}
					return true;

				}
				/*
				 * else if (columnIndex == nSizeIndex) { TextView tv =
				 * (TextView) view; int my_size_i = cursor.getInt(2); try {
				 * tv.setText(size_array[my_size_i]); } catch
				 * (IndexOutOfBoundsException ie) { Log.d("knittingstash",
				 * "index out of bounds on size_array"); } return true;
				 * 
				 * }
				 */

				return false;
			}
		}

		);
		setListAdapter(projects);

		// cur.close();
	}

	public void fill_data_shop() {
		cur = pdb.fetchAllShoppingProjects();
		startManagingCursor(cur);
		String[] displayFields = new String[] { DbAdapter.KEY_NAME,
				DbAdapter.KEY_NEEDED_SHOPPING, DbAdapter.KEY_NOTES };
		int[] displayViews = new int[] { R.id.name_row, R.id.shopping_row,
				R.id.notes_row };
		// setListAdapter(new
		// SimpleCursorAdapter(this,R.layout.project_list_row,
		// cur,displayFields, displayViews));
		SimpleCursorAdapter projects = new SimpleCursorAdapter(this,
				R.layout.project_list_row, cur, displayFields, displayViews);
		projects.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

			public boolean setViewValue(View view, Cursor cursor,
					int columnIndex) {
				int nCheckedIndex = cursor
						.getColumnIndexOrThrow(DbAdapter.KEY_NEEDED_SHOPPING);
				// int nSizeIndex =
				// cursor.getColumnIndexOrThrow(DbAdapter.KEY_SIZE);
				if (columnIndex == nCheckedIndex) {
					Log.d("KnittingStash", "in custom view binder");
					TextView tv = (TextView) view;
					String nShopping = (cursor.getString(nCheckedIndex));
					if (nShopping != null && nShopping.length() > 0) {
						// tv.setTextColor(Color.BLACK);
						tv.setBackgroundColor(Color.rgb(0, 134, 100));
						// tv.setBackgroundColor(r.getColor(R.color.green));

						tv.setPadding(5, 0, 5, 0);
						tv.setText(getString(R.string.menu_shopping) + " "
								+ nShopping);
					} else {
						tv.setPadding(0, 0, 0, 0);
						tv.setBackgroundColor(Color.BLACK);
						tv.setText("");
						// return false;
					}
					return true;

				}
				/*
				 * else if (columnIndex == nSizeIndex) { TextView tv =
				 * (TextView) view; int my_size_i = cursor.getInt(2); try {
				 * tv.setText(size_array[my_size_i]); } catch
				 * (IndexOutOfBoundsException ie) { Log.d("knittingstash",
				 * "index out of bounds on size_array"); } return true;
				 * 
				 * }
				 */

				return false;
			}
		}

		);
		setListAdapter(projects);

		// cur.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean result = super.onCreateOptionsMenu(menu);
		MenuItem addm = menu.add(0, INSERT_ID, 0, R.string.menu_add_project);
		addm.setIcon(android.R.drawable.ic_menu_add);
		MenuItem homem = menu.add(0, PREFS_ID, 0, R.string.menu_preferences);
		homem.setIcon(android.R.drawable.ic_menu_preferences);
		MenuItem allm = menu.add(0, ALL_ID, 0, R.string.menu_all_projects);
		MenuItem wipm = menu.add(0, WIP_ID, 0,
				R.string.menu_unfinished_projects);
		MenuItem shopm = menu.add(0, SHOP_ID, 0, R.string.menu_need_shopping);

		return result;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case INSERT_ID:
			Intent i = new Intent(this, ProjectAddActivity.class);
			startActivity(i);
			return true;
		case ALL_ID:
			fill_data();
			return true;
		case WIP_ID:
			fill_data_wip();
			return true;
		case SHOP_ID:
			fill_data_shop();
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
		menu.add(0, DELETE_ID, 0, R.string.menu_delete);

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case VIEW_ID:
			Intent i = new Intent(this, ProjectView.class);
			i.putExtra("com.underhilllabs.knitting.rowid", info.id);
			startActivity(i);
			return true;
		case EDIT_ID:
			Intent i2 = new Intent(this, ProjectEditActivity.class);
			i2.putExtra("com.underhilllabs.knitting.rowid", info.id);
			startActivity(i2);
			return true;
		case DELETE_ID:
			deleteProject(info.id);
			// fill_data();
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	public boolean deleteProject(long nid) {
		final Long needleId = nid;
		AlertDialog.Builder builder = new AlertDialog.Builder(
				ProjectListView.this);
		builder.setTitle(R.string.delete_dialog_title_project).setMessage(
				R.string.delete_dialog_text_project).setPositiveButton(
				R.string.dialog_button_delete,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						pdb.deleteProject(needleId);
						Intent i2 = new Intent(ProjectListView.this,
								KnittingStashHome.class);
						i2.putExtra("com.underhilllabs.knitting.tabid", 2);
						startActivity(i2);
						// fill_data("KEY_SIZE_I");
						return;
					}
				}).setNegativeButton(R.string.dialog_button_cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						return;
					}
				});

		alertDialog = builder.create();
		alertDialog.show();

		return true;
	}

}
