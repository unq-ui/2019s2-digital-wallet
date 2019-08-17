package support

import wallet.Account
import wallet.User

class UserBuilder() {
    private var idCard: String = "000"
    var firstName: String = "Jon"
    var lastName: String = "Snow"
    var email: String = "jon@snow.io"
    var password: String = "jon"
    var account: Account? = null

    fun build(): User = User(idCard, firstName, lastName, email, password)

    fun idCard(value: String): UserBuilder {
        idCard = value
        return this;
    }
}