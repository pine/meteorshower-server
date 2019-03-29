package moe.pine.meteorshower.config

import org.mybatis.spring.annotation.MapperScan
import org.springframework.context.annotation.Configuration

@Configuration
@MapperScan("moe.pine.meteorshower.setting")
class SettingConfig