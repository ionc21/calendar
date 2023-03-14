package com.icsoft.controller;

import com.icsoft.repository.EventJpaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
class CalendarController {

    private final EventJpaRepository eventRepository;

    CalendarController(EventJpaRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping(value = "/staticcalendar")
    public ModelAndView staticcalendar() {
        return new ModelAndView("staticcalendar");
    }

    @GetMapping(value = "/calendar")
    public ModelAndView calendar() {
        return new ModelAndView("calendar");
    }

    @GetMapping(value = "/jsoncalendar")
    public ModelAndView jsoncalendar() {
        return new ModelAndView("jsoncalendar");
    }

    @GetMapping(value = "/eventlist")
    public String events(HttpServletRequest request, Model model) {
        model.addAttribute("events", eventRepository.findAll());
        return "events";
    }
}
