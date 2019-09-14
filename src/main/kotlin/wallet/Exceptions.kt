package wallet

class NoMoneyException(message: String?) : Exception(message)
class LoginException(message: String?) : Exception(message)
class BlockedAccountException(message: String?) : Exception(message)
