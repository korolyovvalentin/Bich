package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.entities.City;
import org.fuc.repositories.CitiesRepository;
import org.fuc.viewmodels.CityVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/cities")
@Secured("ROLE_ADMIN")
class CityController {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    private CitiesRepository citiesRepository;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView listCities() {
        ModelAndView model = new ModelAndView("cities/index");
        Collection<City> cities = citiesRepository.getCities();
        Collection<CityVm> cityVms = new LinkedList<>();
        for (City city : cities) {
            cityVms.add(mapper.map(city, CityVm.class));
        }
        model.addObject("cities", cityVms);
        return model;
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView model = new ModelAndView("cities/create");
        model.addObject("city", new CityVm());
        return model;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("city") CityVm city, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("cities/create");
        }
        citiesRepository.save(mapper.map(city, City.class));
        return new ModelAndView(new RedirectView("/cities", false));
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("cities/delete");
        City city = citiesRepository.findById(id);
        model.addObject("city", city);
        return model;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@ModelAttribute("city") CityVm city) {
        citiesRepository.delete(mapper.map(city, City.class));
        return new ModelAndView(new RedirectView("/cities", false));
    }
}
