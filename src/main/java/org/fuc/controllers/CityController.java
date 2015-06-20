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
import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/administration/cities")
class CityController {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    private CitiesRepository citiesRepository;

    @Secured("ROLE_ADMIN")
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

    @Secured("ROLE_DRIVER")
    @RequestMapping(value = "/available", method = RequestMethod.POST, headers = "Accept=application/json")
    @ResponseStatus(value = HttpStatus.OK)
    public
    @ResponseBody
    CityVm[] listCities(@RequestBody City[] cities, Principal principal) {
        Collection<City> availableList = citiesRepository.getCities(cities);
        CityVm[] cityVms = new CityVm[availableList.size()];
        int i = 0;
        for (City city : availableList) {
            cityVms[i++] = mapper.map(city, CityVm.class);
        }
        return cityVms;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        ModelAndView model = new ModelAndView("cities/create");
        model.addObject("city", new CityVm());
        return model;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("city") CityVm city, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("cities/create");
        }
        citiesRepository.save(mapper.map(city, City.class));
        return new ModelAndView(new RedirectView("/administration/cities", false));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("cities/delete");
        City city = citiesRepository.findById(id);
        model.addObject("city", city);
        return model;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@ModelAttribute("city") CityVm city) {
        citiesRepository.delete(mapper.map(city, City.class));
        return new ModelAndView(new RedirectView("/administration/cities", false));
    }

    private class CitiesJson {
        private City[] cities;

        public City[] getCities() {
            return cities;
        }

        public void setCities(City[] cities) {
            this.cities = cities;
        }
    }
}
