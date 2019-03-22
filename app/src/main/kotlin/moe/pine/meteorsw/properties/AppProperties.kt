package moe.pine.meteorsw.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app")
class AppProperties(
    var siteUrl: String = ""
)