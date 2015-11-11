package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Command;
import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.*;
import org.fuc.entities.*;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.Rides.RideCreateVm;
import org.fuc.viewmodels.Rides.RideVm;
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
import java.util.List;

@Controller
@RequestMapping("/driver/rides")
@Secured("ROLE_DRIVER")
public class RideController {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    @Qualifier("allCitiesQuery")
    private Query<City> citiesProvider;

    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;

    @Autowired
    @Qualifier("ridesByDriverQuery")
    private Query<Ride> ridesByOwnerQuery;

    @Autowired
    @Qualifier("rideByIdQuery")
    private QuerySingle<Ride> rideById;
    @Autowired
    @Qualifier("createRideCommand")
    private Command<RidePointsCriteria> createRide;
    @Autowired
    @Qualifier("addRideParticipantCommand")
    private Command<RideParticipantCriteria> addParticipantToRide;

    @Autowired
    @Qualifier("requestByIdQuery")
    private QuerySingle<Request> requestById;
    @Autowired
    @Qualifier("requestsNewQuery")
    private Query<Request> requestsNew;
    @Autowired
    @Qualifier("updateRequestCommand")
    private Command<Request> updateRequest;

    @Autowired
    @Qualifier("rideReviewsQuery")
    private Query<Request> rideReviews;

    @Autowired
    @Qualifier("updatePathRequestStatus")
    private Command<IdCriteria> updateRequestStatus;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView listRides(Principal principal) {
        Account driver = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Ride> rides = ridesByOwnerQuery.query(new AccountCriteria(driver));
        Collection<RideVm> rideVms = new LinkedList<>();
        for (Ride ride : rides) {
            RideVm rideVm = mapper.map(ride, RideVm.class);

            for (RidePoint rp : ride.getPoints()) {
                rideVm.getCities().add(rp.getCity());
            }
            rideVms.add(rideVm);
        }
        return new ModelAndView("rides/index", "rides", rideVms);
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView create() {
        Collection<City> cities = citiesProvider.query(Criteria.empty());
        RideCreateVm model = new RideCreateVm();
        model.setAvailableCities(cities);
        return new ModelAndView("rides/create", "ride", model);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("ride") RideCreateVm model, Principal principal, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return new ModelAndView("rides/create");
        }
        Ride ride = mapper.map(model, Ride.class);
        ride.setOwner(findAccountByEmail.query(new EmailCriteria(principal.getName())));
        City[] cities = model.getCities().toArray(new City[model.getCities().size()]);
        createRide.execute(new RidePointsCriteria(ride, cities));
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }

    @RequestMapping(value = " /{rideId}/requests", method = RequestMethod.GET)
    public ModelAndView requests(@PathVariable Long rideId, Principal principal) {
        Ride ride = new Ride();
        ride.setId(rideId);

        Collection<Request> requests = requestsNew.query(
                new RideCriteria(
                        ride,
                        findAccountByEmail.query(new EmailCriteria(principal.getName()))));
        Collection<RequestVm> requestVms = new LinkedList<>();
        for (Request request : requests) {
            requestVms.add(mapper.map(request, RequestVm.class));
        }
        ModelAndView model = new ModelAndView("rides/requests");
        model.addObject("requests", requestVms);
        return model;
    }

    @RequestMapping(value = "/{id}/reviews", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public ModelAndView placeReviews(@PathVariable Long id, Principal principal) {
        Ride ride = rideById.query(new IdCriteria(id));
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));

        List<Request> requests = new LinkedList<>(rideReviews.query(new RideCriteria(ride, account)));

        return new ModelAndView("ride_review/index", "requests", requests);
    }

    @RequestMapping(value = "/approveRequest", method = RequestMethod.POST)
    public ModelAndView approveRequest(@RequestParam("request_id") Long requestId) {
        Request request = requestById.query(new IdCriteria(requestId));
        request.setStatus(RequestStatus.APPROVED);
        updateRequest.execute(request);
        updateRequestStatus.execute(new IdCriteria(request.getPathRequest().getId()));

        addParticipantToRide.execute(new RideParticipantCriteria(request.getOwner(), request.getRide()));
        return new ModelAndView(new RedirectView("/driver/rides", false));
    }

    @RequestMapping(value = "/declineRequest", method = RequestMethod.POST)
    public ModelAndView declineRequest(@RequestParam("request_id") Long requestId) {
        Request request = requestById.query(new IdCriteria(requestId));
        request.setStatus(RequestStatus.DECLINED);
        updateRequest.execute(request);
        updateRequestStatus.execute(new IdCriteria(request.getPathRequest().getId()));

        return new ModelAndView(new RedirectView("/driver/rides", false));
    }
}
