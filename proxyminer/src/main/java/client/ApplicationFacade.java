package client;

import analyzer.ProxyAnalyzer;
import analyzer.ProxyInfo;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import scrapper.GoogleSearchScrapper;
import scrapper.util.Constants;
import util.FileSaver;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class ApplicationFacade {

  private static final Logger log = Logger.getLogger(ApplicationFacade.class);

  protected static List<ProxyInfo> aliveProxies = new CopyOnWriteArrayList<>();
  protected static FileSaver fileSaver = new FileSaver();
  private static GoogleSearchScrapper googleSearchScrapper = new GoogleSearchScrapper();
  private static ProxyAnalyzer proxyAnalyzer = new ProxyAnalyzer();

  public static void main(String[] args) throws IOException {
    String query = Constants.DEFAULT_QUERY;
    int pageCount = Constants.DEFAULT_PAGE_COUNT;
    if (args.length == 2) {
      try {
        pageCount = Integer.parseInt(args[1]);
        query = args[0];
      } catch (NumberFormatException e) {
        log.warn("Second argument is invalid number [" + args[1] + "], using default values.");
      }
    } else {
      log.info("Using default values. Query: '" + query + "', page count to parse = " + pageCount);
    }
    List<String> pageUrlsByQuery = googleSearchScrapper.generatePageUrlsByQuery(query, pageCount);
    List<String> proxyPages = pageUrlsByQuery.stream()
        .map(url -> googleSearchScrapper.parsePageListByUrl(url))
        .flatMap(List::stream).collect(Collectors.toList());
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
      List<String> ips = googleSearchScrapper.extractProxyIpsFromPage(document);
      parsedIps.addAll(ips);
    });
    log.info("Checking for alive proxies.");
    System.err.println("Available commands: 'save', 'save asc' ");
    new UserConsoleThread().start();
    parsedIps.parallelStream().forEach(proxy -> {
      ProxyInfo checkedProxy = proxyAnalyzer.checkProxy(proxy);
      if (checkedProxy != null && checkedProxy.isAlive()) {
        aliveProxies.add(checkedProxy);
        System.out.println(checkedProxy);
      }
    });
    System.out.println(parsedIps.size() + " proxies totally parsed.");
    System.out.println(aliveProxies.size() + " proxies are alive.");
    fileSaver.saveToFile(aliveProxies);
    System.out.println("Successfully saved to file.");
  }

  public static List<ProxyInfo> getAliveProxies() {
    return aliveProxies;
  }
}
