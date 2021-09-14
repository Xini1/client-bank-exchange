package ru.standard1c.format;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Секция передачи остатков по расчетному счету.
 *
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class CheckingAccountBalance {

    /**
     * Дата начала интервала.
     */
    LocalDate startingDate;

    /**
     * Дата конца интервала
     */
    LocalDate endingDate;

    /**
     * Расчетный счет организации.
     */
    String checkingAccount;

    /**
     * Начальный остаток.
     */
    BigDecimal startingBalance;

    /**
     * Обороты входящих платежей.
     */
    BigDecimal totalReceived;

    /**
     * Обороты исходящих платежей.
     */
    BigDecimal totalDecommissioned;

    /**
     * Конечный остаток.
     */
    BigDecimal remainingBalance;
}
