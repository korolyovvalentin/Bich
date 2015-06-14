package org.fuc.entities;

import javax.persistence.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "ride_point")
public class RidePoint {
    private Long id;
    private Ride ride;
    private Integer orderField;
    private City city;

    public RidePoint() {
    }

    public RidePoint(Ride ride, Integer order, City city) {
        this.ride = ride;
        this.orderField = order;
        this.city = city;
    }

    @Id
    @SequenceGenerator(name = "ride_point_id_seq", sequenceName = "ride_point_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ride_point_id_seq")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "RIDE_ID")
    public Ride getRide() {
        return ride;
    }

    public void setRide(Ride ride) {
        this.ride = ride;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "CITY_ID", nullable = false)
    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Integer getOrderField() {
        return orderField;
    }

    public void setOrderField(Integer orderField) {
        this.orderField = orderField;
    }
}
