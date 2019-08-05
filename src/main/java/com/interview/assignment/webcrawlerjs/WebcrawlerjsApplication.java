package com.interview.assignment.webcrawlerjs;

import com.google.common.base.Stopwatch;
import com.interview.assignment.webcrawlerjs.crawl.search.CrawlAndSearchJsTask;
import com.interview.assignment.webcrawlerjs.crawl.search.GoogleCrawler;
import com.interview.assignment.webcrawlerjs.crawl.search.SearchStringValidator;
import com.interview.assignment.webcrawlerjs.domain.JavaScriptDetails;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.ValidationException;
import javax.validation.constraints.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This is main class to do a google search for a given term and downloads all the javascripts used in
 * 1st page of google search result
 */
@SpringBootApplication
public class WebcrawlerjsApplication implements CommandLineRunner {

  private static Logger LOG = LoggerFactory
      .getLogger(WebcrawlerjsApplication.class);

  @Value("${crawer.threadDefaultCount:4}")
  private int defaultNoOfThread;


  @Autowired
  private SearchStringValidator<String> searchStringValidator;

  @Autowired
  private GoogleCrawler googleCrawler;


  private ForkJoinPool threadPool;


  public static void main(String[] args) {
    LOG.info("STARTING THE APPLICATION");
    SpringApplication.run(WebcrawlerjsApplication.class, args);
    LOG.info("APPLICATION FINISHED");
  }

  @Override
  public void run(String... args) throws NoSuchAlgorithmException,ValidationException {
    LOG.info("EXECUTING : command line runner");
    System.out.println("Enter your search term followed by return key <ENTER>:\n");

    threadPool = new ForkJoinPool(defaultNoOfThread);

    Scanner inputTerm = new Scanner(System.in);

    final String searchString = inputTerm.nextLine();

    searchStringValidator.isValid(searchString);

    System.out.println("Started Search for your term: " + searchString);

    Document doc = null;

    final ConcurrentHashMap<String, List<JavaScriptDetails>> results = new ConcurrentHashMap<>();

    final Stopwatch stopwatch=Stopwatch.createStarted();

    try {
      final List<String> jsList = googleCrawler.crawlGoogle(searchString);
      threadPool
          .submit(
              () ->jsList.parallelStream().forEach(link->
              {
                try {
                  results.put(link, new CrawlAndSearchJsTask(link).searchJs());
                } catch (IOException e) {
                  LOG.error("Exception while crawling for JS {} : {}", link,e);
          }
              })).get();

    } catch (IOException  | InterruptedException | ExecutionException  e) {
      LOG.error("Exception while crawling for searchTerm {} : {}", searchString,e);
    }

   //iterate and sort file hash
    final List<JavaScriptDetails> mergedResultsJsObjects = results.keySet().stream()
        .map(key -> results.get(key))
        .flatMap(Collection::stream)
        .collect(Collectors.toList());

    final Map<String,List<JavaScriptDetails>> groupByHash = mergedResultsJsObjects.stream()
        .collect(Collectors.groupingBy(JavaScriptDetails::getFileHash));

    Map<String, Integer> hashValueMapByCount = groupByHash.entrySet().stream()
        .collect(Collectors.toMap(Entry::getKey
            , e -> e.getValue().size()));

    List<Map.Entry<String, Integer> > jsRankList =
        new LinkedList<Entry<String, Integer> >(hashValueMapByCount.entrySet());

    Collections.sort(jsRankList, new Comparator<Map.Entry<String, Integer> >() {
      //always reverse sort
      public int compare(Map.Entry<String, Integer>  source, Map.Entry<String, Integer>  target) {
        return -(source.getValue()).compareTo(target.getValue());
      }
    });


    List<String> top5OrAll =
        jsRankList.size() > 5 ? jsRankList.stream().collect(Collectors.toList())
            .subList(0, 5).stream().map(e->e.getKey()).collect(Collectors.toList()) :
            jsRankList.stream().map(e->e.getKey()).collect(Collectors.toList());


    top5OrAll.stream().forEach(fileHash->
        {
          LOG.info("===================javaScript With Details=================");
          Optional<Entry<String, List<JavaScriptDetails>>> entry = groupByHash.entrySet().stream()
              .filter(hashByFileCount ->
             hashByFileCount.getKey().equals(fileHash))
              .findAny();
          if(entry.isPresent()) {
            LOG.info("Following JavaScripts  used {} times >> Details Below...", entry.get().getValue().size());
            entry.get().getValue().stream().forEach(System.out::println);

          }
        });

    LOG.info("jsLIst.size:{} time elapsed:{}", results.size(),stopwatch.elapsed(TimeUnit.MILLISECONDS));
    ;
  }
}
