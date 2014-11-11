package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.app.ListFragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pkmmte.pkrss.Article;
import com.pkmmte.pkrss.Callback;
import com.pkmmte.pkrss.PkRSS;

import java.util.ArrayList;
import java.util.List;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.adapter.ArticleAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RSSListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSSListFragment extends ListFragment implements Callback {
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
    private List<Article> mDataset;

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

            mDataset = new ArrayList<Article>();
            mAdapter = new ArticleAdapter(getActivity(), android.R.layout.simple_list_item_1, mDataset);
        } else {
            throw new RuntimeException("Invalid parameters in Bundle " + savedInstanceState.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_rsslist,container, false);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();

        fetchFeed();
    }

    private void fetchFeed() {
        PkRSS.with(getActivity()).load(mFeedUrl).callback(this).async();
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


    @Override
    public void OnPreLoad() {

    }

    @Override
    public void OnLoaded(List<Article> articles) {
        mDataset = articles;
        Log.d(TAG, "Fetched: " + articles.size());
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdapter.addAll(mDataset);
                mAdapter.notifyDataSetChanged();
                setListShown(true);
            }
        });
    }

    @Override
    public void OnLoadFailed() {
        Log.e(TAG, "RSS Load Failed");
    }
}
