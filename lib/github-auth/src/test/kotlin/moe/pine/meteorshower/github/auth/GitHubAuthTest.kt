package moe.pine.meteorshower.github.auth

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest
import com.google.api.client.http.LowLevelHttpResponse
import com.google.api.client.http.UrlEncodedParser
import com.google.api.client.testing.http.MockHttpTransport
import com.google.api.client.testing.http.MockLowLevelHttpRequest
import com.google.api.client.testing.http.MockLowLevelHttpResponse
import org.apache.commons.text.RandomStringGenerator
import org.eclipse.egit.github.core.User
import org.eclipse.egit.github.core.client.GitHubClient
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
    GitHubAuth::class,
    GitHubAuthConfig::class,
    RandomStringGenerator::class,
    AuthorizationCodeTokenRequest::class
)
class GitHubAuthTest {
    @Mock
    private lateinit var githubAuthConfig: GitHubAuthConfig

    @Mock
    private lateinit var randomStringGenerator: RandomStringGenerator

    @Mock
    private lateinit var userService: UserService

    @Mock
    private lateinit var httpTransport: MockHttpTransport

    @Mock
    private lateinit var githubClient: GitHubClient

    @InjectMocks
    private lateinit var githubAuth: GitHubAuth

    @Captor
    private lateinit var lengthCaptor: ArgumentCaptor<Int>

    @Test
    fun constructorTest() {
        val gitHubAuth = GitHubAuth(githubAuthConfig, Random())
        val config = Whitebox.getInternalState<GitHubAuthConfig?>(gitHubAuth, "config")
        val randomStringGenerator =
            Whitebox.getInternalState<RandomStringGenerator?>(gitHubAuth, "randomStringGenerator")
        val userService = Whitebox.getInternalState<UserService?>(gitHubAuth, "userService")

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

    @Test
    fun verifyTest() {
        // Mock GitHub config
        val callbackUrl = "https://www.example.com/oauth2/verify"
        val accessTokenUrl = "https://github.com/login/oauth/access_token"
        val accessToken = "e72e16c7e42f292c6912e7710c838347ae178b4a"
        val clientId = "xxxxxxxxxxxxxxxxxxxx"
        val clientSecret = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"

        `when`(githubAuthConfig.callbackUrl).thenReturn(callbackUrl)
        `when`(githubAuthConfig.accessTokenUrl).thenReturn(accessTokenUrl)
        `when`(githubAuthConfig.clientId).thenReturn(clientId)
        `when`(githubAuthConfig.clientSecret).thenReturn(clientSecret)


        // Mock Google HTTP client
        val response = MockLowLevelHttpResponse()
        response.contentType = "application/json"
        response.setContent("""
            {
                "access_token": "$accessToken",
                "scope": "",
                "token_type": "bearer"
            }
        """)

        val request = object : MockLowLevelHttpRequest() {
            override fun execute(): LowLevelHttpResponse = response
        }

        `when`(httpTransport.buildRequest("POST", accessTokenUrl))
            .thenReturn(request)


        // Mock GitHub client
        val user = User()
        user.id = 12345
        user.name = "Homura Akemi"

        `when`(userService.client).thenReturn(githubClient)
        `when`(userService.user).thenReturn(user)


        // Run verify
        val code = "12345"
        val verifyResult = githubAuth.verify(code)
        assertEquals(accessToken, verifyResult.accessToken)
        assertEquals(12345L, verifyResult.userId)
        assertEquals("Homura Akemi", verifyResult.userName)


        // Verify Google HTTP client
        val queryParams = mutableMapOf<String, Any>()
        UrlEncodedParser.parse(request.contentAsString, queryParams)

        assertEquals(listOf("application/json"), request.headers["accept"])
        assertEquals(listOf("12345"), queryParams["code"])
        assertEquals(listOf("authorization_code"), queryParams["grant_type"])
        assertEquals(listOf(callbackUrl), queryParams["redirect_uri"])
        assertEquals(listOf(clientId), queryParams["client_id"])
        assertEquals(listOf(clientSecret), queryParams["client_secret"])


        // Verify GitHub client
        verify(githubClient).setOAuth2Token(accessToken)
    }
}
