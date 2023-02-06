package co.mvpmatch.backendtask1.config

class ResourceNotFoundException : RuntimeException()
class UnauthorizedException : RuntimeException()
class UserNotActivatedException(val msg: String) : RuntimeException()
class ResourceAlreadyExistsException() : RuntimeException()
class UserNotAllowedException() : RuntimeException()
