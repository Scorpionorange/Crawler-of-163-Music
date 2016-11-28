package com.crawler.impl;

import com.crawler.HtmlParser;
import com.crawler.model.Song;
import com.crawler.model.WebPage;
import com.crawler.Crawler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BasicCrawler implements Crawler {

    private final HtmlParser htmlParser = new HtmlParser();
    public List<WebPage> crawlerList;
    public List<Song> songs = new ArrayList<>();


    @Override
    public void initCrawlerList() {
        crawlerList = new ArrayList<WebPage>();
//        for(int i = 0; i < 43; i++) {
//            crawlerList.add(new WebPage("http://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8&limit=35&offset="  + (i * 35), PageType.playlists));
//        }
        crawlerList.add(new WebPage("http://music.163.com/playlist?id=4540168", WebPage.PageType.playlist));
    }


    public void doRun() {
        WebPage webPage;
        HtmlParser htmlParser = new HtmlParser();
        while ((webPage = getUnCrawlPage()) != null) {
            if(webPage.getType().equals(WebPage.PageType.playlists)) {
                List<WebPage> playlists = htmlParser.parsePlaylists(webPage.getUrl());
                addToCrawlList(playlists);
            }
            else if(webPage.getType().equals(WebPage.PageType.playlist)) {
                List<WebPage> songs =htmlParser.parsePlaylist(webPage.getUrl());
                addToCrawlList(songs);
            }
            else if(webPage.getType().equals(WebPage.PageType.song)) {
                long commentNum=htmlParser.parseSong(webPage.getUrl());
                Song song=new Song(webPage.getUrl(),webPage.getTitle(),commentNum);
//                System.out.println(song.toString());
                saveSong(song);
            }
        }
    }

    public WebPage getUnCrawlPage() {
        if(crawlerList.size()==0)
            return null;
        WebPage newWeb=crawlerList.get(0);
        newWeb.setStatus(WebPage.Status.crawled);
        crawlerList.remove(newWeb);
        return newWeb;
    }

    public List<WebPage> addToCrawlList(List<WebPage> webPages) {

        for(WebPage page:webPages){
            crawlerList.add(page);
        }
        return crawlerList;
    }

    public Song saveSong(Song song){
        songs.add(song);
        return song;
    }

    public List<Song> getSongs() {
        return songs;
    }

    public static <T> void main(String[] args) throws Exception {
        Date startTime = new Date();
        Crawler crawler = new BasicCrawler();
        crawler.run();
        for(Song song : crawler.getSongs()) {
            System.out.println(song);
        }
        System.out.println("花费时间：" + (new Date().getTime() - startTime.getTime()));
    }
}