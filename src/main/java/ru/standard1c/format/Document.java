package ru.standard1c.format;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class Document {

    DocumentType documentType;
    int number;
    LocalDate date;
    BigDecimal sum;
    LocalDate receiptDate;
    LocalTime receiptTime;
    String receiptContent;
    String payerAccount;
    LocalDate decommissionDate;
    String payer;
    String payerInn;
    String payer1;
    String payer2;
    String payer3;
    String payer4;
    String payerCheckingAccount;
    String payerBank1;
    String payerBank2;
    String payerBic;
    String payerCorrespondentAccount;
    String receiverAccount;
    LocalDate receivingDate;
    String receiver;
    String receiverInn;
    String receiver1;
    String receiver2;
    String receiver3;
    String receiver4;
    String receiverCheckingAccount;
    String receiverBank1;
    String receiverBank2;
    String receiverBic;
    String receiverCorrespondentAccount;
    PaymentType paymentType;
    int paymentPurposeCode;
    String transactionType;
    String code;
    String paymentPurpose;
    String paymentPurpose1;
    String paymentPurpose2;
    String paymentPurpose3;
    String paymentPurpose4;
    String paymentPurpose5;
    String paymentPurpose6;
    String compilerStatus;
    String payerKpp;
    String receiverKpp;
    String cbcIndicator;
    String oktmo;
    String basisIndicator;
    String periodIndicator;
    String numberIndicator;
    LocalDate dateIndicator;
    String typeIndicator;
    int priority;
    Integer acceptanceTerm;
    String letterOfCreditType;
    LocalDate paymentTerm;
    String paymentCondition1;
    String paymentCondition2;
    String paymentCondition3;
    String paymentOnPresentation;
    String additionalConditions;
    String supplierAccountNumber;
    LocalDate documentDispatchDate;
}
