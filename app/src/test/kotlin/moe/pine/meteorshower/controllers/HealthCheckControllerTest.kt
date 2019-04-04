package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.health.Health
import moe.pine.meteorshower.properties.AppProperties
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class HealthCheckControllerTest {
    private lateinit var mvc: MockMvc
    private lateinit var healthCheckController: HealthCheckController
    private lateinit var appProperties: AppProperties
    private lateinit var healths: ArrayList<Health>

    @Before
    fun setUp() {
        appProperties = AppProperties()
        healths = ArrayList()
        healthCheckController = HealthCheckController(appProperties, healths)
        mvc = MockMvcBuilders
            .standaloneSetup(healthCheckController)
            .build()
    }

    @Test
    fun homeTest_OK() {
        appProperties.siteUrl = "https://www.example.com/"

        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().is3xxRedirection)
            .andExpect(redirectedUrl("https://www.example.com/"))
    }

    @Test
    fun homeTest_NotFound() {
        mvc.perform(MockMvcRequestBuilders.get("/"))
            .andExpect(status().`is`(HttpStatus.NOT_FOUND.value()))
    }

    @Test
    fun healthTest_OK() {
        val okHealth = object : Health {
            override fun alive(): Boolean = true
        }

        healths.add(okHealth)
        healths.add(okHealth)
        healths.add(okHealth)

        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isOk)
    }


    @Test
    fun healthTest_NG() {
        val okHealth = object : Health {
            override fun alive(): Boolean = true
        }
        val ngHealth = object : Health {
            override fun alive(): Boolean = false
        }

        healths.add(okHealth)
        healths.add(okHealth)
        healths.add(ngHealth)

        mvc.perform(MockMvcRequestBuilders.get("/health"))
            .andExpect(status().isServiceUnavailable)
    }
}
