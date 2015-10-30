package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.*;
import org.fuc.core.model.Path;
import org.fuc.entities.*;
import org.fuc.infrastructure.MessageHelper;
import org.fuc.queries.path.AvailablePathsQuery;
import org.fuc.util.Cast;
import org.fuc.viewmodels.PathsVm;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.RidesFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/beatnik/paths")
@Secured("ROLE_BEATNIK")
public class PathController {
    @Autowired
    private MapperFacade mapper;

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

    @Autowired
    @Qualifier("requestByIdQuery")
    private QuerySingle<Request> requestById;
    @Autowired
    @Qualifier("requestsUpdatedQuery")
    private Query<Request> requestsUpdated;
    @Autowired
    @Qualifier("createRequestCommand")
    private Command<Request> createRequest;
    @Autowired
    @Qualifier("updateRequestCommand")
    private Command<Request> updateRequest;

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
    public ModelAndView create(@RequestParam(value = "ride") int[] ride, Model m, Principal principal) {
        Account beatnik = findAccountByEmail.query(new EmailCriteria(principal.getName()));

        List<Ride> rides = new LinkedList<>();
        for (int aRide : ride) {
            rides.add(rideByIdQuery.query(new IdCriteria((long) aRide)));
        }

        createPathRequest.execute(new PathRequestCriteria(beatnik, rides));

        ModelAndView model = new ModelAndView(new RedirectView("/beatnik/requests", false));
        MessageHelper.addSuccessAttribute(model, "Request was successfully added");
        return model;
    }

    @RequestMapping(value = "/updated", method = RequestMethod.GET)
    public ModelAndView processedRequests(Principal principal) {
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Request> requests = requestsUpdated.query(new AccountCriteria(account));

        Collection<RequestVm> requestVms = new LinkedList<>();
        for (Request request : requests) {
            requestVms.add(mapper.map(request, RequestVm.class));
        }
        return new ModelAndView("requests/updated", "requests", requestVms);
    }

    @RequestMapping(value = "/markAsOld", method = RequestMethod.POST)
    public ModelAndView markRequestAsOld(@RequestParam("request_id") Long requestId) {
        Request request = requestById.query(new IdCriteria(requestId));
        request.setStatus(RequestStatus.OLD);
        updateRequest.execute(request);
        return new ModelAndView(new RedirectView("/beatnik/requests/updated", false));
    }
}
