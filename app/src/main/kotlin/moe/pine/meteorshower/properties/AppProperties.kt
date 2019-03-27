package moe.pine.meteorshower.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class AppProperties(
    var siteUrl: String = ""
)