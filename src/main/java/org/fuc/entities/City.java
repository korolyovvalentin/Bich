package org.fuc.entities;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
        @NamedQuery(name = City.ALL_CITIES, query = "select c from City c"),
        @NamedQuery(name = City.FIND_BY_ID, query = "select c from City c where c.id = :id"),
        @NamedQuery(name = City.FIND_BY_NAME, query = "select c from City c where c.name = :name")
})
@Table(name = "city")
public class City implements java.io.Serializable {
    public static final String ALL_CITIES = "City.allCities";
    public static final String FIND_BY_ID = "City.findById";
    public static final String FIND_BY_NAME = "City.findByName";

    public City() {
    }

    public City(String name) {
        this.name = name;
    }

    public City(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Id
    @SequenceGenerator(name = "city_id_seq", sequenceName = "city_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "city_id_seq")
    private Long id;

    @Column(unique = true)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long cityId) {
        this.id = cityId;
    }

    @Override
    public boolean equals(Object obj) {
        City c = (City) obj;
        return this.id == c.id || this.name.equals(c.name);
    }
}
