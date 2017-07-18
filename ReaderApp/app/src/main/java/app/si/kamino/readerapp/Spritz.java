package app.si.kamino.readerapp;

/**
 * Created by DenisK on 18. 07. 2017.
 * Algorithm from https://www.reddit.com/r/programming/comments/1zd1qr/speedread_a_commandline_implementation_of_the
 *
 * Spritz algorithm return pivot index from word.
 */

public class Spritz {
    public static int GetPivot(String word) {
        int pivot;
        word = word.replaceAll("\\s", "");
        switch (word.length()) {
            case 0:
            case 1:
                pivot = 0;
                break;
            case 2:
            case 3:
            case 4:
            case 5:
                pivot = 1;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
                pivot = 2;
                break;
            case 10:
            case 11:
            case 12:
            case 13:
                pivot = 3;
                break;
            default:
                pivot = 4;
        }
        return pivot;
    }
}
