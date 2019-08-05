# Getting Started

This project aims to crawl on a given search string and provide list of top 5 used most used javaScript.
 1. Read​ ​a​ ​string​ ​(search​ ​term)​ ​from​ ​standard​ ​input with minimal validation like null and size
 2. Get​ ​a​ ​Google​ ​result​ ​page​ ​for​ ​the​ ​search​ ​term 
 3. Extract​ ​main​ ​result​ ​links​ ​from​ ​the​ ​page, it used [jSoup]  (https://jsoup.org/)  to process the search result in programmatic way
 4. it also downloads the Java scripts used in the above result with parsing the each page . this download is multi-threaded using forkAndJoin feature and number of thread is configurable , please see application.yml file
 5. To compare and find top 5 javascript from the downloaded javascript below algorithm is used
   
 ### How to Build
 `` 
  mvn clean install``
 
 ##Run
 `` cd target ``
 
 `` java -jar webcrawlerjs-0.0.1-SNAPSHOT.jar
``

``or``
 
``Use Eclipse/IntelliJ SpringBoot Main Application Runner``
   
 ### Alogorithm to find top 5 most used javascript
    input: List of JavaScriptObjects[ JavaScriptFile ]
    
    output: print all Number of time javascript is used and its details
    
    1. Generate 'sha` with google guava Api[Pleas see refeence link below]   
    2. create a custom list of JavaScript objects **Look for class JavaScriptDetails**  which has FileName,`sha` value, and Souce location
    3. Create a Map with Key : HashValue Value: Collection of Javascripts objects whcih has same `sha`
     Map<String,List<JavaScriptDetails>>
    4. now sort in reverse order the Map with custome comparator which sort values equal to size of above which looks like List<Map.Entry<String, Integer> >
    5. colllect the 1st 5 'sha' from the List
    6. Iterate through teh above list and match the hash against Map in step 3 and print all the information along with list size
    
 

### Reference Documentation
For further reference, please consider the following sections:
* [google Guava Hash](https://github.com/google/guava/wiki/HashingExplained)
* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/{bootVersion}/reference/htmlsingle/#using-boot-devtools)

