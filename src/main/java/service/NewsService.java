package service;

import api.NewsApiClient;
import model.Article;
import java.util.List;

public class NewsService {
    private final NewsApiClient newsApiClient = new NewsApiClient();

    public List<Article> getTopHeadlines(String category) {
        return newsApiClient.getTopHeadlines(category);
    }
}