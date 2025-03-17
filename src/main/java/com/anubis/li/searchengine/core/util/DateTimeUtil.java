package com.anubis.li.searchengine.core.util;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtil {
    private static final ZoneId LOCAL_ZONE_ID = ZoneId.systemDefault();
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static String toLocalDateTimeString(Object obj) {
        if (obj instanceof Date) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((Date) obj);
            if (calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0 && calendar.get(Calendar.SECOND) == 0) {
                // 如果时分秒都为0，则输出日期
                ZonedDateTime zdt = ((Date) obj).toInstant().atZone(LOCAL_ZONE_ID);
                return DATE_FORMATTER.format(zdt.toLocalDate());
            } else {
                // 否则，输出完整的日期时间
                ZonedDateTime zdt = ((Date) obj).toInstant().atZone(LOCAL_ZONE_ID);
                return DATE_TIME_FORMATTER.format(zdt.toLocalDateTime());
            }
        } else if (obj instanceof LocalDate) {
            return DATE_FORMATTER.format((LocalDate) obj);
        } else if (obj instanceof LocalDateTime) {
            return DATE_TIME_FORMATTER.format((LocalDateTime) obj);
        } else if (obj instanceof Timestamp) {
            ZonedDateTime zdt = ZonedDateTime.ofInstant(((Timestamp) obj).toInstant(), LOCAL_ZONE_ID);
            return DATE_TIME_FORMATTER.format(zdt.toLocalDateTime());
        } else if (obj instanceof Time) {
            LocalTime localTime = ((Time) obj).toLocalTime();
            return TIME_FORMATTER.format(localTime);
        } else {
            throw new IllegalArgumentException("Unsupported type: " + obj.getClass().getName());
        }
    }

}
