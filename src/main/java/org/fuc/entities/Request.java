package org.fuc.entities;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "request")
@NamedQueries({
        @NamedQuery(name = Request.FIND_BY_RIDE, query = "select req from Request req where req.ride.id = :ride_id and req.status = :status"),
        @NamedQuery(name = Request.FIND_UPDATED,
                query = "select req " +
                        "from Request req " +
                        "where req.owner.id = :beatnik_id " +
                        "and (lower(req.status) = 'approved' " +
                        "     or lower(req.status) = 'declined')")
})
public class Request {
    public static final String FIND_BY_RIDE = "Request.findByRide";
    public static final String FIND_UPDATED = "Request.findUpdated";

    @Id
    @SequenceGenerator(name = "request_id_seq", sequenceName = "request_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "request_id_seq")
    private Long id;

    private Integer rating;

    private String status;

    private String comment;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RIDE_ID", nullable = false)
    private Ride ride;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Account owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PATH_REQUEST_ID", nullable = false)
    private PathRequest pathRequest;

    public Request() {
    }

    public Request(String status, Ride ride, Account owner) {
        this.status = status;
        this.ride = ride;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public PathRequest getPathRequest() {
        return pathRequest;
    }

    public void setPathRequest(PathRequest pathRequest) {
        this.pathRequest = pathRequest;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
