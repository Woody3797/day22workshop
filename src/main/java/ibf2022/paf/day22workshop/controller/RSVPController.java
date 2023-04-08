package ibf2022.paf.day22workshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import ibf2022.paf.day22workshop.model.RSVP;
import ibf2022.paf.day22workshop.repository.RSVPRepository;

@Controller
@RequestMapping(path = "/api")
public class RSVPController {
    
    RSVPRepository repository;

    RSVPController(RSVPRepository repository) {
        this.repository = repository;
    }

    @GetMapping(path = "/rsvp/form")
    public String getLandingPage(Model model, @ModelAttribute RSVP rsvp) {
        model.addAttribute("rsvp", rsvp);
        return "index";
    }
}
