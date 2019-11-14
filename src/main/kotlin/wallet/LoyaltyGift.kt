package wallet

import java.time.LocalDate
import java.time.LocalDateTime

class LoyaltyGift(
    val name: String,
    val strategy: LoyaltyGiftStrategy,
    val minNumberOfTransactions: Int,
    val minAmountPerTransaction: Double,
    val validFrom: LocalDate,
    val validTo: LocalDate
) {
    fun check(account: Account) : Boolean {
        val now = LocalDate.now()
        return account.getAllCashOutTransactions().filter {
            validFrom <= now && now <= validTo && it.amount >= minAmountPerTransaction
        }.size >= minNumberOfTransactions
    }

    fun apply(account: Account, transactional: Transactional) {
        account.addLoyalty(this)
        strategy.apply(account, transactional)
    }
}

interface LoyaltyGiftStrategy {
    fun appliedValue(): Double
    fun apply(account: Account, transactional: Transactional)
}

class DiscountGiftStrategy(val percentage: Double) : LoyaltyGiftStrategy {
    override fun appliedValue(): Double = percentage
    override fun apply(account: Account, transactional: Transactional) {
        account.addTransaction(CashInLoyalty(transactional.amount * percentage / 100.0, LocalDateTime.now()))
    }
}
class FixedGiftStrategy(val amount: Double) : LoyaltyGiftStrategy {
    override fun appliedValue(): Double = amount
    override fun apply(account: Account, transactional: Transactional) {
        account.addTransaction(CashInLoyalty(amount, LocalDateTime.now()))
    }
}
