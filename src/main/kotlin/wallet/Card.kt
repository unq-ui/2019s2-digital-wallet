package wallet

import java.time.LocalDate

abstract class Card(
    val cardNumber: String,
    val fullName: String,
    val endDate: LocalDate,
    val securityCode: String
) {
    fun maskedCard(): String {
        val card = cardNumber.split(" ").joinToString { "" }
        return "xxxx xxxx xxxx ${card.subSequence(card.length - 4, card.length)}"
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