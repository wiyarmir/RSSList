package es.guillermoorellana.rsslist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.Html;
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

import es.guillermoorellana.rsslist.R;
import es.guillermoorellana.rsslist.model.Article;


/**
 * Created by wiyarmir on 11/11/14.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {

    private final int mResource;

    static class ViewHolder {
        private ImageView articleIcon;
        private TextView articleTitle;
        private TextView articleDescription;
    }

    public ArticleAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
        mResource = resource;
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

        // Since images are kinda small, it's worth caching them inside the POJO,
        // but it won't be serializable anymore.
        // Possible improvements, download all images in an independent and paralel fashion
        // FIXME: it will bug when scrolled sort of fast
        if(article.getCachedMedia() == null) {
            holder.articleIcon.setImageResource(R.drawable.polaroid2);
            new ImageAsyncLoadTask(holder.articleIcon).execute(article.getMediaUrl());
        } else {
            holder.articleIcon.setImageBitmap(article.getCachedMedia());
        }

        return convertView;
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
