package org.fuc.integration;

import org.fuc.entities.*;
import org.fuc.config.WebAppConfigurationAware;
import org.fuc.repositories.CitiesRepository;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.AccountService;
import org.fuc.services.RideService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class RideServiceOrderIT extends WebAppConfigurationAware {
    @Autowired
    private AccountService accountService;
    @Autowired
    private RidesRepository ridesRepository;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private RideService rideService;

    private City departure, arrival;
    private Ride ride;
    private Account owner;
    private Collection<Account> participants = new LinkedList<Account>();

    @Before
    public void setUp() {
        owner = new Account("owner@owner.com", "pwd", RoleProvider.ROLE_DRIVER);
        accountService.createAccount(owner);
        departure = new City("Departure");
        citiesRepository.save(departure);
        arrival = new City("Arrival");
        citiesRepository.save(arrival);
    }

    @Test
    public void shouldCreateRide() {
        ride = rideService.createRide(new Ride(departure, arrival, new Date(), owner, 3), new City[] {departure, arrival});

        Ride freshRide = (Ride)ridesRepository.getRidesForOwner(owner).toArray()[0];
        System.out.println("Fresh Id: " + freshRide.getId());
        for (RidePoint rp : freshRide.getPoints()){
            System.out.println("Ridepoint Id: " + rp.getId());
            switch (rp.getCity().getName()){
                case "Departure": assertThat(rp.getOrderField()).isEqualTo(0); break;
                case "Arrival": assertThat(rp.getOrderField()).isEqualTo(1); break;
            }
        }
    }

    @After
    public void tearDown() {
        ridesRepository.delete(ride);
        citiesRepository.delete(departure);
        citiesRepository.delete(arrival);
        accountService.deleteAccount(owner);
        for (Account account : participants) {
            accountService.deleteAccount(account);
        }
    }
}
