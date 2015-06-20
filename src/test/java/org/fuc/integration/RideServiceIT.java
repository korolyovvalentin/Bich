package org.fuc.integration;

import org.fuc.entities.RoleProvider;
import org.fuc.config.WebAppConfigurationAware;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
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

public class RideServiceIT extends WebAppConfigurationAware {
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
    public void shouldAddThreeParticipants() {
        int maxCount = 3;
        ride = createRide(maxCount);
        for (int i = 1; i <= maxCount; i++) {
            Account account = new Account("prt@prt.com" + i, "pwd", RoleProvider.ROLE_BEATNIK);
            participants.add(account);
            accountService.createAccount(account);
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
        citiesRepository.delete(departure);
        citiesRepository.delete(arrival);
        accountService.deleteAccount(owner);
        for (Account account : participants) {
            accountService.deleteAccount(account);
        }
    }
}
