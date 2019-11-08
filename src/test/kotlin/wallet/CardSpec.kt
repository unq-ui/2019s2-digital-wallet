package wallet

import org.junit.jupiter.api.Assertions.*
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe
import java.time.LocalDate

object CardSpec : Spek({
    describe("Card") {

        it("should have properties") {
            val now = LocalDate.now()
            val card = CreditCard(
                cardNumber = "1234 5678 8888 9999",
                fullName = "Jon Snow",
                endDate = now,
                securityCode = "111"
            )
            assertEquals("1234 5678 8888 9999", card.cardNumber)
            assertEquals("Jon Snow", card.fullName)
            assertEquals(now, card.endDate)
            assertEquals("111", card.securityCode)
        }

        it("should get masked number ") {
            val now = LocalDate.now()
            val card1 = CreditCard(
                cardNumber = "9999 8888 7777 6666",
                fullName = "Jon Snow",
                endDate = now,
                securityCode = "111"
            )
            val card2 = CreditCard("1111222233334444", card1.fullName, card1.endDate, card1.securityCode)

            assertEquals("xxxx xxxx xxxx 6666", card1.maskedCard())
            assertEquals("xxxxxxxxxxxx4444", card2.maskedCard())
        }
    }
})
