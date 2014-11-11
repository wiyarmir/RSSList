package es.guillermoorellana.rsslist.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.model.Article;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RSSDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RSSDetailFragment extends Fragment {
    private static final String ARG_ARTICLE = "article";
    public static final String FRAGMENT_TAG = "rssdetail";

    private Article mArticle;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment RSSDetailFragment.
     */
    public static RSSDetailFragment newInstance(Article param1) {
        RSSDetailFragment fragment = new RSSDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_ARTICLE, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public RSSDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mArticle = (Article) getArguments().getSerializable(ARG_ARTICLE);
        } else {
            throw new RuntimeException("Invalid parameters in Bundle " + savedInstanceState.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_rssdetail, container, false);

        ((TextView) v.findViewById(android.R.id.text1)).setText(mArticle.getTitle());
        ((TextView) v.findViewById(android.R.id.text2)).setText(Html.fromHtml(mArticle.getDescription()));
        if (mArticle.getMediaUrl() != null) {
            new ImageAsyncLoadTask((ImageView) v.findViewById(R.id.imageView)).execute(mArticle.getMediaUrl());
        }
        return v;
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

    private class ImageAsyncLoadTask extends AsyncTask<String, Void, Bitmap> {

        private final ImageView mImageView;

        public ImageAsyncLoadTask(ImageView targetView) {
            mImageView = targetView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mImageView.setImageBitmap(bitmap);
            }
        }
    }
}
