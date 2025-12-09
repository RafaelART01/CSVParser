package org.vsu;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class CsvPersonReaderTest {

    @Test
    void testReadData() throws IOException, com.opencsv.exceptions.CsvValidationException {
        CsvPersonReader reader = new CsvPersonReader();
        List<Person> persons = reader.readPersonsFromCsv("foreign_names.csv");

        // Файл содержит 3999 записей
        assertEquals(25898, persons.size());

        // Первая запись: 40899;Iva;Female;08.06.1996;N;3100
        Person first = persons.getFirst();
        assertEquals(28281, first.getId());
        assertEquals("Aahan", first.getName());
        assertEquals("Male", first.getGender());
        assertEquals(LocalDate.of(1970, 5, 15), first.getBirthDate());
        assertEquals("I", first.getDepartment().getName());
        assertEquals(4800, first.getSalary());
    }

    @Test
    void testDepartmentUniqueness() throws IOException, com.opencsv.exceptions.CsvValidationException {
        CsvPersonReader reader = new CsvPersonReader();
        List<Person> persons = reader.readPersonsFromCsv("foreign_names.csv");

        long uniqueDepts = persons.stream()
                .map(p -> p.getDepartment().getId())
                .distinct()
                .count();

        // Division от A до O → 15 уникальных подразделений
        assertEquals(15, uniqueDepts);
    }
}