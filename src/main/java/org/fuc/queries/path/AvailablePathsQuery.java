package org.fuc.queries.path;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.EmptyCriteria;
import org.fuc.core.criterias.PathCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.fuc.core.model.Path;
import org.fuc.core.model.PathSegment;
import org.fuc.util.Cast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.*;

public class AvailablePathsQuery implements Query<Path> {
    private Collection<Ride> rides;

    public AvailablePathsQuery(Collection<Ride> rides) {
        this.rides = rides;
    }

    public Collection<Path> query(Criteria criteria) {
        PathCriteria pathCriteria = Cast.as(PathCriteria.class, criteria);

        if (pathCriteria != null) {
            City start = pathCriteria.getStart();
            City end = pathCriteria.getEnd();

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

        EmptyCriteria emptyCriteria = Cast.as(EmptyCriteria.class, criteria);

        if (emptyCriteria != null) {
            Collection<Path> result = new LinkedList<>();

            for (Ride ride : rides) {
                List<RidePoint> points = ride.getPoints();
                result.add(
                        new Path(Collections.singletonList(
                                new PathSegment(
                                        ride,
                                        points.get(0).getCity(),
                                        points.get(points.size() - 1).getCity()
                                )
                        )));
            }

            return result;
        }

        throw new IllegalArgumentException("Unsupported criteria");
    }

    private Collection<Path> find(Ride ride,
                                  City current,
                                  City goal,
                                  Set<Ride> availableRides,
                                  Path accumulator) {
        if (containsCity(ride, goal)) {
            if (hasCorrectOrder(ride, current, goal)) {
                PathSegment segment = new PathSegment(ride, current, goal);
                return Collections.singleton(accumulator.clone().addSegment(segment));
            }
            return Collections.singleton(accumulator);
        }
        if (availableRides.isEmpty()) {
            return Collections.singleton(accumulator);
        }

        Collection<Path> paths = new LinkedList<>();
        for (Ride r : getNeighbours(ride, availableRides)) {
            City intersection = getIntersection(ride, r);

            if (hasCorrectOrder(ride, current, intersection)) {
                Set<Ride> available = Collections.emptySet();
                available.addAll(availableRides);
                available.remove(r);

                PathSegment segment = new PathSegment(ride, current, intersection);
                paths.addAll(find(r, intersection, goal, available, accumulator.clone().addSegment(segment)));
            }
        }
        return paths;
    }

    private Collection<Ride> getNeighbours(Ride ride, Collection<Ride> availableRides) {
        Collection<Ride> result = new LinkedList<>();

        for (Ride r : availableRides) {
            if (haveIntersection(ride, r)) {
                result.add(r);
            }
        }

        return result;
    }

    private Collection<Path> getAcceptablePaths(Collection<Path> paths, City end) {
        Collection<Path> result = new LinkedList<>();

        for (Path p : paths) {
            if (!p.isEmpty() && p.endsIn(end)) {
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

    private boolean hasCorrectOrder(Ride ride, City first, City second) {
        int firstIndex = 0, secondIndex = 0;
        for (RidePoint point : ride.getPoints()) {
            if (Objects.equals(point.getCity(), first))
                firstIndex = point.getOrderField();
            if (Objects.equals(point.getCity(), second))
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
