package wallet

import java.time.LocalDateTime

/**
 * Registrar usuario de DigitalWallet.
 * Eliminar Usuarios de DigitalWallet.
 * Obtener el listado de usuarios del sistema indicando si es Administrador.
 * Desbloquear usuario de DigitalWallet
 * Login mediante correo electrónico y contraseña.
 * Transferir dinero de un CVU Origen a un CVU destino.
 * Ingresar Dinero a DigitalWallet mediante tarjeta de cŕedito.
 */
class DigitalWallet {
    val users = mutableListOf<User>()
    val accounts = mutableListOf<Account>()

    companion object Support {
        private fun now() = LocalDateTime.now()
        fun generateNewCVU() = "123"
        fun createGift(account: Account, amount: Double) = InitialGift(account, amount, now())
        fun createCashTransfer(amount: Double, from: Account, to: Account): Map<String, Transfer> {
            val now = now()
            return mapOf(
                "cashIn" to CashInTransfer(now, amount, from, to),
                "cashOut" to CashOutTransfer(now, -amount, from, to)
            )
        }
    }

    fun register(user: User) {
        users.add(user)
    }

    fun assignAccount(user: User, account: Account) {
        assertExistsUser(user)
        accounts.add(account)
        user.account = account
    }

    fun addGift(gift: InitialGift) {
        assertExistsAccount(gift.to)
        accounts.first { it.cvu == gift.to.cvu }.addTransaction(gift)
    }

    private fun assertExistsUser(user: User) =
        assert(users.any { it.idCard == user.idCard }) {
            "User ${user.fullName()} with idCard ${user.idCard} is not register"
        }

    private fun assertExistsAccount(account: Account) {
        assert(accounts.any {
            it.cvu == account.cvu && it.user.idCard == account.user.idCard
        }) { "Account doesn't exists or it belongs to another user" }
    }

    fun makeTransfer(cashOut: CashOutTransfer, cashIn: CashInTransfer) {
        assertExistsUser(cashIn.from.user)
        assertExistsUser(cashIn.to.user)
        assertExistsAccount(cashIn.from)
        assertExistsAccount(cashIn.to)
        assert(cashIn.from == cashOut.from) { "Account ${cashIn.from.cvu} is inconsistent" }
        assert(cashIn.to == cashOut.to) { "Account ${cashIn.to.cvu} is inconsistent" }
        assert(cashOut.from.balance >= cashIn.amount) {
            throw NoMoneyException("Account ${cashOut.from} have no enough money to make this transfer")
        }

        accounts.first { it.cvu == cashOut.from.cvu }.addTransaction(cashOut)
        accounts.first { it.cvu == cashIn.to.cvu }.addTransaction(cashIn)
    }

    fun accountByCVU(cvu: String) =
        accounts.firstOrNull { it.cvu == cvu }
            ?: throw NoSuchElementException("Account $cvu doesn't exists")

}