# Стандарт обмена с системами «Клиент банка»

Библиотека для чтения документов в формате 1С Стандарт обмена с системами «Клиент банка». Описание
формата: https://v8.1c.ru/tekhnologii/obmen-dannymi-i-integratsiya/standarty-i-formaty/standart-obmena-s-sistemami-klient-banka/formaty-obmena/

## Пример использования

Файл sample.txt:

```
1CClientBankExchange
ВерсияФормата=1.03
Кодировка=Windows
Отправитель=Отправитель
Получатель=Получатель
ДатаСоздания=01.01.2020
ВремяСоздания=10:00:00
ДатаНачала=01.01.2020
ДатаКонца=01.01.2020
РасчСчет=12345678901234567890
Документ=Платежное поручение
...
```

Создание и использование [`ClientBankExchangeReader`](https://github.com/Xini1/client-bank-exchange/blob/master/src/main/java/ru/standard1c/reader/ClientBankExchangeReader.java):

```java
Path path = Paths.get(Objects.requireNonNull(getClass().getClassLoader().getResource("sample.txt")).toURI());
try (Scanner scanner = new Scanner(path, StandardCharsets.UTF_8)) {
    ClientBankExchangeReader clientBankExchangeReader = new ClientBankExchangeReader();
    ClientBankExchange clientBankExchange = clientBankExchangeReader.read(new ScannerAttributeSource(scanner));
    System.out.println(clientBankExchange.getFormatVersion()); //1.03
}
```