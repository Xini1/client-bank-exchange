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
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * @author Maxim Tereshchenko
 */
class ReaderWithTxtSamplesTest {

    @Test
    void givenSample_whenRead_thenBuildExpectedClientBankExchange() throws IOException, URISyntaxException {
        assertThat(new ClientBankExchangeReader().read(attributeSourceFromFile())).isEqualTo(expected());
    }

    private ClientBankExchange expected() {
        var date = LocalDate.of(2020, 1, 1);
        var time = LocalTime.of(10, 0, 0);
        var amount = new BigDecimal("10.10");

        return ClientBankExchange.builder()
                .formatVersion(1.03f)
                .encoding(Encoding.WINDOWS)
                .sender("Отправитель")
                .receiver("Получатель")
                .creationDate(date)
                .creationTime(time)
                .startingDate(date)
                .endingDate(date)
                .checkingAccount("12345678901234567890")
                .documentTypeList(
                        List.of(
                                DocumentType.PAYMENT_ORDER,
                                DocumentType.PAYMENT_CLAIM
                        )
                )
                .checkingAccountBalanceList(
                        List.of(
                                checkingAccountBalance(date, amount, "12345678901234567890"),
                                checkingAccountBalance(date, amount, "12345678901234567891")
                        )
                )
                .documentList(
                        List.of(
                                document(date, time, amount, DocumentType.PAYMENT_ORDER),
                                document(date, time, amount, DocumentType.PAYMENT_CLAIM)
                        )
                )
                .build();
    }

    private Document document(LocalDate date, LocalTime time, BigDecimal amount, DocumentType documentType) {
        return Document.builder()
                .documentType(documentType)
                .number(1)
                .date(date)
                .sum(amount)
                .receiptDate(date)
                .receiptTime(time)
                .receiptContent("КвитанцияСодержание")
                .payerAccount("12345678901234567890")
                .decommissionDate(date)
                .payer("Плательщик")
                .payerInn("1234567890")
                .payer1("Плательщик1")
                .payer2("Плательщик2")
                .payer3("Плательщик3")
                .payer4("Плательщик4")
                .payerCheckingAccount("12345678901234567890")
                .payerBank1("ПлательщикБанк1")
                .payerBank2("ПлательщикБанк2")
                .payerBic("123456789")
                .payerCorrespondentAccount("12345678901234567890")
                .receiverAccount("12345678901234567890")
                .receivingDate(date)
                .receiver("Получатель")
                .receiverInn("1234567890")
                .receiver1("Получатель1")
                .receiver2("Получатель2")
                .receiver3("Получатель3")
                .receiver4("Получатель4")
                .receiverCheckingAccount("12345678901234567890")
                .receiverBank1("ПолучательБанк1")
                .receiverBank2("ПолучательБанк2")
                .receiverBic("123456789")
                .receiverCorrespondentAccount("12345678901234567890")
                .paymentType(PaymentType.POST)
                .paymentPurposeCode(1)
                .operationType("01")
                .code("0")
                .paymentPurpose("НазначениеПлатежа")
                .paymentPurpose1("НазначениеПлатежа1")
                .paymentPurpose2("НазначениеПлатежа2")
                .paymentPurpose3("НазначениеПлатежа3")
                .paymentPurpose4("НазначениеПлатежа4")
                .paymentPurpose5("НазначениеПлатежа5")
                .paymentPurpose6("НазначениеПлатежа6")
                .compilerStatus("СтатусСоставителя")
                .payerKpp("123456789")
                .receiverKpp("123456789")
                .cbcIndicator("12345678901234567890")
                .oktmo("12345678901")
                .basisIndicator("12")
                .periodIndicator("1234567890")
                .numberIndicator("ПоказательНомера")
                .dateIndicator(date)
                .typeIndicator("1")
                .priority(1)
                .acceptanceTerm(1)
                .letterOfCreditType("ВидАккредитива")
                .paymentTerm(date)
                .paymentCondition1("УсловиеОплаты1")
                .paymentCondition2("УсловиеОплаты2")
                .paymentCondition3("УсловиеОплаты3")
                .paymentOnPresentation("ПлатежПоПредст")
                .additionalConditions("ДополнУсловия")
                .supplierAccountNumber("12345678901234567890")
                .documentDispatchDate(date)
                .build();
    }

    private CheckingAccountBalance checkingAccountBalance(LocalDate date, BigDecimal amount, String account) {
        return CheckingAccountBalance.builder()
                .startingDate(date)
                .endingDate(date)
                .checkingAccount(account)
                .startingBalance(amount)
                .totalReceived(amount)
                .totalDecommissioned(amount)
                .remainingBalance(amount)
                .build();
    }

    private AttributeSource attributeSourceFromFile() throws URISyntaxException, IOException {
        return new ScannerAttributeSource(
                new Scanner(
                        Paths.get(
                                Objects.requireNonNull(
                                                getClass()
                                                        .getClassLoader()
                                                        .getResource("sample.txt")
                                        )
                                        .toURI()
                        ),
                        StandardCharsets.UTF_8
                )
        );
    }
}
