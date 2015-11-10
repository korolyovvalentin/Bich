package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Command;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.core.criterias.EmailCriteria;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.core.criterias.PlaceTypeCriteria;
import org.fuc.entities.Account;
import org.fuc.entities.Place;
import org.fuc.entities.PlaceRequest;
import org.fuc.entities.RequestStatus;
import org.fuc.viewmodels.PlaceRequestVm;
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
@RequestMapping("/beatnik/place_requests")
@Secured("ROLE_BEATNIK")
public class PlaceRequestController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;

    @Autowired
    @Qualifier("placeByIdQuery")
    private QuerySingle<Place> placeByIdQuery;
    @Autowired
    @Qualifier("placesByTypeQuery")
    private Query<Place> placesByTypeQuery;

    @Autowired
    @Qualifier("placeRequestByIdQuery")
    private QuerySingle<PlaceRequest> placeRequestById;
    @Autowired
    @Qualifier("placeRequestsUpdatedQuery")
    private Query<PlaceRequest> placeRequestsUpdated;
    @Autowired
    @Qualifier("createPlaceRequestCommand")
    private Command<PlaceRequest> createPlaceRequest;
    @Autowired
    @Qualifier("updatePlaceRequestCommand")
    private Command<PlaceRequest> updatePlaceRequest;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView index() {
        Collection<Place> places = placesByTypeQuery.query(new PlaceTypeCriteria(null, null));
        Collection<PlaceVm> placeVms = new LinkedList<>();
        for (Place place : places) {
            placeVms.add(mapper.map(place, PlaceVm.class));
        }
        return new ModelAndView("places/index", "places", placeVms);
    }

    @RequestMapping(value = "/{placeId}/create", method = RequestMethod.GET)
    public ModelAndView create(@PathVariable Long placeId) {
        Place place = placeByIdQuery.query(new IdCriteria(placeId));
        PlaceRequestVm vm = new PlaceRequestVm();
        vm.setPlace(place);
        return new ModelAndView("place_requests/create", "placeRequest", vm);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("placeRequest") PlaceRequestVm vm, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("place_requests/create");
        }

        PlaceRequest placeRequest = mapper.map(vm, PlaceRequest.class);
        placeRequest.setOwner(findAccountByEmail.query(new EmailCriteria(principal.getName())));
        createPlaceRequest.execute(placeRequest);
        return new ModelAndView(new RedirectView("/beatnik/place_requests", false));
    }

    @RequestMapping(value = "/requests", method = RequestMethod.GET)
    public ModelAndView requests(Principal principal) {
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<PlaceRequest> requests = placeRequestsUpdated.query(new AccountCriteria(account));

        Collection<PlaceRequestVm> requestVms = new LinkedList<>();
        for (PlaceRequest request : requests) {
            requestVms.add(mapper.map(request, PlaceRequestVm.class));
        }
        return new ModelAndView("place_requests/index", "requests", requestVms);
    }

    @RequestMapping(value = "/{id}/review", method = RequestMethod.GET)
    public ModelAndView reviewPlace(@PathVariable Long id) {
        PlaceRequest placeRequest = placeRequestById.query(new IdCriteria(id));
        return new ModelAndView("place_review/create", "request", placeRequest);
    }

    @RequestMapping(value = "/review", method = RequestMethod.POST)
    public ModelAndView declineRequest(@ModelAttribute PlaceRequest request) {
        PlaceRequest r = placeRequestById.query(new IdCriteria(request.getId()));

        r.setStatus(RequestStatus.OLD);
        r.setComment(request.getComment());
        r.setRating(request.getRating());

        updatePlaceRequest.execute(r);

        return new ModelAndView(new RedirectView("/beatnik/place_requests/requests", false));
    }
}
