package com.crawler;

import org.jsoup.*;

import java.io.IOException;

public class HtmlFetcher {
    public String fetch(String url) {
        if(url==null||url.equals(""))
            return null;
        Connection.Response response=null;
        try {
            response = Jsoup.connect(url).timeout(3000).execute();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (response==null)
                return null;
            else
                return response.body();
        }

    }

    public static <T> void main(String[] args) throws Exception {
        HtmlFetcher htmlFetcher = new HtmlFetcher();
        System.out.println(htmlFetcher.fetch("http://music.163.com/playlist?id=454016843"));
    }
}
