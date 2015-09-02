package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.QuerySingle;
import org.fuc.entities.Account;
import org.fuc.entities.Request;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.fuc.queries.account.EmailCriteria;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RequestsService;
import org.fuc.services.RideService;
import org.fuc.support.web.MessageHelper;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.Rides.RideVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;
    @Autowired
    private RideService rideService;
    @Autowired
    private RequestsService requestsService;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView availableRides(Principal principal){
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Ride> rides = rideService.getAvailableRides(account);
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam("ride_id") Long rideId, Principal principal){
        Ride ride = ridesRepository.findById(rideId);
        Account beatnik = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        requestsService.create(ride, beatnik);
        ModelAndView model = new ModelAndView(new RedirectView("/beatnik/requests", false));
        MessageHelper.addSuccessAttribute(model, "Request was successfully added");
        return model;
    }

    @RequestMapping(value = "/updated", method = RequestMethod.GET)
    public ModelAndView processedRequests(Principal principal){
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Request> requests = requestsService.findUpdatedRequests(account.getId());
        Collection<RequestVm> requestVms = new LinkedList<>();
        for(Request request : requests){
            requestVms.add(mapper.map(request, RequestVm.class));
        }
        return new ModelAndView("requests/updated", "requests", requestVms);
    }

    @RequestMapping(value = "/markAsOld", method = RequestMethod.POST)
    public ModelAndView declineRequest(@RequestParam("request_id") Long requestId){
        requestsService.markAsOld(requestId);
        return new ModelAndView(new RedirectView("/beatnik/requests/updated", false));
    }
}
