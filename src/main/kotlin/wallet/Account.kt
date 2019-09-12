package wallet

interface Accountable {
    fun addTransaction(account: Account, transaction: Transactional)
    fun addLoyalty(account: Account, loyaltyGift: LoyaltyGift)
}

class Account(val user: User, val cvu: String) {

    var balance: Double = 0.0
    val transactions: MutableList<Transactional> = mutableListOf()
    val appliedLoyalties: MutableList<LoyaltyGift> = mutableListOf()
    private var state: Accountable = UnblockedState()
    var isBlocked = false

    fun addTransaction(transaction: Transactional) {
        state.addTransaction(this, transaction)
    }

    fun block() {
        state = BlockedState()
        isBlocked = true
    }

    fun unblock() {
        state = UnblockedState()
        isBlocked = false
    }

    fun addLoyalty(loyaltyGift: LoyaltyGift) {
        state.addLoyalty(this, loyaltyGift)
    }

    fun isLoyaltyApplied(loyaltyGift: LoyaltyGift): Boolean {
        return appliedLoyalties.contains(loyaltyGift)
    }

    fun getAllCashOutTransactions() : List<Transactional> {
        return transactions.filter { it.isCashOut() }
    }
}

class UnblockedState: Accountable {
    override fun addTransaction(account: Account, transaction: Transactional) {
        account.transactions.add(transaction)
        account.balance += transaction.amount
    }

    override fun addLoyalty(account: Account, loyaltyGift: LoyaltyGift) {
        account.appliedLoyalties.add(loyaltyGift)
    }
}

class BlockedState: Accountable {
    override fun addTransaction(account: Account, transaction: Transactional) {
        throw BlockedAccountException("Account with cvu ${account.cvu} is blocked and unable to perform operations")
    }

    override fun addLoyalty(account: Account, loyaltyGift: LoyaltyGift) {
        throw BlockedAccountException("Account with cvu ${account.cvu} is blocked and unable to accept any loyalty")
    }
}
