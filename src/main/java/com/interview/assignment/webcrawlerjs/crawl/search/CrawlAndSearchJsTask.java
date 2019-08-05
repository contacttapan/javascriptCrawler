package com.interview.assignment.webcrawlerjs.crawl.search;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import com.interview.assignment.webcrawlerjs.domain.JavaScriptDetails;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CrawlAndSearchJsTask {

  private static Logger LOG = LoggerFactory.getLogger(CrawlAndSearchJsTask.class);
  private String website;
  private Random random = new Random();

  private JSParser parser = new JSParser();

  public CrawlAndSearchJsTask(String website) {
    this.website = website;
  }


  /**
   * Given a website , it opens the website as Jsoup documents and parses Javascript imported
   * and uses @{@link JSParser} to find out javascripts and creates list of @{@link JavaScriptDetails}
   * @return
   * @throws IOException
   */
  public List<JavaScriptDetails> searchJs() throws IOException {

    final List<JavaScriptDetails> jsList = new ArrayList<>();
    final Document linkedDoc = Jsoup.connect(website).timeout(10000).get();
    if (linkedDoc.select("script") != null) {
      final Iterator<Element> scriptElementItr = linkedDoc.select("script").iterator();
      while (scriptElementItr.hasNext()) {
        final Element jsElement = scriptElementItr.next();
        final String fileName = "download/JsFile" + random.nextInt() + ".js";
        String jsCode = jsElement != null ? jsElement.attr("src") : "";
        if (jsCode.length() > 0) {
          if (!jsCode.startsWith("http")) {
            jsCode = parser.parseJavaScriptLocation(jsCode, website);
          }
          LOG.info("jsURL:{} ", jsCode);
          try (BufferedInputStream inputStream = new BufferedInputStream(
              new URL(jsCode).openStream());
              final FileOutputStream fileOS = new FileOutputStream(fileName)) {
            final byte data[] = new byte[1024];
            int byteContent;
            while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
              fileOS.write(data, 0, byteContent);
            }
            final String sha = Files.hash(new File(fileName), Hashing.sha1()).toString();
            LOG.info("link:{}  js:{} digest:{}", website, jsCode,
                sha);
            jsList.add(new JavaScriptDetails(jsCode, fileName, sha));
          } catch (Exception e) {
            LOG.error("Couldn't download " + jsCode, e);
          }
        }


      }

    }
    return jsList;
  }

}