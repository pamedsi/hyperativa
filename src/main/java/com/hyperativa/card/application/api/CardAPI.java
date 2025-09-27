package com.hyperativa.card.application.api;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RequestMapping("/card")
public interface CardAPI {

    @PostMapping("/single")
    @ResponseStatus(HttpStatus.CREATED)
    void addSingleCard(
            @RequestBody @Valid CardNumber cardNumber,
            @RequestHeader ("Authorization") String token
    );

}
