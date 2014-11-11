package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.guillermoorellana.rsslist.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SplashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SplashFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String FRAGMENT_TAG = "splashfragment";
    private static final String ARG_TIMEOUT = FRAGMENT_TAG + "_timeout";
    private static final String ARG_NEXT_FRAGMENT = FRAGMENT_TAG + "_nextfragment";

    private String mNextFragment;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param timeout      Parameter 1.
     * @param nextFragment Parameter 2.
     * @return A new instance of fragment SplashFragment.
     */
    public static SplashFragment newInstance(long timeout, String nextFragment) {
        SplashFragment fragment = new SplashFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_TIMEOUT, timeout);
        args.putString(ARG_NEXT_FRAGMENT, nextFragment);
        fragment.setArguments(args);
        return fragment;
    }

    public SplashFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long mTimeout = getArguments().getLong(ARG_TIMEOUT);
            mNextFragment = getArguments().getString(ARG_NEXT_FRAGMENT);

            Uri.Builder ub = new Uri.Builder();
            ub.scheme("fragment");
            ub.authority(mNextFragment);
            final Uri uri = ub.build();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    onTimeoutExpired(uri);
                }
            }, mTimeout);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    public void onTimeoutExpired(Uri uri) {
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
