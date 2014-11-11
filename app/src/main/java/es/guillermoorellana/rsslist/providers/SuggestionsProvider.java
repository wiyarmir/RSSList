package es.guillermoorellana.rsslist.providers;

import android.content.SearchRecentSuggestionsProvider;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class SuggestionsProvider extends SearchRecentSuggestionsProvider {
    public final static String AUTHORITY = "es.guillermoorellana.rsslist.providers.SuggestionsProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public SuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }
}
