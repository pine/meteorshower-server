package moe.pine.meteorshower.setting.repositories

import moe.pine.meteorshower.setting.models.UserExcludedRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class UserExcludedRepositoryRepositoryTest : TestBase() {
    private lateinit var userExcludedRepositoryRepository
        : UserExcludedRepositoryRepository

    @Before
    override fun setUp() {
        super.setUp()
        userExcludedRepositoryRepository =
            sqlSession.getMapper(UserExcludedRepositoryRepository::class.java)
    }

    @Test
    fun addTest_empty() {
        userExcludedRepositoryRepository.add()
    }

    @Test
    fun addTest_multi() {
        val userId = 12345L
        val userExcludedRepository1 =
            UserExcludedRepository(
                userId = userId,
                owner = "homuhomu",
                name = "qb-killer"
            )
        val userExcludedRepository2 =
            UserExcludedRepository(
                userId = userId,
                owner = "homuhomu",
                name = "home"
            )

        userExcludedRepositoryRepository
            .add(userExcludedRepository1, userExcludedRepository2)

        val foundExcludedRepositories =
            userExcludedRepositoryRepository.filterByUserId(userId)
        assertEquals(2, foundExcludedRepositories.size)

        assertNotNull(foundExcludedRepositories[0].id)
        assertEquals(userId, foundExcludedRepositories[0].userId)
        assertEquals(userExcludedRepository1.owner, foundExcludedRepositories[0].owner)
        assertEquals(userExcludedRepository1.name, foundExcludedRepositories[0].name)
        assertNotNull(foundExcludedRepositories[0].createdAt)
        assertNotNull(foundExcludedRepositories[0].updatedAt)

        assertNotNull(foundExcludedRepositories[1].id)
        assertEquals(userId, foundExcludedRepositories[1].userId)
        assertEquals(userExcludedRepository2.owner, foundExcludedRepositories[1].owner)
        assertEquals(userExcludedRepository2.name, foundExcludedRepositories[1].name)
        assertNotNull(foundExcludedRepositories[1].createdAt)
        assertNotNull(foundExcludedRepositories[1].updatedAt)
    }

    @Test
    fun removeTest_empty() {
        val userExcludedRepository =
            UserExcludedRepository(
                userId = 12345L,
                owner = "homuhomu",
                name = "qb-killer"
            )

        val insertedCount =
            userExcludedRepositoryRepository.add(userExcludedRepository)
        assertEquals(1, insertedCount)

        userExcludedRepositoryRepository.remove()

        val foundUserExcludedRepositories =
            userExcludedRepositoryRepository.filterByUserId(userExcludedRepository.userId)
        assertEquals(1, foundUserExcludedRepositories.size)
    }

    @Test
    fun removeTest_multi() {
        val userId = 12345L
        val userExcludedRepository1 =
            UserExcludedRepository(
                userId = userId,
                owner = "homuhomu",
                name = "qb-killer"
            )
        val userExcludedRepository2 =
            UserExcludedRepository(
                userId = userId,
                owner = "homuhomu",
                name = "home"
            )

        val insertedCount =
            userExcludedRepositoryRepository
                .add(userExcludedRepository1, userExcludedRepository2)
        assertEquals(2, insertedCount)

        val foundUserExcludedRepositories =
            userExcludedRepositoryRepository.filterByUserId(userId)
        assertEquals(2, foundUserExcludedRepositories.size)

        val removedCount =
            userExcludedRepositoryRepository
                .remove(*foundUserExcludedRepositories.toTypedArray())
        assertEquals(2, removedCount)

        val remainedUserExcludedRepositories =
            userExcludedRepositoryRepository.filterByUserId(userId)
        assertEquals(0, remainedUserExcludedRepositories.size)
    }
}