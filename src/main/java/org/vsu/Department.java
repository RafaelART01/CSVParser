package org.vsu;

import java.util.Objects;

public class Department {
    private final int id;
    private final String name;

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public String getName() { return name; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Department d)) return false;
        return id == d.id && Objects.equals(name, d.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
