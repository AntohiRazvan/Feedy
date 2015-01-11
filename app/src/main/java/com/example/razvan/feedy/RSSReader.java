package com.example.razvan.feedy;
import org.xml.sax.SAXException;

import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class RSSReader {
    private String rssUrl;

    public RSSReader(String rssUrl) {
        this.rssUrl = rssUrl;
    }

    public List<RSSItem> getItems() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        RSSParseHandler handler = new RSSParseHandler();
        try {
            saxParser.parse(rssUrl, handler);
            return handler.getItems();
        } catch(SAXException e){
            return handler.getItems();
        }
        catch (Exception e){
            return null;
        }
    }
}
