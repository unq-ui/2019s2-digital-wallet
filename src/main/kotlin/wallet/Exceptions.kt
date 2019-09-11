package wallet

class NoMoneyException(message: String?) : Throwable(message)
class LoginException(message: String?) : Throwable(message)
class BlockedAccountException(message: String?) : Throwable(message)