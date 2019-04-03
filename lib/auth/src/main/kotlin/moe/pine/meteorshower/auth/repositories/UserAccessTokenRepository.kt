package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.UserAccessToken
import org.apache.ibatis.annotations.Delete
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select
import org.apache.ibatis.annotations.Update

@Mapper
interface UserAccessTokenRepository {
    @Select("""
        SELECT *
        FROM `user_access_token`
        WHERE `user_access_token`.`access_token` = #{accessToken}
        LIMIT 1
    """)
    fun findByAccessToken(accessToken: String) : UserAccessToken?

    @Insert("""
        INSERT INTO `user_access_token` (
            `user_id`,
            `access_token`,
            `last_accessed_at`
        ) VALUES (
            #{userId},
            #{accessToken},
            #{lastAccessedAt}
        )
    """)
    fun add(userAccessToken: UserAccessToken) : Long

    @Update("""
        UPDATE `user_access_token`
        SET
            `last_accessed_at` = #{lastAccessedAt}
        WHERE
            `access_token` = #{accessToken}
            AND `user_id` = #{userId}
        LIMIT 1
    """)
    fun update(userAccessToken: UserAccessToken) : Long

    @Delete("""
        DELETE FROM `user_access_token`
        WHERE
            `user_access_token`.`access_token` = #{accessToken}
            AND `user_access_token`.`user_id` = #{userId}
        LIMIT 1
    """)
    fun remove(userAccessToken: UserAccessToken) : Long
}