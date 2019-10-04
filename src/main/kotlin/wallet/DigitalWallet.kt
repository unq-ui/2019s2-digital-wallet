package wallet

import java.time.LocalDateTime
import kotlin.random.Random


fun assert(condition: Boolean, lazyMessage: () -> String) {
    if(!condition) throw Exception(lazyMessage());
}

class DigitalWallet {
    val users = mutableListOf<User>()
    val accounts = mutableListOf<Account>()
    val loyaltyGifts = mutableListOf<LoyaltyGift>()

    companion object Support {
        private var random = Random(5988)
        private fun now() = LocalDateTime.now()
        fun generateNewCVU(): String {
            return "${random.nextInt(0, 1000000000)}".padStart(9, '0')
        }
        fun createGift(account: Account, amount: Double) = InitialGift(account, amount, now())
        fun createCashTransfer(amount: Double, from: Account, to: Account): Map<String, Transfer> {
            val now = now()
            return mapOf(
                "cashIn" to CashInTransfer(now, amount, from, to),
                "cashOut" to CashOutTransfer(now, -amount, from, to)
            )
        }
    }

    fun login(email: String, password: String) : User {
        return users.firstOrNull{ it.email == email && it.password == password}
            ?: throw LoginException("Wrong email or password")
    }

    fun register(user: User) {
        assertNotTakenIdCardOrEMail(user)
        users.add(user)
    }

    fun deleteUser(user: User) {
        this.assertAccountWithoutFund(user.account)
        this.accounts.remove(user.account)
        this.users.remove(user)
    }

    fun blockAccount(account: Account) {
        this.accountByCVU(account.cvu).block()
    }

    fun unblockAccount(account: Account) {
        this.accountByCVU(account.cvu).unblock()
    }

    fun getAllAdmins() = this.users.filter { it.isAdmin }

    fun transfer(fromCVU : String, toCVU: String, amount: Double) {
        val fromAccount = this.accountByCVU(fromCVU)
        val toAccount = this.accountByCVU(toCVU)
        val date = LocalDateTime.now()
        this.makeTransfer(CashOutTransfer(date, -amount, fromAccount, toAccount), CashInTransfer(date, amount, fromAccount, toAccount))
    }

    fun transferMoneyFromCard(fromCVU: String, card: Card, amount: Double) {
        val account = accountByCVU(fromCVU)
        assertExistsUser(account.user)
        assertAccountUnblocked(account)
        account.addTransaction(CashInWithCard(now(), amount, card, account))
    }

    fun assignAccount(user: User, account: Account) {
        assertExistsUser(user)
        accounts.add(account)
        user.account = account
    }

    fun addLoyalty(loyaltyGift: LoyaltyGift) {
        this.loyaltyGifts.add(loyaltyGift)
    }

    fun addGift(gift: InitialGift) {
        assertExistsAccount(gift.to)
        assertAccountUnblocked(gift.to)
        accounts.first { it.cvu == gift.to.cvu }.addTransaction(gift)
    }

    private fun assertExistsUser(user: User) =
        assert(users.any { it.idCard == user.idCard }) {
            "User ${user.fullName()} with idCard ${user.idCard} is not register"
        }

    private fun assertNotTakenIdCardOrEMail(user: User) =
        assert(users.all { it.idCard != user.idCard && it.email != user.email }) {
            "Credit card or e-mail already registered"
        }

    private fun assertExistsAccount(account: Account) {
        assert(accounts.any {
            it.cvu == account.cvu && it.user.idCard == account.user.idCard
        }) { "Account doesn't exists or it belongs to another user" }
    }

    private fun assertAccountUnblocked(account: Account) {
        assert(!account.isBlocked) {
            throw BlockedAccountException("Account with ${account.cvu} is Blocked")
        }
    }

    private fun assertAccountWithoutFund(account: Account?) {
        if (account !== null) {
            assert(account.balance == 0.0) {
                "Can not remove ${account.cvu} with funds"
            }
        }
    }

    fun makeTransfer(cashOut: CashOutTransfer, cashIn: CashInTransfer) {
        assertExistsUser(cashIn.from.user)
        assertExistsUser(cashIn.to.user)
        assertExistsAccount(cashIn.from)
        assertExistsAccount(cashIn.to)
        if(cashIn.from == cashOut.from) { "Account ${cashIn.from.cvu} is inconsistent" }
        assert(cashIn.to == cashOut.to) { "Account ${cashIn.to.cvu} is inconsistent" }
        assertAccountUnblocked(cashIn.from)
        assertAccountUnblocked(cashIn.to)
        assert(!cashIn.to.isBlocked) {
            throw BlockedAccountException("Account with ${cashIn.from.cvu} is Blocked")
        }
        assert(cashOut.from.balance >= cashIn.amount) {
            throw NoMoneyException("Account ${cashOut.from} have no enough money to make this transfer")
        }

        val cashOutAccount = accounts.first { it.cvu == cashOut.from.cvu }
        cashOutAccount.addTransaction(cashOut)
        checkLoyalties(cashOutAccount, cashOut)
        accounts.first { it.cvu == cashIn.to.cvu }.addTransaction(cashIn)
    }

    fun checkLoyalties(account: Account, transactional: Transactional) {
        this.loyaltyGifts.forEach {
            if (!account.isLoyaltyApplied(it) && it.check(account)) {
                it.apply(account, transactional)
            }
        }
    }

    fun accountByCVU(cvu: String) =
        accounts.firstOrNull { it.cvu == cvu }
            ?: throw NoSuchElementException("Account $cvu doesn't exists")

}
