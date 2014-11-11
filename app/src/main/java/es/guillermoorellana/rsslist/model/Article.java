package es.guillermoorellana.rsslist.model;

import java.io.Serializable;

import at.theengine.android.simple_rss2_android.RSSItem;

/**
 * Decoupling from the library a bit...
 * <p/>
 * Created by wiyarmir on 11/11/14.
 */
public class Article extends RSSItem implements Serializable {


    private String mediaUrl;

    @Override
    public String toString() {
        return getTitle();
    }

    public Article copy() {
        Article ret = new Article();
        ret.setContent(getContent());
        ret.setDate(getDate());
        ret.setDescription(getDescription());
        ret.setLink(getLink().toString());
        ret.setTitle(getTitle());
        ret.setMediaUrl(getMediaUrl());
        return ret;
    }

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

}
