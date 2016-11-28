package com.crawler.impl;

import com.crawler.model.Song;
import com.crawler.Crawler;
import com.crawler.repository.SongRepository;
import com.crawler.repository.WebPageRepository;
import com.crawler.model.WebPage;
import com.crawler.model.WebPage.PageType;
import com.crawler.model.WebPage.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Component
public class MultiCrawlerWithJpa implements Crawler {

    public static final Integer MAX_THREADS = 20;

    @Autowired
    private WebPageRepository webPageRepository;

    @Autowired
    private SongRepository songRepository;

    @Override
    public void initCrawlerList() {
//        for(int i = 0; i < 43; i++) {
//            webPageRepository.saveAndFlush(new WebPage("http://music.163.com/discover/playlist/?order=hot&cat=%E5%85%A8%E9%83%A8&limit=35&offset="  + (i * 35), PageType.playlists));
//        }
        webPageRepository.saveAndFlush(new WebPage("http://music.163.com/playlist?id=80450298", PageType.playlist));
    }

    @Override
    public synchronized WebPage getUnCrawlPage() {
        WebPage webPage = webPageRepository.findTopByStatus(Status.uncrawl);
        if(webPage == null) {
            return null;
        }
        webPage.setStatus(Status.crawled);
        return webPageRepository.saveAndFlush(webPage);
    }

    @Override
    public List<WebPage> addToCrawlList(List<WebPage> webPages) {
        webPages = webPageRepository.save(webPages);
        webPageRepository.flush();
        return webPages;
    }

    @Override
    public Song saveSong(Song song) {
        return songRepository.saveAndFlush(song);
    }

    @Override
    public void doRun(){
        ExecutorService executorService = Executors.newFixedThreadPool(MAX_THREADS);
        for(int i = 0; i < MAX_THREADS; i++) {
            executorService.execute(new MultiCrawlerThread(this));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Song> getSongs() {
        return songRepository.findAll();
    }

}
