package es.guillermoorellana.rsslist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.model.Article;


/**
 * Created by wiyarmir on 11/11/14.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String TAG = "ArticleAdapter";
    private final int mResource;
    protected Map<Integer, Bitmap> mBitmapCache;

    static class ViewHolder {
        private ImageView articleIcon;
        private TextView articleTitle;
        private TextView articleDescription;
    }

    public ArticleAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
        mResource = resource;
        mBitmapCache = new ConcurrentHashMap<Integer, Bitmap>();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        Article article = getItem(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new ViewHolder();
            holder.articleIcon = (ImageView) convertView.findViewById(R.id.imageView);
            holder.articleTitle = (TextView) convertView.findViewById(android.R.id.text1);
            holder.articleDescription = (TextView) convertView.findViewById(android.R.id.text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.articleTitle.setText(article.getTitle());
        holder.articleDescription.setText(Html.fromHtml(article.getDescription()));


        if (article.getMediaUrl() != null) {
            if (mBitmapCache.containsKey(position)) {
                holder.articleIcon.setImageBitmap(mBitmapCache.get(position));
            } else {
                holder.articleIcon.setImageResource(R.drawable.polaroid2);
                new ImageAsyncLoadTask(holder.articleIcon, position).execute(article.getMediaUrl());
            }
        }


        return convertView;
    }

    @Override
    public void notifyDataSetInvalidated() {
        // positions are no longer valid
        mBitmapCache.clear();
        super.notifyDataSetInvalidated();
    }

    private class ImageAsyncLoadTask extends AsyncTask<String, Void, Bitmap> {

        private final ImageView mImageView;
        private int mPosition;

        public ImageAsyncLoadTask(ImageView targetView, int position) {
            mImageView = targetView;
            mPosition = position;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            Bitmap mIcon = null;
            try {
                InputStream in = new URL(urls[0]).openStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                Log.e(TAG, "Offending URL:" + urls[0]);
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return mIcon;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                mBitmapCache.put(mPosition, bitmap);
                mImageView.setImageBitmap(bitmap);
            }
        }
    }

}
