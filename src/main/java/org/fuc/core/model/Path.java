package org.fuc.core.model;

import org.fuc.entities.City;

import java.util.List;
import java.util.Objects;

public class Path {
    private List<PathSegment> segments;

    public Path(List<PathSegment> segments) {
        this.segments = segments;
    }

    public static Path clone(Path source){
        return new Path(source.getSegments());
    }

    public Path clone(){
        return Path.clone(this);
    }

    public Path addSegment(PathSegment segment){
        segments.add(segment);
        return this;
    }

    public boolean endsIn(City city){
        return Objects.equals(segments.get(segments.size() - 1).getEnd(), city);
    }

    public boolean isEmpty(){
        return segments.isEmpty();
    }

    public List<PathSegment> getSegments() {
        return segments;
    }

    public void setSegments(List<PathSegment> segments) {
        this.segments = segments;
    }
}
