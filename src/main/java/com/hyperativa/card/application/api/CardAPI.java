package com.hyperativa.card.application.api;


import com.hyperativa.card.infra.ValidTextFile;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


@RequestMapping("/card")
public interface CardAPI {

    @PostMapping("/single")
    @ResponseStatus(HttpStatus.CREATED)
    void addSingleCard(
            @RequestBody @Valid CardNumber cardNumber,
            @RequestHeader ("Authorization") String token
    );

    @PostMapping(value = "/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    void addBatchCards(
            @Valid @ValidTextFile(
                    allowedTypes = {"text/plain"}
            )
            @RequestParam("file") MultipartFile file,
            @RequestHeader("Authorization") String token
    );

}
