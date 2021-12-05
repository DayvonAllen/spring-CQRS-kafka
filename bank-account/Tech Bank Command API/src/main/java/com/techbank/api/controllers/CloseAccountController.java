package com.techbank.api.controllers;

import com.example.common.dto.BaseResponse;
import com.example.common.events.AccountClosedEvent;
import com.example.core.exceptions.AggregateNotFoundException;
import com.example.core.infrastructure.CommandDispatcher;
import com.techbank.api.commands.CloseAccountCommand;
import com.techbank.api.commands.WithdrawFundsCommand;
import com.techbank.api.dto.OpenAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/closeAccount")
public class CloseAccountController {
    private final Logger logger = Logger.getLogger(AccountClosedEvent.class.getName());

    private final CommandDispatcher commandDispatcher;

    public CloseAccountController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable(value = "id") String id) {
        try {
            commandDispatcher.send(new CloseAccountCommand(id));
            return new ResponseEntity<>(new BaseResponse("Account closed"), HttpStatus.NO_CONTENT);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(new OpenAccountResponse(e.getMessage(), id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
