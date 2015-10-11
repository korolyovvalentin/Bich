package org.fuc.services.searchpath;

import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;

import java.util.*;

public class SearchPathService {
    private Collection<Ride> rides;
    private Collection<City> cities;

    public SearchPathService(Collection<Ride> rides) {
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
            paths.addAll(tryFindPath(r, intersection, goal, available))
        }
        return paths;
    }

    private void get(Ride ride,
                           Collection<Ride> availableRides,
                           City start,
                           City finish,
                           List<Path> memo) {
        if(containsCity(ride, finish)){

        }
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

    private Collection<City> getCities(Ride ride) {
        List<City> result = new LinkedList<>();

        for (RidePoint ridePoint : ride.getPoints()) {
            result.add(ridePoint.getCity());
        }

        return result;
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
