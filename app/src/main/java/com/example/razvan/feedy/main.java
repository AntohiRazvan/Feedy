package com.example.razvan.feedy;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class main extends Activity {
    LinearLayout screen;
    Categories categories = new Categories();
    boolean internetConnection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        screen = (LinearLayout)findViewById(R.id.screen);
    }

    @Override
    public void onResume(){
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Map<String, ?> newPrefs = prefs.getAll();
        screen.removeAllViews();
        categories.clearPreferredCategories();
        for(Map.Entry entry : newPrefs.entrySet()) {
            if((Boolean)entry.getValue() == true)
                categories.addPreferredCategory(entry.getKey().toString());
        }
        populateScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.category_selection, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.categories:
                chooseCategories();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class GetRSSDataTask extends AsyncTask<String, Void, List<RSSItem> > {
        private String categoryName;
        @Override
        protected List<RSSItem> doInBackground(String... params) {
            try {
                categoryName = params[0];
                RSSReader rssReader = new RSSReader(params[1]);
                return rssReader.getItems();
            } catch (Exception e) {
                Log.e("Error:", e.getMessage());
            }
            return null;
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected void onPostExecute(List<RSSItem> result) {
            HorizontalScrollView rowContainer = new HorizontalScrollView(getApplicationContext());
            LinearLayout row = new LinearLayout(getApplicationContext());
            row.setOrientation(LinearLayout.VERTICAL);
            LinearLayout items = new LinearLayout(getApplicationContext());

            FrameLayout categoryContainer = new FrameLayout(getApplicationContext());
            FrameLayout.LayoutParams categoryContainerParams = new FrameLayout.LayoutParams(800, 75);
            categoryContainer.setLayoutParams(categoryContainerParams);
            TextView category = new TextView(getApplicationContext());
            category.setText(categoryName);
            category.setGravity(Gravity.CENTER_VERTICAL);
            category.setTextSize(25);
            categoryContainer.addView(category);

            screen.addView(categoryContainer);
            rowContainer.addView(items);
            screen.addView(rowContainer);

            FrameLayout.LayoutParams containerParams = new FrameLayout.LayoutParams(350, 305);
            LinearLayout.LayoutParams articleParams = new LinearLayout.LayoutParams(300, 305);
            LinearLayout.LayoutParams thumbnailParams = new LinearLayout.LayoutParams(300, 200);

            for (final RSSItem item : result) {
                LinearLayout article = new LinearLayout(getApplicationContext());
                article.setOrientation(LinearLayout.VERTICAL);
                article.setLayoutParams(articleParams);
                article.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getLink()));
                        startActivity(browserIntent);
                    }
                });

                TextView title = new TextView(getApplicationContext());
                title.setText(item.getTitle());
                title.setTextSize(14);
                title.setGravity(Gravity.CENTER);

                ImageView iv = new ImageView(getApplicationContext());
                iv.setLayoutParams(thumbnailParams);
                iv.setImageBitmap(item.getThumbnail());

                FrameLayout container = new FrameLayout(getApplicationContext());
                container.setLayoutParams(containerParams);

                article.addView(iv);
                article.addView(title);
                article.setBackgroundColor(Color.rgb(3, 2, 2));
                container.addView(article);

                items.addView(container);
                container.setForegroundGravity(Gravity.CENTER);
            }
        }
    }

    private void populateScreen(){
        findViewById(R.id.scrollView).setBackgroundColor(Color.rgb(43, 43, 43));
        for(Map.Entry entry : categories.getPreferredCategories().entrySet()){
            if(isNetworkAvailable()) {
                GetRSSDataTask task = new GetRSSDataTask();
                task.execute(entry.getKey().toString(), entry.getValue().toString());
            } else {
                noNetworkAvailableAlert();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void noNetworkAvailableAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        builder.setTitle("No internet connection!");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    void chooseCategories(){
        Intent intent = new Intent(this, CategoriesPreference.class);
        startActivity(intent);
    }
}
