package org.fuc.viewmodels;

import org.fuc.core.model.Path;
import org.fuc.entities.City;

import java.util.Collection;
import java.util.List;

public class PathsVm {
    private RidesFilter filter;
    private List<Path> paths;
    private Collection<City> cities;

    public PathsVm() {
    }

    public PathsVm(RidesFilter filter, List<Path> paths, Collection<City> cities) {
        this.filter = filter;
        this.paths = paths;
        this.cities = cities;
    }

    public RidesFilter getFilter() {
        return filter;
    }

    public void setFilter(RidesFilter filter) {
        this.filter = filter;
    }

    public List<Path> getPaths() {
        return paths;
    }

    public void setPaths(List<Path> paths) {
        this.paths = paths;
    }

    public Collection<City> getCities() {
        return cities;
    }

    public void setCities(Collection<City> cities) {
        this.cities = cities;
    }
}
