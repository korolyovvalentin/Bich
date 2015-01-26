package org.fuc.services;

import org.fuc.entities.*;
import org.fuc.repositories.RequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RequestsService {
    @Autowired
    private RequestRepository requestRepository;

    public Request create(Ride ride, Account beatnik) {
        if (!RoleProvider.ROLE_BEATNIK.equals(beatnik.getRole())) {
            throw new IllegalArgumentException(String.format("Role %s is illegal", beatnik.getRole()));
        }
        Request request = new Request(RequestStatus.NEW, ride, beatnik);
        requestRepository.save(request);
        return request;
    }

    public Collection<Request> findRequestsForRide(Long rideId){
        return requestRepository.findRequests(rideId, RequestStatus.getStatus("new"));
    }
}
