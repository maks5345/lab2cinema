package org.example;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final SessionRepository sessionRepository;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("tickets", ticketService.findAll());
        return "tickets/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("sessions", sessionRepository.findAll());
        return "tickets/form";
    }

    @PostMapping
    public String save(@ModelAttribute Ticket ticket) {
        // Якщо дата купівлі відсутня, ставимо зараз
        if (ticket.getPurchaseDate() == null) {
            ticket.setPurchaseDate(java.time.LocalDateTime.now());
        }
        ticketService.save(ticket);
        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.findById(id));
        model.addAttribute("sessions", sessionRepository.findAll());
        return "tickets/form";
    }

    @GetMapping("/")
    public String home() {
        return "index"; // повертає index.html з templates
    }


    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        ticketService.delete(id);
        return "redirect:/tickets";
    }
}
