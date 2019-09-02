package wallet

import org.spekframework.spek2.Spek
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.style.specification.describe
import support.UserBuilder
import java.time.LocalDate

object DigitalWalletSpec : Spek({
    val wallet by memoized { DigitalWallet() }

    describe("Digital Wallet Creation") {
        it("is created without users") {
            assertEquals(0, wallet.users.size)
        }
        it("is created without accounts") {
            assertEquals(0, wallet.accounts.size)
        }
    }

    describe("Enrollment") {
        it("should add user to list when register") {
            val user = UserBuilder().idCard("123").build()
            assertEquals(0, wallet.users.size)
            wallet.register(user)
            assertEquals(1, wallet.users.size)
            assertEquals("123", wallet.users.first().idCard)
        }

        it("should need all information when register") {
            val user = User(
                idCard = "12.345.678",
                firstName = "Cosme",
                lastName = "Fulanito",
                email = "cosme@fulanito.io",
                password = "homero",
                isAdmin = false
            )
            wallet.register(user)
            assertEquals("12.345.678", wallet.users.first().idCard)
            assertEquals("Cosme", wallet.users.first().firstName)
            assertEquals("Fulanito", wallet.users.first().lastName)
            assertEquals("cosme@fulanito.io", wallet.users.first().email)
            assertEquals("homero", wallet.users.first().password)
            assertFalse(wallet.users.first().isAdmin)
        }

        context("Account creation") {
            val cvu by memoized { DigitalWallet.generateNewCVU() }
            val user by memoized { UserBuilder().idCard("11.222.333").build()  }

            it("should not assign account if user was not register") {
                val account = Account(user, cvu)
                assertThrows<AssertionError> { wallet.assignAccount(user, account) }
            }

            it("user should be register and then assign an account") {
                val account = Account(user, cvu)

                assertEquals(0, wallet.accounts.size)
                wallet.register(user)
                wallet.assignAccount(user, account)
                assertEquals(1, wallet.accounts.size)
                assertEquals(account, wallet.accounts.first())
                assertEquals(account, wallet.users.first().account)
            }
            it("when user is assigned to an account it recibes $200 as a gift") {
                val account = Account(user, cvu)
                wallet.register(user)
                wallet.assignAccount(user, account)

                assertEquals(0.0, wallet.users.first().account!!.balance)
                assertEquals(0, wallet.users.first().account!!.transactions.size)
                wallet.addGift(DigitalWallet.createGift(account, 200.0))
                assertEquals(200.0, wallet.users.first().account!!.balance)
                assertEquals(1, wallet.users.first().account!!.transactions.size)
            }
        }
    }

    describe("Transfers") {
        lateinit var accountFrom: Account
        lateinit var accountTo: Account
        val userFrom by memoized { UserBuilder().idCard("11222333").build() }
        val userTo by memoized { UserBuilder().idCard("44555666").build() }
        beforeEachTest {
            accountFrom = Account(userFrom, "00001111")
            accountTo = Account(userTo, "00002222")
            wallet.register(userFrom)
            wallet.register(userTo)
            wallet.assignAccount(userFrom, accountFrom)
            wallet.assignAccount(userTo, accountTo)
            wallet.addGift(DigitalWallet.createGift(accountFrom, 200.0))
            wallet.addGift(DigitalWallet.createGift(accountTo, 200.0))
        }
        
        it("should have enough money to transfer") {
            val transfer = DigitalWallet.createCashTransfer(300.0, accountFrom, accountTo)
            assertThrows<NoMoneyException> {
                wallet.makeTransfer(
                    transfer["cashOut"] as CashOutTransfer,
                    transfer["cashIn"] as CashInTransfer
                )
            }
        }

        it("allow to make transfer when have enough money") {
            val transfer = DigitalWallet.createCashTransfer(150.0, accountFrom, accountTo)

            assertEquals(200.0, wallet.accountByCVU(accountFrom.cvu).balance)
            assertEquals(200.0, wallet.accountByCVU(accountTo.cvu).balance)

            wallet.makeTransfer(
                transfer["cashOut"] as CashOutTransfer,
                transfer["cashIn"] as CashInTransfer
            )

            assertEquals(50.0, wallet.accountByCVU(accountFrom.cvu).balance)
            assertEquals(350.0, wallet.accountByCVU(accountTo.cvu).balance)
        }

        it("add money to an existing account") {
            val creditCard = CreditCard("1111 1111 1111 1111", "fullName", LocalDate.now(), "1234")
            assertEquals(200.0, wallet.accountByCVU("00001111").balance)
            wallet.transferMoneyFromCard("00001111", creditCard, 100.0)
            assertEquals(300.0, wallet.accountByCVU("00001111").balance)
        }
    }
})