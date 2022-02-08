package ru.job4j.grabber.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Map;

import static java.util.Map.entry;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final Map<String, String> MONTHS = Map.ofEntries(
            entry("янв", "01"),
            entry("фев", "02"),
            entry("мар", "03"),
            entry("апр", "04"),
            entry("май", "05"),
            entry("июн", "06"),
            entry("июл", "07"),
            entry("авг", "08"),
            entry("сен", "09"),
            entry("окт", "10"),
            entry("ноя", "11"),
            entry("дек", "12")
    );

    @Override
    public LocalDateTime parse(String parse) {
        if (parse.startsWith("сегодня")) {
            String[] arrayTime = parse.replace(" ", "").split(",")[1].split(":");
            LocalTime lt = LocalTime.of(
                    Integer.parseInt(arrayTime[0]),
                    Integer.parseInt(arrayTime[1])
            );
            return LocalDateTime.of(LocalDate.now(), lt);
        } else if (parse.startsWith("вчера")) {
            String[] arrayTime = parse.replace(" ", "").split(",")[1].split(":");
            LocalTime lt = LocalTime.of(
                    Integer.parseInt(arrayTime[0]),
                    Integer.parseInt(arrayTime[1])
            );
            return LocalDateTime.of(LocalDate.now().minusDays(1), lt);
        } else {
            String[] arrayDateTime = parse.split(" ");
            LocalDate ld = LocalDate.of(
                    Integer.parseInt("20".concat(arrayDateTime[2]).replace(",", "")),
                    Integer.parseInt(MONTHS.get(arrayDateTime[1])),
                    Integer.parseInt(arrayDateTime[0])
            );
            String[] arrayTime = arrayDateTime[3].split(":");
            LocalTime lt = LocalTime.of(
                    Integer.parseInt(arrayTime[0]),
                    Integer.parseInt(arrayTime[1])
            );
            return LocalDateTime.of(ld, lt);
        }
    }
}
