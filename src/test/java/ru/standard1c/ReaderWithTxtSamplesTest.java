package ru.standard1c;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import ru.standard1c.format.CheckingAccountBalance;
import ru.standard1c.format.ClientBankExchange;
import ru.standard1c.format.Document;
import ru.standard1c.format.DocumentType;
import ru.standard1c.format.Encoding;
import ru.standard1c.format.PaymentType;
import ru.standard1c.reader.ClientBankExchangeReader;
import ru.standard1c.reader.source.AttributeSource;
import ru.standard1c.reader.source.ScannerAttributeSource;
import ru.standard1c.reader.source.SkippingEmptyAttributeSource;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author Maxim Tereshchenko
 */
class ReaderWithTxtSamplesTest {

    @TestFactory
    Stream<DynamicTest> tests() {
        return Map.of(
                        "sample1.txt", expected1(),
                        "sample2.txt", expected2(),
                        "sample3.txt", expected3(),
                        "sample4.txt", expected4(),
                        "sample5.txt", expected5()
                )
                .entrySet()
                .stream()
                .map(pair -> toDynamicTest(pair.getKey(), pair.getValue()));
    }

    private DynamicTest toDynamicTest(String fileName, ClientBankExchange expected) {
        return dynamicTest(
                "given " + fileName + ", then build expected ClientBankExchange",
                () -> assertThat(
                        new ClientBankExchangeReader()
                                .read(attributeSourceFromFile(fileName))
                )
                        .isEqualTo(expected)
        );
    }

    private ClientBankExchange expected1() {
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
                                document1(date, time, amount, DocumentType.PAYMENT_ORDER),
                                document1(date, time, amount, DocumentType.PAYMENT_CLAIM)
                        )
                )
                .build();
    }

    private ClientBankExchange expected2() {
        return ClientBankExchange.builder()
                .formatVersion(1.01f)
                .encoding(Encoding.WINDOWS)
                .sender("OEBS R12")
                .receiver("ДБО BS-Client v.3 BSS")
                .creationDate(LocalDate.of(2021, 8, 20))
                .creationTime(LocalTime.of(7, 27, 12))
                .startingDate(LocalDate.of(2021, 8, 20))
                .endingDate(LocalDate.of(2021, 8, 20))
                .checkingAccount("40706810900000064381")
                .documentTypeList(Collections.singletonList(DocumentType.PAYMENT_ORDER))
                .documentList(Collections.singletonList(document2()))
                .build();
    }

    private ClientBankExchange expected3() {
        return ClientBankExchange.builder()
                .formatVersion(1.03f)
                .encoding(Encoding.WINDOWS)
                .sender("OEBS R12")
                .creationDate(LocalDate.of(2021, 8, 20))
                .creationTime(LocalTime.of(8, 20, 16))
                .startingDate(LocalDate.of(2021, 8, 20))
                .endingDate(LocalDate.of(2021, 8, 20))
                .checkingAccount("40702810942020002415")
                .documentTypeList(Collections.singletonList(DocumentType.PAYMENT_ORDER))
                .documentList(Collections.singletonList(document3()))
                .build();
    }

    private ClientBankExchange expected4() {
        return ClientBankExchange.builder()
                .formatVersion(1.03f)
                .encoding(Encoding.WINDOWS)
                .sender("OEBS R12")
                .creationDate(LocalDate.of(2021, 8, 20))
                .creationTime(LocalTime.of(8, 21, 48))
                .startingDate(LocalDate.of(2021, 8, 20))
                .endingDate(LocalDate.of(2021, 8, 20))
                .checkingAccount("40821810840020000005")
                .documentTypeList(Collections.singletonList(DocumentType.PAYMENT_ORDER))
                .documentList(Collections.singletonList(document4()))
                .build();
    }

    private ClientBankExchange expected5() {
        return ClientBankExchange.builder()
                .formatVersion(1.03f)
                .encoding(Encoding.WINDOWS)
                .sender("OEBS R12")
                .receiver("ДБО BS-Client v.3 BSS")
                .creationDate(LocalDate.of(2021, 8, 20))
                .creationTime(LocalTime.of(8, 23, 18))
                .startingDate(LocalDate.of(2021, 8, 20))
                .endingDate(LocalDate.of(2021, 8, 20))
                .checkingAccount("40702810800030005413")
                .documentTypeList(Collections.singletonList(DocumentType.PAYMENT_ORDER))
                .documentList(
                        List.of(
                                document5_1(),
                                document5_2(),
                                document5_3(),
                                document5_4(),
                                document5_5(),
                                document5_6()
                        )
                )
                .build();
    }

    private Document document1(LocalDate date, LocalTime time, BigDecimal amount, DocumentType documentType) {
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

    private Document document2() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(119)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40706810900000064381")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40706810900000064381")
                .payerBank1("ПАО \"ПРОМСВЯЗЬБАНК\"")
                .payerBic("044525555")
                .payerCorrespondentAccount("30101810400000000555")
                .receiverAccount("40702810938120061991")
                .receiver("АО \"Российские космические системы\"")
                .receiverInn("7722698789")
                .receiver1("АО \"Российские космические системы\"")
                .receiverCheckingAccount("40702810938120061991")
                .receiverBank1("ПАО СБЕРБАНК")
                .receiverBank2("г Москва")
                .receiverBic("044525225")
                .receiverCorrespondentAccount("30101810400000000225")
                .paymentType(PaymentType.DIGITAL)
                .operationType("01")
                .payerKpp("770545001")
                .receiverKpp("774550001")
                .priority(5)
                .code("2021730100412217000244871")
                .paymentPurpose("Тип 3.Расчет за оказ.услуги. в т.ч. НДС 20%: 0.00")
                .build();
    }

    private Document document3() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(288)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810942020002415")
                .payerInn("7707049388")
                .payerKpp("770545001")
                .payer("ПАО \"Ростелеком\"")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810942020002415")
                .payerBank1("ВОЛГО-ВЯТСКИЙ БАНК ПАО СБЕРБАНК")
                .payerBic("042202603")
                .payerCorrespondentAccount("30101810900000000603")
                .receiverAccount("03100643000000015400")
                .receiverInn("5753029294")
                .receiverKpp("575301001")
                .receiver("Управление Федерального казначейства по Орловской области (УПРАВЛЕНИЕ ФЕДЕРАЛЬНОЙ СЛУЖБЫ БЕЗОПАСНОСТИ ПО ОРЛОВСКОЙ ОБЛАСТИ)")
                .receiver1("Управление Федерального казначейства по Орловской области (УПРАВЛЕНИЕ ФЕДЕРАЛЬНОЙ СЛУЖБЫ БЕЗОПАСНОСТИ ПО ОРЛОВСКОЙ ОБЛАСТИ)")
                .receiverCheckingAccount("03100643000000015400")
                .receiverBank1("ОТДЕЛЕНИЕ ОРЕЛ БАНКА РОССИИ//УФК по Орловской области")
                .receiverBank2("Г. Орёл")
                .receiverBic("015402901")
                .receiverCorrespondentAccount("40102810545370000046")
                .paymentType(PaymentType.DIGITAL)
                .operationType("01")
                .priority(1)
                .compilerStatus("08")
                .cbcIndicator("18911301050016000130")
                .oktmo("54701000")
                .paymentPurpose("Расчет по договору N 0320 от 21.06.2021 за проведение экспертизы. Не облагается НДС")
                .build();
    }

    private Document document4() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(23720)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40821810840020000005")
                .payerInn("7707049388")
                .payerKpp("770545001")
                .payer("ПАО \"Ростелеком\"")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40821810840020000005")
                .payerBank1("ПАО СБЕРБАНК")
                .payerBank2("г Москва")
                .payerBic("044525225")
                .payerCorrespondentAccount("30101810400000000225")
                .receiverAccount("40821810462000000144")
                .receiverInn("1650136610")
                .receiverKpp("165001001")
                .receiver("ТСЖ \"БЕРЕГ\"")
                .receiver1("ТСЖ \"БЕРЕГ\"")
                .receiverCheckingAccount("40821810462000000144")
                .receiverBank1("ОТДЕЛЕНИЕ \"БАНК ТАТАРСТАН\" N8610 ПАО СБЕРБАНК")
                .receiverBic("049205603")
                .receiverCorrespondentAccount("30101810600000000603")
                .paymentType(PaymentType.DIGITAL)
                .operationType("01")
                .priority(5)
                .paymentPurpose("За Татарстан. Расчет по договору N б/н от 01.09.2008 за аренду. Не облагается НДС")
                .build();
    }

    private Document document5_1() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2335)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("03100643000000012100")
                .receiver("УФК по Ставропольскому краю (ИФНС по Ленинскому району г.Ставрополя)")
                .receiverInn("2635028348")
                .receiver1("УФК по Ставропольскому краю (ИФНС по Ленинскому району г.Ставрополя)")
                .receiverCheckingAccount("03100643000000012100")
                .receiverBank1("ОТДЕЛЕНИЕ СТАВРОПОЛЬ БАНКА РОССИИ//УФК по Ставропольскому краю г. Ставрополь")
                .receiverBic("010702101")
                .receiverCorrespondentAccount("40102810345370000013")
                .operationType("01")
                .compilerStatus("02")
                .payerKpp("263445004")
                .receiverKpp("263501001")
                .cbcIndicator("18210102010011000110")
                .oktmo("07701000")
                .basisIndicator("ТП")
                .periodIndicator("МС.08.2021")
                .dateIndicator(LocalDate.of(2021, 8, 20))
                .priority(3)
                .paymentPurpose("НДФЛ Август 2021 Не облагается НДС")
                .build();
    }

    private Document document5_2() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2322)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("03100643000000013400")
                .receiver("УФК ПО ИРКУТСКОЙ ОБЛАСТИ (МЕЖРАЙОННАЯ ИФНС РОССИИ № 18 ПО ИРКУТСКОЙ ОБЛАСТИ)")
                .receiverInn("3819023623")
                .receiver1("УФК ПО ИРКУТСКОЙ ОБЛАСТИ (МЕЖРАЙОННАЯ ИФНС РОССИИ № 18 ПО ИРКУТСКОЙ ОБЛАСТИ)")
                .receiverCheckingAccount("03100643000000013400")
                .receiverBank1("ОТДЕЛЕНИЕ ИРКУТСК БАНКА РОССИИ//УФК ПО ИРКУТСКОЙ ОБЛАСТИ г. Иркутск")
                .receiverBic("012520101")
                .receiverCorrespondentAccount("40102810145370000026")
                .operationType("01")
                .compilerStatus("02")
                .payerKpp("385145001")
                .receiverKpp("385101001")
                .cbcIndicator("18210102010011000110")
                .oktmo("25736000")
                .basisIndicator("ТП")
                .periodIndicator("МС.08.2021")
                .dateIndicator(LocalDate.of(2021, 8, 20))
                .priority(3)
                .paymentPurpose("НДФЛ за август 2021 г. Не облагается НДС")
                .build();
    }

    private Document document5_3() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2991)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("03100643000000016900")
                .receiver("УФК МИНФИНА РОССИИ ПО ЧЕЛЯБИНСКОЙ ОБЛАСТИ (МЕЖРАЙОННАЯ ИФНС РОССИИ №23 ПО ЧЕЛЯБИНСКОЙ ОБЛАСТИ)")
                .receiverInn("7415005658")
                .receiver1("УФК МИНФИНА РОССИИ ПО ЧЕЛЯБИНСКОЙ ОБЛАСТИ (МЕЖРАЙОННАЯ ИФНС РОССИИ №23 ПО ЧЕЛЯБИНСКОЙ ОБЛАСТИ)")
                .receiverCheckingAccount("03100643000000016900")
                .receiverBank1("ОТДЕЛЕНИЕ ЧЕЛЯБИНСК БАНКА РОССИИ//УФК по Челябинской области г. Челябинск")
                .receiverBic("017501500")
                .receiverCorrespondentAccount("40102810645370000062")
                .operationType("01")
                .compilerStatus("02")
                .payerKpp("741545001")
                .receiverKpp("741501001")
                .cbcIndicator("18210102010011000110")
                .oktmo("75742000")
                .basisIndicator("ТП")
                .periodIndicator("МС.08.2021")
                .dateIndicator(LocalDate.of(2021, 8, 20))
                .priority(3)
                .paymentPurpose("НДФЛ за август 2021 года. Не облагается НДС")
                .build();
    }

    private Document document5_4() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2349)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("03100643000000013400")
                .receiver("УФК по Иркутской области (Межрайонная ИФНС России № 18 по Иркутской области)")
                .receiverInn("3819023623")
                .receiver1("УФК по Иркутской области (Межрайонная ИФНС России № 18 по Иркутской области)")
                .receiverCheckingAccount("03100643000000013400")
                .receiverBank1("ОТДЕЛЕНИЕ ИРКУТСК БАНКА РОССИИ//УФК ПО ИРКУТСКОЙ ОБЛАСТИ г. Иркутск")
                .receiverBic("012520101")
                .receiverCorrespondentAccount("40102810145370000026")
                .operationType("01")
                .compilerStatus("01")
                .payerKpp("770543002")
                .receiverKpp("385101001")
                .cbcIndicator("18210803010011050110")
                .oktmo("25736000")
                .basisIndicator("ТП")
                .periodIndicator("МС.08.2021")
                .priority(5)
                .paymentPurpose("За рассмотрение заявления о выдаче судебного приказа. Не облагается НДС")
                .build();
    }

    private Document document5_5() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2451)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("03100643000000017200")
                .receiver("УФК по г. Санкт-Петербургу (Межрайонная инспекция Федеральной налоговой службы № 11 по Санкт-Петербургу)")
                .receiverInn("7842000011")
                .receiver1("УФК по г. Санкт-Петербургу (Межрайонная инспекция Федеральной налоговой службы № 11 по Санкт-Петербургу)")
                .receiverCheckingAccount("03100643000000017200")
                .receiverBank1("СЕВЕРО-ЗАПАДНОЕ ГУ БАНКА РОССИИ//УФК по г.Санкт-Петербургу г. Санкт-Петербург")
                .receiverBic("014030106")
                .receiverCorrespondentAccount("40102810945370000005")
                .operationType("01")
                .compilerStatus("01")
                .payerKpp("784201001")
                .receiverKpp("784201001")
                .cbcIndicator("18210202090071010160")
                .oktmo("40911000")
                .basisIndicator("ТП")
                .periodIndicator("МС.07.2021")
                .dateIndicator(LocalDate.of(2021, 8, 20))
                .priority(5)
                .paymentPurpose("Страховые взносы на обязательное социальное страхование на случай временной нетрудоспособности и в связи с материнством. Не облагается НДС")
                .build();
    }

    private Document document5_6() {
        return Document.builder()
                .documentType(DocumentType.PAYMENT_ORDER)
                .number(2930)
                .date(LocalDate.of(2021, 8, 20))
                .sum(new BigDecimal("0.01"))
                .payerAccount("40702810800030005413")
                .payer("ПАО \"Ростелеком\"")
                .payerInn("7707049388")
                .payer1("ПАО \"Ростелеком\"")
                .payerCheckingAccount("40702810800030005413")
                .payerBank1("БАНК ВТБ (ПАО)")
                .payerBic("044525187")
                .payerCorrespondentAccount("30101810700000000187")
                .receiverAccount("40822810400000000006")
                .receiver("ПАО \"МОБИЛЬНЫЕ ТЕЛЕСИСТЕМЫ\"")
                .receiverInn("7740000076")
                .receiver1("ПАО \"МОБИЛЬНЫЕ ТЕЛЕСИСТЕМЫ\"")
                .receiverCheckingAccount("40822810400000000006")
                .receiverBank1("ПАО \"МТС-БАНК\"")
                .receiverBic("044525232")
                .receiverCorrespondentAccount("30101810600000000232")
                .operationType("01")
                .payerKpp("770545001")
                .receiverKpp("434502001")
                .priority(5)
                .code("424330352486400000000ЛС12")
                .paymentPurpose("Расчет по договору N Н 394/536-10-16 от 05.05.2010 за услуги связи. в т.ч. НДС 20%: 0.00")
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

    private AttributeSource attributeSourceFromFile(String fileName) throws URISyntaxException, IOException {
        return new SkippingEmptyAttributeSource(
                new ScannerAttributeSource(
                        new Scanner(
                                Paths.get(
                                        Objects.requireNonNull(
                                                        getClass()
                                                                .getClassLoader()
                                                                .getResource(fileName)
                                                )
                                                .toURI()
                                ),
                                StandardCharsets.UTF_8
                        )
                )
        );
    }
}
