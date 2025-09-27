package com.hyperativa.card.application.api;


import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;


@RequestMapping("/card")
public interface CardAPI {

    @PostMapping("/single")
    @ResponseStatus(HttpStatus.CREATED)
    void addSingleCard(
            @RequestBody @Valid CardNumber cardNumber,
            @RequestHeader ("Authorization") String token
    );

}
