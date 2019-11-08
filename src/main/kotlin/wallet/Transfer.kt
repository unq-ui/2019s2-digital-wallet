package wallet

import java.time.LocalDateTime

interface Transactional {
    val amount: Double
    val dateTime: LocalDateTime
    fun description(): String
    fun fullDescription(): String
    fun isCashOut(): Boolean
}

class InitialGift (
    val to: Account,
    override val amount: Double,
    override val dateTime: LocalDateTime
): Transactional {
    override fun description(): String = "Regalo de bienvenida"
    override fun fullDescription(): String = "Regalo de bienvenida por $$amount"
    override fun isCashOut() = false
}

class CashInLoyalty(
    override val amount: Double,
    override val dateTime: LocalDateTime
): Transactional {
    override fun description(): String = "Programa de beneficios"
    override fun fullDescription(): String = "Programa de beneficios por $$amount"
    override fun isCashOut() = false
}

class CashInWithCard(
    override val dateTime: LocalDateTime,
    override val amount: Double,
    val card: Card,
    val to: Account) : Transactional {
        init {
            assert(amount >= 0) { "Amount should be >= 0. Actual value: $amount" }
        }
        override fun description(): String = "Carga con tarjeta"
        override fun fullDescription(): String = "Carga con tarjeta ${card.maskedCard()} de $$amount"
        override fun isCashOut() = false
}

abstract class Transfer(
    override val dateTime: LocalDateTime,
    override val amount: Double,
    val from: Account,
    val to: Account
) : Transactional

class CashInTransfer (
    dateTime: LocalDateTime,
    amount: Double,
    from: Account,
    to: Account
) : Transfer(dateTime, amount, from, to) {
    init {
        assert(amount >= 0) { "Amount should be >= 0. Actual value: $amount" }
    }
    override fun description(): String = "Transferencia de Ingreso"
    override fun fullDescription(): String = "Transferencia de Ingreso desde ${from.cvu} por $$amount"
    override fun isCashOut() = false
}

class CashOutTransfer (
    datetime: LocalDateTime,
    amount: Double,
    from: Account,
    to: Account
) : Transfer(datetime, amount, from, to) {
    init {
        assert(amount <= 0) { "Amount should be <= 0. Actual value: $amount" }
    }
    override fun description(): String = "Transferencia de Egreso"
    override fun fullDescription(): String = "Transferencia de Egreso hacia $${from.cvu} por $amount"
    override fun isCashOut() = true
}

