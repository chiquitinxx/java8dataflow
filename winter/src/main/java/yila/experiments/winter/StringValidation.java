package yila.experiments.winter;

/**
 * JFL 17/11/18
 */
public class StringValidation {

    static boolean isNotEmpty(String string) {
        return string != null && !string.trim().isEmpty();
    }

    static boolean trimLengthAtLeast(String string, int lenght) {
        return isNotEmpty(string) && string.trim().length() >= lenght;
    }
}
