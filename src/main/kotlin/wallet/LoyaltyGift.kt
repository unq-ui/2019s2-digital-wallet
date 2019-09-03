package wallet

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Nombre
 * Tipo de Beneficio (descuento o regalo)
 * Porcentaje de descuento (en caso de ser descuento)
 * Importe de Regalo (en caso de ser regalo)
 * Cantidad de operaciones (*)
 * Importe de Cada Operación(**)
 * Vigencia Fecha Desde
 * Vigencia Fecha Hasta
 */
class LoyaltyGift(
    val name: String,
    val strategy: LoyaltyGiftStrategy,
    val minNumberOfTransactions: Int,
    val minAmountPerTransaction: Int,
    val validFrom: LocalDate,
    val validTo: LocalDate
) {
    fun check(account: Account) : Boolean {
        val date = LocalDate.now()
        return account.getAllCashOutTransactions().filter {
            validFrom.isAfter(date) && validTo.isBefore(date) && it.amount >= minAmountPerTransaction
        }.size >= minNumberOfTransactions
    }

    fun apply(account: Account, transactional: Transactional) {
        account.addLoyalty(this)
        strategy.apply(account, transactional)
    }
}

interface LoyaltyGiftStrategy {
    fun apply(account: Account, transactional: Transactional)
}

class DiscountGiftStrategy(val percentage: Int) : LoyaltyGiftStrategy {
    override fun apply(account: Account, transactional: Transactional) {
        account.addTransaction(CashInLoyalty(transactional.amount * percentage / 100.0, LocalDateTime.now()))
    }
}
class FixedGiftStrategy(val amount: Double) : LoyaltyGiftStrategy {
    override fun apply(account: Account, transactional: Transactional) {
        account.addTransaction(CashInLoyalty(amount, LocalDateTime.now()))
    }
}