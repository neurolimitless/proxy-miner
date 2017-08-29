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

public class ApplicationFacade {

  protected static FileSaver fileSaver = new FileSaver();
  protected static List<ProxyInfo> aliveProxies = new CopyOnWriteArrayList<>();
  private static GoogleSearchScrapper proxyScrapper = new GoogleSearchScrapper();
  private static ProxyAnalyzer proxyAnalyzer = new ProxyAnalyzer();
  private static final Logger log = Logger.getLogger(ApplicationFacade.class);

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
