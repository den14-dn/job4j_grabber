package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, LocalDate> DAYS_OF_WEEK = Map.ofEntries(
            entry("вчера", LocalDate.now().minusDays(1)),
            entry("сегодня", LocalDate.now())
    );
    private static final Map<String, Integer> MONTHS = Map.ofEntries(
            entry("янв", 1),
            entry("фев", 2),
            entry("мар", 3),
            entry("апр", 4),
            entry("май", 5),
            entry("июн", 6),
            entry("июл", 7),
            entry("авг", 8),
            entry("сен", 9),
            entry("окт", 10),
            entry("ноя", 11),
            entry("дек", 12)
    );

    @Override
    public LocalDateTime parse(String parse) {
        parse = parse.replace(",", "");
        String[] arrayValues = parse.split(" ");

        LocalDate date = DAYS_OF_WEEK.get(arrayValues[0]);
        if (date == null) {
            date = LocalDate.of(
                    Integer.parseInt("20".concat(arrayValues[2])),
                    MONTHS.get(arrayValues[1]),
                    Integer.parseInt(arrayValues[0])
            );
        }

        String[] arrayTime = arrayValues[arrayValues.length - 1].split(":");
        LocalTime time = LocalTime.of(Integer.parseInt(arrayTime[0]), Integer.parseInt(arrayTime[1]));

        return LocalDateTime.of(date, time);
    }
}
