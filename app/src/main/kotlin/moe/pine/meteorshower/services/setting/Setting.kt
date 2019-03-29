package moe.pine.meteorshower.services.setting

data class Setting(
    val excludeRepositories: List<SettingExcludeRepository>,
    val excludeForked: Boolean,
    val excludeGist: Boolean
)