package org.vsu;

import org.vsu.Department;
import org.vsu.Person;
import com.opencsv.*;
import com.opencsv.exceptions.CsvValidationException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

                String[] allFields = reader.readNext();

                if (allFields == null || allFields.length == 0) {
                    return Collections.emptyList();
                }

                List<Person> persons = new ArrayList<>();
                int totalRecords = allFields.length / 6;

                for (int i = 0; i < totalRecords; i++) {
                    int start = i * 6;
                    String idStr = allFields[start];
                    String name = allFields[start + 1];
                    String gender = allFields[start + 2];
                    String dateStr = allFields[start + 3];
                    String divisionCode = allFields[start + 4];
                    String salaryStr = allFields[start + 5];

                    try {
                        int id = Integer.parseInt(idStr);
                        LocalDate birthDate = LocalDate.parse(dateStr, DATE_FORMAT);
                        int salary = Integer.parseInt(salaryStr);

                        Department dept = deptMap.computeIfAbsent(divisionCode,
                                code -> new Department(nextDeptId.getAndIncrement(), "Department " + code));

                        persons.add(new Person(id, name, gender, birthDate, dept, salary));
                    } catch (Exception e) {
                        System.err.println("Ошибка в записи #" + (i + 1) + ": " + Arrays.toString(Arrays.copyOfRange(allFields, start, start + 6)));
                    }
                }

                return persons;
            }
        }
    }
}