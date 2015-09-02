package org.fuc.integration;

import org.fuc.commands.account.AccountCriteria;
import org.fuc.commands.city.CityCriteria;
import org.fuc.config.WebAppConfigurationAware;
import org.fuc.core.Command;
import org.fuc.entities.*;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RideService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import static org.assertj.core.api.Assertions.assertThat;

public class RideServiceOrderIT extends WebAppConfigurationAware {
    @Autowired
    @Qualifier("createAccountCommand")
    private Command createAccount;
    @Autowired
    @Qualifier("deleteAccountCommand")
    private Command deleteAccount;
    @Autowired
    private RidesRepository ridesRepository;
    @Autowired
    private RideService rideService;
    @Autowired
    @Qualifier("createCityCommand")
    private Command createCity;
    @Autowired
    @Qualifier("deleteCityCommand")
    private Command deleteCity;

    private City departure, arrival;
    private Ride ride;
    private Account owner;
    private Collection<Account> participants = new LinkedList<Account>();

    @Before
    public void setUp() {
        owner = new Account("owner@owner.com", "pwd", RoleProvider.ROLE_DRIVER);
        createAccount.execute(new AccountCriteria(owner));
        departure = new City("Departure");
        createCity.execute(new CityCriteria(departure));
        arrival = new City("Arrival");
        createCity.execute(new CityCriteria(arrival));
    }

    @Test
    public void shouldCreateRide() {
        ride = rideService.createRide(new Ride(new Date(), owner, 3), new City[] {departure, arrival});

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
        deleteCity.execute(new CityCriteria(departure));
        deleteCity.execute(new CityCriteria(arrival));
        deleteAccount.execute(new AccountCriteria(owner));
        for (Account account : participants) {
            deleteAccount.execute(new AccountCriteria(account));
        }
    }
}
