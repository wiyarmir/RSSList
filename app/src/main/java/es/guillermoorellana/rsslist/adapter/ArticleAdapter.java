package es.guillermoorellana.rsslist.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.pkmmte.pkrss.Article;

import java.util.List;


/**
 * Created by wiyarmir on 11/11/14.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    public ArticleAdapter(Context context, int resource, List<Article> objects) {
        super(context, resource, objects);
    }
}
