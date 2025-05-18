package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sessions")
public class SessionController {

    private final SessionRepository sessionRepository;
    private final CinemaRepository cinemaRepository;

    public SessionController(SessionRepository sessionRepository, CinemaRepository cinemaRepository) {
        this.sessionRepository = sessionRepository;
        this.cinemaRepository = cinemaRepository;
    }

    // Список сеансів
    @GetMapping
    public String listSessions(Model model) {
        List<Session> sessions = sessionRepository.findAll();
        model.addAttribute("sessions", sessions);
        return "sessions/list";
    }

    @GetMapping("/")
    public String home() {
        return "index"; // повертає index.html з templates
    }



    // Форма створення нового сеансу
    @GetMapping("/new")
    public String createSessionForm(Model model) {
        model.addAttribute("movieSession", new Session());
        return "sessions/form";
    }

    // Форма редагування сеансу
    @GetMapping("/{id}")
    public String editSessionForm(@PathVariable Long id, Model model) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid session Id:" + id));
        model.addAttribute("movieSession", session);
        return "sessions/form";
    }

    // Збереження (створення або оновлення)
    @PostMapping
    public String saveSession(@ModelAttribute("movieSession") Session session) {
        // Прив’язуємо завжди до кінотеатру з id=1 (або створюємо його)
        Cinema cinema = cinemaRepository.findById(1L).orElseGet(() -> {
            Cinema c = new Cinema();
            c.setName("Мій кінотеатр");
            return cinemaRepository.save(c);
        });
        session.setCinema(cinema);

        sessionRepository.save(session);
        return "redirect:/sessions";
    }

    // Видалення сеансу
    @GetMapping("/delete/{id}")
    public String deleteSession(@PathVariable Long id) {
        sessionRepository.deleteById(id);
        return "redirect:/sessions";
    }
}
