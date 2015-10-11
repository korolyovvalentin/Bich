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
        Collection<Path> result = new LinkedList<>();

        for (Ride ride : rides) {
            if (containsCity(ride, start)) {
                Set<Ride> availableRides = new HashSet<>(rides);
                availableRides.remove(ride);
                result.addAll(
                        getAcceptablePaths(
                                find(ride, start, end, availableRides, new Path(new ArrayList<PathSegment>())),
                                end
                        ));
            }
        }

        return result;
    }

    private Collection<Path> find(Ride ride,
                                  City current,
                                  City goal,
                                  Set<Ride> availableRides,
                                  Path accumulator){
        if(containsCity(ride, goal)){
            if(hasCorrectOrder(ride, current, goal)){
                PathSegment segment = new PathSegment(ride, current, goal);
                return Collections.singleton(accumulator.clone().addSegment(segment));
            }
            return Collections.singleton(accumulator);
        }
        if(availableRides.isEmpty()){
            return Collections.singleton(accumulator);
        }

        Collection<Path> paths = new LinkedList<>();
        for(Ride r : getNeighbours(ride, availableRides)){
            City intersection = getIntersection(ride, r);

            if(hasCorrectOrder(ride, current, intersection)) {
                Set<Ride> available = Collections.emptySet();
                available.addAll(availableRides);
                available.remove(r);

                PathSegment segment = new PathSegment(ride, current, intersection);
                paths.addAll(find(r, intersection, goal, available, accumulator.clone().addSegment(segment)));
            }
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

    private Collection<Path> getAcceptablePaths(Collection<Path> paths, City end){
        Collection<Path> result = new LinkedList<>();

        for(Path p : paths){
            if(!p.isEmpty() && p.endsIn(end)){
                result.add(p);
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

    private boolean hasCorrectOrder(Ride ride, City first, City second){
        int firstIndex = 0, secondIndex = 0;
        for(RidePoint point: ride.getPoints()){
            if(Objects.equals(point.getCity(), first))
                firstIndex = point.getOrderField();
            if(Objects.equals(point.getCity(), second))
                secondIndex = point.getOrderField();
        }
        return firstIndex < secondIndex;
    }

    private boolean containsCity(Ride ride, City city) {
        for (RidePoint point : ride.getPoints()) {
            if (Objects.equals(point.getCity(), city)) {
                return true;
            }
        }
        return false;
    }
}
