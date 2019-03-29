package moe.pine.meteorshower.services.auth

class IllegalAccessTokenException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
}