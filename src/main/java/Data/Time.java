package Data;

import java.math.BigDecimal;

/**
 * Created by Алекс on 20.05.2015.
 */
public final class Time {

    public static String formatTime(long value) {
        value /= 1000;
        int hours = (int) value / 3600;
        int remainder = (int) value - hours * 3600;
        int minutes = remainder / 60;
        remainder = remainder - minutes * 60;
        int seconds = remainder;
        return String.format("%d:%02d:%02d", hours, minutes, seconds);
    }

    public static int getSeconds(long value) {

        double seconds = new BigDecimal(value / 1000).setScale(-1, BigDecimal.ROUND_HALF_UP).doubleValue();

        return (int) seconds;
    }
}
