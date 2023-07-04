package club.aurorapvp.aurorachat.util;

public class StringUtil {
  public static double similarity(String s1, String s2) {
    s1 = s1.toLowerCase();
    s2 = s2.toLowerCase();

    int[][] distance = new int[s1.length() + 1][s2.length() + 1];

    for (int i = 0; i <= s1.length(); i++) {
      distance[i][0] = i;
    }
    for (int j = 0; j <= s2.length(); j++) {
      distance[0][j] = j;
    }

    for (int i = 1; i <= s1.length(); i++) {
      for (int j = 1; j <= s2.length(); j++) {
        if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
          distance[i][j] = distance[i - 1][j - 1];
        } else {
          distance[i][j] = Math.min(distance[i - 1][j] + 1,
              Math.min(distance[i][j - 1] + 1,
                  distance[i - 1][j - 1] + 1));
        }
      }
    }

    double maxLength = Math.max(s1.length(), s2.length());
    return 1 - ((double) distance[s1.length()][s2.length()] / maxLength);
  }
}
