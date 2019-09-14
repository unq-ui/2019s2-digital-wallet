package wallet

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import support.AccountBuilder
import support.UserBuilder
import java.time.LocalDate
import java.time.LocalDateTime

object AccountSpec : Spek({
    describe("Account") {
        val cvu by memoized { DigitalWallet.generateNewCVU() }
        val aUser by memoized { UserBuilder().idCard("11.222.333").build() }
        val to by memoized { AccountBuilder().user(aUser).cvu(cvu).build() }

        it("should have an empty balance on creation") {
            assertEquals(0.0, to.balance)
        }

        it("should have no transactions on creation") {
            assertEquals(0, to.transactions.size)
        }

        it("should have no applied loyalties on creation") {
            assertEquals(0, to.appliedLoyalties.size)
        }

        it("should be able to operate on an Unblocked state") {
            val amount = 200.0
            val anotherUser = UserBuilder().idCard("22.333.444").build()
            val from = AccountBuilder().user(anotherUser).build()
            val cashInTransfer = CashInTransfer(LocalDateTime.now(), amount, from, to )

            to.unblock()
            to.addTransaction(cashInTransfer)

            assert(!to.isBlocked)
            assertEquals(amount, to.balance)
        }

        it("should not be able to operate on a Blocked state") {
            val amount = 200.0
            val anotherUser = UserBuilder().idCard("22.333.444").build()
            val from = AccountBuilder().user(anotherUser).build()
            val cashInTransfer = CashInTransfer(LocalDateTime.now(), amount, from, to )

            to.block()

            assert(to.isBlocked)
            assertThrows<BlockedAccountException> { to.addTransaction(cashInTransfer) }
        }

        it("should be able to receive loyalties on an Unblocked state") {
            val cashInLoyalty = LoyaltyGift(
                "Discount Loyalty",
                DiscountGiftStrategy(10),
                10,
                100,
                LocalDate.now(),
                LocalDate.MAX
            )

            to.unblock()
            to.addLoyalty(cashInLoyalty)

            assert(!to.isBlocked)
            assert(to.appliedLoyalties.contains(cashInLoyalty))
        }

        it("should be able to receive loyalties on an Unblocked state") {
            val cashInLoyalty = LoyaltyGift(
                "Discount Loyalty",
                DiscountGiftStrategy(10),
                10,
                100,
                LocalDate.now(),
                LocalDate.MAX
            )

            to.block()

            assert(to.isBlocked)
            assertThrows<BlockedAccountException> { to.addLoyalty(cashInLoyalty) }
        }
    }
})
