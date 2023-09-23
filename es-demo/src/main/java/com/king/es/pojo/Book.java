package com.king.es.pojo;

/***
 * ClassName: Book
 * Package: com.king.es.pojo
 * @author GK
 * @date 2023/9/23 16:12
 * @description
 * @version 1.0
 */
public class Book {
    private Long id;
    private String title;
    private String content;

    public Book(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public Book() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
