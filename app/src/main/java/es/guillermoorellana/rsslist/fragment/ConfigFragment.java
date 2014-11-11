package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.adapter.FeedAdapter;
import es.guillermoorellana.rsslist.model.Feed;
import es.guillermoorellana.rsslist.parser.ParserCallback;
import es.guillermoorellana.rsslist.parser.SimpleRSS2Parser;
import es.guillermoorellana.rsslist.providers.DatabaseHelper;

/**
 *
 */
public class ConfigFragment extends Fragment {

    public static final String FRAGMENT_TAG = "config";
    private static final String TAG = "ConfigFragment";


    private OnFragmentInteractionListener mListener;
    private ListView feedList;
    private FeedAdapter feedAdapter;
    private Cursor feedsCursor;
    private EditText mEditRSS;
    private Button bAddRSS;


    public ConfigFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_config, container, false);

        feedsCursor = DatabaseHelper.getInstance().getAllFeedsCursor();

        feedAdapter = new FeedAdapter(getActivity(), android.R.layout.simple_list_item_1, feedsCursor, new String[]{DatabaseHelper.KEY_FEED_TITLE}, new int[]{android.R.id.text1}, 0);

        feedList = (ListView) v.findViewById(R.id.listView);
        feedList.setAdapter(feedAdapter);
        feedList.setOnItemClickListener(new OnListItemClickListener());

        mEditRSS = (EditText) v.findViewById(R.id.e_rss);
        mEditRSS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    validateAndSubmit();
                    return true;
                }
                return false;
            }
        });

        bAddRSS = (Button) v.findViewById(R.id.b_rss);
        bAddRSS.setOnClickListener(new OnAddButtonClickListener());

        return v;
    }

    private void validateAndSubmit() {
        String text = mEditRSS.getText().toString();

        // Reset errors
        mEditRSS.setError(null);

        if (Patterns.WEB_URL.matcher(text).matches()) {
            Log.d(TAG, "pattern OK");

            SimpleRSS2Parser parser = new SimpleRSS2Parser(text, new ParserCallback() {
                @Override
                public void onFeedParsed(Feed feed) {
                    DatabaseHelper.getInstance().insertFeed(feed);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mEditRSS.setText(null);
                            updateCursor();
                        }
                    });
                }

                @Override
                public void onError(Exception e) {
                    Log.d(TAG, "parse exception");
                    e.printStackTrace();
                }
            });

            parser.parseFeedAsync();
        } else {
            mEditRSS.setError("Invalid or malformed URL");
        }
    }

    private void updateCursor() {
        feedAdapter.changeCursor(DatabaseHelper.getInstance().getAllFeedsCursor());
    }

    public void onListItemClick(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private class OnListItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Cursor c = (Cursor) parent.getItemAtPosition(position);
            Log.d(TAG, c.getString(0) + c.getString(1) + c.getString(2));
            Uri.Builder ub = new Uri.Builder();
            ub.scheme("fragment");
            ub.authority(RSSListFragment.FRAGMENT_TAG);
            ub.appendQueryParameter(RSSListFragment.ARG_FEED_ID, c.getString(0));
            ub.appendQueryParameter(RSSListFragment.ARG_FEED_TITLE, c.getString(1));
            ub.appendQueryParameter(RSSListFragment.ARG_FEED_URL, c.getString(2));
            onListItemClick(ub.build());
        }
    }

    public class OnAddButtonClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            validateAndSubmit();
        }
    }

}
