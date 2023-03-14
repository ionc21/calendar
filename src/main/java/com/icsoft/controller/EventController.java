package com.icsoft.controller;
import com.icsoft.exception.BadDateFormatException;
import com.icsoft.model.Event;
import com.icsoft.repository.EventJpaRepository;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
class EventController {

    private final EventJpaRepository eventRepository;

    EventController(EventJpaRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @GetMapping(value = "/allevents")
    public List<Event> allEvents() {
        return eventRepository.findAll();
    }

    @PostMapping(value = "/event")
    public Event addEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @PatchMapping(value = "/event")
    public Event updateEvent(@RequestBody Event event) {
        return eventRepository.save(event);
    }

    @DeleteMapping(value = "/event")
    public void removeEvent(@RequestBody Event event) {
        eventRepository.delete(event);
    }

    @GetMapping(value = "/events")
    public List<Event> getEventsInRange(@RequestParam(value = "start", required = true) String start,
                                        @RequestParam(value = "end", required = true) String end) {
        Date startDate = null;
        Date endDate = null;
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            startDate = inputDateFormat.parse(start);
        } catch (ParseException e) {
            throw new BadDateFormatException("bad start date: " + start);
        }

        try {
            endDate = inputDateFormat.parse(end);
        } catch (ParseException e) {
            throw new BadDateFormatException("bad end date: " + end);
        }

        LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate.toInstant(),
                ZoneId.systemDefault());

        LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate.toInstant(),
                ZoneId.systemDefault());

        return eventRepository.findByDateBetween(startDateTime, endDateTime);
    }
}
