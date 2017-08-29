import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import scrapper.GoogleSearchScrapper;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class GoogleSearchScrapperTest {

  private GoogleSearchScrapper googleSearchScrapper = new GoogleSearchScrapper();

  @Test
  public void parsePagesFromSearch() throws IOException {
    List<String> proxy = googleSearchScrapper.parsePageListByQuery("proxy list");
    assertTrue(proxy != null && proxy.size() == 10);
  }

  @Test
  public void extractProxyIpsFromPages() throws IOException {
    List<String> pages = googleSearchScrapper.parsePageListByQuery("proxy list");
    pages.parallelStream().forEach(page -> {
      try {
        System.out.println(page);
        Document document = Jsoup.connect(page).get();
        List<String> ips = googleSearchScrapper.extractProxyIpsFromPage(document);
        System.out.println(page + " parsed ip's: " + ips.size());
        ips.forEach(System.out::println);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
  }

  //TODO: Write a valid test without hardcoded proxy.
//  @Test
//  public void patternTest(){
//    Pattern portPattern = Pattern.compile("^(\\d{2,})$");
//    Pattern ipWithoutPortPattern = Pattern.compile(  "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
//    Matcher matcher = ipWithoutPortPattern.matcher("172.21.33.241");
//    Matcher matcherTwo = portPattern.matcher("8080");
//    assertTrue(matcher.matches());
//    assertTrue(matcherTwo.matches());
//  }

  @Test
  public void pageTableScrapping() throws IOException {
    String page = "https://www.us-proxy.org/";
    Document document = Jsoup.connect(page).userAgent("Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.4 Safari/537.36").get();
    List<String> ips = googleSearchScrapper.extractProxyIpsFromPage(document);
    System.out.println(page + " parsed ip's: " + ips.size());
    ips.forEach(System.out::println);
  }

  @Test
  public void generatePageUrlsByQuery() {
    String query = "proxy list";
    List<String> pageUrlsByQuery = googleSearchScrapper.generatePageUrlsByQuery(query, 2);
    assertTrue(pageUrlsByQuery.get(0).equals("https://www.google.com/search?q=proxy%20list"));
    assertTrue(pageUrlsByQuery.get(1).equals("https://www.google.com/search?q=proxy%20list&start=10"));
  }
}
