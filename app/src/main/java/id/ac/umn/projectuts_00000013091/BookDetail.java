package id.ac.umn.projectuts_00000013091;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


public class BookDetail extends AppCompatActivity {
    Books aBook;
    boolean duplicates;
    DatabaseHelper dbAdapter;

    TextView ASIN_R;
    TextView GROUPS_R;
    TextView FORMAT_R;
    TextView TITLE_R;
    TextView AUTHOR_R;
    TextView PUBLISHER_R;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        dbAdapter = new DatabaseHelper(getApplicationContext(), null);
        dbAdapter.onOpen();


        ASIN_R = findViewById(R.id.ASIN_R);
        GROUPS_R = findViewById(R.id.GROUPS_R);
        FORMAT_R =  findViewById(R.id.FORMAT_R);
        TITLE_R =  findViewById(R.id.TITLE_R);
        AUTHOR_R =  findViewById(R.id.AUTHOR_R);
        PUBLISHER_R =  findViewById(R.id.PUBLISHER_R);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String BookNo = getIntent().getStringExtra("BookNo");

        getBookData(BookNo);

        GROUPS_R.setText(aBook.getGroup());
        ASIN_R.setText(BookNo);
        FORMAT_R.setText(aBook.getFormat());
        TITLE_R.setText(aBook.getTitle());
        AUTHOR_R.setText(aBook.getAuthor());
        PUBLISHER_R.setText(aBook.getPublisher());

        checkDuplicates();
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (duplicates) {
                    if(dbAdapter.DeleteFavoriteBook(ASIN_R.getText().toString())) {
                        Snackbar.make(findViewById(R.id.fab), "This book is now removed from My Favorite", Snackbar.LENGTH_LONG).show();
                        checkDuplicates();
                    }
                } else {
                    if (dbAdapter.addFavoriteBook(ASIN_R.getText().toString())) {
                        Snackbar.make(findViewById(R.id.fab), "This book is now added to My Favorite", Snackbar.LENGTH_LONG).show();
                        checkDuplicates();
                    }
                }
            }
        });
    }

    void checkDuplicates(){
        Cursor check = dbAdapter.checkFavoriteBookDuplicates(ASIN_R.getText().toString());
        duplicates = check.getCount() > 0;
    }

    private void getBookData(String key){
        Cursor cursor;
        cursor = dbAdapter.getABook(key);
        cursor.moveToFirst();
        aBook = new Books(cursor.getString(0), cursor.getString(1), cursor.getString(2),
                cursor.getString(3), cursor.getString(4), cursor.getString(5));
    }

}
