package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Place;
import org.fuc.entities.RoleProvider;
import org.fuc.repositories.AccountRepository;
import org.fuc.repositories.CitiesRepository;
import org.fuc.repositories.PlaceRepository;
import org.fuc.viewmodels.Places.PlaceCreateVm;
import org.fuc.viewmodels.Places.PlaceVm;
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
}
