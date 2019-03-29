package moe.pine.meteorshower.setting.repositories

import moe.pine.meteorshower.setting.models.UserOption
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UserOptionRepositoryTest : TestBase() {
    private lateinit var userOptionRepository: UserOptionRepository

    @Before
    override fun setUp() {
        super.setUp()
        userOptionRepository =
            sqlSession.getMapper(UserOptionRepository::class.java)
    }

    @Test
    fun findByUserIdTest() {
        val userOption =
            UserOption(
                userId = 12345L,
                excludeForked = true,
                excludeGist = true
            )

        val insertedCount = userOptionRepository.add(userOption)
        assertEquals(1, insertedCount)

        val foundUser = userOptionRepository.findByUserId(userOption.userId)
            ?: return fail()

        assertEquals(userOption.userId, foundUser.userId)
        assertEquals(userOption.excludeForked, foundUser.excludeForked)
        assertEquals(userOption.excludeGist, foundUser.excludeGist)
        assertNotNull(foundUser.createdAt)
        assertNotNull(foundUser.updatedAt)
    }

    @Test
    fun insertOrUpdateTest() {
        val userOption =
            UserOption(
                userId = 12345L,
                excludeForked = false,
                excludeGist = false
            )
        val excludedUserOption =
            userOption.copy(
                excludeForked = true,
                excludeGist = true
            )

        val insertedCount = userOptionRepository.insertOrUpdate(userOption)
        assertEquals(1, insertedCount)

        val insertedUserOption =
            userOptionRepository.findByUserId(userOption.userId)
                ?: return fail()
        assertEquals(userOption.userId, insertedUserOption.userId)
        assertEquals(userOption.excludeForked, insertedUserOption.excludeForked)
        assertEquals(userOption.excludeGist, insertedUserOption.excludeGist)
        assertNotNull(insertedUserOption.createdAt)
        assertNotNull(insertedUserOption.updatedAt)

        val updatedCount = userOptionRepository.insertOrUpdate(excludedUserOption)
        assertEquals(2, updatedCount)

        val updatedUserOption =
            userOptionRepository.findByUserId(userOption.userId)
                ?: return fail()
        assertEquals(userOption.userId, updatedUserOption.userId)
        assertEquals(excludedUserOption.excludeForked, updatedUserOption.excludeForked)
        assertEquals(excludedUserOption.excludeGist, updatedUserOption.excludeGist)
        assertEquals(insertedUserOption.createdAt, updatedUserOption.createdAt)
    }
}