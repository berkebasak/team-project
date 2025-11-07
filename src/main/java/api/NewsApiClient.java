package api;

import model.Article;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import java.util.*;

public class NewsApiClient {
    private static final String API_KEY = "pub_bfcfbe16d3df4bf4b577b1b6096daf57";
    private static final String BASE_URL = "https://newsdata.io/api/1/news?country=us&language=en";

    private final OkHttpClient client = new OkHttpClient();

    public List<Article> getTopHeadlines(String category) {
        return fetchArticles(BASE_URL + "&category=" + category);
    }

    private List<Article> fetchArticles(String url) {
        List<Article> articles = new ArrayList<>();
        try {
            Request request = new Request.Builder()
                    .url(url + "&apikey=" + API_KEY)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    System.err.println("NewsData error: " + response.code());
                    return articles;
                }

                JSONObject obj = new JSONObject(response.body().string());
                JSONArray results = obj.optJSONArray("results");
                if (results == null) return articles;

                for (int i = 0; i < results.length(); i++) {
                    JSONObject a = results.getJSONObject(i);
                    articles.add(new Article(
                            UUID.randomUUID().toString(),
                            a.optString("title", ""),
                            a.optString("description", ""),
                            a.optString("link", ""),
                            a.optString("image_url", ""),
                            a.optString("source_id", "")
                    ));
                }
            }
        } catch (Exception e) {
            System.err.println("NewsData error: " + e.getMessage());
        }
        return articles;
    }
}