package id.ac.umn.projectuts_00000013091;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "books.db";
    private Context contexts;

    private SQLiteDatabase db;

    public static final String ASIN = "ASIN";
    public static final String GROUPS = "GROUPS";
    public static final String FORMAT = "FORMAT";
    public static final String TITLE = "TITLE";
    public static final String AUTHOR = "AUTHOR";
    public static final String PUBLISHER = "PUBLISHER";
    public static final String TABLE = "books";

    DatabaseHelper(Context context, SQLiteDatabase.CursorFactory factory) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        contexts=context;
    }

    private void fetchSQLs(SQLiteDatabase db) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(contexts.getAssets().open("books.db")));
            String mLine;
            while ((mLine = reader.readLine()) != null) { db.execSQL(mLine); }
        }catch (IOException ignored) { }
        finally {
            if (reader != null) {
                try { reader.close(); }
                catch (IOException e) { e.printStackTrace(); }
            }
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.beginTransaction();
        try { fetchSQLs(db); }
        catch (SQLException ignored){ }
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS 'books';");
        onCreate(db);
    }

    void onOpen(){
        super.onOpen(db);
        db=this.getWritableDatabase();
    }

    public synchronized void close(){
        db.close();
    }

    Cursor getAllBooks(){
        return db.query(TABLE,
                new String[] {ASIN,GROUPS,FORMAT,TITLE,AUTHOR,PUBLISHER},
                null, null, null, null, null, null);
    }

    Cursor getABook(String Key){
        return db.rawQuery("SELECT ASIN,GROUPS,FORMAT,TITLE,AUTHOR,PUBLISHER FROM " +
                TABLE + " where ASIN =  '"+Key+"'", null);
    }

    Cursor getAllBooksWithSort(String base, String Mode){
        return db.rawQuery("SELECT ASIN,GROUPS,FORMAT,TITLE,AUTHOR,PUBLISHER FROM " +
                TABLE + " ORDER BY "+base+ " "+ Mode + ";", null);
    }

    Cursor getAllBooksWithKeyword(String Title){
        return db.rawQuery("SELECT ASIN,GROUPS,FORMAT,TITLE,AUTHOR,PUBLISHER FROM " +
                TABLE + " WHERE TITLE LIKE '%"+Title+"%';", null);
    }

    Cursor getAllFavoriteBooks(){
        return db.rawQuery("SELECT ASIN FROM favorites;", null);
    }

    boolean addFavoriteBook(String ASIN){
        String sql = "INSERT INTO favorites VALUES('"+ASIN+"');";
        try { db.execSQL(sql); }
        catch (SQLException e) { return false; }
        return true;
    }


    boolean DeleteFavoriteBook(String ASIN){
        String sql = "DELETE FROM favorites WHERE ASIN = '"+ASIN+"';";
        try { db.execSQL(sql); }
        catch (SQLException e) { return false; }
        return true;
    }

    Cursor checkFavoriteBookDuplicates(String ASIN){
        return db.rawQuery("SELECT ASIN FROM favorites WHERE ASIN LIKE '"+ASIN+"';", null);
    }


}
