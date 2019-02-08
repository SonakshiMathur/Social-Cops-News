package com.example.sonakshi.socialcops.network;

import com.example.sonakshi.socialcops.BuildConfig;
import com.example.sonakshi.socialcops.models.ArticleResponseWrapper;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * An Api interface to send network requests
 * Includes Category enum that provides category names for requests
 */
public interface NewsApi {
    String API_KEY = BuildConfig.NewsApiKey;


    @Headers("X-Api-Key:" + API_KEY)
    @GET("/v2/top-headlines")
    Call<ArticleResponseWrapper> getHeadlines(
            @Query("category") String category,
            @Query("country") String country,
            @Query("q") String q
    );

    @Headers("X-Api-Key:" + API_KEY)
    @GET("/v2/top-headlines")
    Call<ArticleResponseWrapper> getHeadlinesBySource(
            @Query("sources") String source
    );

    @Headers("X-Api-Key:" + API_KEY)
    @GET("/v2/everything")
    Call<ArticleResponseWrapper> getSearchHeadlines(
            @Query("q") String q,
            @Query("from") String from,
            @Query("to") String to
    );
    enum Category {
        business("Business"),
        entertainment("Entertainment"),
        general("General"),
        health("Health"),
        science("Science"),
        sports("Sports"),
        all(""),
        technology("Technology");

        public final String title;

        Category(String title) {
            this.title = title;
        }
    }
}