package moe.pine.meteorshower.setting.repositories

import moe.pine.meteorshower.setting.models.UserExcludedRepository
import org.apache.ibatis.annotations.*

@Mapper
interface UserExcludedRepositoryRepository {
    @Select("""
        SELECT *
        FROM `user_excluded_repository`
        WHERE `user_excluded_repository`.`user_id` = #{userId}
        ORDER BY `user_excluded_repository`.`id`
    """)
    fun filterByUserId(userId: Long): List<UserExcludedRepository>

    @Insert("""
        <script>
            <choose>
                <when test="items.length == 0">
                    SELECT 1
                </when>
                <otherwise>
                    INSERT INTO `user_excluded_repository` (
                        `id`,
                        `user_id`,
                        `owner`,
                        `name`
                    ) VALUES
                        <foreach collection="items"
                                item="item"
                                index="index"
                                separator=",">
                            (#{item.id}, #{item.userId}, #{item.owner}, #{item.name})
                        </foreach>
                </otherwise>
            </choose>
        </script>
    """)
    fun add(@Param("items") vararg userExcludedRepositories: UserExcludedRepository): Long

    @Delete("""
        <script>
            <choose>
                <when test="items.length == 0">
                    SELECT 1
                </when>
                <otherwise>
                    DELETE FROM `user_excluded_repository`
                        WHERE `user_excluded_repository`.`id` IN
                            <foreach collection="items"
                                    item="item"
                                    index="index"
                                    open="("
                                    separator=","
                                    close=")">
                                #{item.id}
                            </foreach>
                </otherwise>
            </choose>
        </script>
    """)
    fun remove(@Param("items") vararg userExcludedRepositories: UserExcludedRepository): Long
}