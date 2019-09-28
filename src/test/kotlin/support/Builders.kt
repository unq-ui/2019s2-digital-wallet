package support

import wallet.*

class UserBuilder(
    private var idCard: String = "000",
    var firstName: String = "Jon",
    var lastName: String = "Snow",
    var email: String = "jon@snow.io",
    var password: String = "jon",
    var account: Account? = null,
    var isAdmin: Boolean = false
) {
    fun build(): User = User(idCard, firstName, lastName, email, password, isAdmin)

    fun idCard(value: String): UserBuilder {
        idCard = value
        return this
    }

    fun email(value: String): UserBuilder {
        email = value
        return this
    }

    fun password(value: String): UserBuilder {
        password = value
        return this
    }

    fun isAdmin(value: Boolean) : UserBuilder {
        isAdmin = value
        return this
    }
}

class AccountBuilder() {
    var user: User = UserBuilder().build()
    var cvu: String = "12345678-12345678910121"

    fun build(): Account = Account(user, cvu)

    fun user(value: User): AccountBuilder {
        user = value
        return this
    }

    fun cvu(value: String): AccountBuilder {
        cvu = value
        return this
    }
}