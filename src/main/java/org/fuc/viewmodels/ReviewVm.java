package org.fuc.viewmodels;

public class ReviewVm {
    private Long id;

    private String comment;

    private Integer rating;

    public ReviewVm() {
    }

    public ReviewVm(Long id, String comment, Integer rating) {
        this.id = id;
        this.comment = comment;
        this.rating = rating;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}
