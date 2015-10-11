package org.fuc.services.searchpath;

import java.util.List;

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

    public List<PathSegment> getSegments() {
        return segments;
    }
}
