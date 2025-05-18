package org.example;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String seatNumber;
    private Double price;

    private java.time.LocalDateTime purchaseDate;  // Додамо це поле, якщо хочемо показувати дату купівлі

    @ManyToOne
    @JoinColumn(name = "session_id")
    private Session session;
}
