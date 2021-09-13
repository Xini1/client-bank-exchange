package ru.standard1c;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.standard1c.format.CheckingAccountBalance;
import ru.standard1c.format.ClientBankExchange;
import ru.standard1c.format.Document;
import ru.standard1c.format.DocumentType;
import ru.standard1c.format.Encoding;
import ru.standard1c.format.PaymentType;
import ru.standard1c.reader.ClientBankExchangeReader;
import ru.standard1c.reader.source.AttributeSource;
import ru.standard1c.reader.source.ScannerAttributeSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author Maxim Tereshchenko
 */
class ReaderWithTxtSamplesTest {

    @Test
    void givenFile_whenRead_thenBuildExpectedClientBankExchange() throws IOException, URISyntaxException {
        assertThat(new ClientBankExchangeReader().read(attributeSourceFromFile())).isEqualTo(expected());
    }

    private ClientBankExchange expected() {
        return ClientBankExchange.builder()
                .formatVersion(1.03f)
                .encoding(Encoding.WINDOWS)
                .sender("Бухгалтерия предприятия, редакция 3.0")
                .receiver("")
                .creationDate(LocalDate.of(2015, 12, 9))
                .creationTime(LocalTime.of(10, 33, 20))
                .startingDate(LocalDate.of(2015, 12, 9))
                .endingDate(LocalDate.of(2015, 12, 9))
                .checkingAccount("40702810300180001774")
                .documentTypeList(
                        List.of(
                                DocumentType.PAYMENT_ORDER,
                                DocumentType.PAYMENT_CLAIM
                        )
                )
                .checkingAccountBalanceList(
                        Collections.singletonList(
                                CheckingAccountBalance.builder()
                                        .startingDate(LocalDate.of(2020, 1, 1))
                                        .endingDate(LocalDate.of(2020, 1, 1))
                                        .checkingAccount("123456789")
                                        .startingBalance(new BigDecimal("100"))
                                        .totalReceived(new BigDecimal("100.02"))
                                        .totalDecommissioned(new BigDecimal("100.00"))
                                        .remainingBalance(new BigDecimal("100.99"))
                                        .build()
                        )
                )
                .documentList(
                        Collections.singletonList(
                                Document.builder()
                                        .documentType(DocumentType.PAYMENT_ORDER)
                                        .number(105)
                                        .date(LocalDate.of(2015, 12, 9))
                                        .sum(new BigDecimal("12354.00"))
                                        .payerAccount("40702810300180001774")
                                        .payer("ИНН 7719617469 ОАО Крокус")
                                        .payerInn("7719617469")
                                        .payer1("ОАО Крокус")
                                        .payerCheckingAccount("40702810300180001774")
                                        .payerBank1("АО ОТП БАНК")
                                        .payerBank2("Г. МОСКВА")
                                        .payerBic("044525311")
                                        .payerCorrespondentAccount("30101810000000000311")
                                        .receiverAccount("40702810123111111114")
                                        .receiver("ИНН 7701325469 ОАО Прогресс Парк")
                                        .receiverInn("7701325469")
                                        .receiver1("ОАО Прогресс Парк")
                                        .receiverCheckingAccount("40702810123111111114")
                                        .receiverBank1("ОАО БАНК ПЕТРОКОММЕРЦ")
                                        .receiverBank2("Г. МОСКВА")
                                        .receiverBic("044525352")
                                        .receiverCorrespondentAccount("30101810700000000352")
                                        .paymentType(PaymentType.POST)
                                        .paymentPurposeCode(1)
                                        .transactionType("01")
                                        .payerKpp("771901001")
                                        .priority(5)
                                        .paymentPurpose("Оплата по договору. Сумма 12354-00 без налога (НДС)")
                                        .paymentPurpose1("Оплата по договору. Сумма 12354-00 без налога (НДС)")
                                        .build()
                        )
                )
                .build();
    }

    private AttributeSource attributeSourceFromFile() throws URISyntaxException, IOException {
        return new ScannerAttributeSource(
                new Scanner(
                        Paths.get(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource("1CClientBankExchange.txt")
                                        )
                                        .toURI()
                        ),
                        StandardCharsets.UTF_8
                )
        );
    }
}
