/*
 * StringUtils.java created on 16.06.2005 (c) alexander noehrer
 */

package at.jku.sea.cloud.utils;

import static java.util.Locale.ENGLISH;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * a collection of static methods that are related with strings
 * 
 * @author alexander noehrer
 */
public final class StringUtils {

  public static final String EMPTY_STRING = "";
  public static final String NEW_LINE = "\n";
  public static final String[] SIZE_UNITS = new String[] { "B", "KB", "MB", "GB", "TB" };
  public static final DecimalFormat FORMAT_2_DECIMAL_PLACES = new DecimalFormat("0.00");
  public static final DecimalFormat FORMAT_PERCENT = new DecimalFormat("0.00%");
  public static final DateFormat FORMAT_DATE = new SimpleDateFormat("dd.MM.yyyy");

  private static final DecimalFormat format = new DecimalFormat("0.00");
  private static final Date DATE_OBJECT = new Date();

  static {
    format.setMinimumIntegerDigits(1);
    format.setMaximumIntegerDigits(340);
    format.setMinimumFractionDigits(1);
    format.setMaximumFractionDigits(340);
  }

  /**
   * private constructor
   */
  private StringUtils() {
    throw new RuntimeException(this.getClass().getName() + ": call to the constructor makes no sense");
  }

  private static final int insert(final StringBuilder sb, final int index, final Object value, final int width, final boolean left, final char padding) {
    final int length = sb.length();
    if (width > 0) {
      int inserted = sb.length();
      sb.insert(index, value);
      inserted = sb.length() - inserted;
      if (left) {

        while (inserted < width) {
          sb.insert(index + inserted, padding);
          inserted++;
        }
      } else {
        while (inserted < width) {
          sb.insert(index, padding);
          inserted++;
        }
      }
    } else {
      sb.insert(index, value);
    }
    return (sb.length() - length);
  }

  public static final String getHexString(final Object o, final boolean stretch, final boolean uppercase) {
    String value = null;
    if (o instanceof Byte) {
      value = Integer.toHexString(0xff & (Byte) o);
    } else if (o instanceof Short) {
      value = Integer.toHexString((Short) o);
    } else if (o instanceof Integer) {
      value = Integer.toHexString((Integer) o);
    } else if (o instanceof Long) {
      value = Long.toHexString((Long) o);
    }
    if (value == null) {
      throw new IllegalArgumentException();
    }
    if (uppercase) {
      value = value.toUpperCase();
    }
    if (stretch) {
      value = "0x" + value;
    }
    return value;
  }

  public static final String getBinaryString(final Object o, final boolean stretch) {
    String value = null;
    if (o instanceof Byte) {
      value = Integer.toBinaryString(0xff & (Byte) o);
    } else if (o instanceof Short) {
      value = Integer.toBinaryString((Short) o);
    } else if (o instanceof Integer) {
      value = Integer.toBinaryString((Integer) o);
    } else if (o instanceof Long) {
      value = Long.toBinaryString((Long) o);
    }
    if (value == null) {
      throw new IllegalArgumentException();
    }
    if (stretch) {
      value = "#" + value;
    }
    return value;
  }

  public static final String getOctalString(final Object o, final boolean stretch) {
    String value = null;
    if (o instanceof Byte) {
      value = Integer.toOctalString(0xff & (Byte) o);
    } else if (o instanceof Short) {
      value = Integer.toOctalString((Short) o);
    } else if (o instanceof Integer) {
      value = Integer.toOctalString((Integer) o);
    } else if (o instanceof Long) {
      value = Long.toOctalString((Long) o);
    }
    if (value == null) {
      throw new IllegalArgumentException();
    }
    if (stretch) {
      value = "O" + value;
    }
    return value;
  }

  /**
   * @param formatString
   *          %[flags][width][.precision]specifier
   * @param objects
   * @return
   */
  public static final String printf(final String formatString, final Object... objects) {
    try {
      final StringBuilder sb = new StringBuilder(formatString);
      int argument = 0;
      int index = sb.indexOf("%");
      int size, width, insertedLength;
      boolean alignment;
      char flag, specifier, padding;
      boolean stretch;
      String temp;

      while (index >= 0) {
        insertedLength = 0;
        if (sb.charAt(index + 1) != '%') {
          alignment = false;
          size = 1;
          width = 0;
          specifier = ' ';
          padding = ' ';
          stretch = false;
          temp = null;

          flag = sb.charAt(index + 1);
          switch (flag) {
            case '-':
              alignment = true;
              break;
            case '0':
            case ' ':
              padding = flag;
              break;
            case '#':
              stretch = true;
              break;
            default:
              specifier = flag;
              break;
          }

          if (specifier == ' ') {
            size++;
            while (Character.isDigit(sb.charAt(index + size))) {
              size++;
            }
            temp = sb.substring(index + 2, index + size);
            if (temp.length() > 0) {
              width = Integer.valueOf(temp);
            }
            specifier = sb.charAt(index + size);
          }
          size++;
          sb.delete(index, index + size);
          switch (specifier) {
          // TODO add %a for arrays
            case 'i':
            case 'd':
            case 's':
              insertedLength = insert(sb, index, objects[argument], width, alignment, padding);
              break;
            case 'x':
            case 'X':
              insertedLength = insert(sb, index, getHexString(objects[argument], stretch, specifier == 'X'), width, alignment, padding);
              break;
            case 'b':
              insertedLength = insert(sb, index, getBinaryString(objects[argument], stretch), width, alignment, padding);
              break;
            case 'o':
              insertedLength = insert(sb, index, getOctalString(objects[argument], stretch), width, alignment, padding);
              break;
            case 'f':
              format.setDecimalSeparatorAlwaysShown(stretch);
              insertedLength = insert(sb, index, format.format(objects[argument]), width, alignment, padding);
              break;
            case 'e':
              format.setDecimalSeparatorAlwaysShown(stretch);
              format.applyPattern("0.0E0");
              insertedLength = insert(sb, index, format.format(objects[argument]).toLowerCase(), width, alignment, padding);
              break;
            case 'E':
              format.setDecimalSeparatorAlwaysShown(stretch);
              format.applyPattern("0.0E0");
              insertedLength = insert(sb, index, format.format(objects[argument]), width, alignment, padding);
              break;
            case 'D':
              DATE_OBJECT.setTime((Long) objects[argument]);
              insertedLength = insert(sb, index, FORMAT_DATE.format(DATE_OBJECT), width, alignment, padding);
              break;
            case 't':
              final Throwable throwable = (Throwable) objects[argument];
              if (throwable != null) {
                int oldLength = sb.length();
                sb.insert(index, throwable.getClass().getName());
                index += sb.length() - oldLength;
                oldLength = sb.length();
                sb.insert(index, ": ");
                index += sb.length() - oldLength;
                oldLength = sb.length();
                sb.insert(index, throwable.getMessage());
                index += sb.length() - oldLength;
                oldLength = sb.length();
                final StackTraceElement[] stackTrace = throwable.getStackTrace();
                for (final StackTraceElement element : stackTrace) {
                  sb.insert(index, "\n\tat ");
                  index += sb.length() - oldLength;
                  oldLength = sb.length();
                  sb.insert(index, element);
                  index += sb.length() - oldLength;
                  oldLength = sb.length();
                }
                sb.insert(index, '\n');
              }
              break;
            default:
              throw new IllegalArgumentException(specifier + " is not a vaild specifier");
          }
          argument++;
        } else {
          // delete escape character '%'
          sb.deleteCharAt(index);
          index++;
        }
        index = sb.indexOf("%", index + insertedLength);
      }
      return sb.toString();
    } catch (final Exception e) {
      return EMPTY_STRING;
    }
  }

  /**
   * converts an Integer value to a String with the specified number of digits
   * 
   * @param value
   *          the value that should be converted to a String with the specified number of digits
   * @param x
   *          defines the number of digits
   * @return a String that represents the assigned value
   * @throws ConstraintException
   *           if the value cannot be converted into a String with the specified number of digits
   */
  public static final String toXDigitString(final Integer value, final int x) {
    final StringBuilder sb = new StringBuilder(value.toString());
    while (sb.length() < x) {
      sb.insert(0, '0');
    }
    return sb.toString();
  }

  /**
   * expands a String to the designated length
   * 
   * @param string
   *          the String that should be expanded
   * @param x
   *          the number characters the new String should have
   * @return the expanded String
   * @throws ConstraintException
   */
  public static final String toXCharacterString(final String string, final int x) {
    return StringUtils.toXCharacterString(string, x, true, true);
  }

  /**
   * expands a String to the designated length
   * 
   * @param string
   *          the String that should be expanded
   * @param x
   *          the number characters the new String should have
   * @param alignment
   *          the Alignment the String should have
   * @return the expanded String
   * @throws ConstraintException
   */
  public static final String toXCharacterString(final String string, final int x, final boolean left) {
    return StringUtils.toXCharacterString(string, x, left, true);
  }

  /**
   * expands a String to the designated length
   * 
   * @param string
   *          the String that should be expanded
   * @param x
   *          the number characters the new String should have
   * @param alignment
   *          the Alignment the String should have
   * @param enableLogging
   *          boolean that indicates if logging should be enabled during the call
   * @return the expanded String
   * @throws ConstraintException
   */
  public static final String toXCharacterString(final String string, final int x, final boolean left, final boolean enableLogging) {
    final StringBuilder sb = new StringBuilder(string);
    if (left) {
      while (sb.length() < x) {
        sb.append(' ');
      }
    } else {
      while (sb.length() < x) {
        sb.insert(0, ' ');
      }
    }
    return sb.toString();
  }

  public static final String removeStrings(final String str) {
    if (str == null) {
      return null;
    }
    // "[^"\\\r\n]*(\\.[^"\\\r\n]*)*"
    return str.replaceAll("\"[^\"\\\\\\r\\n]*(\\\\.[^\"\\\\\\r\\n]*)*\"", "");
  }

  public static final String removeComments(final String str) {
    if (str == null) {
      return null;
    }
    String result = str.replaceAll("/\\*.*?\\*/", "");
    result = result.replaceAll("//.*$", "");
    return result;
  }

  public static final String removeWhitespacesInXML(final String str) {
    if (str == null) {
      return null;
    }
    // (?<=>)[ \t\r\n]+(?=<)
    return str.replaceAll("(?<=>)[ \\t\\r\\n]+(?=<)", "");
  }

  public static final String removeCommentsInXML(final String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("(?s)<!--.*?-->", "");
  }

  public static final String escapeSymbolsInStringToPrepareForXML(final String str) {
    if (str == null) {
      return null;
    }
    String temp = str.replaceAll("&", "&amp;");
    temp = temp.replaceAll("<", "&lt;");
    temp = temp.replaceAll(">", "&gt;");
    temp = temp.replaceAll("'", "&apos;");
    return temp.replaceAll("\"", "&quot;");
  }

  public static final String unescapeSymbolsOfXML(final String str) {
    if (str == null) {
      return null;
    }
    String temp = str.replaceAll("&lt;", "<");
    temp = temp.replaceAll("&gt;", ">");
    temp = temp.replaceAll("&apos;", "'");
    temp = temp.replaceAll("&#039;", "'");
    temp = temp.replaceAll("&#133;", "...");
    temp = temp.replaceAll("&#150;", "-");
    temp = temp.replaceAll("&quot;", "\"");
    return temp.replaceAll("&amp;", "&");
  }

  public static final String formatXML(final String str) {
    if (str == null) {
      return null;
    }
    final char identationChar = ' ';
    final int numberOfSpaces = 2;
    int identation = 0;
    final String[] tags = str.split("><");
    final StringBuilder sb = new StringBuilder();
    if (tags.length > 1) {
      sb.append(tags[0]);
      sb.append(">\n");
      for (int i = 1; i < (tags.length - 1); i++) {
        if (tags[i].startsWith("/") || tags[i].endsWith("/")) {
          identation -= numberOfSpaces;
        } else {
          identation += numberOfSpaces;
        }
        for (int j = 0; j < identation; j++) {
          sb.append(identationChar);
        }
        sb.append('<');
        sb.append(tags[i]);
        sb.append(">\n");
      }
      sb.append('<');
      sb.append(tags[tags.length - 1]);
    } else {
      sb.append(tags[0]);
    }

    return sb.toString();
  }

  /**
   * returns the assigned duration human readable
   * 
   * @param durationInMilliseconds
   *          the duration in milliseconds
   * @return a String that holds the assigned duration human readable
   */
  public static final String getHumanReadableTime(final long durationInMilliseconds) {
    final StringBuilder sb = new StringBuilder();
    long ms = durationInMilliseconds;
    final long days = ms / 86400000;
    ms -= days * 86400000;
    final long hours = ms / 3600000;
    ms -= hours * 3600000;
    final long minutes = ms / 60000;
    ms -= minutes * 60000;
    final long seconds = ms / 1000;
    ms -= seconds * 1000;
    if (days > 0) {
      sb.append(days);
      sb.append("d ");
    }
    if ((hours > 0) || (sb.length() > 0)) {
      sb.append(hours);
      sb.append("h ");
    }
    if ((minutes > 0) || (sb.length() > 0)) {
      sb.append(minutes);
      sb.append("m ");
    }
    if ((seconds > 0) || (sb.length() > 0)) {
      sb.append(seconds);
      sb.append("s ");
    }
    sb.append(ms);
    sb.append("ms");
    return sb.toString();
  }

  /**
   * returns the assigned size human readable
   * 
   * @param size
   *          the size in bytes
   * @return a String that holds the assigned size human readable
   */
  public static final String getHumanReadableSize(final long size) {
    if (size < 1024) {
      return size + SIZE_UNITS[0];
    }
    int unit = 0;
    double humanReadableSize = size;
    while ((unit < 5) && (humanReadableSize > 1024)) {
      humanReadableSize = humanReadableSize / 1024.0;
      unit++;
    }
    unit = Math.min(unit, 4);
    return FORMAT_2_DECIMAL_PLACES.format(humanReadableSize) + SIZE_UNITS[unit];
  }

  public static final String getHumanReadableTransferRate(final long size, final long duration) {
    return getHumanReadableSize(Math.round(size * (1000.0 / duration))) + "/s";
  }

  public static String replaceLinebreakLiterals(final String str) {
    if (str == null) {
      return null;
    }
    return str.replaceAll("\\\\n", "\n");
  }

  public static int getLevensteinDistance(final String s, final String t) {

    final int n = s.length();
    final int m = t.length();

    if (n == 0) {
      return m;
    } else if (m == 0) {
      return n;
    }

    int p[] = new int[n + 1]; // 'previous' cost array, horizontally
    int d[] = new int[n + 1]; // cost array, horizontally
    int _d[]; // placeholder to assist in swapping p and d

    // indexes into strings s and t
    int i; // iterates through s
    int j; // iterates through t

    char t_j; // jth character of t

    int cost; // cost

    for (i = 0; i <= n; i++) {
      p[i] = i;
    }

    for (j = 1; j <= m; j++) {
      t_j = t.charAt(j - 1);
      d[0] = j;

      for (i = 1; i <= n; i++) {
        cost = s.charAt(i - 1) == t_j ? 0 : 1;
        // minimum of cell to the left+1, to the top+1, diagonally left
        // and up +cost
        d[i] = Math.min(Math.min(d[i - 1] + 1, p[i] + 1), p[i - 1] + cost);
      }

      // copy current distance counts to 'previous row' distance counts
      _d = p;
      p = d;
      d = _d;
    }

    // our last action in the above loop was to switch d and p, so p now
    // actually has the most recent cost counts
    return p[n];
  }

  /**
   * Returns a String which capitalizes the first letter of the string.
   * 
   * @param string
   *          the String to capitalize
   * @return the capitalized String
   */
  public static String capitalize(final String string) {
    if ((string == null) || (string.length() == 0)) {
      return string;
    }
    return string.substring(0, 1).toUpperCase(ENGLISH) + string.substring(1);
  }

}
