package ru.standard1c;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import ru.standard1c.format.CheckingAccountBalance;
import ru.standard1c.format.ClientBankExchange;
import ru.standard1c.format.Document;
import ru.standard1c.format.DocumentType;
import ru.standard1c.format.Encoding;
import ru.standard1c.format.PaymentType;
import ru.standard1c.reader.AttributeSource;
import ru.standard1c.reader.Reader;
import ru.standard1c.reader.ScannerAttributeSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.function.Function;

/**
 * @author Maxim Tereshchenko
 */
class ReaderWithTxtSamplesTest {

    @Test
    void givenFile_whenRead_thenBuildExpectedClientBankExchange() throws IOException, URISyntaxException {
        var dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        Function<String, LocalDate> dateMapper = date -> dateFormatter.parse(date, LocalDate::from);
        var timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        Function<String, LocalTime> timeMapper = time -> timeFormatter.parse(time, LocalTime::from);
        var actual = new Reader<>("1CClientBankExchange", "КонецФайла", name -> ClientBankExchange.builder())
                .onAttribute("ВерсияФормата", Float::parseFloat, ClientBankExchange.ClientBankExchangeBuilder::formatVersion)
                .onAttribute("Кодировка", Encoding::from, ClientBankExchange.ClientBankExchangeBuilder::encoding)
                .onAttribute("Отправитель", ClientBankExchange.ClientBankExchangeBuilder::sender)
                .onAttribute("Получатель", ClientBankExchange.ClientBankExchangeBuilder::receiver)
                .onAttribute("ДатаСоздания", dateMapper, ClientBankExchange.ClientBankExchangeBuilder::creationDate)
                .onAttribute("ВремяСоздания", timeMapper, ClientBankExchange.ClientBankExchangeBuilder::creationTime)
                .onAttribute("ДатаНачала", dateMapper, ClientBankExchange.ClientBankExchangeBuilder::startingDate)
                .onAttribute("ДатаКонца", dateMapper, ClientBankExchange.ClientBankExchangeBuilder::endingDate)
                .onAttribute("РасчСчет", ClientBankExchange.ClientBankExchangeBuilder::checkingAccount)
                .onAttribute("Документ", DocumentType::from, ClientBankExchange.ClientBankExchangeBuilder::documentType)
                .onSection(
                        new Reader<>("СекцияРасчСчет", "КонецРасчСчет", name -> CheckingAccountBalance.builder())
                                .onAttribute("ДатаНачала", dateMapper, CheckingAccountBalance.CheckingAccountBalanceBuilder::startingDate)
                                .onAttribute("ДатаКонца", dateMapper, CheckingAccountBalance.CheckingAccountBalanceBuilder::endingDate)
                                .onAttribute("РасчСчет", CheckingAccountBalance.CheckingAccountBalanceBuilder::checkingAccount)
                                .onAttribute("НачальныйОстаток", BigDecimal::new, CheckingAccountBalance.CheckingAccountBalanceBuilder::startingBalance)
                                .onAttribute("ВсегоПоступило", BigDecimal::new, CheckingAccountBalance.CheckingAccountBalanceBuilder::totalReceived)
                                .onAttribute("ВсегоСписано", BigDecimal::new, CheckingAccountBalance.CheckingAccountBalanceBuilder::totalDecommissioned)
                                .onAttribute("КонечныйОстаток", BigDecimal::new, CheckingAccountBalance.CheckingAccountBalanceBuilder::remainingBalance),
                        (clientBankExchangeBuilder, checkingAccountBalanceBuilder) -> clientBankExchangeBuilder.checkingAccountBalance(checkingAccountBalanceBuilder.build())
                )
                .onSection(
                        new Reader<>("СекцияДокумент", "КонецДокумента", DocumentType::from, type -> Document.builder().documentType(type))
                                .onAttribute("Номер", Integer::parseInt, Document.DocumentBuilder::number)
                                .onAttribute("Дата", dateMapper, Document.DocumentBuilder::date)
                                .onAttribute("Сумма", BigDecimal::new, Document.DocumentBuilder::sum)
                                .onAttribute("КвитанцияДата", dateMapper, Document.DocumentBuilder::receiptDate)
                                .onAttribute("КвитанцияВремя", timeMapper, Document.DocumentBuilder::receiptTime)
                                .onAttribute("КвитанцияСодержание", Document.DocumentBuilder::receiptContent)
                                .onAttribute("ПлательщикСчет", Document.DocumentBuilder::payerAccount)
                                .onAttribute("ДатаСписано", dateMapper, Document.DocumentBuilder::decommissionDate)
                                .onAttribute("Плательщик", Document.DocumentBuilder::payer)
                                .onAttribute("ПлательщикИНН", Document.DocumentBuilder::payerInn)
                                .onAttribute("Плательщик1", Document.DocumentBuilder::payer1)
                                .onAttribute("Плательщик2", Document.DocumentBuilder::payer2)
                                .onAttribute("Плательщик3", Document.DocumentBuilder::payer3)
                                .onAttribute("Плательщик4", Document.DocumentBuilder::payer4)
                                .onAttribute("ПлательщикРасчСчет", Document.DocumentBuilder::payerCheckingAccount)
                                .onAttribute("ПлательщикБанк1", Document.DocumentBuilder::payerBank1)
                                .onAttribute("ПлательщикБанк2", Document.DocumentBuilder::payerBank2)
                                .onAttribute("ПлательщикБИК", Document.DocumentBuilder::payerBic)
                                .onAttribute("ПлательщикКорсчет", Document.DocumentBuilder::payerCorrespondentAccount)
                                .onAttribute("ПолучательСчет", Document.DocumentBuilder::receiverAccount)
                                .onAttribute("ДатаПоступило", dateMapper, Document.DocumentBuilder::receivingDate)
                                .onAttribute("Получатель", Document.DocumentBuilder::receiver)
                                .onAttribute("ПолучательИНН", Document.DocumentBuilder::receiverInn)
                                .onAttribute("Получатель1", Document.DocumentBuilder::receiver1)
                                .onAttribute("Получатель2", Document.DocumentBuilder::receiver2)
                                .onAttribute("Получатель3", Document.DocumentBuilder::receiver3)
                                .onAttribute("Получатель4", Document.DocumentBuilder::receiver4)
                                .onAttribute("ПолучательРасчСчет", Document.DocumentBuilder::receiverCheckingAccount)
                                .onAttribute("ПолучательБанк1", Document.DocumentBuilder::receiverBank1)
                                .onAttribute("ПолучательБанк2", Document.DocumentBuilder::receiverBank2)
                                .onAttribute("ПолучательБИК", Document.DocumentBuilder::receiverBic)
                                .onAttribute("ПолучательКорсчет", Document.DocumentBuilder::receiverCorrespondentAccount)
                                .onAttribute("ВидПлатежа", PaymentType::from, Document.DocumentBuilder::paymentType)
                                .onAttribute("КодНазПлатежа", Integer::parseInt, Document.DocumentBuilder::paymentPurposeCode)
                                .onAttribute("ВидОплаты", Document.DocumentBuilder::transactionType)
                                .onAttribute("Код", Document.DocumentBuilder::code)
                                .onAttribute("НазначениеПлатежа", Document.DocumentBuilder::paymentPurpose)
                                .onAttribute("НазначениеПлатежа1", Document.DocumentBuilder::paymentPurpose1)
                                .onAttribute("НазначениеПлатежа2", Document.DocumentBuilder::paymentPurpose2)
                                .onAttribute("НазначениеПлатежа3", Document.DocumentBuilder::paymentPurpose3)
                                .onAttribute("НазначениеПлатежа4", Document.DocumentBuilder::paymentPurpose4)
                                .onAttribute("НазначениеПлатежа5", Document.DocumentBuilder::paymentPurpose5)
                                .onAttribute("НазначениеПлатежа6", Document.DocumentBuilder::paymentPurpose6)
                                .onAttribute("СтатусСоставителя", Document.DocumentBuilder::compilerStatus)
                                .onAttribute("ПлательщикКПП", Document.DocumentBuilder::payerKpp)
                                .onAttribute("ПолучательКПП", Document.DocumentBuilder::receiverKpp)
                                .onAttribute("ПоказательКБК", Document.DocumentBuilder::cbcIndicator)
                                .onAttribute("ОКАТО", Document.DocumentBuilder::oktmo)
                                .onAttribute("ПоказательОснования", Document.DocumentBuilder::basisIndicator)
                                .onAttribute("ПоказательПериода", Document.DocumentBuilder::periodIndicator)
                                .onAttribute("ПоказательНомера", Document.DocumentBuilder::numberIndicator)
                                .onAttribute("ПоказательДаты", dateMapper, Document.DocumentBuilder::dateIndicator)
                                .onAttribute("ПоказательТипа", Document.DocumentBuilder::typeIndicator)
                                .onAttribute("Очередность", Integer::parseInt, Document.DocumentBuilder::priority)
                                .onAttribute("СрокАкцепта", Integer::valueOf, Document.DocumentBuilder::acceptanceTerm)
                                .onAttribute("ВидАккредитива", Document.DocumentBuilder::letterOfCreditType)
                                .onAttribute("СрокПлатежа", dateMapper, Document.DocumentBuilder::paymentTerm)
                                .onAttribute("УсловиеОплаты1", Document.DocumentBuilder::paymentCondition1)
                                .onAttribute("УсловиеОплаты2", Document.DocumentBuilder::paymentCondition2)
                                .onAttribute("УсловиеОплаты3", Document.DocumentBuilder::paymentCondition3)
                                .onAttribute("ПлатежПоПредст", Document.DocumentBuilder::paymentOnPresentation)
                                .onAttribute("ДополнУсловия", Document.DocumentBuilder::additionalConditions)
                                .onAttribute("НомерСчетаПоставщика", Document.DocumentBuilder::supplierAccountNumber)
                                .onAttribute("ДатаОтсылкиДок", dateMapper, Document.DocumentBuilder::documentDispatchDate),
                        (clientBankExchangeBuilder, documentBuilder) -> clientBankExchangeBuilder.document(documentBuilder.build())
                )
                .read(attributeSourceFromFile())
                .build();

        var expected = ClientBankExchange.builder()
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

        assertThat(actual).isEqualTo(expected);
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
