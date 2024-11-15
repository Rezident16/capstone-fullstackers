package stocks.domain;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Validations {

    public static boolean isNullOrBlank(String value) {
        return value == null || value.isBlank();
    }
}
