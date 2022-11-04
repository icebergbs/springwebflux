package reactor3.springwebflux.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class Article implements Serializable {

    private static final long serialVersionUID = 6228455867297081086L;
    private String id;

    private String title;

    private String content;

    private String author;

    @Override
    public String toString() {
        return "Article{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
