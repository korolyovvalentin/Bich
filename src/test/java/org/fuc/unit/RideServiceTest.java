package org.fuc.unit;

import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.fuc.repositories.RidesRepository;
import org.fuc.services.RideService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RideServiceTest {

    @Mock
    private RidesRepository ridesRepository;

    @InjectMocks
    private RideService service = new RideService();

    @Test
    public void orderTest(){
        Ride ride = new Ride();
        City[] cities = new City[]{new City("NY"), new City("LA"), new City("SF")};
        service.createRide(ride, cities);

        for (RidePoint rp : ride.getPoints()){
            switch (rp.getCity().getName()){
                case "NY": assertThat(rp.getOrderField()).isEqualTo(0); break;
                case "LA": assertThat(rp.getOrderField()).isEqualTo(1); break;
                case "SF": assertThat(rp.getOrderField()).isEqualTo(2); break;
            }
        }
    }
}
