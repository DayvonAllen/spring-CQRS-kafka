package com.techbank.api.controllers;

import com.example.common.dto.BaseResponse;
import com.example.core.exceptions.AggregateNotFoundException;
import com.example.core.infrastructure.CommandDispatcher;
import com.techbank.api.commands.DepositFundsCommand;
import com.techbank.api.commands.WithdrawFundsCommand;
import com.techbank.api.dto.OpenAccountResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/withdrawFunds")
public class WithdrawFundsController {
    private final Logger logger = Logger.getLogger(WithdrawFundsController.class.getName());

    private final CommandDispatcher commandDispatcher;

    public WithdrawFundsController(CommandDispatcher commandDispatcher) {
        this.commandDispatcher = commandDispatcher;
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<BaseResponse> withdrawFunds(@PathVariable(value = "id") String id, @RequestBody WithdrawFundsCommand command) {
        try {
            command.setId(id);
            commandDispatcher.send(command);
            return new ResponseEntity<>(new BaseResponse("Funds were successfully withdrawn"), HttpStatus.OK);
        } catch (IllegalStateException | AggregateNotFoundException e) {
            logger.log(Level.WARNING, e.getMessage());
            return new ResponseEntity<>(new BaseResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getMessage());
            return new ResponseEntity<>(new OpenAccountResponse(e.getMessage(), id), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
