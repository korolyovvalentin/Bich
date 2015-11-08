package org.fuc.queries.path;

import org.fuc.core.Criteria;
import org.fuc.core.Query;
import org.fuc.core.criterias.PathCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.fuc.core.model.Path;
import org.fuc.core.model.PathSegment;
import org.fuc.util.Cast;
import org.fuc.util.DateComparer;
import org.fuc.util.Predicate;

import java.util.*;

public class AvailablePathsQuery implements Query<Path> {
    private Collection<Ride> rides;

    public AvailablePathsQuery(Collection<Ride> rides) {
        this.rides = rides;
    }

    public Collection<Path> query(Criteria criteria) {
        PathCriteria pathCriteria = ((PathCriteria) criteria);

        City start = pathCriteria.getStart();
        City end = pathCriteria.getEnd();

        if (start == null && end == null)
            return findAll();
        if (start == null)
            return findByEnd(end);
        if (end == null)
            return findByStart(start);

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

    private Collection<Path> findAll() {
        return find(new Predicate<Ride>() {
            @Override
            public boolean isTrue(Ride o) {
                return true;
            }
        });
    }

    private Collection<Path> findByStart(final City start) {
        return find(new Predicate<Ride>() {
            @Override
            public boolean isTrue(Ride o) {
                List<RidePoint> points = o.getPoints();
                return Objects.equals(points.get(0).getCity(), start);
            }
        });
    }

    private Collection<Path> findByEnd(final City end) {
        return find(new Predicate<Ride>() {
            @Override
            public boolean isTrue(Ride o) {
                List<RidePoint> points = o.getPoints();
                return Objects.equals(points.get(points.size() - 1).getCity(), end);
            }
        });
    }

    private Collection<Path> find(Predicate<Ride> predicate) {
        Collection<Path> result = new LinkedList<>();

        for (Ride ride : rides) {
            List<RidePoint> points = ride.getPoints();

            if (predicate.isTrue(ride)) {
                result.add(new Path(
                        Collections.singletonList(
                                new PathSegment(
                                        ride,
                                        points.get(0).getCity(),
                                        points.get(points.size() - 1).getCity()
                                )
                        )
                ));
            }
        }

        return result;
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
                Set<Ride> available = new HashSet<>();
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
            if (haveSameDate(ride, r) && haveIntersection(ride, r)) {
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

    private boolean haveSameDate(Ride first, Ride second) {
        return DateComparer.compare(first.getDate(), second.getDate()) == 0;
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
            if (point.getCity().equals(first))
                firstIndex = point.getOrderField();
            if (point.getCity().equals(second))
                secondIndex = point.getOrderField();
        }
        return firstIndex < secondIndex;
    }

    private boolean containsCity(Ride ride, City city) {
        for (RidePoint point : ride.getPoints()) {
            if (point.getCity().equals(city)) {
                return true;
            }
        }
        return false;
    }

}
