package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.Place;
import org.fuc.entities.PlaceRequest;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.PlaceRepository;
import org.fuc.repositories.PlaceRequestRepository;
import org.fuc.viewmodels.PlaceRequestVm;
import org.fuc.viewmodels.Places.PlaceVm;
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
@RequestMapping("/beatnik/place_requests")
@Secured("ROLE_BEATNIK")
public class PlaceRequestController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PlaceRepository placeRepository;
    @Autowired
    private PlaceRequestRepository prRepository;

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

    @RequestMapping(value = "/{placeId}/create", method=RequestMethod.GET)
    public ModelAndView create(@PathVariable Long placeId){
        Place place = placeRepository.findById(placeId);
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
        placeRequest.setOwner(accountRepository.findByEmail(principal.getName()));
        prRepository.save(placeRequest);
        return new ModelAndView(new RedirectView("/beatnik/place_requests", false));
    }
}
