package data

import wallet.Account
import wallet.DigitalWallet
import wallet.User
import kotlin.random.Random

/**
 * Example of use:
 *
 * val digitalWallet = DigitalWalletData.build()
 */

object DigitalWalletData {
    fun build(): DigitalWallet {
        val digitalWallet = DigitalWallet()
        createUsers().forEach {
            digitalWallet.register(it)
            digitalWallet.assignAccount(it, Account(it, DigitalWallet.generateNewCVU()))
        }

        return digitalWallet
    }

    private fun createUsers(): List<User> {
        val r = Random(9855)
        return listOf(
            User("1", "a", "a", "a@gmail.com", "a", true),
            User("2", "b", "b", "b@gmail.com", "b", true),
            User("3", "c", "c", "c@gmail.com", "c", false),
            User("4", "d", "d", "d@gmail.com", "d", false),
            User("4137583890", "esperanza", "Rux", "esperanza@gmail.com", "esperanza", r.nextBoolean()),
            User("2284259016", "kati", "Kempf", "kati@gmail.com", "kati", r.nextBoolean()),
            User("6285120465", "eura", "Bungard", "eura@gmail.com", "eura", r.nextBoolean()),
            User("0230201595", "joanna", "Tackitt", "joanna@gmail.com", "joanna", r.nextBoolean()),
            User("7599375568", "glady", "Gahagan", "glady@gmail.com", "glady", r.nextBoolean()),
            User("0398517694", "bernie", "Munford", "bernie@gmail.com", "bernie", r.nextBoolean()),
            User("7864004244", "trang", "Justus", "trang@gmail.com", "trang", r.nextBoolean()),
            User("7921741175", "tonisha", "Sclafani", "tonisha@gmail.com", "tonisha", r.nextBoolean()),
            User("1361609173", "nelle", "Quillin", "nelle@gmail.com", "nelle", r.nextBoolean()),
            User("1026657383", "august", "Gallien", "august@gmail.com", "august", r.nextBoolean()),
            User("8619132213", "sarai", "Castaldo", "sarai@gmail.com", "sarai", r.nextBoolean()),
            User("6705012711", "yun", "Ashline", "yun@gmail.com", "yun", r.nextBoolean()),
            User("3535392435", "cesar", "Branning", "cesar@gmail.com", "cesar", r.nextBoolean()),
            User("1689935557", "pearl", "Stairs", "pearl@gmail.com", "pearl", r.nextBoolean()),
            User("7609223911", "wiley", "Depaul", "wiley@gmail.com", "wiley", r.nextBoolean()),
            User("6634555646", "lani", "Shufelt", "lani@gmail.com", "lani", r.nextBoolean()),
            User("6211797008", "aurora", "Severson", "aurora@gmail.com", "aurora", r.nextBoolean()),
            User("4590026689", "katharina", "Adams", "katharina@gmail.com", "katharina", r.nextBoolean()),
            User("7938618362", "cathi", "Ayotte", "cathi@gmail.com", "cathi", r.nextBoolean()),
            User("9233485558", "karl", "Coggin", "karl@gmail.com", "karl", r.nextBoolean()),
            User("0029251760", "reginald", "Rubalcaba", "reginald@gmail.com", "reginald", r.nextBoolean()),
            User("3021929234", "libbie", "Kullman", "libbie@gmail.com", "libbie", r.nextBoolean()),
            User("4100811869", "johnetta", "Carwell", "johnetta@gmail.com", "johnetta", r.nextBoolean()),
            User("0859197420", "hung", "Christoff", "hung@gmail.com", "hung", r.nextBoolean()),
            User("9443969610", "harriet", "Ghoston", "harriet@gmail.com", "harriet", r.nextBoolean()),
            User("5067410975", "yuk", "Melillo", "yuk@gmail.com", "yuk", r.nextBoolean()),
            User("7302343978", "lecia", "Trimm", "lecia@gmail.com", "lecia", r.nextBoolean()),
            User("3227050690", "tyree", "Mayon", "tyree@gmail.com", "tyree", r.nextBoolean()),
            User("9538295278", "raina", "Bonier", "raina@gmail.com", "raina", r.nextBoolean()),
            User("6027423467", "fatima", "Mcentee", "fatima@gmail.com", "fatima", r.nextBoolean()),
            User("1010441575", "rosario", "Levitan", "rosario@gmail.com", "rosario", r.nextBoolean()),
            User("0655305586", "ozella", "Keltz", "ozella@gmail.com", "ozella", r.nextBoolean()),
            User("2586141043", "bunny", "Wadkins", "bunny@gmail.com", "bunny", r.nextBoolean()),
            User("2554413495", "kitty", "Mathias", "kitty@gmail.com", "kitty", r.nextBoolean()),
            User("8146090169", "solange", "Clinkscales", "solange@gmail.com", "solange", r.nextBoolean()),
            User("5911239670", "melda", "Broe", "melda@gmail.com", "melda", r.nextBoolean()),
            User("4155490655", "ian", "Barnes", "ian@gmail.com", "ian", r.nextBoolean()),
            User("9004714342", "elidia", "Bortle", "elidia@gmail.com", "elidia", r.nextBoolean()),
            User("9398448694", "librada", "Cape", "librada@gmail.com", "librada", r.nextBoolean()),
            User("8458291925", "sammie", "Floyd", "sammie@gmail.com", "sammie", r.nextBoolean()),
            User("1059243438", "xavier", "Gupton", "xavier@gmail.com", "xavier", r.nextBoolean()),
            User("9020803943", "janett", "Finnie", "janett@gmail.com", "janett", r.nextBoolean()),
            User("6360920540", "kenya", "Mcculley", "kenya@gmail.com", "kenya", r.nextBoolean()),
            User("9520488709", "tommie", "Angelos", "tommie@gmail.com", "tommie", r.nextBoolean()),
            User("5706437634", "cecile", "Rook", "cecile@gmail.com", "cecile", r.nextBoolean()),
            User("5672827374", "paulita", "Crete", "paulita@gmail.com", "paulita", r.nextBoolean()),
            User("7018322814", "filiberto", "Miedema", "filiberto@gmail.com", "filiberto", r.nextBoolean()),
            User("2931942848", "phuong", "Sowinski", "phuong@gmail.com", "phuong", r.nextBoolean()),
            User("9367782855", "bong", "Prowse", "bong@gmail.com", "bong", r.nextBoolean()),
            User("2194552043", "kandice", "Worth", "kandice@gmail.com", "kandice", r.nextBoolean())
        )
    }
}

// DO NOT RUN!!
// FOR TESTING ONLY!!
fun main() {
    val digitalWallet = DigitalWallet()
    digitalWallet.register(User("1", "a", "a", "a@gmail.com", "a", true))
    digitalWallet.register(User("1", "a", "a", "a@gmail.com", "a", true))
    println(digitalWallet.users.size)
}
