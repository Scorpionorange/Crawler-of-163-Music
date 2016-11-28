package com.crawler.repository;

import com.crawler.model.WebPage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WebPageRepository extends JpaRepository<WebPage, String> {

    WebPage findTopByStatus(WebPage.Status status);

    long countByStatus(WebPage.Status status);

    List<WebPage> findByType(WebPage.PageType pageType);
}
