package moe.pine.meteorshower.health.repositories

import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface HealthRepository {
    @Select("SELECT 1")
    fun alive(): Long
}
