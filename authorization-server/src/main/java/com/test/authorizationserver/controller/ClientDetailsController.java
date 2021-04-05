package com.test.authorizationserver.controller;

import com.test.authorizationserver.model.dto.ClientDetailsDto;
import com.test.authorizationserver.service.ClientDetailsDtoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ClientDetailsController {

    private ClientDetailsDtoService clientDetailsDtoService;

    @PostMapping(value="/clientDetail",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ClientDetailsDto persist(@Valid @RequestBody ClientDetailsDto clientDetailsDto) {
        return this.clientDetailsDtoService.persist(clientDetailsDto);
    }

    @GetMapping(value="/userDetail", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ClientDetailsDto> findAll() {
        return this.clientDetailsDtoService.findAll();
    }

}
