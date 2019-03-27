package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.UserAccessToken
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserAccessTokenRepository {
    @Select("""
        SELECT *
        FROM `user_access_token`
        WHERE `user_access_token`.`access_token` = #{accessToken}
    """)
    fun findByAccessToken(accessToken: String) : UserAccessToken?

    @Insert("""
        INSERT INTO `user_access_token` (`user_id`, `access_token`)
        VALUES (#{userId}, #{accessToken})
    """)
    fun add(userAccessToken: UserAccessToken) : Long

    @Delete("""
        DELETE FROM `user_access_token`
        WHERE `user_access_token`.`access_token` = #{accessToken}
    """)
    fun remove(userAccessToken: UserAccessToken) : Long
}