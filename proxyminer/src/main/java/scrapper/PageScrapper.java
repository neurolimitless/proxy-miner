package scrapper;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import scrapper.util.Patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PageScrapper {

  public List<String> parsePageTables(Document document) {
    Elements rows = document.getElementsByTag("tr");
    List<String> proxyIps = new ArrayList<>();
    rows.forEach(elements -> {
      List<Elements> rowValues = rows.stream()
          .map(row -> row.getElementsByTag("td"))
          .collect(Collectors.toList());
      for (Elements rowElements : rowValues) {
        List<String> valueList = rowElements.stream().map(Element::html).collect(Collectors.toList());
        String ip = valueList.stream().filter(Patterns.IP_WITHOUT_PORT.asPredicate()).findFirst().orElse(null);
        String port = valueList.stream().filter(Patterns.PORT.asPredicate()).findFirst().orElse(null);
        if (ip != null && port != null) {
          proxyIps.add(ip + ":" + port);
        }
      }
    });
    return proxyIps;
  }
}
