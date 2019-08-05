package com.interview.assignment.webcrawlerjs.crawl.search;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSParser {

  private static Logger LOG = LoggerFactory.getLogger(JSParser.class);

  /**
   * Find out the dowload scheme for javascript object and location of the script
   * @param jsCodeString
   * @param website
   * @return
   */
  public String parseJavaScriptLocation(final String jsCodeString,final String website) {
    String jsCode = jsCodeString;
    String originalUrl = website.substring(0,
        website.indexOf("/", website.indexOf("://") + 3));
    LOG.info("jscode: {}", jsCode);
    if (!jsCode.startsWith("//")) {
      jsCode = originalUrl + jsCode;
    } else if (!originalUrl.contains("https")) {
      jsCode = "http:" + jsCode;
    } else {
      jsCode = "https:" + jsCode;
    }
    LOG.info("originalUrl:{} ", originalUrl);
    return jsCode;
    }


}
