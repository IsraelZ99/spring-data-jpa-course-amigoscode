package com.example.demo;

import com.example.demo.entities.*;
import com.example.demo.repositories.StudentIdCardRepository;
import com.example.demo.repositories.StudentRepository;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@Slf4j
public class Initializer {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository,
                                        StudentIdCardRepository studentIdCardRepository) {
        return args -> {
            Student student = createStudent();
            student.addBook(new Book("Clean Code", LocalDateTime.now().minusDays(4)));
            student.addBook(new Book("Think and Grow Rich", LocalDateTime.now()));
            student.addBook(new Book("Spring Data JPA", LocalDateTime.now().minusDays(1)));

            StudentIdCard studentIdCard = new StudentIdCard("123456789",
                    student);

            student.setStudentIdCard(studentIdCard);

            // student.addEnrolment(new Course("Computer Science", "IT"));
            // student.addEnrolment(new Course("Amigoscode Spring Data JPA", "IT"));

            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 1L),
                    student,
                    new Course("Computer Science", "IT"),
                    LocalDateTime.now())
            );
            student.addEnrolment(new Enrolment(
                    new EnrolmentId(1L, 2L),
                    student,
                    new Course("Amigoscode Spring Data JPA", "IT"),
                    LocalDateTime.now().minusDays(18))
            );

            studentRepository.save(student);

            // studentIdCardRepository.save(studentIdCard); // CascadeType.PERSIST in action

            studentRepository.findById(1L)
                    .ifPresent(s -> {
                        // Lazy meaning that te data is not retrieve
                        System.out.println("Fetch book lazy...");
                        List<Book> books = student.getBooks();
                        books.forEach(book -> {
                            System.out.println(
                                    s.getFirstName() + " borrowed " + book.getBookName()
                            );
                        });
                    });

            /*
            studentIdCardRepository.findById(1L)
                    .ifPresent(System.out::println);
             */

            // studentRepository.deleteById(1L);
        };
    }

    private Student createStudent() {
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = String.format("%s.%s@amigoscode.edu",
                firstName, lastName);
        return new Student(
                firstName,
                lastName,
                email,
                faker.number().numberBetween(17, 45)
        );
    }

    CommandLineRunner commandLineRunnerPagination(StudentRepository studentRepository) {
        return args -> {
            generateRandomStudents(studentRepository);

            PageRequest pageRequest = PageRequest.of(0,
                    5,
                    Sort.by("firstName").ascending());
            Page<Student> page = studentRepository.findAll(pageRequest);
            System.out.println(page);
        };
    }

    CommandLineRunner commandLineRunnerSort(StudentRepository studentRepository) {
        return args -> {
            generateRandomStudents(studentRepository);
            Sort sort = Sort.by("firstName")
                    .ascending()
                    .and(Sort.by("age").descending()
                    );
            studentRepository.findAll(sort)
                    .forEach(student ->
                            System.out.println(student.getFirstName() + " " + student.getAge()));
        };
    }

    private void generateRandomStudents(StudentRepository studentRepository) {
        for (int i = 0; i < 20; i++) {
            Student student = createStudent();
            studentRepository.save(student);
        }
    }

    CommandLineRunner commandLineRunnerOld(StudentRepository studentRepository) {
        return args -> {
            Student maria = new Student(
                    "Maria", "Jones",
                    "maria.jones@amigoscode.edu",
                    21
            );
            Student maria2 = new Student(
                    "Maria", "Jones",
                    "maria2.jones@amigoscode.edu",
                    25
            );
            Student amhed = new Student(
                    "Ahmed", "Ali",
                    "amhed.ali@amigoscode.edu",
                    18
            );

            log.info("Adding maria and ahmed");
            studentRepository.saveAll(
                    List.of(maria, amhed, maria2)
            );

            studentRepository.findStudentByEmail("amhed.ali@amigoscode.edu")
                    .ifPresentOrElse(student ->
                                    log.info(student.toString())
                            , () ->
                                    log.error("Student with email amhed.ali@amigoscode.edu not found.")
                    );

            studentRepository.selectStudentWhereFirstNameAndAgeGreaterOrEqualsNative(
                    "Maria",
                    21
            ).forEach(student -> log.info(student.toString()));

            studentRepository.selectStudentWhereFirstNameAndAgeGreaterOrEqualsNativeParam(
                    "Maria",
                    21
            ).forEach(student -> log.info(student.toString()));

            log.info("Deleting Maria 2");
            log.info(String.valueOf(studentRepository.deleteStudentById(3L)));

            /*
            log.warn("Number of students ... " + studentRepository.count());

            studentRepository.findById(2L)
                    .ifPresentOrElse(student -> {
                        log.info(student.toString());
                    }, () ->
                            log.error("Student with ID 2 not found"));
            studentRepository.findById(3L)
                    .ifPresentOrElse(student -> {
                        log.info(student.toString());
                    }, () ->
                            log.error("Student with ID 3 not found"));

            log.info("Select all students");
            List<Student> students = studentRepository.findAll();
            students.forEach(student -> log.info(student.toString()));

            log.info("Delete maria");
            studentRepository.deleteById(1L);

            log.warn("Number of students ... " + studentRepository.count());

             */
        };
    }

}
