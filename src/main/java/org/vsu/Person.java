package org.vsu;

import java.time.LocalDate;

public class Person {
    private final int id;
    private final String name;
    private final String gender;
    private final LocalDate birthDate;
    private final Department department;
    private final int salary;

    public Person(int id, String name, String gender, LocalDate birthDate,
                  Department department, int salary) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.department = department;
        this.salary = salary;
    }

    // Геттеры
    public int getId() { return id; }
    public String getName() { return name; }
    public String getGender() { return gender; }
    public LocalDate getBirthDate() { return birthDate; }
    public Department getDepartment() { return department; }
    public int getSalary() { return salary; }
}
