package com.example.demo.repositories;

import com.example.demo.entities.StudentIdCard;
import org.springframework.data.repository.CrudRepository;

public interface StudentIdCardRepository
        extends CrudRepository<StudentIdCard, Long> {
}
