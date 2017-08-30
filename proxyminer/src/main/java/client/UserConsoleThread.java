package client;

import analyzer.ProxyInfo;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class UserConsoleThread extends Thread {

  private Scanner scanner = new Scanner(System.in);
  private static final Logger log = Logger.getLogger(UserConsoleThread.class);
  private volatile boolean isRunning = true;

  @Override
  public void run() {
    while (isRunning) {
      String input = scanner.nextLine();
      if (input != null) {
        executeCommand(input);
      }
    }
  }

  private void executeCommand(String command) {
    switch (command.toLowerCase()) {
      case "save":
        saveProxies();
        break;
      case "":
        isRunning = false;
        break;
      case "save asc":
        saveProxiesAscendingPing();
        break;
    }
  }

  private void saveProxiesAscendingPing() {
    List<ProxyInfo> proxyInfoList = new ArrayList<>(ApplicationFacade.getAliveProxies().size());
    proxyInfoList.addAll(ApplicationFacade.getAliveProxies());
    proxyInfoList.sort(Comparator.comparing(ProxyInfo::getPing));
    ApplicationFacade.fileSaver.saveToFile(proxyInfoList);
  }

  private void saveProxies() {
    if (ApplicationFacade.getAliveProxies().size() != 0)
      ApplicationFacade.fileSaver.saveToFile(ApplicationFacade.getAliveProxies());
    else
      log.warn("Can't save the proxies. List is empty!");
  }
}
