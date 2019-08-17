package wallet

import java.time.LocalDate

abstract class Card(
    val cardNumber: String,
    val fullName: String,
    val endDate: LocalDate,
    val securityCode: String
) : Accountable

class CreditCard(
    cardNumber: String,
    fullName: String,
    endDate: LocalDate,
    securityCode: String
) : Card(cardNumber, fullName, endDate, securityCode) {
    override fun addTransaction(transaction: Transactional) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class DebitCard(
    cardNumber: String,
    fullName: String,
    endDate: LocalDate,
    securityCode: String
) : Card(cardNumber, fullName, endDate, securityCode) {
    override fun addTransaction(transaction: Transactional) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}