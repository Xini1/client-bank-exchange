package ru.standard1c.format;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class ClientBankExchange {

    float formatVersion;
    Encoding encoding;
    String sender;
    String receiver;
    LocalDate creationDate;
    LocalTime creationTime;
    LocalDate startingDate;
    LocalDate endingDate;
    String checkingAccount;
    @Singular("documentType")
    List<DocumentType> documentTypeList;
    @Singular("checkingAccountBalance")
    List<CheckingAccountBalance> checkingAccountBalanceList;
    @Singular("document")
    List<Document> documentList;
}
