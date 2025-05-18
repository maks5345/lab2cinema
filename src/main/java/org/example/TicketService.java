package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepo;

    public List<Ticket> findAll() {
        return ticketRepo.findAll();
    }

    public Ticket findById(Long id) {
        return ticketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket not found"));
    }

    public void save(Ticket ticket) {
        ticketRepo.save(ticket);
    }

    public void delete(Long id) {
        ticketRepo.deleteById(id);
    }
}
