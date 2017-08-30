package scrapper;

import java.io.IOException;
import java.util.List;

public interface WebScrapper {
  List<String> parsePageListByUrl(String url) throws IOException;
}
