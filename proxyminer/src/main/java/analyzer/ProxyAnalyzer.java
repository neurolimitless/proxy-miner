package analyzer;

import org.apache.log4j.Logger;
import scrapper.util.Constants;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ProxyAnalyzer {

  private static final Logger log = Logger.getLogger(ProxyAnalyzer.class);
  private String checkUrl;

  public ProxyAnalyzer() {
    checkUrl = Constants.GOOGLE_CHECK_URL;
  }

  public ProxyAnalyzer(String checkUrl) {
    this.checkUrl = checkUrl;
  }

  public String getCheckUrl() {
    return checkUrl;
  }

  public void setCheckUrl(String checkUrl) {
    this.checkUrl = checkUrl;
  }

  public ProxyInfo checkProxy(String proxyIp) {
    String[] proxyData = parseProxyString(proxyIp);
    String host = proxyData[0];
    int port = Integer.parseInt(proxyData[1]);
    Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
    try {
      URL url = new URL(checkUrl);
      long timestamp = System.currentTimeMillis();
      URLConnection http = url.openConnection(proxy);
      http.connect();
      long ping = System.currentTimeMillis() - timestamp;
      ProxyInfo proxyInfo = new ProxyInfo();
      proxyInfo.setAlive(true);
      proxyInfo.setProxyHost(host);
      proxyInfo.setPort(port);
      proxyInfo.setPing(ping);
      return proxyInfo;
    } catch (IOException e) {
      log.error(e.getMessage() + " " + host + ":" + port);
      return null;
    }
  }

  private String[] parseProxyString(String proxyString) {
    return proxyString.split(":");
  }
}
