package es.guillermoorellana.rsslist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class FeedAdapter extends SimpleCursorAdapter {
    public FeedAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
    }
}
