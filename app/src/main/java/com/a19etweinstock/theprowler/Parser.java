package com.a19etweinstock.theprowler;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ethan on 7/25/2017.
 */

public class Parser {

    public static Bundle parseJson(JSONObject jsonPaper){
        Bundle info = new Bundle();
        try {
            info.putString("currentEdition", jsonPaper.getJSONObject("current_edition").getString("edition"));

            JSONArray editions = jsonPaper.getJSONArray("editions");
            ArrayList<String> editionTitles = new ArrayList<String>();
            for (int index = 0; index < editions.length(); index++) {
                editionTitles.add(editions.getJSONObject(index).getString("title"));
                info.putBundle(editionTitles.get(index), parseEdition(editions.getJSONObject(index)));
            }
            info.putStringArrayList("editionTitles", editionTitles);
        } catch (JSONException e){
            e.printStackTrace();
            return info;
        }
        return info;
    }

    public static Bundle parseEdition(JSONObject jsonEdition){
        Bundle edition = new Bundle();
        try {
            edition.putString("title", jsonEdition.getString("title"));
            edition.putString("time", jsonEdition.getString("time"));

            JSONArray articles = jsonEdition.getJSONArray("articles");
            ArrayList<String> categoryTitles = new ArrayList<String>();
            for(int index = 0; index < articles.length(); index++){
                categoryTitles.add(articles.getJSONObject(index).getString("title"));
                edition.putBundle(categoryTitles.get(index),parseCategory(articles.getJSONObject(index).getJSONArray("content")));
            }
            edition.putStringArrayList("categoryTitles", categoryTitles);
        } catch (JSONException e) {
            e.printStackTrace();
            return edition;
        }
        return edition;
    }

    public static Bundle parseCategory(JSONArray jsonCategory){
        Bundle category = new Bundle();
        try {
            ArrayList<String> articleTitles = new ArrayList<String>();
            for(int index = 0; index < jsonCategory.length(); index++){
                articleTitles.add(jsonCategory.getJSONObject(index).getString("title"));
                category.putBundle(articleTitles.get(index),parseArticle(jsonCategory.getJSONObject(index)));
            }
            category.putStringArrayList("articleTitles", articleTitles);
        } catch (JSONException e){
            e.printStackTrace();
            return category;
        }
        return category;
    }

    public static Bundle parseArticle(JSONObject jsonArticle){
        Bundle article = new Bundle();
        try{
            article.putString("title", jsonArticle.getString("title"));
            article.putString("author", jsonArticle.getString("author"));
            article.putString("summary", jsonArticle.getString("summary"));
            article.putString("article", jsonArticle.getString("article").replaceAll("#NEWLINE", "\n"));
        } catch (JSONException e){
            e.printStackTrace();
            return article;
        }
        return article;
    }
}
