package com.crawler.impl;

import com.crawler.HtmlParser;
import com.crawler.model.Song;
import com.crawler.model.WebPage;
import com.crawler.Crawler;

import java.util.List;

public class MultiCrawlerThread implements Runnable {

    private Crawler multiCrawler;
    private HtmlParser htmlParser = new HtmlParser();

    public MultiCrawlerThread(Crawler multiCrawler) {
        super();
        this.multiCrawler = multiCrawler;
    }

    @Override
    public void run() {
        WebPage webPage;
        int getUnCrawlPageTimes = 0;
        while (true) {
            webPage = multiCrawler.getUnCrawlPage();
            if(webPage == null) {//防止开始时爬虫队列中未爬取页面过少，线程拿不到足够的未爬页面而过早退出
                if(getUnCrawlPageTimes > 10) {
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getUnCrawlPageTimes++;
                    continue;
                }
            }
            getUnCrawlPageTimes = 0;
            if(webPage.getType().equals(WebPage.PageType.playlists)) {
                List<WebPage> playlists = htmlParser.parsePlaylists(webPage.getUrl());
                multiCrawler.addToCrawlList(playlists);
            }
            else if(webPage.getType().equals(WebPage.PageType.playlist)) {
                List<WebPage> songs =htmlParser.parsePlaylist(webPage.getUrl());
                multiCrawler.addToCrawlList(songs);
            }
            else if(webPage.getType().equals(WebPage.PageType.song)) {
                long commentNum=htmlParser.parseSong(webPage.getUrl());
                Song song=new Song(webPage.getUrl(),webPage.getTitle(),commentNum);
                multiCrawler.saveSong(song);
            }
        }
    }

}
