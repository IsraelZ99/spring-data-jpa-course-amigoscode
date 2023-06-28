package com.example.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrolmentId implements Serializable { // When we use @Embeddable we need to implement Serializable

    @Column
    private Long studentId;

    @Column
    private Long courseId;

}
