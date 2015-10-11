package org.fuc.services.searchpath;

import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;

import java.util.*;

public class SearchPathSvc {
    private Collection<Ride> rides;
    private Collection<City> cities;

    public SearchPathSvc(Collection<Ride> rides) {
        this.rides = rides;

        cities = new HashSet<>();
        for (Ride ride : rides) {
            for (RidePoint ridePoint : ride.getPoints()) {
                cities.add(ridePoint.getCity());
            }
        }
    }

    public Collection<Path> search(City start, City end) {
        for (Ride ride : rides) {
            if (containsCity(ride, start)) {
                Set<Ride> availableRides = new HashSet<>(rides);
                availableRides.remove(ride);
                tryFindPath(ride, start, end, availableRides, new Path(new ArrayList<PathSegment>()));
            }
        }
    }

    private Collection<Path> tryFindPath(Ride ride,
                                         City current,
                                         City goal,
                                         Set<Ride> availableRides,
                                         Path accumulator){
        if(containsCity(ride, goal)){
            PathSegment segment = new PathSegment(ride, current, goal);
            return Collections.singleton(accumulator.clone().addSegment(segment));
        }
        if(availableRides.isEmpty()){
            return Collections.singleton(accumulator);
        }

        Collection<Path> paths = new LinkedList<>();
        for(Ride r : getNeighbours()){
            City intersection = getIntersection(ride, r);

            Set<Ride> available = Collections.emptySet();
            available.addAll(availableRides);

            PathSegment segment = new PathSegment(ride, current, intersection);
            paths.addAll(tryFindPath(r, intersection, goal, available, accumulator.clone().addSegment(segment)))
        }
        return paths;
    }

    private Collection<Ride> getNeighbours(Ride ride, Collection<Ride> availableRides){
        Collection<Ride> result = new LinkedList<>();

        for(Ride r : availableRides){
            if(haveIntersection(ride, r)){
                result.add(r);
            }
        }

        return result;
    }

    private boolean haveIntersection(Ride first, Ride second) {
        for (RidePoint ridePoint : first.getPoints()) {
            if (containsCity(second, ridePoint.getCity())) {
                return true;
            }
        }
        return false;
    }

    private City getIntersection(Ride first, Ride second) {
        for (RidePoint ridePoint : first.getPoints()) {
            if (containsCity(second, ridePoint.getCity())) {
                return ridePoint.getCity();
            }
        }
        return null;
    }

    private boolean containsCity(Ride ride, City city) {
        for (RidePoint point : ride.getPoints()) {
            if (Objects.equals(point.getCity().getId(), city.getId())) {
                return true;
            }
        }
        return false;
    }
}
