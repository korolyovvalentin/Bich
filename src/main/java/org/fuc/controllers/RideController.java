package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.entities.*;
import org.fuc.queries.account.EmailCriteria;
import org.fuc.services.RequestsService;
import org.fuc.services.RideService;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.Rides.RideCreateVm;
import org.fuc.viewmodels.Rides.RideVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    @Qualifier("allCitiesQuery")
    private Query<City> citiesProvider;
    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;
    @Autowired
    private RideService rideService;
    @Autowired
    private RequestsService requestsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView listRides(Principal principal) {
        Account driver = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Ride> rides = rideService.getDriverRides(driver);
        Collection<RideVm> rideVms = new LinkedList<>();
        for (Ride ride : rides) {
            RideVm rideVm = mapper.map(ride, RideVm.class);

            for (RidePoint rp : ride.getPoints()) {
                rideVm.getCities().add(rp.getCity());
            }
            rideVms.add(rideVm);
        }
        return new ModelAndView("rides/index", "rides", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        Collection<City> cities = citiesProvider.query(Criteria.empty());
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
        ride.setOwner(findAccountByEmail.query(new EmailCriteria(principal.getName())));
        rideService.createRide(ride, model.getCities());
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }

    @RequestMapping(value = " /{rideId}/requests", method = RequestMethod.GET)
    public ModelAndView requests(@PathVariable Long rideId) {
        Collection<Request> requests = requestsService.findRequestsForRide(rideId);
        Collection<RequestVm> requestVms = new LinkedList<>();
        for (Request request : requests) {
            requestVms.add(mapper.map(request, RequestVm.class));
        }
        ModelAndView model = new ModelAndView("rides/requests");
        model.addObject("requests", requestVms);
        return model;
    }

    @RequestMapping(value = "/approveRequest", method = RequestMethod.POST)
    public ModelAndView approveRequest(@RequestParam("request_id") Long requestId) {
        requestsService.approveRequest(requestId);
        Request request = requestsService.findById(requestId);
        rideService.addParticipant(request.getRide(), request.getOwner());
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }

    @RequestMapping(value = "/declineRequest", method = RequestMethod.POST)
    public ModelAndView declineRequest(@RequestParam("request_id") Long requestId) {
        requestsService.declineRequest(requestId);
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }
}
