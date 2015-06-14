package org.fuc.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "place")
@NamedQueries({
        @NamedQuery(name = Place.FIND_BY_OWNER,
                query = "select p from Place p " +
                        "where p.owner.id = :owner_id "),
        @NamedQuery(name = Place.FIND_BY_CITY,
                query = "select p from Place p " +
                        "where p.city.id = :city_id ")
})
public class Place {
    public static final String FIND_BY_OWNER = "Place.findByOwner";
    public static final String FIND_BY_CITY = "Place.findByCity";

    @Id
    @SequenceGenerator(name = "place_id_seq", sequenceName = "place_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "place_id_seq")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Account owner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    private City city;

    @OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "place_id")
    private Set<PlaceRequest> requests = new HashSet<>();

    private String name;

    private String type;

    private String description;

    public Place() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Set<PlaceRequest> getRequests() {
        return requests;
    }

    public void setRequests(Set<PlaceRequest> requests) {
        this.requests = requests;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
