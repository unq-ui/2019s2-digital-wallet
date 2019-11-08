package wallet

import java.time.LocalDate

abstract class Card(
    val cardNumber: String,
    val fullName: String,
    val endDate: LocalDate,
    val securityCode: String
) {
    fun maskedCard(): String {
        return cardNumber.mapIndexed { index, char ->
            if (char == ' ' || index >= cardNumber.length - 4) char else 'x'
        }.joinToString("")
    }
}

class CreditCard(
    cardNumber: String,
    fullName: String,
    endDate: LocalDate,
    securityCode: String
) : Card(cardNumber, fullName, endDate, securityCode)

class DebitCard(
    cardNumber: String,
    fullName: String,
    endDate: LocalDate,
    securityCode: String
) : Card(cardNumber, fullName, endDate, securityCode)