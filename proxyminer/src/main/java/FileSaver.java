import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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

}
