package moe.pine.meteorshower.auth.repositories

import moe.pine.meteorshower.auth.models.UserAccessToken
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

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
        val userAccessToken = UserAccessToken(
            userId = 12345L,
            accessToken = "homuhomu"
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
        assertNotNull(foundUserAccessToken.createdAt)
        assertNotNull(foundUserAccessToken.updatedAt)
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