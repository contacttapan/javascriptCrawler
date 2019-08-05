package com.interview.assignment.webcrawlerjs.crawl.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

@Component
public class GoogleCrawler {

  private String googleSerachURI = "https://google.co.in/search?q=";

  /**
   * This crawler only searches links in the 1st page
   * @param searchString
   * @return
   * @throws IOException
   */
  public List<String> crawlGoogle(String searchString) throws IOException {
    Document doc = Jsoup.connect(googleSerachURI + searchString).get();
    Iterator<Element> pageLinkDivsItr = doc.select("div.r").iterator();
    final List<String> jsList = new ArrayList<>();

    while (pageLinkDivsItr.hasNext()) {
      String link = pageLinkDivsItr.next().select("a").attr("href");
      if (link != null && link.trim().length() > 0) {
        jsList.add(link);
      }
    }
    return jsList;
  }
}
