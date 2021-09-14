package ru.standard1c.format;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Представление файла в формате "Стандарт обмена с системами «Клиент банка»".
 *
 * @author Maxim Tereshchenko
 */
@Value
@Builder
public class ClientBankExchange {

    /**
     * Номер версии формата обмена.
     */
    float formatVersion;

    /**
     * Кодировка файла.
     */
    Encoding encoding;

    /**
     * Программа-отправитель.
     */
    String sender;

    /**
     * Программа-получатель.
     */
    String receiver;

    /**
     * Дата формирования файла.
     */
    LocalDate creationDate;

    /**
     * Время формирования файла.
     */
    LocalTime creationTime;

    /**
     * Дата начала интервала.
     */
    LocalDate startingDate;

    /**
     * Дата конца интервала.
     */
    LocalDate endingDate;

    /**
     * Расчетные счета организации.
     */
    @Singular("checkingAccount")
    List<String> checkingAccountList;

    /**
     * Виды документов.
     */
    @Singular("documentType")
    List<DocumentType> documentTypeList;

    /**
     * Секции передачи остатков по расчетному счету.
     */
    @Singular("checkingAccountBalance")
    List<CheckingAccountBalance> checkingAccountBalanceList;

    /**
     * Секции платежных документов.
     */
    @Singular("document")
    List<Document> documentList;
}
