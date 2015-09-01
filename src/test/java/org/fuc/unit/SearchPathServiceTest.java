package org.fuc.unit;

import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class SearchPathServiceTest {
    private City city1 = new City("one");
    private City city2 = new City("two");
    private City city3 = new City("three");
    private City city4 = new City("four");

    private Ride ride1;
    private Ride ride2;
    private Ride ride3;

    public SearchPathServiceTest() {
        ride1.getPoints().add(new RidePoint(ride1, 0, city1));
        ride1.getPoints().add(new RidePoint(ride1, 1, city2));
    }


}
