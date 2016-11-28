package com.crawler;

import com.crawler.impl.MultiCrawlerWithJpa;
import com.crawler.model.WebPage;
import com.crawler.model.WebPage.Status;
import com.crawler.model.WebPage.PageType;
import com.crawler.repository.WebPageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UpdateSchedule {
    @Autowired
    private MultiCrawlerWithJpa multiCrawler;

    @Autowired
    private WebPageRepository webPageRepository;

    @Scheduled(cron="0 0 1 * * *")
    public void update() {
        List<WebPage> webPages = webPageRepository.findByType(PageType.song);
        webPages.forEach(p -> p.setStatus(Status.uncrawl));
        webPageRepository.save(webPages);
        multiCrawler.doRun();
    }
}
