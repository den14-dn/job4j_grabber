package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.time.LocalDateTime;

public class SqlRuParse {
    private static final int COUNT_PAGES = 5;

    public static void main(String[] args) throws Exception {
        SqlRuParse parser = new SqlRuParse();
        for (int pageNumber = 1; pageNumber <= COUNT_PAGES; pageNumber++) {
            parser.parseURL("https://www.sql.ru/forum/job-offers/" + pageNumber);
        }
    }

    private void parseURL(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element parent = td.parent();
            LocalDateTime ldt = new SqlRuDateTimeParser().parse(parent.child(5).text());
            Element href = td.child(0);
            System.out.printf("%s - %s - '%s'", ldt, href.attr("href"), href.text());
            System.out.println(System.lineSeparator());
        }
    }
}
