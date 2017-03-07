package com.crawler.controller;

import com.crawler.model.Song;
import com.crawler.example.Crawler;
import com.crawler.impl.MultiCrawlerWithList;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/multi")
public class MultiCrawlerController {

    private final Crawler crawler = new MultiCrawlerWithList();

    @GetMapping("/start")
    public String start() throws InterruptedException {
        crawler.run();
        return "爬取完毕";
    }

    @GetMapping("/songs")
    public List<Song> songs() {
        return crawler.getSongs();
    }

}
