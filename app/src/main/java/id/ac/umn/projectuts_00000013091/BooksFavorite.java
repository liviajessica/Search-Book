package id.ac.umn.projectuts_00000013091;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

class FavoriteBooks{
    public String ASIN;
    public String TITLE;
    public String AUTHOR;
    FavoriteBooks(String ASIN, String TITLE, String AUTHOR){
        this.ASIN = ASIN;
        this.TITLE = TITLE;
        this.AUTHOR = AUTHOR;
    }
}

public class BooksFavorite extends AppCompatActivity {

    private ArrayList<FavoriteBooks> favoriteBookList;
    DatabaseHelper dbAdapter;

    @Override
    protected void onResume() {
        super.onResume();
        getFavorites();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbAdapter.close();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_favorite);
        dbAdapter = new DatabaseHelper(getApplicationContext(), null);
        dbAdapter.onOpen();
        getFavorites();
    }

    void getFavorites(){
        getData();
        RecyclerView recyclerView = findViewById(R.id.favorite_book_list);
        GetFavorite adapter = new GetFavorite(favoriteBookList, getApplicationContext());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BooksFavorite.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    void getData() {
        favoriteBookList = new ArrayList<>();
        Cursor cursorASIN = dbAdapter.getAllFavoriteBooks();
        if (cursorASIN.getCount() == 0) {
            Snackbar.make(findViewById(R.id.favorite_book_list), "No Favorite Books Here", Snackbar.LENGTH_LONG).show();
        }else {
            cursorASIN.moveToFirst();
            do {
                Cursor cursorComplete = dbAdapter.getABook(cursorASIN.getString(0));
                cursorComplete.moveToFirst();
                do{
                    favoriteBookList.add(new FavoriteBooks(cursorComplete.getString(0),
                            cursorComplete.getString(3), cursorComplete.getString(4)));
                }while(cursorComplete.moveToNext());
            }while (cursorASIN.moveToNext());
        }

    }
}
