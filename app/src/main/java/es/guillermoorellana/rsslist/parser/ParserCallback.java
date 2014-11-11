package es.guillermoorellana.rsslist.parser;

import es.guillermoorellana.rsslist.model.Feed;

/**
 * Created by wiyarmir on 11/11/14.
 */
public abstract class ParserCallback {
    public abstract void onFeedParsed(Feed feed);
    public abstract void onError(Exception ex);
}
