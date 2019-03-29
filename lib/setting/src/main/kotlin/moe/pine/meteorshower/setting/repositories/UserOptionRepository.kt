package moe.pine.meteorshower.setting.repositories

import moe.pine.meteorshower.setting.models.UserOption
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserOptionRepository {
    @Select("""
        SELECT *
        FROM `user_option`
        WHERE `user_option`.`user_id` = #{userId}
        LIMIT 1
    """)
    fun findByUserId(userId: Long): UserOption?

    @Insert("""
        INSERT INTO `user_option` (
            `user_id`,
            `exclude_forked`,
            `exclude_gist`
        ) VALUES (
            #{userId},
            #{excludeForked},
            #{excludeGist}
        )
    """)
    fun add(userOption: UserOption): Long

    @Insert("""
        INSERT INTO `user_option` (
            `user_id`,
            `exclude_forked`,
            `exclude_gist`
        ) VALUES (
            #{userId},
            #{excludeForked},
            #{excludeGist}
        ) ON DUPLICATE KEY UPDATE
            `exclude_forked` = #{excludeForked},
            `exclude_gist` = #{excludeGist}
    """)
    fun insertOrUpdate(userOption: UserOption): Long
}
