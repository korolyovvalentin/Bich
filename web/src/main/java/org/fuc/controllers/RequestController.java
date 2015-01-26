package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.Account;
import org.fuc.entities.Ride;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RequestsService;
import org.fuc.services.RideService;
import org.fuc.support.web.MessageHelper;
import org.fuc.viewmodels.Rides.RideVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/beatnik/requests")
@Secured("ROLE_BEATNIK")
public class RequestController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    private RidesRepository ridesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RideService rideService;
    @Autowired
    private RequestsService requestsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView availableRides(Principal principal){
        Account account = accountRepository.findByEmail(principal.getName());
        Collection<Ride> rides = rideService.getAvailableRides(account);
        Collection<RideVm> rideVms = new LinkedList<>();
        for (Ride ride : rides) {
            rideVms.add(mapper.map(ride, RideVm.class));
        }
        return new ModelAndView("requests/rides", "rides", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam("ride_id") Long rideId, Principal principal){
        Ride ride = ridesRepository.findById(rideId);
        Account beatnik = accountRepository.findByEmail(principal.getName());
        requestsService.create(ride, beatnik);
        ModelAndView model = new ModelAndView(new RedirectView("/beatnik/requests", false));
        MessageHelper.addSuccessAttribute(model, "Request was successfully added");
        return model;
    }
}
