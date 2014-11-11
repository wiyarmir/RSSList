package es.guillermoorellana.rsslist.model;

import java.net.URL;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class Feed {
    private int id;
    private String name;
    private String URL;

    public Feed(int id, String name, String URL) {
        this.id = id;
        this.name = name;
        this.URL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
