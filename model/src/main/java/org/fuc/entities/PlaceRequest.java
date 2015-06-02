package org.fuc.entities;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "placeRequest")
public class PlaceRequest {
    @Id
    @SequenceGenerator(name = "place_request_id_seq", sequenceName = "place_request_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_request_id_seq")
    private Long id;

    private Integer rating;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PLACE_ID", nullable = false)
    private Place place;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Account owner;

    public PlaceRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }
}
