package org.fuc.entities;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@NamedQueries({
        @NamedQuery(name = City.ALL_CITIES, query = "select c from City c"),
        @NamedQuery(name = City.FIND_BY_ID, query = "select c from City c where c.id = :id")
})
@Table(name = "city")
public class City implements java.io.Serializable {
    public static final String ALL_CITIES = "City.allCities";
    public static final String FIND_BY_ID = "City.findById";

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
}
