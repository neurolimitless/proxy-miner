package scrapper;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSearchScrapper implements WebScrapper {

  private PageScrapper pageScrapper = new PageScrapper();
  private static final Logger log = Logger.getLogger(GoogleSearchScrapper.class);

  public List<String> parsePageListByQuery(String searchQuery) throws IOException {
    String query = formatQuery(searchQuery);
    Document searchPage = Jsoup.connect("https://www.google.com/search?q="+query).get();
    List<String> pages = searchPage.getElementsByClass("rc").stream()
        .map(element -> element.getElementsByTag("a").attr("href"))
        .collect(Collectors.toList());
    log.info("Extracted " + pages.size() + " from query search: '" + searchQuery + "'");
    return pages;
  }

  private String formatQuery(String searchQuery){
    return searchQuery.replace(" ","%20");
  }

  public List<String> extractProxyIpsFromPage(Document document){
    return pageScrapper.parsePageTables(document);
  }

}
