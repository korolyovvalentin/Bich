package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.entities.*;
import org.fuc.core.criterias.EmailCriteria;
import org.fuc.repositories.PlaceRepository;
import org.fuc.repositories.PlaceRequestRepository;
import org.fuc.viewmodels.PlaceRequestVm;
import org.fuc.viewmodels.Places.PlaceCreateVm;
import org.fuc.viewmodels.Places.PlaceVm;
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
@RequestMapping("/business/places")
@Secured("ROLE_BUSINESS")
public class PlaceController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    @Qualifier("allCitiesQuery")
    private Query<City> citiesProvider;
    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceRequestRepository prRepository;


    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView index(Principal principal) {
        Account businessman = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Place> places = placeRepository.getOwnerPlaces(businessman);
        Collection<PlaceVm> rideVms = new LinkedList<>();
        for (Place place : places) {
            rideVms.add(mapper.map(place, PlaceVm.class));
        }
        return new ModelAndView("places/index", "places", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        Collection<City> cities = citiesProvider.query(Criteria.empty());
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
        place.setOwner(findAccountByEmail.query(new EmailCriteria(principal.getName())));

        if (!place.getOwner().getRole().equals(RoleProvider.ROLE_BUSINESS)) {
            throw new IllegalArgumentException("Place's owner must be businessman");
        }
        placeRepository.save(place);
        return new ModelAndView(new RedirectView("/business/places", false));
    }

    @RequestMapping(value = "/{placeId}/requests", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable Long placeId) {
        Place place = placeRepository.findById(placeId);
        Collection<PlaceRequest> requests =
                prRepository.findNewRequests(place);
        Collection<PlaceRequestVm> requestVms = new LinkedList<>();
        for (PlaceRequest request : requests) {
            requestVms.add(mapper.map(request, PlaceRequestVm.class));
        }
        return new ModelAndView("place_requests/index", "requests", requestVms);
    }

    @RequestMapping(value = "/approveRequest", method = RequestMethod.POST)
    public ModelAndView approveRequest(@RequestParam("request_id") Long requestId) {
        PlaceRequest request = prRepository.findById(requestId);
        request.setStatus(RequestStatus.APPROVED);
        prRepository.update(request);

        return new ModelAndView(new RedirectView("/business/places/" + requestId + "/requests", false));
    }

    @RequestMapping(value = "/declineRequest", method = RequestMethod.POST)
    public ModelAndView declineRequest(@RequestParam("request_id") Long requestId) {
        PlaceRequest request = prRepository.findById(requestId);
        request.setStatus(RequestStatus.DECLINED);
        prRepository.update(request);

        return new ModelAndView(new RedirectView("/business/places/" + requestId + "/requests", false));
    }
}
