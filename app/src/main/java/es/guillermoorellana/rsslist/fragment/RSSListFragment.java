package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.adapter.ArticleAdapter;
import es.guillermoorellana.rsslist.model.Article;
import es.guillermoorellana.rsslist.model.Feed;
import es.guillermoorellana.rsslist.parser.ParserCallback;
import es.guillermoorellana.rsslist.parser.SimpleRSS2Parser;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RSSListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSSListFragment extends ListFragment {
    public static final String ARG_FEED_ID = "FEED_ID";
    public static final String ARG_FEED_TITLE = "FEED_TITLE";
    public static final String ARG_FEED_URL = "FEED_URL";
    public static final String FRAGMENT_TAG = "rsslist";
    private static final String TAG = "RSSListFragment";

    private int mFeedId;
    private String mFeedTitle;
    private String mFeedUrl;

    private OnFragmentInteractionListener mListener;
    private ArticleAdapter mAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param feedId    Parameter 1.
     * @param feedTitle Parameter 2.
     * @return A new instance of fragment RSSListFragment.
     */
    public static RSSListFragment newInstance(int feedId, String feedTitle, String feedUrl) {
        RSSListFragment fragment = new RSSListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FEED_ID, feedId);
        args.putString(ARG_FEED_TITLE, feedTitle);
        args.putString(ARG_FEED_URL, feedUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public RSSListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFeedId = getArguments().getInt(ARG_FEED_ID);
            mFeedTitle = getArguments().getString(ARG_FEED_TITLE);
            mFeedUrl = getArguments().getString(ARG_FEED_URL);

        } else {
            throw new RuntimeException("Invalid parameters in Bundle " + savedInstanceState.toString());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getView().setBackgroundResource(R.color.background);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Article a = (Article) adapterView.getItemAtPosition(position);
                RSSDetailFragment rssDetailFragment = RSSDetailFragment.newInstance(a);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_in_up, 0, 0, R.animator.slide_out_down);
                ft.add(R.id.container, rssDetailFragment, RSSDetailFragment.FRAGMENT_TAG);
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        fetchFeed();
    }

    private void fetchFeed() {
        SimpleRSS2Parser parser = new SimpleRSS2Parser(mFeedUrl, new ParserCallback() {
            @Override
            public void onFeedParsed(final Feed feed) {
                Log.d(TAG, "Fetched: " + feed.getArticleList().size());
                if (getActivity() != null) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (feed.getArticleList().size() == 0) {
                                Toast.makeText(getActivity(), "The RSS feed was empty!", Toast.LENGTH_LONG).show();
                            } else {
                                mAdapter = new ArticleAdapter(getActivity(), R.layout.rss_list_item, feed.getArticleList());
                                setListAdapter(mAdapter);
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception ex) {
                Log.d(TAG, ex.toString());
            }
        });
        parser.parseFeedAsync();
    }

    public void onButtonPressed(Uri uri) {
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
