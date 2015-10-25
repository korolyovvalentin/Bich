package org.fuc.unit;

import org.fuc.core.Criteria;
import org.fuc.core.criterias.PathCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.fuc.core.model.Path;
import org.fuc.queries.path.AvailablePathsQuery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.Collection;

@RunWith(JUnit4.class)
public class SearchPathServiceTest {
    private City city1 = new City("one");
    private City city2 = new City("two");
    private City city3 = new City("three");
    private City city4 = new City("four");

    private Ride ride1;
    private Ride ride2;

    private AvailablePathsQuery service;

    @Before
    public void setup(){
        ride1 = new Ride();
        ride2 = new Ride();
    }

    @Test
    public void shouldNotReturnAnyPathsIfNoEntriesExists() {
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));
        service = new AvailablePathsQuery(Arrays.asList(ride1));

        Collection<Path> result = service.query(new PathCriteria(city3, city4));

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnAnyPathsIfNoRidesContainsEndCity() {
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));
        service = new AvailablePathsQuery(Arrays.asList(ride1));

        Collection<Path> result = service.query(new PathCriteria(city1, city3));

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldNotReturnPathsIfOrderIsWrong(){
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));

        service = new AvailablePathsQuery(Arrays.asList(ride1));

        Collection<Path> result = service.query(new PathCriteria(city2, city4));

        Assert.assertEquals(0, result.size());
    }

    @Test
    public void shouldReturnTwoPaths(){
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));
        ride1.getPoints().add(new RidePoint(ride1, 2, city4));


        ride2.getPoints().add(new RidePoint(ride2, 0, city1));
        ride2.getPoints().add(new RidePoint(ride2, 1, city3));
        ride2.getPoints().add(new RidePoint(ride2, 2, city4));

        service = new AvailablePathsQuery(Arrays.asList(ride1, ride2));

        Collection<Path> result = service.query(new PathCriteria(city1, city4));

        Assert.assertEquals(2, result.size());
    }

    @Test
    public void shouldReturnTwoPathsIfCriteriaIsEmpty(){
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));
        ride1.getPoints().add(new RidePoint(ride1, 2, city4));

        ride2.getPoints().add(new RidePoint(ride2, 0, city1));
        ride2.getPoints().add(new RidePoint(ride2, 1, city3));
        ride2.getPoints().add(new RidePoint(ride2, 2, city4));

        service = new AvailablePathsQuery(Arrays.asList(ride1, ride2));

        Collection<Path> result = service.query(Criteria.empty());

        Assert.assertEquals(2, result.size());
    }
}
