import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class xkcdpwgen {
  static String modifiers;
  static int w, c, n, s;
  static ArrayList<String> words = new ArrayList<>();
  
  public static void main(String[] args) {
    try {
      BufferedReader reader = new BufferedReader(new FileReader("words.txt"));
      String word = reader.readLine();
      
      while (word != null) {
        words.add(word);
        word = reader.readLine();
      }
      
      reader.close();
    } catch(IOException error) {
      System.out.println("Error: " + error.getMessage());
    }
    
    if(args.length > 0) {
      modifiers = String.join(" ", args);
      
      if(modifiers.contains("-h") || modifiers.contains("--help")) {
        System.out.println("usage: xkcdpwgen [-h] [-w WORDS] [-c CAPS] [-n NUMBERS] [-s SYMBOLS]\r\n"
            + "                \r\n"
            + "Generate a secure, memorable password using the XKCD method\r\n"
            + "                \r\n"
            + "optional arguments:\r\n"
            + "    -h, --help            show this help message and exit\r\n"
            + "    -w WORDS, --words WORDS\r\n"
            + "                          include WORDS words in the password (default=4)\r\n"
            + "    -c CAPS, --caps CAPS  capitalize the first letter of CAPS random words\r\n"
            + "                          (default=0)\r\n"
            + "    -n NUMBERS, --numbers NUMBERS\r\n"
            + "                          insert NUMBERS random numbers in the password\r\n"
            + "                          (default=0)\r\n"
            + "    -s SYMBOLS, --symbols SYMBOLS\r\n"
            + "                          insert SYMBOLS random symbols in the password\r\n"
            + "                          (default=0)");
        System.exit(0);
      }
      
      if(modifiers.contains("--words")) {
        w = readModifier("--words");
      } else if(modifiers.contains("-w")) {
        w = readModifier("-w");
      } else {
        w = 4;
      }
      if(modifiers.contains("--caps")) {
        c = readModifier("--caps");
      } else if(modifiers.contains("-c")) {
        c = readModifier("-c");
      } else {
        c = -1;
      }
      if(modifiers.contains("--numbers")) {
        n = readModifier("--numbers");
      } else if(modifiers.contains("-n")) {
        n = readModifier("-n");
      } else {
        n = -1;
      }
      if(modifiers.contains("--symbols")) {
        s = readModifier("--symbols");
      } else if(modifiers.contains("-s")) {
        s = readModifier("-s");
      } else {
        s = -1;
      }
      
      System.out.println(createPassword(w, c, n, s, words));
    }
    System.out.println(createPassword(4, -1, -1, -1, words));
  }
  
  static int readModifier(String modifier) {
    String rest = modifiers.substring(modifiers.indexOf(modifier) + modifier.length() + 1);
    if (rest.contains(" ")) {
      return Integer.parseInt(rest.substring(0, rest.indexOf(" ")));
    }
    else {
      return Integer.parseInt(rest);
    }
  }
  static String createPassword(int w, int c, int n, int s, ArrayList<String> words) {
    String password = "";
    String symbols = "~!@#$%^&*.:;";
    Random rand = new Random(System.currentTimeMillis());
    
    for(int i = 0; i < w; i++) {
      String newWord = words.get(rand.nextInt(words.size()));
      
      if (c != -1 && rand.nextDouble() < (double)c/(w-i)) {
        newWord = newWord.substring(0,1).toUpperCase() + newWord.substring(1);    
        c--;
      }
      
      if (n != -1 && rand.nextDouble() < (double)n/(w-i)) {
        int num = rand.nextInt(10);
        if (rand.nextBoolean()) {
          newWord = num + newWord;
        } else {
          newWord = newWord + num;
        }
        n--;
      }
      
      if (s != -1 && rand.nextDouble() < (double)s/(w-i)) {
        int num = rand.nextInt(symbols.length());
        if (rand.nextBoolean()) {
          newWord = String.valueOf(symbols.charAt(num)) + newWord;
        } else {
          newWord = newWord + String.valueOf(symbols.charAt(num));    
        }
        s--;
      }
      
      password = password + newWord;
    }
    
    if (n > 0) {
      for(int i = 0; i < n; i++) {
        int num = rand.nextInt(10);
        if (rand.nextBoolean()) {
          password = password + num;
        } else {
          password = num + password;
        }
      }
    }
    
    if (s > 0) {
      for(int i = 0; i < s; i++) {
        int num = rand.nextInt(symbols.length());
        if (rand.nextBoolean()) {
          password = password + String.valueOf(symbols.charAt(num));
        } else {
          password = String.valueOf(symbols.charAt(num)) + password;
        }
      }
    }
    
    return password;
  }
}
