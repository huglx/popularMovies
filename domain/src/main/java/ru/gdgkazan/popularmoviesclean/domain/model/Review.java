package ru.gdgkazan.popularmoviesclean.domain.model;

/**
 * @author Artur Vasilov
 */
public class Review {

    private String mAuthor;
    private String mContent;

    public Review() {
    }

    public Review(String mAuthor, String mContent) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
