package es.guillermoorellana.rsslist.providers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.guillermoorellana.rsslist.RSSListApplication;
import es.guillermoorellana.rsslist.model.Feed;


/**
 * Created by wiyarmir on 11/11/14.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    static final public String DATABASE_NAME = "RSSList_DB";
    static final public int DATABASE_VERSION = 1;

    private static final String TABLE_FEEDS = "FEEDS";
    public static final String KEY_FEED_ID = BaseColumns._ID;
    public static final String KEY_FEED_URL = "FEED_URL";
    public static final String KEY_FEED_TITLE = "FEED_TITLE";

    private static final String TABLE_ARTICLES = "ARTICLES";
    public static final String KEY_ARTICLE_ID = BaseColumns._ID;
    public static final String KEY_PARENT_FEED_ID = "PARENT_FEED_ID";
    public static final String KEY_ARTICLE_URL = "ARTICLE_URL";
    public static final String KEY_ARTICLE_NAME = "ARTICLE_NAME";


    private static DatabaseHelper mInstance;

    protected DatabaseHelper(Context c) {
        super(c, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
     * Singleton lazy instantiation
     *
     */
    public static DatabaseHelper getInstance() {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(RSSListApplication.getAppContext());
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating database");

        String CREATE_FEEDS_TABLE = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT)",
                TABLE_FEEDS, KEY_FEED_ID, KEY_FEED_TITLE, KEY_FEED_URL);

        db.execSQL(CREATE_FEEDS_TABLE);

        String CREATE_ARTICLES_TABLE = String.format(
                "CREATE TABLE %s (" +
                        "%s INTEGER PRIMARY KEY, " +
                        "%s TEXT, " +
                        "%s TEXT, " +
                        "%s INTEGER," +
                        "FOREIGN KEY(%s) REFERENCES %s(%s0)" +
                        ")",
                TABLE_ARTICLES,
                KEY_ARTICLE_ID,
                KEY_ARTICLE_NAME,
                KEY_ARTICLE_URL,
                KEY_PARENT_FEED_ID,
                KEY_PARENT_FEED_ID, TABLE_FEEDS, KEY_FEED_ID);

        db.execSQL(CREATE_ARTICLES_TABLE);

        populateTestData(db);
    }

    private void wipeDatabase(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FEEDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
    }

    private void populateTestData(SQLiteDatabase db) {
        String[][] data = new String[][]{
                {"Android Developers Blog", "http://feeds.feedburner.com/blogspot/hsDu?format=xml"},
                {"Android Police", "http://feeds.feedburner.com/AndroidPolice?format=xml"},
                {"Android Official Blog", "http://feeds.feedburner.com/OfficialAndroidBlog?format=xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"},
                {"TestFeed", "http://test.com/feed.xml"}
        };
        for (String strings[] : data) {
            ContentValues values = new ContentValues();
            values.put(KEY_FEED_TITLE, strings[0]);
            values.put(KEY_FEED_URL, strings[1]);
            db.insert(TABLE_FEEDS, null, values);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "wiping database");
        // this is the very only version, should not be called, but just in case...
        wipeDatabase(db);
        onCreate(db);
    }

    public void insertFeed(Feed f) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_FEED_TITLE, f.getName());
        values.put(KEY_FEED_URL, f.getURL());
        db.insert(TABLE_FEEDS, null, values);
        db.close();
    }

    public Feed getFeedById(int id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_FEEDS,
                new String[]{KEY_FEED_ID, KEY_FEED_TITLE, KEY_FEED_URL},
                KEY_FEED_ID + "=?",
                new String[]{String.valueOf(id)},
                null, null, null);
        Feed f = null;
        if (cursor != null) {
            cursor.moveToFirst();
            f = populateFeed(cursor);
        }
        return f;
    }

    public List<Feed> getAllFeeds() {
        List<Feed> l = new ArrayList<Feed>();
        Cursor cursor = getAllFeedsCursor();

        if (cursor.moveToFirst()) {
            do {
                l.add(populateFeed(cursor));
            } while (cursor.moveToNext());
        }

        return l;
    }

    public Cursor getAllFeedsCursor() {
        String q = "SELECT * FROM " + TABLE_FEEDS;
        return getReadableDatabase().rawQuery(q, null);
    }

    public Cursor getFeedSearchCursor(String search) {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(TABLE_FEEDS,
                new String[]{KEY_FEED_ID, KEY_FEED_TITLE, KEY_FEED_URL},
                KEY_FEED_TITLE + " LIKE ?", new String[]{search},
                null, null, null, null);
    }

    public Cursor getFeedSuggestionsCursor() {
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT DISTINCT " + KEY_FEED_TITLE +
                " FROM " + TABLE_FEEDS +
                " AS SUGGEST_COLUMN_TEXT_1";
        return db.rawQuery(query, null);
    }

    public int getFeedCount() {
        Cursor c = getAllFeedsCursor();
        // must close so getcount is accurate
        c.close();
        return c.getCount();
    }

    public Feed populateFeed(Cursor cursor) {
        return new Feed(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
    }

    public void deleteFeed(Feed f) {
        deleteFeed(f.getId());
    }

    public void deleteFeed(int feedid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FEEDS, KEY_FEED_ID + "=?",
                new String[]{String.valueOf(feedid)});
        db.close();

    }


}
