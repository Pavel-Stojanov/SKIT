package org.example.course;

public class Course {
    private final Long id;
    private final String name;
    private final int credits;
    private final boolean active;

    public Course(Long id, String name, int credits, boolean active) {
        this.id = id;
        this.name = name;
        this.credits = credits;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCredits() {
        return credits;
    }

    public boolean isActive() {
        return active;
    }
}
