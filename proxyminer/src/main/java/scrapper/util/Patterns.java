package scrapper.util;

import java.util.regex.Pattern;

public class Patterns {
  public static final Pattern IP_WITHOUT_PORT = Pattern.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
  public static final Pattern PORT = Pattern.compile("^(\\d{2,})$");
}
