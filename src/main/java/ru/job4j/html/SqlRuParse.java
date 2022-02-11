package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.time.LocalDateTime;

public class SqlRuParse {
    private static final int COUNT_PAGES = 5;
    private static final String URL_SITE = "https://www.sql.ru/forum/job-offers/";

    public static void main(String[] args) throws Exception {
        SqlRuParse parser = new SqlRuParse();
        for (int pageNumber = 1; pageNumber <= parser.COUNT_PAGES; pageNumber++) {
            parser.parseURL(parser.URL_SITE + pageNumber);
        }
    }

    private void parseURL(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        Elements row = doc.select(".postslisttopic");
        for (Element td : row) {
            Element parent = td.parent();
            LocalDateTime created = new SqlRuDateTimeParser().parse(parent.child(5).text());
            Element href = td.child(0);
            String link = href.attr("href");
            System.out.printf("%s - %s - '%s'", created, link, href.text());
            System.out.println(System.lineSeparator());

            Post post = getDataPost(link);
            System.out.println(post.toString());
            System.out.println(System.lineSeparator());
        }
    }

    private Post getDataPost(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();
        String title = doc.title();
        String stringDate = doc.select(".msgFooter").get(0).text();
        LocalDateTime created = new SqlRuDateTimeParser().parse(stringDate.substring(0, stringDate.indexOf('[')));
        String description = doc.select(".msgBody").get(1).text();
        return new Post(title, url, description, created);
    }
}
