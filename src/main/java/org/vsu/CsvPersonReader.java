package org.vsu;

import org.vsu.Department;
import org.vsu.Person;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class CsvPersonReader {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public List<Person> readPersonsFromCsv(String csvFilePath) throws IOException, CsvValidationException {
        Map<String, Department> deptMap = new HashMap<>();
        AtomicInteger nextDeptId = new AtomicInteger(1);

        try (InputStream in = getClass().getClassLoader().getResourceAsStream(csvFilePath)) {
            if (in == null) {
                throw new IllegalArgumentException("CSV not found: " + csvFilePath);
            }

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .withIgnoreQuotations(true)
                    .build();

            try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(in))
                    .withCSVParser(parser)
                    .build()) {
                // ← ← ← ← ← ОБЯЗАТЕЛЬНО: пропускаем заголовок
                String[] header = reader.readNext();
                if (header == null) {
                    return Collections.emptyList();
                }

                // Теперь каждая следующая строка — данные
                String[] line;
                List<Person> persons = new ArrayList<>();

                int recordIndex = 0;
                while ((line = reader.readNext()) != null) {
                    if (line.length < 6) {
                        System.err.println("⚠️ Пропущена короткая строка #" + (++recordIndex) + ": " + Arrays.toString(line));
                        continue;
                    }

                    try {
                        int id = Integer.parseInt(line[0]);
                        String name = line[1];
                        String gender = line[2];
                        LocalDate birthDate = LocalDate.parse(line[3], DATE_FORMAT);
                        String divisionCode = line[4];
                        int salary = Integer.parseInt(line[5]);

                        Department dept = deptMap.computeIfAbsent(divisionCode,
                                code -> new Department(nextDeptId.getAndIncrement(), code));

                        persons.add(new Person(id, name, gender, birthDate, dept, salary));
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.err.println("❌ Ошибка парсинга в строке #" + (++recordIndex) + ": " + Arrays.toString(line) + " — " + e.getMessage());
                    }
                }

                return persons;
            }
        }
    }
}