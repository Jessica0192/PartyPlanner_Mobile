package com.example.assignment1;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class GuestActivity extends AppCompatActivity {

    // String[] guests= {};
    ArrayList<String> selectedGuests=new ArrayList<>();
    SharedPreferences sp_obj;
    SharedPreferences sp_obj2;
    ListView guestList;
    private Button button;
    //  Button invite = new Button();
    SearchView searchView;
    String[] nameList = {"Maria", "Suka", "Hoda", "Jessica", "Troy", "Norbert", "Igor", "Marianna", "Yeji", "Priyanka"};
    ArrayAdapter<String> arrayAdapter;
    //   private GuestListAdapter mAdapter;
    private EditText mNewGuestNameEditText;
    private EditText mNewPartySizeEditText;
    private final static String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //  FloatingActionButton fab = findViewById(R.id.fab);
        // fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //   public void onClick(View view) {
        //      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //             .setAction("Action", null).show();
        // }
        //});


        // Button butn = (Button)findViewById(R.id.add_to_waitlist_button);
        //  butn.setHeight(70);
        // butn.setWidth(120);

        // final float scale = getContext().getResources().getDisplayMetrics().density;
        //int pixels = (int) (200 * scale + 0.5f);

//        butn.setLayoutParams (new Toolbar.LayoutParams(50, Toolbar.LayoutParams.WRAP_CONTENT));

        guestList=(ListView)findViewById(R.id.guestList);

        guestList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        guestList.setTextFilterEnabled(true);

        ArrayList<String> arrayList = new ArrayList<>();

        //  arrayList.add("Hello");
        //  arrayList.add("There");
        //  arrayList.add("I'm");
        //  arrayList.add("Mary");
        //arrayList.add("Hello");
        //  arrayList.add("There");


        // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);


        searchView = findViewById(R.id.search_bar); //этого не было
        //  guestList.setAdapter(arrayAdapter);

        //новое
        // Context context = this;
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1,nameList);
        guestList.setAdapter(arrayAdapter);
        guestList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
                selectedGuests.add(guestList.getItemAtPosition(i).toString()); //JUST ADDED
                Toast.makeText(GuestActivity.this, adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query){
                GuestActivity.this.arrayAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText){
                GuestActivity.this.arrayAdapter.getFilter().filter(newText);
                return false;
            }
        });



        //this,android.R.layout.simple_expandable_list_item_1, android.R.id.text1,nameList

        //String[] my_guest_list = new String[] {"Hello", "My", "Name", "Is Mary"};

        // button = (Button) findViewById(R.id.guestListBtn);

        //  button.setOnClickListener(new View.OnClickListener(){
        //    @Override
        //       public void onClick(View v){
        //  selectedGuests.
        //    SharedPreferences.Editor editor = sp_obj.edit();
        //   int arraySize = selectedGuests.size();
        //  for(int i = 0; i < arraySize; i++) {

        // editor.putString(getString(i), selectedGuests.get(i));
        //       }
        //editor.commit();
        //openNextActivity();
        //   }
        //     });

    }





    public void goToInviteActivity(){
        Intent intent = new Intent(this, InviteActivity.class);
        startActivity(intent);
    }
    //  private Cursor getAllGuests()
    // {
    //return mDb query
    // }



    public void addToWaitList(View view)
    {


        //NEW THING
        //  String selectedGuest=((TextView)view).getText().toString();

        //     if(!(selectedGuests.contains(selectedGuest))){
//           selectedGuests.add(selectedGuest);
        //   }
        //  if (mNewGuestNameEditText.getText().length() == 0 ||
        //     mNewPartySizeEditText.getText().length() == 0)
        // {
        //   return;
        //}

        ////int partySize = 1;
        ////try{
        ////    partySize = Integer.parseInt(mNewPartySizeEditText.getText().toString());

        ////} catch (NumberFormatException ex) {
        ////   Log.e(LOG_TAG, "Failed to parse party size text to number: " + ex.getMessage());
        //// }

        //     addNewGuest(mNewGuestNameEditText.getText().toString(), partySize);

        //  mAdapter.swapCursor(getAllGuests());

        // //mNewPartySizeEditText.clearFocus();
        //// mNewGuestNameEditText.getText().clear();
        ////mNewPartySizeEditText.getText().clear();




        guestList=(ListView)findViewById(R.id.guestList);

        //  guestList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        // guestList.setTextFilterEnabled(true);

        ArrayList<String> arrayList = new ArrayList<>();

        //  arrayList.add("Hello");
        //  arrayList.add("There");
        //  arrayList.add("I'm");
        //  arrayList.add("Mary");
        //arrayList.add("Hello");
        //  arrayList.add("There");


        // ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);


        searchView = findViewById(R.id.search_bar); //этого не было
        //  guestList.setAdapter(arrayAdapter);

        //новое
        // Context context = this;
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, android.R.id.text1,nameList);
        guestList.setAdapter(arrayAdapter);

        /////

        sp_obj2 = getSharedPreferences("GuestPrefs", Context.MODE_PRIVATE);

        //////

        SharedPreferences.Editor editor = sp_obj2.edit();

        //**   guestList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
        //**     @Override
        //**    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l){
        //**    selectedGuests.add(guestList.getItemAtPosition(position).toString());
        //**        Toast.makeText(MainActivity.this, "You Click -"+adapterView.getItemAtPosition(i).toString(), Toast.LENGTH_SHORT).show();
        //selectedGuests.add(adapterView.getItemAtPosition(i).toString());

        /////NEW
        //**        editor.putString("cur_guest", adapterView.getItemAtPosition(i).toString());

        //  Toast.makeText(MainActivity.this, "The array is -"+selectedGuests, Toast.LENGTH_SHORT).show();
        //     SetDefaultValue(InviteActivity,9, false);

        //  goToInviteActivity();

        //**        }
        //**     });

        int arraySize = selectedGuests.size();
        editor.putString("cur_guest", selectedGuests.get(arraySize - 1));
        editor.apply();
        //  editor.commit();
        //   Toast.makeText(MainActivity.this, "Information saved.", Toast.LENGTH_LONG).show();
        goToInviteActivity();
        // int arraySize = selectedGuests.size();
        // for(int i = 0; i < arraySize; i++) {
        //    myTextView.append(selectedGuests[i]);
        //   Toast.makeText(MainActivity.this, "The array is -"+selectedGuests, Toast.LENGTH_SHORT).show();
        // }



    }









}