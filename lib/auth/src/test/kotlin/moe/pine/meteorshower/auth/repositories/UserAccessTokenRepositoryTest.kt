package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.UserAccessToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class UserAccessTokenRepositoryTest : TestBase() {
    private lateinit var userAccessTokenRepository: UserAccessTokenRepository

    @Before
    override fun setUp() {
        super.setUp()
        userAccessTokenRepository =
            sqlSession.getMapper(UserAccessTokenRepository::class.java)
    }

    @Test
    fun findByAccessTokenTest() {
        val lastAccessedAt = LocalDateTime.of(2019, 4, 3, 11, 44, 0)
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu",
            lastAccessedAt = lastAccessedAt
        )

        val existedUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
        assertNull(existedUserAccessToken)

        val insertedCount = userAccessTokenRepository.add(userAccessToken)
        assertEquals(1, insertedCount)

        val foundUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
                ?: return fail()

        assertEquals(userAccessToken.userId, foundUserAccessToken.userId)
        assertEquals(userAccessToken.accessToken, foundUserAccessToken.accessToken)
        assertEquals(lastAccessedAt, foundUserAccessToken.lastAccessedAt)
        assertNotNull(foundUserAccessToken.createdAt)
        assertNotNull(foundUserAccessToken.updatedAt)
    }

    @Test
    fun addTest_nullLastAccessedAt() {
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu",
            lastAccessedAt = null
        )

        val existedUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
        assertNull(existedUserAccessToken)

        val insertedCount = userAccessTokenRepository.add(userAccessToken)
        assertEquals(1, insertedCount)

        val foundUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
                ?: return fail()

        assertEquals(userAccessToken.userId, foundUserAccessToken.userId)
        assertEquals(userAccessToken.accessToken, foundUserAccessToken.accessToken)
        assertNull(foundUserAccessToken.lastAccessedAt)
        assertNotNull(foundUserAccessToken.createdAt)
        assertNotNull(foundUserAccessToken.updatedAt)
    }

    @Test
    fun updateTest() {
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu",
            lastAccessedAt = null
        )
        val insertedCount = userAccessTokenRepository.add(userAccessToken)
        assertEquals(1, insertedCount)

        val foundUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
                ?: return fail()
        assertEquals(userAccessToken.userId, foundUserAccessToken.userId)
        assertEquals(userAccessToken.accessToken, foundUserAccessToken.accessToken)
        assertNull(foundUserAccessToken.lastAccessedAt)
        assertNotNull(foundUserAccessToken.createdAt)
        assertNotNull(foundUserAccessToken.updatedAt)

        val lastAccessedAt = LocalDateTime.of(2019, 4, 3, 11, 44, 0)
        val updatedCount = userAccessTokenRepository.update(
            userAccessToken.copy(lastAccessedAt = lastAccessedAt))
        assertEquals(1, updatedCount)

        val updatedUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
                ?: return fail()
        assertEquals(userAccessToken.userId, updatedUserAccessToken.userId)
        assertEquals(userAccessToken.accessToken, updatedUserAccessToken.accessToken)
        assertEquals(lastAccessedAt, updatedUserAccessToken.lastAccessedAt)
        assertEquals(foundUserAccessToken.createdAt, updatedUserAccessToken.createdAt)
        assertTrue(foundUserAccessToken.updatedAt!! <= updatedUserAccessToken.updatedAt!!)
    }

    @Test
    fun updateTest_accessTokenNonMatch() {
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu",
            lastAccessedAt = null
        )
        val insertedCount = userAccessTokenRepository.add(userAccessToken)
        assertEquals(1, insertedCount)

        val lastAccessedAt = LocalDateTime.of(2019, 4, 3, 11, 44, 0)
        val updatedCount = userAccessTokenRepository.update(
            userAccessToken.copy(
                accessToken = "bakabaka",
                lastAccessedAt = lastAccessedAt))
        assertEquals(0, updatedCount)
    }

    @Test
    fun removeTest() {
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu"
        )

        val insertedCount = userAccessTokenRepository.add(userAccessToken)
        assertEquals(1, insertedCount)

        val foundUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
                ?: return fail()

        val deletedCount = userAccessTokenRepository.remove(foundUserAccessToken)
        assertEquals(1, deletedCount)

        val deletedUserAccessToken =
            userAccessTokenRepository.findByAccessToken(userAccessToken.accessToken)
        assertNull(deletedUserAccessToken)
    }
}