package wallet

interface Accountable {
    fun addTransaction(transaction: Transactional)
}

class Account(val user: User, val cvu: String) : Accountable {

    var balance: Double = 0.0
    val transactions: MutableList<Transactional> = mutableListOf()
    val appliedLoyalties: MutableList<LoyaltyGift> = mutableListOf()

    override fun addTransaction(transaction: Transactional) {
        transactions.add(transaction)
        balance += transaction.amount
    }

    fun addLoyalty(loyaltyGift: LoyaltyGift) {
        appliedLoyalties.add(loyaltyGift)
    }

    fun isLoyaltyApplied(loyaltyGift: LoyaltyGift): Boolean {
        return appliedLoyalties.contains(loyaltyGift)
    }

    fun getAllCashOutTransactions() : List<Transactional> {
        return transactions.filter { it.isCashOut() }
    }
}
