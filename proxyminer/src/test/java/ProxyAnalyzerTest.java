import analyzer.ProxyAnalyzer;
import analyzer.ProxyInfo;
import org.junit.Test;

public class ProxyAnalyzerTest {

  ProxyAnalyzer proxyAnalyzer = new ProxyAnalyzer();

  @Test
  public void checkProxy(){
    String proxy = "66.158.211.165:35923";
    ProxyInfo proxyInfo = proxyAnalyzer.checkProxy(proxy);
    System.out.println(proxyInfo);
  }

}
