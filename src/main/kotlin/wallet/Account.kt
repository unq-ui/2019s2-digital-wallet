package wallet

interface Accountable {
    fun addTransaction(transaction: Transactional)
}

class Account(val user: User, val cvu: String) : Accountable {
    override fun addTransaction(transaction: Transactional) {
        transactions.add(transaction)
        balance += transaction.amount
    }

    var balance: Double = 0.0
    val transactions: MutableList<Transactional> = mutableListOf()
}
