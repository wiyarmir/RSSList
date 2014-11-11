package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.adapter.FeedAdapter;
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

        feedList = (ListView) v.findViewById(R.id.listView);
        feedAdapter = new FeedAdapter(getActivity(), android.R.layout.simple_list_item_1, feedsCursor, new String[]{DatabaseHelper.KEY_FEED_TITLE}, new int[]{android.R.id.text1}, 0);
        feedList.setAdapter(feedAdapter);
        feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });

        return v;
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

}
