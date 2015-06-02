package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.*;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.CitiesRepository;
import org.fuc.repositories.PlaceRepository;
import org.fuc.services.RequestsService;
import org.fuc.services.RideService;
import org.fuc.viewmodels.Places.PlaceCreateVm;
import org.fuc.viewmodels.Places.PlaceVm;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.Rides.RideCreateVm;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/business/places")
@Secured("ROLE_BUSINESS")
public class PlaceController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    private CitiesRepository citiesRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RideService rideService;
    @Autowired
    private RequestsService requestsService;
    @Autowired
    private PlaceRepository placeRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView index(Principal principal) {
        Account businessman = accountRepository.findByEmail(principal.getName());
        Collection<Place> places = placeRepository.getOwnerPlaces(businessman);
        Collection<PlaceVm> rideVms = new LinkedList<>();
        for (Place place : places) {
            rideVms.add(mapper.map(place, PlaceVm.class));
        }
        return new ModelAndView("places/index", "places", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        Collection<City> cities = citiesRepository.getCities();
        PlaceCreateVm model = new PlaceCreateVm();
        model.setAvailableCities(cities);
        return new ModelAndView("places/create", "place", model);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("place") PlaceCreateVm model, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("places/create");
        }
        Place place = mapper.map(model, Place.class);
        place.setOwner(accountRepository.findByEmail(principal.getName()));

        if (!place.getOwner().getRole().equals(RoleProvider.ROLE_BUSINESS)) {
            throw new IllegalArgumentException("Place's owner must be businessman");
        }
        placeRepository.save(place);
        return new ModelAndView(new RedirectView("/business/places", false));
    }

    @RequestMapping(value = " /{rideId}/requests", method=RequestMethod.GET)
    public ModelAndView requests(@PathVariable Long rideId){
        Collection<Request> requests = requestsService.findRequestsForRide(rideId);
        Collection<RequestVm> requestVms = new LinkedList<>();
        for(Request request : requests){
            requestVms.add(mapper.map(request, RequestVm.class));
        }
        ModelAndView model = new ModelAndView("rides/requests");
        model.addObject("requests", requestVms);
        return model;
    }

    @RequestMapping(value = "/approveRequest", method = RequestMethod.POST)
    public ModelAndView approveRequest(@RequestParam("request_id") Long requestId){
        requestsService.approveRequest(requestId);
        Request request = requestsService.findById(requestId);
        rideService.addParticipant(request.getRide(), request.getOwner());
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }

    @RequestMapping(value = "/declineRequest", method = RequestMethod.POST)
    public ModelAndView declineRequest(@RequestParam("request_id") Long requestId){
        requestsService.declineRequest(requestId);
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }
}
