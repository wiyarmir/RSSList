package es.guillermoorellana.rsslist.parser;

import android.os.AsyncTask;
import android.sax.Element;
import android.sax.ElementListener;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;
import android.util.Xml;

import org.xml.sax.Attributes;

import java.util.List;

import at.theengine.android.simple_rss2_android.RSSItem;
import at.theengine.android.simple_rss2_android.SimpleRss2Parser;
import es.guillermoorellana.rsslist.model.Article;
import es.guillermoorellana.rsslist.model.Feed;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class SimpleRSS2Parser extends SimpleRss2Parser {

    private final String mUrl;
    private ParserCallback mCallback;

    // Replicating due to bad design in library...
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String CONTENT = "content";
    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";

    public SimpleRSS2Parser(String feedUrl, ParserCallback callback) {
        super(feedUrl, null);
        this.mUrl = feedUrl;
        this.mCallback = callback;
    }

    public void parseFeedAsync() {
        AsyncTask task = new AsyncTask() {

            private Exception mEx;
            private List<RSSItem> items;
            private Feed feed;

            @Override
            protected void onPostExecute(Object result) {
                if (mEx != null) {
                    if (mCallback != null) {
                        mCallback.onError(mEx);
                    }
                } else {
                    if (mCallback != null) {
                        mCallback.onFeedParsed(feed);
                    }
                }
            }

            @Override
            protected Object doInBackground(Object... arg0) {
                try {
                    feed = parseFeed();
                } catch (Exception e) {
                    mEx = e;
                }

                return null;
            }
        };

        task.execute();
    }

    public Feed parseFeed() {
        final Feed mFeed = new Feed();
        mFeed.setURL(mUrl);
        final Article currentMessage = new Article();
        RootElement root = new RootElement("rss");
        Element channel = root.getChild("channel");
        channel.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                mFeed.setTitle(s);
            }
        });
        channel.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            @Override
            public void end(String s) {
                mFeed.setDescription(s);
            }
        });

        Element item = channel.getChild(ITEM);
        item.setEndElementListener(new EndElementListener() {
            public void end() {
                mFeed.addArticle(currentMessage.copy());
            }
        });
        item.getChild(TITLE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setTitle(body);
            }
        });
        item.getChild(LINK).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setLink(body);
            }
        });
        item.getChild(DESCRIPTION).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setDescription(body);
            }
        });
        item.getChild("http://purl.org/rss/1.0/modules/content/", "encoded").setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setContent(body);
            }
        });
        item.getChild(CONTENT).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setContent(body);
            }
        });
        item.getChild(PUB_DATE).setEndTextElementListener(new EndTextElementListener() {
            public void end(String body) {
                currentMessage.setDate(body);
            }
        });
        item.getChild("http://search.yahoo.com/mrss/", "content").setStartElementListener(new StartElementListener() {
            @Override
            public void start(Attributes attributes) {
                if (attributes.getIndex("url") >= 0) {
                    currentMessage.setMediaUrl(attributes.getValue("url"));
                }
            }
        });
        try {
            Xml.parse(this.getInputStream(), Xml.Encoding.UTF_8, root.getContentHandler());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return mFeed;
    }
}
