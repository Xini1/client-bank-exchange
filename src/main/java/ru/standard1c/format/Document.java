package ru.standard1c.format;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Секция платежного документа.
 *
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class Document {

    /**
     * Вид документа.
     */
    DocumentType documentType;

    /**
     * Номер документа.
     */
    int number;

    /**
     * Дата документа.
     */
    LocalDate date;

    /**
     * Сумма платежа.
     */
    BigDecimal sum;

    /**
     * Дата формирования квитанции.
     */
    LocalDate receiptDate;

    /**
     * Время формирования квитанции.
     */
    LocalTime receiptTime;

    /**
     * Содержание квитанции.
     */
    String receiptContent;

    /**
     * Расчетный счет плательщика.
     */
    String payerAccount;

    /**
     * Дата списания средств с р/с.
     */
    LocalDate decommissionDate;

    /**
     * Плательщик.
     */
    String payer;

    /**
     * ИНН плательщика.
     */
    String payerInn;

    /**
     * Наименование плательщика, стр. 1.
     */
    String payer1;

    /**
     * Наименование плательщика, стр. 2.
     */
    String payer2;

    /**
     * Наименование плательщика, стр. 3.
     */
    String payer3;

    /**
     * Наименование плательщика, стр. 4.
     */
    String payer4;

    /**
     * Расчетный счет плательщика.
     */
    String payerCheckingAccount;

    /**
     * Банк плательщика.
     */
    String payerBank1;

    /**
     * Город банка плательщика.
     */
    String payerBank2;

    /**
     * БИК банка плательщика.
     */
    String payerBic;

    /**
     * Корсчет банка плательщика.
     */
    String payerCorrespondentAccount;

    /**
     * Расчетный счет получателя.
     */
    String receiverAccount;

    /**
     * Дата поступления средств на р/с.
     */
    LocalDate receivingDate;

    /**
     * Получатель.
     */
    String receiver;

    /**
     * ИНН получателя.
     */
    String receiverInn;

    /**
     * Наименование получателя.
     */
    String receiver1;

    /**
     * Наименование получателя, стр. 2.
     */
    String receiver2;

    /**
     * Наименование получателя, стр. 3.
     */
    String receiver3;

    /**
     * Наименование получателя, стр. 4.
     */
    String receiver4;

    /**
     * Расчетный счет получателя.
     */
    String receiverCheckingAccount;

    /**
     * Банк получателя.
     */
    String receiverBank1;

    /**
     * Город банка получателя.
     */
    String receiverBank2;

    /**
     * БИК банка получателя.
     */
    String receiverBic;

    /**
     * Корсчет банка получателя.
     */
    String receiverCorrespondentAccount;

    /**
     * Вид платежа.
     */
    PaymentType paymentType;

    /**
     * Назначение платежа кодовое.
     */
    int paymentPurposeCode;

    /**
     * Вид оплаты (вид операции).
     */
    String operationType;

    /**
     * Уникальный идентификатор платежа/
     */
    String code;

    /**
     * Назначение платежа/
     */
    String paymentPurpose;

    /**
     * Назначение платежа, стр. 1.
     */
    String paymentPurpose1;

    /**
     * Назначение платежа, стр. 2.
     */
    String paymentPurpose2;

    /**
     * Назначение платежа, стр. 3.
     */
    String paymentPurpose3;

    /**
     * Назначение платежа, стр. 4.
     */
    String paymentPurpose4;

    /**
     * Назначение платежа, стр. 5.
     */
    String paymentPurpose5;

    /**
     * Назначение платежа, стр. 6.
     */
    String paymentPurpose6;

    /**
     * Статус составителя расчетного документа.
     */
    String compilerStatus;

    /**
     * КПП плательщика
     */
    String payerKpp;

    /**
     * КПП получателя/
     */
    String receiverKpp;

    /**
     * Показатель кода бюджетной классификации.
     */
    String cbcIndicator;

    /**
     * Код ОКТМО территории, на которой мобилизуются
     * денежные средства от уплаты налога, сбора и иного платежа.
     */
    String oktmo;

    /**
     * Показатель основания налогового платежа/
     */
    String basisIndicator;

    /**
     * Показатель налогового периода / Код таможенного органа.
     */
    String periodIndicator;

    /**
     * Показатель номера документа.
     */
    String numberIndicator;

    /**
     * Показатель даты документа.
     */
    LocalDate dateIndicator;

    /**
     * Код выплат указывается при переводе денежных
     * средств физическим лицам в целях осуществления
     * выплат за счет средств бюджетов бюджетной системы РФ.
     */
    String typeIndicator;

    /**
     * Очередность платежа.
     */
    int priority;

    /**
     * Срок акцепта, количество дней.
     */
    Integer acceptanceTerm;

    /**
     * Вид аккредитива.
     */
    String letterOfCreditType;

    /**
     * Срок платежа (аккредитива).
     */
    LocalDate paymentTerm;

    /**
     * Условие оплаты, стр. 1.
     */
    String paymentCondition1;

    /**
     * Условие оплаты, стр. 2.
     */
    String paymentCondition2;

    /**
     * Условие оплаты, стр. 3.
     */
    String paymentCondition3;

    /**
     * Платеж по представлению.
     */
    String paymentOnPresentation;

    /**
     * Дополнительные условия.
     */
    String additionalConditions;

    /**
     * № счета поставщика.
     */
    String supplierAccountNumber;

    /**
     * Дата отсылки документов.
     */
    LocalDate documentDispatchDate;
}
