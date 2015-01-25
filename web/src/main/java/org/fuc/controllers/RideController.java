package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.CitiesRepository;
import org.fuc.services.RideService;
import org.fuc.viewmodels.Rides.RideCreateVm;
import org.fuc.viewmodels.Rides.RideVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/driver/rides")
@Secured("ROLE_DRIVER")
public class RideController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RideService rideService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView listCities(Principal principal) {
        Account driver = accountRepository.findByEmail(principal.getName());
        Collection<Ride> rides = rideService.getDriverRides(driver);
        Collection<RideVm> rideVms = new LinkedList<>();
        for (Ride ride : rides) {
            rideVms.add(mapper.map(ride, RideVm.class));
        }
        return new ModelAndView("rides/index", "rides", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        Collection<City> cities = citiesRepository.getCities();
        RideCreateVm model = new RideCreateVm();
        model.setAvailableCities(cities);
        return new ModelAndView("rides/create", "ride", model);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("ride") RideCreateVm model, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("rides/create");
        }
        Ride ride = mapper.map(model, Ride.class);
        ride.setOwner(accountRepository.findByEmail(principal.getName()));
        rideService.createRide(ride);
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }
}
