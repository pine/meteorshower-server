package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.User
import org.apache.ibatis.annotations.Insert
import org.apache.ibatis.annotations.Mapper
import org.apache.ibatis.annotations.Select

@Mapper
interface UserRepository {
    @Select("""
        SELECT *
        FROM `user`
        WHERE `user`.`id` = #{id}
        LIMIT 1
    """)
    fun findById(id: Long): User?

    @Select("""
        SELECT *
        FROM `user`
        WHERE `user`.`github_id` = #{githubId}
        LIMIT 1
    """)
    fun findByGitHubId(githubId: Int): User?

    @Insert("""
        INSERT INTO `user` (
            `id`,
            `github_id`,
            `name`
        ) VALUES (
            #{id},
            #{githubId},
            #{name}
        )
    """)
    fun add(user: User): Long
}
