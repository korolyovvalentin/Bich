package org.fuc.integration;

import org.fuc.config.WebAppConfigurationAware;
import org.fuc.core.Command;
import org.fuc.core.Query;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.core.criterias.RidePointsCriteria;
import org.fuc.entities.*;
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
    private Command<Account> createAccount;
    @Autowired
    @Qualifier("deleteAccountCommand")
    private Command<Account> deleteAccount;

    @Autowired
    @Qualifier("ridesByDriverQuery")
    private Query<Ride> ridesByOwnerQuery;
    @Autowired
    @Qualifier("deleteRideCommand")
    private Command<Ride> deleteRide;
    @Autowired
    @Qualifier("createRideCommand")
    private Command<RidePointsCriteria> createRide;

    @Autowired
    @Qualifier("createCityCommand")
    private Command<City> createCity;
    @Autowired
    @Qualifier("deleteCityCommand")
    private Command<City> deleteCity;

    private City departure, arrival;
    private Ride ride;
    private Account owner;
    private Collection<Account> participants = new LinkedList<Account>();

    @Before
    public void setUp() {
        owner = new Account("owner@owner.com", "pwd", RoleProvider.ROLE_DRIVER);
        createAccount.execute(owner);
        departure = new City("Departure");
        createCity.execute(departure);
        arrival = new City("Arrival");
        createCity.execute(arrival);
        ride = new Ride(new Date(), owner, 3);
    }

    @Test
    public void shouldCreateRide() {
        createRide.execute(new RidePointsCriteria(ride, new City[]{departure, arrival}));

        Ride freshRide = (Ride) ridesByOwnerQuery.query(new AccountCriteria(owner)).toArray()[0];
        System.out.println("Fresh Id: " + freshRide.getId());
        for (RidePoint rp : freshRide.getPoints()) {
            System.out.println("Ridepoint Id: " + rp.getId());
            switch (rp.getCity().getName()) {
                case "Departure":
                    assertThat(rp.getOrderField()).isEqualTo(0);
                    break;
                case "Arrival":
                    assertThat(rp.getOrderField()).isEqualTo(1);
                    break;
            }
        }
    }

    @After
    public void tearDown() {
        deleteRide.execute(ride);
        deleteCity.execute(departure);
        deleteCity.execute(arrival);
        deleteAccount.execute(owner);
        for (Account account : participants) {
            deleteAccount.execute(account);
        }
    }
}
