package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.service.StatsService;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequiredArgsConstructor
@Slf4j
public class StatsController {

    private final StatsService service;


    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> get(@RequestParam
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                                  @RequestParam
                                                  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                                  @RequestParam(required = false) List<String> uris,
                                                  @RequestParam(defaultValue = "false") Boolean unique) {

        validateParamForGetMapping(start, end);
        log.info("Получен запрос GET /stats");
        return new ResponseEntity<>(service.get(start, end, uris, unique), HttpStatus.OK);
    }


    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> save(@RequestBody EndpointHitDto dto) {
        log.info("Получен запрос POST /hit");
        return new ResponseEntity<>(service.save(dto), HttpStatus.CREATED);
    }

    private void validateParamForGetMapping(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate.isAfter(endDate)) {
            throw new BadRequestException("start date is after end date");
        }
    }
}
