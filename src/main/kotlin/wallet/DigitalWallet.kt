package wallet

import java.time.LocalDateTime
import kotlin.random.Random

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
        return users.firstOrNull{ it.email == email && it.password == password }
            ?: throw LoginException("Wrong email or password")
    }

    fun register(user: User) {
        requireNotTakenIdCardOrEMail(user)
        users.add(user)
    }

    fun deleteUser(user: User) {
        this.requireAccountWithoutFund(user.account)
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
        requireExistsUser(account.user)
        requireAccountUnblocked(account)
        account.addTransaction(CashInWithCard(now(), amount, card, account))
    }

    fun assignAccount(user: User, account: Account) {
        requireExistsUser(user)
        accounts.add(account)
        user.account = account
    }

    fun addLoyalty(loyaltyGift: LoyaltyGift) {
        this.loyaltyGifts.add(loyaltyGift)
    }

    fun addGift(gift: InitialGift) {
        requireExistsAccount(gift.to)
        requireAccountUnblocked(gift.to)
        accounts.first { it.cvu == gift.to.cvu }.addTransaction(gift)
    }

    private fun requireExistsUser(user: User) =
        require(users.any { it.idCard == user.idCard }) {
            "User ${user.fullName()} with idCard ${user.idCard} is not register"
        }

    private fun requireNotTakenIdCardOrEMail(user: User) =
        require(users.all { it.idCard != user.idCard && it.email != user.email }) {
            "Credit card or e-mail already registered"
        }

    private fun requireExistsAccount(account: Account) {
        require(accounts.any {
            it.cvu == account.cvu && it.user.idCard == account.user.idCard
        }) { "Account doesn't exists or it belongs to another user" }
    }

    private fun requireAccountUnblocked(account: Account) {
        require(!account.isBlocked) {
            throw BlockedAccountException("Account with ${account.cvu} is Blocked")
        }
    }

    private fun requireAccountWithoutFund(account: Account?) {
        if (account !== null) {
            require(account.balance == 0.0) {
                "Can not remove ${account.cvu} with funds"
            }
        }
    }

    fun makeTransfer(cashOut: CashOutTransfer, cashIn: CashInTransfer) {
        requireExistsUser(cashIn.from.user)
        requireExistsUser(cashIn.to.user)
        requireExistsAccount(cashIn.from)
        requireExistsAccount(cashIn.to)
        require(cashIn.from == cashOut.from) { "Account ${cashIn.from.cvu} is inconsistent" }
        require(cashIn.to == cashOut.to) { "Account ${cashIn.to.cvu} is inconsistent" }
        requireAccountUnblocked(cashIn.from)
        requireAccountUnblocked(cashIn.to)
        require(!cashIn.to.isBlocked) {
            throw BlockedAccountException("Account with ${cashIn.from.cvu} is Blocked")
        }
        require(cashOut.from.balance >= cashIn.amount) {
            throw NoMoneyException("Account ${cashOut.from.cvu} doesn't have enough money to make this transfer")
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
