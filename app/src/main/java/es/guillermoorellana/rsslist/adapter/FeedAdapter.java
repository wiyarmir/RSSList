package es.guillermoorellana.rsslist.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.Normalizer;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.providers.DatabaseHelper;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class FeedAdapter extends SimpleCursorAdapter {
    private final int mHighlightColor;
    private String highlightKey;

    public FeedAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        mHighlightColor = context.getResources().getColor(R.color.accent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        if (highlightKey != null) {
            TextView text = (TextView) v.findViewById(android.R.id.text1);
            Cursor c = (Cursor) getItem(position);

            text.setText(highlight(highlightKey, c.getString(c.getColumnIndexOrThrow(DatabaseHelper.KEY_FEED_TITLE))));
        }
        return v;
    }

    public CharSequence highlight(String search, String originalText) {
        // ignore case and accents
        // the same thing should have been done for the search text
        String normalizedText = Normalizer.normalize(originalText, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "").toLowerCase();

        int start = normalizedText.indexOf(search);
        if (start < 0) {
            // not found, nothing to to
            return originalText;
        } else {
            // highlight each appearance in the original text
            // while searching in normalized text
            Spannable highlighted = new SpannableString(originalText);
            while (start >= 0) {
                int spanStart = Math.min(start, originalText.length());
                int spanEnd = Math.min(start + search.length(), originalText.length());

                highlighted.setSpan(new BackgroundColorSpan(mHighlightColor), spanStart, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                start = normalizedText.indexOf(search, spanEnd);
            }

            return highlighted;
        }
    }

    public String getHighlightKey() {
        return highlightKey;
    }

    public void setHighlightKey(String highlightKey) {
        this.highlightKey = highlightKey;
    }
}
