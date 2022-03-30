package reactor3.springwebflux.entity;

import lombok.Data;

@Data
public class Article {

    private String id;

    private String title;

    private String content;

    private String author;
}
