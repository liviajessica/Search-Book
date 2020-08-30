package id.ac.umn.projectuts_00000013091;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;

import java.util.ArrayList;


public class HomeActivity extends AppCompatActivity {

    Intent intent;
    String SortBase;
    String SortMode;
    DatabaseHelper dbAdapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.data_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.sort_by:
                createSortDialog();
                return true;
            case R.id.favorites:
                intent=new Intent(HomeActivity.this,BooksFavorite.class);
                startActivityForResult(intent,0x01);
                return true;
            case R.id.about_me:
                intent=new Intent(HomeActivity.this,AboutMe.class);
                startActivityForResult(intent,0x01);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();

    }

    public void createSortDialog() {
        SortMode="ASC";
        final Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.sortby);
        Button btnDisplay = dialog.findViewById(R.id.sort_button);
        final RadioButton SortByTitle = dialog.findViewById(R.id.sort_by_title);
        final RadioButton SortByAuthor = dialog.findViewById(R.id.sort_by_author);
        final RadioButton SortByPublisher = dialog.findViewById(R.id.sort_by_publisher);
        final Switch sequenceSelect = dialog.findViewById(R.id.sort_by_sequence);
        sequenceSelect.setText(sequenceSelect.getTextOff());
        sequenceSelect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(sequenceSelect.isChecked()){
                    SortMode="DESC";
                    sequenceSelect.setText(sequenceSelect.getTextOn());
                }else{
                    SortMode="ASC";
                    sequenceSelect.setText(sequenceSelect.getTextOff());
                }
            }
        });

        btnDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SortByTitle.isChecked()) SortBase="TITLE";
                else if(SortByAuthor.isChecked()) SortBase="AUTHOR";
                else if(SortByPublisher.isChecked()) SortBase="PUBLISHER";
                PrintData(SortBase,SortMode);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private RecyclerView recyclerView;
    private GetBook adapter;
    private ArrayList<Books> booksArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        dbAdapter = new DatabaseHelper(getApplicationContext(), null);
        dbAdapter.onOpen();

        setContentView(R.layout.activity_home);
        PrintData();

        final EditText search = findViewById(R.id.search_by_title);
        Button resetSearch = findViewById(R.id.reset_search);

        resetSearch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                search.setText("");
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                PrintData(search.getText().toString());
            }
        });

    }

    void PrintData(String title){
        booksArrayList = new ArrayList<>();
        Cursor cursor = dbAdapter.getAllBooksWithKeyword(title);
        if(cursor.getCount() == 0) {
            Snackbar.make(findViewById(R.id.search_by_title), "No Books Found", Snackbar.LENGTH_LONG).show();
        }else {
            cursor.moveToFirst();
            do {
                booksArrayList.add(new Books(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                        cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            } while (cursor.moveToNext());
        }
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new GetBook(booksArrayList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void PrintData(String SortBase, String SortMode){
        Cursor cursor = dbAdapter.getAllBooksWithSort(SortBase,SortMode);
        cursor.moveToFirst();
        booksArrayList = new ArrayList<>();
        do{
            booksArrayList.add(new Books(cursor.getString(0),cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        }while(cursor.moveToNext());
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new GetBook(booksArrayList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void PrintData() {
        Cursor cursor = dbAdapter.getAllBooks();
        cursor.moveToFirst();
        booksArrayList = new ArrayList<>();
        do {
            booksArrayList.add(new Books(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5)));
        } while (cursor.moveToNext());
        recyclerView = findViewById(R.id.recycler_view);
        adapter = new GetBook(booksArrayList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HomeActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
