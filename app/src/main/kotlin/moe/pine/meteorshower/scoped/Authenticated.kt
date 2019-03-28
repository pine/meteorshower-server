package moe.pine.meteorshower.scoped

import moe.pine.meteorshower.auth.models.User
import moe.pine.meteorshower.auth.models.UserAccessToken
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope

@Component
@RequestScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
data class Authenticated(
    var user: User? = null,
    var userAccessToken: UserAccessToken? = null
)