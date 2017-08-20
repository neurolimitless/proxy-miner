package util;

import analyzer.ProxyInfo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FileSaver {

  private String generateTimestamp() {
    DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd HH_mm_ss");
    Date date = new Date();
    return dateFormat.format(date);
  }

  public void saveToFile(Collection<String> ips) throws IOException {
    String filename = "proxy " + generateTimestamp() + ".txt";
    File file = new File(filename);
    FileWriter writer = new FileWriter(file, true);
    ips.forEach(line -> {
      try {
        writer.write(line + "\n");
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    writer.close();
    System.out.println("Successfully saved.");
  }

  public void saveToFile(List<ProxyInfo> proxyInfoList) {
    List<String> proxyToSave = proxyInfoList.stream().map(proxyInfo -> proxyInfo.getProxyHost() + ":" + proxyInfo.getPort()).collect(Collectors.toList());
    try {
      saveToFile(proxyToSave);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
