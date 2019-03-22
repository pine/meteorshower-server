package moe.pine.meteorsw.config

import moe.pine.meteorsw.properties.GitHubProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(GitHubProperties::class)
class GitHubConfig