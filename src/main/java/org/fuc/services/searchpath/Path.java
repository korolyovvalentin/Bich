package org.fuc.services.searchpath;

import java.util.List;

public class Path {
    private List<PathSegment> segments;

    public Path(List<PathSegment> segments) {
        this.segments = segments;
    }
}
