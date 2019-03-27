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
    """)
    fun findById(id: Long): User?

    @Select("""
        SELECT *
        FROM `user`
        WHERE `user`.`twitter_id` = #{twitterId}
    """)
    fun findByTwitterId(twitterId: Int): User?

    @Insert("""
        INSERT INTO `user` (
            `id`,
            `twitter_id`,
            `name`
        ) VALUES (
            #{id},
            #{twitterId},
            #{name}
        )
    """)
    fun add(user: User): Long
}