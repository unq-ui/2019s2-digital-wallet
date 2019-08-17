package wallet

import java.time.LocalDateTime

interface Transactional {
    val amount: Double
    val dateTime: LocalDateTime
    fun description(): String
    fun fullDescription(): String
}

class InitialGift (
    val to: Account,
    override val amount: Double,
    override val dateTime: LocalDateTime
): Transactional {
    override fun description(): String = "Regalo de bienvenida"
    override fun fullDescription(): String = "Regalo de bienvenida por $$amount"
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
    override fun fullDescription(): String = "Transferencia de Ingreso desde $from por $$amount"
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
    override fun fullDescription(): String = "Transferencia de Egreso hacia $$from por $amount"
}

