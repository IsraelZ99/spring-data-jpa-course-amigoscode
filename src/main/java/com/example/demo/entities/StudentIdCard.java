package com.example.demo.entities;

import com.example.demo.entities.Student;
import lombok.*;

import javax.persistence.*;

@Entity(name = "StudentIdCard")
@Table(name = "student_id_card",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "student_card_number_unique",
                        columnNames = "card_number"
                )
        })
@Data
@NoArgsConstructor
public class StudentIdCard {

    @Id
    @SequenceGenerator(
            name = "student_card_id_sequence",
            sequenceName = "student_card_id_sequence",
            allocationSize = 1 // increased number
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "student_card_id_sequence"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Long id;

    @Column(
            name = "card_number",
            nullable = false,
            length = 15
    )
    private String cardNumber;

    // All: Save, edit, delete all persistence data
    // This is uni-directional relationship (because in Student entity doesn't have relation notation with this entity)
    @OneToOne(
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER, // EAGER is the default fetch for @OneToOne (Retrieve the full data of Relation [Student])
            orphanRemoval = false // If it's true, when I deleted studentIdCard a student field is deleted too (isn't a good practice)
    )
    @JoinColumn(
            name = "student_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "student_id_card_student_id_fk"
            )
    )
    private Student student;

    public StudentIdCard(String cardNumber,
                         Student student) {
        this.cardNumber = cardNumber;
        this.student = student;
    }

}
