package com.techbank.api.commands;

import com.example.common.dto.AccountType;
import com.example.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class OpenAccountCommand extends BaseCommand {
    private String accountHolder;
    private AccountType accountType;
    private double openingBalance;
}
