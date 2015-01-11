package com.example.razvan.feedy;

import java.util.HashMap;
import java.util.Map;

public class Categories {
    Map<String, String> categories = new HashMap<String, String>();
    Map<String, String> preferredCategories = new HashMap<String, String>();

    public Categories(){
        categories.put("Top Stories", "http://feeds.bbci.co.uk/news/rss.xml");
        categories.put("World", "http://feeds.bbci.co.uk/news/world/rss.xml");
        categories.put("Business", "http://feeds.bbci.co.uk/news/business/rss.xml");
        categories.put("Politics", "http://feeds.bbci.co.uk/news/politics/rss.xml");
        categories.put("Health", "http://feeds.bbci.co.uk/news/politics/rss.xml");
        categories.put("Education and Family", "http://feeds.bbci.co.uk/news/education/rss.xml");
        categories.put("Science and Environment", "http://feeds.bbci.co.uk/news/science_and_environment/rss.xml");
        categories.put("Technology", "http://feeds.bbci.co.uk/news/technology/rss.xml");
        categories.put("Entertainment and Arts", "http://feeds.bbci.co.uk/news/entertainment_and_arts/rss.xml");
        categories.put("Africa", "http://feeds.bbci.co.uk/news/world/africa/rss.xml");
        categories.put("England", "http://feeds.bbci.co.uk/news/england/rss.xml");
        categories.put("Europe", "http://feeds.bbci.co.uk/news/world/europe/rss.xml");
        categories.put("Latin America", "http://feeds.bbci.co.uk/news/world/latin_america/rss.xml");
        categories.put("Middle East", "http://feeds.bbci.co.uk/news/world/middle_east/rss.xml");
        categories.put("US and Canada", "http://feeds.bbci.co.uk/news/world/us_and_canada/rss.xml");

        addPreferredCategory("Top Stories");
        addPreferredCategory("Europe");
        addPreferredCategory("Technology");
        addPreferredCategory("Science and Environment");
    }

    Map<String, String> getCategories(){
        return categories;
    }

    Map<String, String> getPreferredCategories(){
        return preferredCategories;
    }

    public void addPreferredCategory(String category){
        preferredCategories.put(category, categories.get(category));
    }

    public void clearPreferredCategories(){
        preferredCategories.clear();
    }

}

