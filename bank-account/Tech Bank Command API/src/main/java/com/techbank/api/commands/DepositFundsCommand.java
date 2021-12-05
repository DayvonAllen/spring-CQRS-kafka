package com.techbank.api.commands;

import com.example.core.commands.BaseCommand;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class DepositFundsCommand extends BaseCommand {
    private double amount;
}
