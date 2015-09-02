package org.fuc.integration;

import org.fuc.config.WebAppConfigurationAware;
import org.fuc.core.Command;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RoleProvider;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RideService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

public class RideServiceIT extends WebAppConfigurationAware {
    @Autowired
    @Qualifier("createAccountCommand")
    private Command<Account> createAccount;
    @Autowired
    @Qualifier("deleteAccountCommand")
    private Command<Account> deleteAccount;
    @Autowired
    private RidesRepository ridesRepository;
    @Autowired
    private RideService rideService;
    @Autowired
    @Qualifier("createCityCommand")
    private Command<City> createCity;
    @Autowired
    @Qualifier("deleteCityCommand")
    private Command<City> deleteCity;

    private City departure, arrival;
    private Ride ride;
    private Account owner;
    private Collection<Account> participants = new LinkedList<>();

    @Before
    public void setUp() {
        owner = new Account("owner@owner.com", "pwd", RoleProvider.ROLE_DRIVER);
        createAccount.execute(owner);
        departure = new City("Departure");
        createCity.execute(departure);
        arrival = new City("Arrival");
        createCity.execute(arrival);
    }

    @Test
    public void shouldAddThreeParticipants() {
        int maxCount = 3;
        ride = createRide(maxCount);
        for (int i = 1; i <= maxCount; i++) {
            Account account = new Account("prt@prt.com" + i, "pwd", RoleProvider.ROLE_BEATNIK);
            participants.add(account);
            createAccount.execute(account);
            rideService.addParticipant(ride, account);
            Ride testRide = ridesRepository.findById(ride.getId());
            Assert.assertEquals("Participant was not persist", i, testRide.getParticipants().size());
        }
    }

    private Ride createRide(Integer maxCount) {
        Ride ride = new Ride(new Date(), owner, maxCount);
        ride = rideService.createRide(ride);
        Collection<Ride> rides = ridesRepository.getRidesForOwner(owner);
        Assert.assertEquals(rides.size(), 1);
        return ride;
    }

    @After
    public void tearDown() {
        ridesRepository.delete(ride);
        deleteCity.execute(departure);
        deleteCity.execute(arrival);
        deleteAccount.execute(owner);
        for (Account account : participants) {
            deleteAccount.execute(account);
        }
    }
}
