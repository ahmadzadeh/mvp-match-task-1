package co.mvpmatch.backendtask1.config

class ResourceNotFoundException : RuntimeException()
class UnauthorizedException : RuntimeException()
class UserNotActivatedException(val msg: String) : RuntimeException()
class ResourceAlreadyExistsException : RuntimeException()
class UserNotAllowedException : RuntimeException()
class InvalidParamException(val msg: String) : RuntimeException()
class InsufficientUserBalanceException : RuntimeException()
class InsufficientProductAmount(val productId: Long) : RuntimeException()
class UnableToChangeCoinsException : RuntimeException()
class UnexpectedException: RuntimeException()
