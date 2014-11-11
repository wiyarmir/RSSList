package es.guillermoorellana.rsslist.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wiyarmir on 11/11/14.
 */
public class Feed {
    private int id;
    private String title;
    private String URL;
    private List<Article> articleList;
    private String description;

    public Feed(int id, String name, String URL) {
        this.id = id;
        this.title = name;
        this.URL = URL;
        articleList = new ArrayList<Article>();
    }

    public Feed() {
        articleList = new ArrayList<Article>();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void addArticle(Article article) {
        articleList.add(article);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
