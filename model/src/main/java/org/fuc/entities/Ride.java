package org.fuc.entities;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name = "ride")
@NamedQueries({
        @NamedQuery(name = Ride.FIND_BY_OWNER, query = "select r from Ride r where r.owner.id = :owner_id"),
        @NamedQuery(name = Ride.FIND_BY_ID, query = "select r from Ride r where r.id = :id")}
)
public class Ride {
    public static final String FIND_BY_OWNER = "Ride.findByOwner";
    public static final String FIND_BY_ID = "Ride.findById";

    private Long id;
    private City departure;
    private City arrival;
    private Date date;
    private Account owner;
    private Set<Account> participants;
    private Integer maxParticipants;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "DEPARTURE_ID", nullable = false)
    public City getDeparture() {
        return departure;
    }

    public void setDeparture(City departure) {
        this.departure = departure;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ARRIVAL_ID", nullable = false)
    public City getArrival() {
        return arrival;
    }

    public void setArrival(City arrival) {
        this.arrival = arrival;
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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "account_ride",
            joinColumns = {
                    @JoinColumn(name = "ride_id", nullable = false, updatable = false)
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "participant_id", nullable = false, updatable = false)
            })
    public Set<Account> getParticipants() {
        return participants;
    }

    public void setParticipants(Set<Account> participants) {
        this.participants = participants;
    }
}
