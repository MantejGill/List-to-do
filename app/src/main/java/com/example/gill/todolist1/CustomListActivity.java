package com.example.gill.todolist1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class CustomListActivity extends Activity {
	private ArrayList<User> items;
	private CustomUsersAdapter itemsAdapter;
	private ListView lvItems;
	private Button b1,b2;
	private EditText etNewItem;
	private AlertDialog dialog;
	private int ItemNo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		lvItems = (ListView) findViewById(R.id.lvUsers);
		b1=(Button)findViewById(R.id.btnAddItem);
		b2=(Button)findViewById(R.id.btnSearch);
		etNewItem = (EditText) findViewById(R.id.etNewItem);

		items = User.getUsers();
		writeItems();
		readItems();
		// Create the adapter to convert the array to views
		 itemsAdapter = new CustomUsersAdapter(this, items);
		// Attach the adapter to a ListView

		lvItems.setAdapter(itemsAdapter);
 		//User u=new User("Heelo","World");
     	//	items.add(u);
		//items.add("Second Item");


		b1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String itemText = etNewItem.getText().toString();
				User u = new User(itemText);
				items.add(u);
				etNewItem.setText("");
				writeItems();
//				Collections.sort(items, new CustomComparator());

			}
		});
		b2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = 0;
				int flag=0;
				String findName = etNewItem.getText().toString();

				while (i < items.size())//more efficient?
				{
					User temp = items.get(i);
					String tempName = temp.getName();
					String tempDate = temp.getDate();
					//int i = items.indexOf("heelo");
					if (findName.equals(tempName)) {
						Toast.makeText(getApplication(), "Content found at " + (i + 1), Toast.LENGTH_LONG).show();
						flag++;
					}
					if(findName.equals(tempDate)){
						Toast.makeText(getApplication(), "Date found at " + (i + 1), Toast.LENGTH_LONG).show();
						flag++;
					}
					i++;
				}
				if(flag==0)
					Toast.makeText(getApplicationContext(), "String not found", Toast.LENGTH_LONG).show();

			}
		});


		final String[] option = new String[] {"Edit",
				"Delete","Share" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, option);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Option");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				if(which==1){
				        Toast.makeText(getApplicationContext(), "Item is deleted", Toast.LENGTH_LONG).show();
					// Remove the item within array at position
						items.remove(ItemNo);
						// Refresh the adapter
						itemsAdapter.notifyDataSetChanged();
						// Return true consumes the long click event (marks it handled)
						writeItems();
				}
				else if(which==0)
				{
					Intent intentGetMessage=new Intent(getApplicationContext(),EditNote.class);
					startActivityForResult(intentGetMessage, 2);



				}

				else if(which==2) {


					User temp = items.get(ItemNo);
					String postName = temp.getName();
					String postDate = temp.getDate();

					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_SEND);

					// change the type of data you need to share,
//					# for image use "image/*"
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_TEXT, "["+postDate+"]: "+postName);
					startActivity(Intent.createChooser(intent, "Share"));
				}
			}
		});
		dialog = builder.create();

		setupListViewListener();

	}

	// Call Back method  to get the Message form other Activity
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		// check if the request code is same as what is passed  here it is 2
		if(requestCode==2)
		{
			if(null!=data)
			{
				// fetch the message String
				String message=data.getStringExtra("MESSAGE");
				// Set the message string in textView

				User u=new User(message);
				items.set(ItemNo,u);
				itemsAdapter.notifyDataSetChanged();
				// Return true consumes the long click event (marks it handled)
				writeItems();
				Toast.makeText(getApplicationContext(), "Item has been edited", Toast.LENGTH_LONG).show();

			}
		}
	}

	private void setupListViewListener() {
		lvItems.setOnItemLongClickListener(
				new AdapterView.OnItemLongClickListener() {
					@Override
					public boolean onItemLongClick(AdapterView<?> adapter,
												   View item, int pos, long id) {
						dialog.show();
						ItemNo=pos;
						/*// Remove the item within array at position
						items.remove(pos);
						// Refresh the adapter
						itemsAdapter.notifyDataSetChanged();
						// Return true consumes the long click event (marks it handled)
						writeItems();*/
						return true;
					}

				});
	}

	private void writeItems() {

		Type listOfObjects = new TypeToken<List<User>>(){}.getType();
		Gson gson = new Gson();

		String strObject = gson.toJson(items, listOfObjects); // Here list is your List<CUSTOM_CLASS> object
		SharedPreferences myPrefs = getSharedPreferences("hellopref1", Context.MODE_PRIVATE);
		SharedPreferences.Editor prefsEditor = myPrefs.edit();
		prefsEditor.putString("MyList", strObject);
		prefsEditor.commit();
	}

	private void readItems() {
		Type listOfObjects = new TypeToken<List<User>>(){}.getType();
		SharedPreferences myPrefs = getSharedPreferences("hellopref1", Context.MODE_PRIVATE);

		Gson gson = new Gson();
		String json = myPrefs.getString("MyList", "");
		ArrayList<User> list2 = gson.fromJson(json, listOfObjects);
		items = new ArrayList<User>(list2);

	}



}
