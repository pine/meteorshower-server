package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.auth.AuthFlowService
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {
    @Mock
    private lateinit var authFlowService: AuthFlowService

    @InjectMocks
    private lateinit var authController: AuthController

    private lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders
            .standaloneSetup(authController)
            .build()
    }

    @Test
    fun requestTest() {
        val callbackUrl = "https://www.example.com/callback"
        val redirectUrl = "https://www.auth.org/oauth2/authorize?foo=bar"

        `when`(authFlowService.request(callbackUrl))
            .thenReturn(redirectUrl)

        val request = MockMvcRequestBuilders
            .get("/oauth2/request")
            .param("callback_url", callbackUrl)

        mvc.perform(request)
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl(redirectUrl))
    }
}
