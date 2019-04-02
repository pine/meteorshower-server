package moe.pine.meteorshower.github.auth

import org.apache.commons.text.RandomStringGenerator
import org.eclipse.egit.github.core.service.UserService
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.powermock.api.mockito.PowerMockito.`when`
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import org.springframework.web.util.UriComponentsBuilder
import java.util.Random

@RunWith(PowerMockRunner::class)
@PrepareForTest(
    GitHubAuthConfig::class,
    RandomStringGenerator::class
)
class GitHubAuthTest {
    @Mock
    private lateinit var githubAuthConfig: GitHubAuthConfig

    @Mock
    private lateinit var randomStringGenerator: RandomStringGenerator

    @Mock
    private lateinit var userService: UserService

    @InjectMocks
    private lateinit var githubAuth: GitHubAuth

    @Captor
    private lateinit var lengthCaptor: ArgumentCaptor<Int>

    @Test
    fun constructorTest() {
        val gitHubAuth = GitHubAuth(githubAuthConfig, Random())
        val config = Whitebox.getInternalState<GitHubAuthConfig>(gitHubAuth, "config")
        val randomStringGenerator =
            Whitebox.getInternalState<RandomStringGenerator>(gitHubAuth, "randomStringGenerator")
        val userService = Whitebox.getInternalState<UserService>(gitHubAuth, "userService")

        assertNotNull(config)
        assertNotNull(randomStringGenerator)
        assertNotNull(userService)
    }

    @Test
    fun requestTest() {
        val authorizeUrl = "https://github.com/login/oauth/authorize"
        val callbackUrl = "https://www.example.com/oauth2/verify"
        val clientId = "xxxxxxxxxxxxxxxxxxxx"
        `when`(githubAuthConfig.authorizeUrl).thenReturn(authorizeUrl)
        `when`(githubAuthConfig.clientId).thenReturn(clientId)
        `when`(githubAuthConfig.callbackUrl).thenReturn(callbackUrl)

        `when`(randomStringGenerator.generate(lengthCaptor.capture()))
            .thenReturn("state")
            .thenReturn("nonce")

        val authRequest = githubAuth.request()
        val redirectUri = UriComponentsBuilder
            .fromUriString(authRequest.redirectUrl).build()
        val queryParams = redirectUri.queryParams

        assertEquals("state", authRequest.state)
        assertEquals("https", redirectUri.scheme)
        assertEquals("github.com", redirectUri.host)
        assertEquals(clientId, queryParams["client_id"]!![0])
        assertEquals(callbackUrl, queryParams["redirect_uri"]!![0])
        assertEquals("code", queryParams["response_type"]!![0])
        assertEquals("state", queryParams["state"]!![0])
        assertEquals("nonce", queryParams["nonce"]!![0])

        verify(githubAuthConfig).authorizeUrl
        verify(githubAuthConfig).clientId
        verify(githubAuthConfig).callbackUrl

        assertEquals(GitHubAuth.STATE_LENGTH, lengthCaptor.allValues[0])
        assertEquals(GitHubAuth.NONCE_LENGTH, lengthCaptor.allValues[1])
        verify(randomStringGenerator, times(2)).generate(anyInt())
    }
}
