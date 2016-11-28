package com.crawler.controller;

import com.crawler.impl.MultiCrawlerWithJpa;
import com.crawler.model.Song;
import com.crawler.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/jpa")
public class JpaCrawlerController {

    @Autowired
    private MultiCrawlerWithJpa multiCrawler;

    @Autowired
    private SongRepository songRepository;

    @GetMapping("/start")
    public String start() throws InterruptedException {
        multiCrawler.run();
        return "正在爬取中...";
    }

    @GetMapping("/songs")
    public Page<Song> songs(Pageable pageable) {
        return songRepository.findAll(pageable);
    }

}
