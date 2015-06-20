package org.fuc.entities;

import javax.persistence.*;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "ride")
@NamedQueries({
        @NamedQuery(name = Ride.FIND_BY_OWNER,
                query = "select r from Ride r " +
                        "where r.owner.id = :owner_id " +
                        "and (select count(p) from r.participants p) < r.maxParticipants"),
        @NamedQuery(name = Ride.FIND_BY_ID, query = "select r from Ride r where r.id = :id"),
        @NamedQuery(name = Ride.FILTER_AVAILABLE_RIDES,
                query = "select r from Ride r " +
                        "where not exists (select req " +
                        "       from r.requests req " +
                        "       where req.owner.id = :beatnik_id) " +
                        "   and r.date > :date " +
                        "   and (select count(p) from r.participants p) < r.maxParticipants"),
        @NamedQuery(name = Ride.AVAILABLE_RIDES,
                query = "select r from Ride r " +
                        "where not exists (select req " +
                        "       from r.requests req " +
                        "       where req.owner.id = :beatnik_id) " +
                        "   and r.date > :date " +
                        "and (select count(p) from r.participants p) < r.maxParticipants")
})
public class Ride {
    public static final String FIND_BY_OWNER = "Ride.findByOwner";
    public static final String FIND_BY_ID = "Ride.findById";
    public static final String FILTER_AVAILABLE_RIDES = "Ride.filterAvailableRides";
    public static final String AVAILABLE_RIDES = "Ride.allAvailableRides";

    private Long id;
    private Date date;
    private Account owner;
    private Set<Account> participants = new HashSet<Account>();
    private Set<Request> requests = new HashSet<Request>();
    private List<RidePoint> points = new LinkedList<>();
    private Integer maxParticipants;

    public Ride() {
    }

    public Ride(Date date,
                Account owner,
                Integer maxParticipants) {
        this.date = date;
        this.owner = owner;
        this.maxParticipants = maxParticipants;
    }

    @Id
    @SequenceGenerator(name = "ride_id_seq", sequenceName = "ride_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_id_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "account_ride",
            joinColumns = @JoinColumn(name = "ride_id"),
            inverseJoinColumns = @JoinColumn(name = "participant_id"))
    public Set<Account> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Account> participants) {
        this.participants = participants;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "ride_id")
    public Set<Request> getRequests() {
        return requests;
    }

    public void setRequests(Set<Request> requests) {
        this.requests = requests;
    }

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE})
    @JoinColumn(name = "ride_id")
    @OrderBy("orderfield")
    public List<RidePoint> getPoints() {
        return points;
    }

    public void setPoints(List<RidePoint> points) {
        this.points = points;
    }

    public boolean hasVacantSeat() {
        return participants.size() < maxParticipants;
    }
}
