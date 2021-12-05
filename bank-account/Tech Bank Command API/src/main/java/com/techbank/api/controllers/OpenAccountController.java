package com.techbank.api.controllers;

import com.example.common.dto.BaseResponse;
import com.example.core.infrastructure.CommandDispatcher;
import com.techbank.api.commands.OpenAccountCommand;
import com.techbank.api.dto.OpenAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/openBankAccount")
public class OpenAccountController {
    private final Logger logger = Logger.getLogger(OpenAccountController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public OpenAccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PostMapping("")
    public ResponseEntity<BaseResponse> openAccount(@RequestBody OpenAccountCommand command) {
        String id = UUID.randomUUID().toString();
        command.setId(id);

        try {
            commandDispatcher.send(command);
            return new ResponseEntity<>(new OpenAccountResponse("Bank account creation was successful", id), HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(new OpenAccountResponse(e.getMessage(), id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
