package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.User
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class UserRepositoryTest : TestBase() {
    private lateinit var userRepository: UserRepository

    @Before
    override fun setUp() {
        super.setUp()
        userRepository = sqlSession.getMapper(UserRepository::class.java)
    }

    @Test
    fun findByIdTest() {
        val id = 12345L
        val user = User(
            id = id,
            githubId = 12345,
            name = "Homura Akemi"
        )

        val existedUser = userRepository.findById(id)
        assertNull(existedUser)

        val insertedCount = userRepository.add(user)
        assertEquals(1, insertedCount)

        val foundUser = userRepository.findById(id)
            ?: return fail()

        assertEquals(id, foundUser.id)
        assertEquals(user.githubId, foundUser.githubId)
        assertEquals(user.name, foundUser.name)
    }

    @Test
    fun addTest() {
        val user = User(
            githubId = 12345,
            name = "Homura Akemi"
        )

        val existedUser = userRepository.findByGitHubId(user.githubId)
        assertNull(existedUser)

        val insertedCount = userRepository.add(user)
        assertEquals(1, insertedCount)

        val foundUser = userRepository.findByGitHubId(user.githubId)
            ?: return fail()

        assertNotNull(foundUser.id)
        assertEquals(user.githubId, foundUser.githubId)
        assertEquals(user.name, foundUser.name)
        assertNotNull(foundUser.createdAt)
        assertNotNull(foundUser.updatedAt)
    }
}