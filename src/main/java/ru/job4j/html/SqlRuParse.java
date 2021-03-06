package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.Parse;
import ru.job4j.grabber.Post;
import ru.job4j.grabber.PsqlStore;
import ru.job4j.grabber.Store;
import ru.job4j.grabber.utils.DateTimeParser;
import ru.job4j.grabber.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class SqlRuParse implements Parse {
    private static final int COUNT_PAGES = 1;
    private static final String URL_SITE = "https://www.sql.ru/forum/job-offers/";
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    public static void main(String[] args) {
        Parse parse = new SqlRuParse(new SqlRuDateTimeParser());
        List<Post> list = parse.list(URL_SITE);
        try (InputStream in = SqlRuParse.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            Properties cfg = new Properties();
            cfg.load(in);
            Store store = new PsqlStore(cfg);
            for (Post el : list) {
                store.save(el);
            }
            List<Post> findList = store.getAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        for (int pageNumber = 1; pageNumber <= COUNT_PAGES; pageNumber++) {
            try {
                Document doc = Jsoup.connect(link + pageNumber).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    if (!td.child(0).text().toLowerCase().contains("java")) {
                        continue;
                    }
                    list.add(detail(td.child(0).attr("href")));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public Post detail(String link) {
        Post post = null;
        try {
            Document doc = Jsoup.connect(link).get();
            String title = doc.title();
            String stringDate = doc.select(".msgFooter").get(0).text();
            LocalDateTime created = dateTimeParser.parse(stringDate.substring(0, stringDate.indexOf('[')));
            String description = doc.select(".msgBody").get(1).text();
            post = new Post(title, link, description, created);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }
}
