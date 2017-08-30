package scrapper;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GoogleSearchScrapper implements WebScrapper {

  private PageScrapper pageScrapper = new PageScrapper();
  private static final Logger log = Logger.getLogger(GoogleSearchScrapper.class);

  public List<String> parsePageListByUrl(String url) {
    List<String> pages = null;
    try {
      Document searchPage = Jsoup.connect(url).get();
      pages = searchPage.getElementsByClass("rc").stream()
          .map(element -> element.getElementsByTag("a").attr("href"))
          .collect(Collectors.toList());
      log.info("Extracted " + pages.size() + " from query search: '" + url + "'");
    } catch (IOException e) {
      log.warn(e);
    }
    return pages;
  }

  public List<String> generatePageUrlsByQuery(String query, int pageCount) {
    String searchQuery = formatQuery(query);
    List<String> pageUrls = new ArrayList<>();
    StringBuilder pageUrlBuilder;
    for (int i = 0; i < pageCount; i++) {
      pageUrlBuilder = new StringBuilder("https://www.google.com/search?q=").append(searchQuery);
      if (i != 0) {
        pageUrlBuilder.append("&start=").append(i * 10);
      }
      pageUrls.add(pageUrlBuilder.toString());
    }
    return pageUrls;
  }

  private String formatQuery(String searchQuery){
    return searchQuery.replace(" ","%20");
  }

  public List<String> extractProxyIpsFromPage(Document document){
    return pageScrapper.parsePageTables(document);
  }

}
