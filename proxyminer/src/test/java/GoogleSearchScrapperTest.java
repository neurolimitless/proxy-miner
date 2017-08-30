import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;
import scrapper.GoogleSearchScrapper;
import scrapper.util.Constants;
import scrapper.util.Patterns;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.assertTrue;

public class GoogleSearchScrapperTest {

  private GoogleSearchScrapper googleSearchScrapper = new GoogleSearchScrapper();

  @Test
  public void parsePagesFromSearch() throws IOException {
    String query = Constants.DEFAULT_QUERY;
    List<String> pageUrlsByQuery = googleSearchScrapper.generatePageUrlsByQuery(query, 1);
    List<String> proxy = googleSearchScrapper.parsePageListByUrl(pageUrlsByQuery.get(0));
    assertTrue(proxy != null && proxy.size() == 10);
  }

  @Test
  public void extractProxyIpsFromPages() throws IOException {
    String query = Constants.DEFAULT_QUERY;
    List<String> pageUrlsByQuery = googleSearchScrapper.generatePageUrlsByQuery(query, 1);
    List<String> pages = googleSearchScrapper.parsePageListByUrl(pageUrlsByQuery.get(0));
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

  @Test
  public void patternTest() {
    String exampleIp = "172.21.33.241";
    String examplePort = "8080";
    Pattern portPattern = Patterns.IP_WITHOUT_PORT;
    Pattern ipWithoutPortPattern = Patterns.PORT;
    Matcher matcher = ipWithoutPortPattern.matcher(exampleIp);
    Matcher matcherTwo = portPattern.matcher(examplePort);
    assertTrue(matcher.matches());
    assertTrue(matcherTwo.matches());
  }

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
