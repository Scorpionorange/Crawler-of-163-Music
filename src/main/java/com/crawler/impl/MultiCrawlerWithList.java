package com.crawler.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.crawler.model.Song;
import com.crawler.model.WebPage;
import com.crawler.example.Crawler;

public class MultiCrawlerWithList implements Crawler {

    public List<WebPage> crawlerList;
    public List<Song> songs = new ArrayList<>();
    public static final Integer MAX_THREADS = 20;

    public void initCrawlerList() {
        crawlerList = new ArrayList<WebPage>();
        crawlerList.add(new WebPage("http://music.163.com/playlist?id=80450298", WebPage.PageType.playlist));
    }

    public synchronized WebPage getUnCrawlPage() {
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

    public Song saveSong(Song song) {
        songs.add(song);
        return song;
    }

    public void doRun() {
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        for (int i = 0; i < MAX_THREADS; i++) {
            MultiCrawlerThread mct = new MultiCrawlerThread(this);
            executorService.execute(mct);
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public List<Song> getSongs() {
        return songs;
    }

    public static <T> void main(String[] args) throws Exception {
        Date startTime = new Date();
        Crawler crawler = new MultiCrawlerWithList();
        crawler.run();
        for(Song song : crawler.getSongs()) {
            System.out.println(song);
        }
        System.out.println("multi花费时间：" + (new Date().getTime() - startTime.getTime()));
    }

}
