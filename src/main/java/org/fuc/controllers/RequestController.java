package org.fuc.controllers;

import ma.glasnost.orika.MapperFacade;
import org.fuc.core.Command;
import org.fuc.core.Query;
import org.fuc.core.QuerySingle;
import org.fuc.core.criterias.AccountCriteria;
import org.fuc.core.criterias.EmailCriteria;
import org.fuc.core.criterias.IdCriteria;
import org.fuc.entities.*;
import org.fuc.support.web.MessageHelper;
import org.fuc.viewmodels.RequestVm;
import org.fuc.viewmodels.Rides.RideVm;
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

@Controller
@RequestMapping("/beatnik/requests")
@Secured("ROLE_BEATNIK")
public class RequestController {
    @Autowired
    private MapperFacade mapper;

    @Autowired
    @Qualifier("rideByIdQuery")
    private QuerySingle<Ride> rideByIdQuery;

    @Autowired
    @Qualifier("accountByEmailQuery")
    private QuerySingle<Account> findAccountByEmail;

    @Autowired
    @Qualifier("ridesByBeatnikQuery")
    private Query<Ride> ridesByBeatnikQuery;

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
    public ModelAndView availableRides(Principal principal) {
        Account account = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        Collection<Ride> rides = ridesByBeatnikQuery.query(new AccountCriteria(account));
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@RequestParam("ride_id") Long rideId, Principal principal) {
        Ride ride = rideByIdQuery.query(new IdCriteria(rideId));
        Account beatnik = findAccountByEmail.query(new EmailCriteria(principal.getName()));
        createRequest.execute(new Request("", ride, beatnik));

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
