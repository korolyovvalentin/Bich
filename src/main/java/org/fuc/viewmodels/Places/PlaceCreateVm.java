package org.fuc.viewmodels.Places;

import org.fuc.entities.City;

import java.util.Collection;

public class PlaceCreateVm extends PlaceVm {
    private Collection<City> availableCities;

    public Collection<City> getAvailableCities() {
        return availableCities;
    }

    public void setAvailableCities(Collection<City> availableCities) {
        this.availableCities = availableCities;
    }
}
