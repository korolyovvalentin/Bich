package org.fuc.controllers;

import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.*;
import org.fuc.core.model.Path;
import org.fuc.entities.Account;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.infrastructure.MessageHelper;
import org.fuc.queries.path.AvailablePathsQuery;
import org.fuc.util.Cast;
import org.fuc.viewmodels.PathsVm;
import org.fuc.viewmodels.RidesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import java.util.List;

@Controller
@RequestMapping("/beatnik/paths")
@Secured("ROLE_BEATNIK")
public class PathController {
    @Autowired
    @Qualifier("rideByIdQuery")
    private QuerySingle<Ride> rideByIdQuery;

    @Autowired
    @Qualifier("allCitiesQuery")
    private Query<City> citiesProvider;

    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;

    @Autowired
    @Qualifier("cityByNameQuery")
    private QuerySingle<City> findCityByName;

    @Autowired
    @Qualifier("ridesByFilterQuery")
    private Query<Ride> ridesByFilterQuery;

    @Autowired
    @Qualifier("createPathRequestCommand")
    private Command<PathRequestCriteria> createPathRequest;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView availableRides(
            @RequestParam(value = "filter.date", required = false) String date,
            @RequestParam(value = "filter.start", required = false) String startCity,
            @RequestParam(value = "filter.end", required = false) String endCity) {
        RidesFilter filter = new RidesFilter(startCity, endCity, Cast.convertToDate(date));
        Collection<Ride> rides = ridesByFilterQuery.query(new RideFilterCriteria(filter.getDate()));

        Query<Path> pathQuery = new AvailablePathsQuery(rides);

        City start = filter.getStart() == null
                ? null
                : findCityByName.query(new NameCriteria(filter.getStart()));
        City end = filter.getEnd() == null
                ? null
                : findCityByName.query(new NameCriteria(filter.getEnd()));

        Collection<Path> paths = pathQuery.query(
                new PathCriteria(start, end));

        PathsVm pathsVm = new PathsVm(
                filter,
                new LinkedList<>(paths),
                new LinkedList<>(citiesProvider.query(Criteria.empty())));

        return new ModelAndView("paths/index", "model", pathsVm);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView create(@RequestParam(value = "ride") int[] rideIds,
                               @RequestParam(value = "start") String start,
                               @RequestParam(value = "end") String end,
                               Principal principal) {
        Account beatnik = findAccountByEmail.query(new EmailCriteria(principal.getName()));

        List<Ride> rides = new LinkedList<>();
        for (int rideId : rideIds) {
            rides.add(rideByIdQuery.query(new IdCriteria((long) rideId)));
        }

        createPathRequest.execute(new PathRequestCriteria(beatnik, rides, start, end));

        ModelAndView model = new ModelAndView(new RedirectView("/beatnik/paths", false));
        MessageHelper.addSuccessAttribute(model, "Request was successfully added");
        return model;
    }
}
