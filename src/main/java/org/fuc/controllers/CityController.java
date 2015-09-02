package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.commands.city.CityCriteria;
import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.entities.City;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.core.criterias.CitiesCriteria;
import org.fuc.viewmodels.CityVm;
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
import java.util.Collection;
import java.util.LinkedList;

@Controller
@RequestMapping("/administration/cities")
public class CityController {
    @Autowired
    private MapperFacade mapper;
    @Autowired
    @Qualifier("allCitiesQuery")
    private Query<City> citiesProvider;
    @Autowired
    @Qualifier("citiesExceptSelectedQuery")
    private Query<City> citiesExceptSelectedProvider;
    @Autowired
    @Qualifier("cityByIdQuery")
    private QuerySingle<City> singleCityProvider;
    @Autowired
    @Qualifier("createCityCommand")
    private Command createCity;
    @Autowired
    @Qualifier("deleteCityCommand")
    private Command deleteCity;

    @Secured("ROLE_ADMIN")
    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView listCities() {
        ModelAndView model = new ModelAndView("cities/index");
        Collection<City> cities = citiesProvider.query(Criteria.empty());
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
    CityVm[] listCities(@RequestBody City[] cities) {
        Collection<City> availableList = citiesExceptSelectedProvider.query(new CitiesCriteria(cities));
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
        createCity.execute(new CityCriteria(mapper.map(city, City.class)));
        return new ModelAndView(new RedirectView("/administration/cities", false));
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public ModelAndView delete(@PathVariable("id") Long id) {
        ModelAndView model = new ModelAndView("cities/delete");
        City city = singleCityProvider.query(new IdCriteria(id));
        model.addObject("city", city);
        return model;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView delete(@ModelAttribute("city") CityVm city) {
        deleteCity.execute(new CityCriteria(mapper.map(city, City.class)));
        return new ModelAndView(new RedirectView("/administration/cities", false));
    }
}
