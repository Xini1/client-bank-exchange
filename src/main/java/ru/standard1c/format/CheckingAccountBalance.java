package ru.standard1c.format;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class CheckingAccountBalance {

    LocalDate startingDate;
    LocalDate endingDate;
    String checkingAccount;
    BigDecimal startingBalance;
    BigDecimal totalReceived;
    BigDecimal totalDecommissioned;
    BigDecimal remainingBalance;
}
