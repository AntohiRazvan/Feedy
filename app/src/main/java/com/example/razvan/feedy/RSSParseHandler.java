package com.example.razvan.feedy;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class RSSParseHandler extends DefaultHandler {
    private List<RSSItem> rssItems;
    private RSSItem currentItem;
    private boolean parsingTitle;
    private boolean parsingLink;
    private int items = 0;
    private final int MAX_ITEMS = 12;

    public RSSParseHandler() {
        rssItems = new ArrayList();
    }

    public List<RSSItem> getItems() {
        return rssItems;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if ("item".equals(qName)) {
            currentItem = new RSSItem();
        } else if ("title".equals(qName)) {
            parsingTitle = true;
        } else if ("link".equals(qName)) {
            parsingLink = true;
        } else if ("media:thumbnail".equals(qName)) {
            String urlString = attributes.getValue("url");
            try {
                if(attributes.getValue("width").equals("144")) {
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    Bitmap myBitmap = BitmapFactory.decodeStream(input);
                    myBitmap = Bitmap.createScaledBitmap(myBitmap, 300, 175, true);
                    currentItem.setThumbnail(myBitmap);
                    ++items;
                    if(items > MAX_ITEMS)
                        throw new MaxItemsException();
                }
            } catch (IOException e) {
                Log.e("Error:", e.getMessage());
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("item".equals(qName)) {
            if(currentItem.getThumbnail() != null)
                rssItems.add(currentItem);
            currentItem = null;
        } else if ("title".equals(qName)) {
            parsingTitle = false;
        } else if ("link".equals(qName)) {
            parsingLink = false;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (parsingTitle) {
            if (currentItem != null)
                currentItem.setTitle(new String(ch, start, length));
        } else if (parsingLink) {
            if (currentItem != null) {
                currentItem.setLink(new String(ch, start, length));
                parsingLink = false;
            }
        }
    }
}
