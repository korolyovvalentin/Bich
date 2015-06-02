package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.Account;
import org.fuc.entities.Place;
import org.fuc.entities.Request;
import org.fuc.entities.Ride;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.PlaceRepository;
import org.fuc.repositories.PlaceRequestRepository;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RequestsService;
import org.fuc.services.RideService;
import org.fuc.support.web.MessageHelper;
import org.fuc.viewmodels.Places.PlaceVm;
import org.fuc.viewmodels.RequestVm;
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
@RequestMapping("/beatnik/place_requests")
@Secured("ROLE_BEATNIK")
public class PlaceRequestController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    private RidesRepository ridesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RequestsService requestsService;
    @Autowired
    private PlaceRepository placeRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView index(){
        Collection<Place> places = placeRepository.getPlaces(null, null);
        Collection<PlaceVm> placeVms = new LinkedList<>();
        for (Place place : places) {
            placeVms.add(mapper.map(place, PlaceVm.class));
        }
        return new ModelAndView("places/index", "places", placeVms);
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

    @RequestMapping(value = "/updated", method = RequestMethod.GET)
    public ModelAndView processedRequests(Principal principal){
        Account account = accountRepository.findByEmail(principal.getName());
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