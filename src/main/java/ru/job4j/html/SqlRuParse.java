package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.time.LocalDateTime;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element parent = td.parent();
            try {
                LocalDateTime ldt = new SqlRuDateTimeParser().parse(parent.child(5).text());
                System.out.println(ldt);
            } catch (Exception e) {
                System.out.println(parent.child(5).text());
            }
        }
    }
}
