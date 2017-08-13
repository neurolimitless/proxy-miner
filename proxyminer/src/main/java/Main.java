import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapper.GoogleSearchScrapper;
import scrapper.util.Constants;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

  private static FileSaver fileSaver = new FileSaver();
  private static GoogleSearchScrapper proxyScrapper = new GoogleSearchScrapper();

  public static void main(String[] args) throws IOException {
    List<String> proxyPages = proxyScrapper.parsePageListByQuery("proxy list");
    Set<String> parsedIps = new HashSet<>();
    proxyPages.forEach(pageUrl -> {
      Document document = null;
      System.out.println("Downloading page: " + pageUrl);
      try {
        document = Jsoup.connect(pageUrl).userAgent(Constants.USER_AGENT).get();
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println("Extracting IP's...");
      List<String> ips = proxyScrapper.extractProxyIpsFromPage(document);
      parsedIps.addAll(ips);
    });
    parsedIps.forEach(System.out::println);
    System.out.println(parsedIps.size() + " proxies totally parsed.");
    fileSaver.saveToFile(parsedIps);
  }
}
