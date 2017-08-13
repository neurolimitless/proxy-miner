package analyzer;

public class ProxyInfo {
  private String proxyHost;
  private int port;
  private boolean isAlive;
  private long ping;

  public String getProxyHost() {
    return proxyHost;
  }

  public void setProxyHost(String proxyHost) {
    this.proxyHost = proxyHost;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public void setAlive(boolean alive) {
    isAlive = alive;
  }

  public long getPing() {
    return ping;
  }

  public void setPing(long ping) {
    this.ping = ping;
  }

  @Override
  public String toString() {
    return "ProxyInfo{" +
        "proxyHost='" + proxyHost + '\'' +
        ", port=" + port +
        ", isAlive=" + isAlive +
        ", ping=" + ping +
        '}';
  }
}
