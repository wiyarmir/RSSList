package es.guillermoorellana.rsslist.model;

import android.graphics.Bitmap;

import at.theengine.android.simple_rss2_android.RSSItem;

/**
 * Decoupling from the library a bit...
 * <p/>
 * Created by wiyarmir on 11/11/14.
 */
public class Article extends RSSItem {


    private String mediaUrl;
    private Bitmap cachedMedia;

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

    public Bitmap getCachedMedia() {
        return cachedMedia;
    }

    public void setCachedMedia(Bitmap cachedMedia) {
        this.cachedMedia = cachedMedia;
    }
}
