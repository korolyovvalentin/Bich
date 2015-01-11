package org.fuc.controllers;

import org.fuc.entities.City;
import org.fuc.repositories.CitiesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/cities")
@Secured("ROLE_ADMIN")
class CityController {

    private CitiesRepository citiesRepository;

    @Autowired
    public CityController(CitiesRepository citiesRepository) {
        this.citiesRepository = citiesRepository;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public String listCities(Model model) {
        model.addAttribute("cities", citiesRepository.getCities());
        return "cities/index";
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        model.addAttribute("city", new City());
        return "cities/create";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String create(@Valid City city, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "cities/create";
        }
        citiesRepository.save(city);
        return "redirect:/cities";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String delete(@PathVariable("id") int id, Model model) {
        City city = citiesRepository.findById(id);
        model.addAttribute("city", city);
        return "cities/delete";
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public String delete(@ModelAttribute("city") City city) {
        citiesRepository.delete(city);
        return "redirect:/cities";
    }
}
