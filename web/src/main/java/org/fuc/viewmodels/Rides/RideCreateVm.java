package org.fuc.viewmodels.Rides;

import org.fuc.entities.City;

import java.util.Collection;

public class RideCreateVm extends RideVm {
    private Collection<City> availableCities;

    public Collection<City> getAvailableCities() {
        return availableCities;
    }

    public void setAvailableCities(Collection<City> availableCities) {
        this.availableCities = availableCities;
    }
}
