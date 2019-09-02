package wallet

interface Accountable {
    fun addTransaction(transaction: Transactional)
}

class Account(val user: User, val cvu: String, appliedLoyalties: MutableList<LoyaltyGift>) : Accountable {
    override fun addTransaction(transaction: Transactional) {
        transactions.add(transaction)
        balance += transaction.amount
    }

    fun addLoyalty(loyaltyGift: LoyaltyGift) {
        this.appliedLoyalties.add(loyaltyGift)
    }

    fun isLoyaltyApplied(loyaltyGift: LoyaltyGift): Boolean {
        return this.appliedLoyalties.includes(loyaltyGift)
    }

    fun getAllCashOutTransactions() : List<Transactional> {
        return this.transactions.filter { it.isCashOut() }
    }

    var balance: Double = 0.0
    val transactions: MutableList<Transactional> = mutableListOf()
}
