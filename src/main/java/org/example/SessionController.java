package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final CinemaRepository cinemaRepository;
    private final TicketRepository ticketRepository;

    public SessionController(SessionRepository sessionRepository,
                             CinemaRepository cinemaRepository,
                             TicketRepository ticketRepository) {
        this.sessionRepository = sessionRepository;
        this.cinemaRepository = cinemaRepository;
        this.ticketRepository = ticketRepository;
    }

    @GetMapping
    public String listSessions(Model model) {
        List<Session> sessions = sessionRepository.findAll();
        model.addAttribute("sessions", sessions);
        return "sessions/list";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/new")
    public String createSessionForm(Model model) {
        model.addAttribute("movieSession", new Session());
        return "sessions/form";
    }

    @GetMapping("/{id}")
    public String editSessionForm(@PathVariable Long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        model.addAttribute("movieSession", session);
        return "sessions/form";
    }

    @PostMapping
    public String saveSession(@ModelAttribute("movieSession") Session session) {
        Cinema cinema = cinemaRepository.findById(1L).orElseGet(() -> {
            Cinema c = new Cinema();
            c.setName("Мій кінотеатр");
            return cinemaRepository.save(c);
        });
        session.setCinema(cinema);

        sessionRepository.save(session);
        return "redirect:/sessions";
    }

    // Додано @Transactional для відкриття транзакції під час видалення
    @Transactional
    @PostMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id,
                                @RequestParam(required = false, defaultValue = "false") boolean deleteTickets,
                                Model model) {

        long ticketsCount = ticketRepository.countBySessionId(id);

        if (ticketsCount > 0 && !deleteTickets) {
            // Якщо є квитки і не підтверджено їх видалення — показати підтвердження
            model.addAttribute("sessionId", id);
            model.addAttribute("ticketsCount", ticketsCount);
            return "sessions/confirmDelete";
        }

        if (deleteTickets) {
            // Видаляємо спочатку всі квитки для цього сеансу
            ticketRepository.deleteBySessionId(id);
        }
        // Потім видаляємо сам сеанс
        sessionRepository.deleteById(id);

        return "redirect:/sessions";
    }
}
