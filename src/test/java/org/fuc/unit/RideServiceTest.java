package org.fuc.unit;

import org.fuc.commands.ride.CreateRideCommand;
import org.fuc.core.Command;
import org.fuc.core.criterias.RidePointsCriteria;
import org.fuc.entities.City;
import org.fuc.entities.Ride;
import org.fuc.entities.RidePoint;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class RideServiceTest {

    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private Command<RidePointsCriteria> createRideCommand = new CreateRideCommand();

    @Test
    public void orderTest() {
        Ride ride = new Ride();
        City[] cities = new City[]{new City("NY"), new City("LA"), new City("SF")};

        createRideCommand.execute(new RidePointsCriteria(ride, cities));

        for (RidePoint rp : ride.getPoints()) {
            switch (rp.getCity().getName()) {
                case "NY":
                    assertThat(rp.getOrderField()).isEqualTo(0);
                    break;
                case "LA":
                    assertThat(rp.getOrderField()).isEqualTo(1);
                    break;
                case "SF":
                    assertThat(rp.getOrderField()).isEqualTo(2);
                    break;
            }
        }
    }
}
