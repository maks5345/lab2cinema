package org.example;

import org.example.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    long countBySessionId(Long sessionId);
    void deleteBySessionId(Long sessionId);
}

