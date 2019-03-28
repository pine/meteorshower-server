package moe.pine.meteorshower.scoped

import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.SessionScope
import java.io.Serializable

@Component
@SessionScope(proxyMode = ScopedProxyMode.TARGET_CLASS)
data class AuthFlow(
    var callbackUrl: String = "",
    var state: String = ""
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}
