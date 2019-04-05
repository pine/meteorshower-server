package moe.pine.meteorshower.controllers

import moe.pine.meteorshower.services.setting.Setting
import moe.pine.meteorshower.services.setting.SettingExcludeRepository
import moe.pine.meteorshower.services.setting.SettingService
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.verify
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders


@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SettingControllerTest {
    @Mock
    private lateinit var settingService: SettingService

    @InjectMocks
    private lateinit var settingController: SettingController

    private lateinit var mvc: MockMvc

    @Before
    fun setUp() {
        mvc = MockMvcBuilders
            .standaloneSetup(settingController)
            .build()
    }

    @Test
    fun getTest() {
        val repository1 = SettingExcludeRepository(
            owner = "homuhomu"
        )
        val repository2 = SettingExcludeRepository(
            owner = "mami",
            name = "diary"
        )
        val setting = Setting(
            excludeRepositories = listOf(repository1, repository2),
            excludeForked = false,
            excludeGist = true
        )
        `when`(settingService.get()).thenReturn(setting)

        mvc.perform(MockMvcRequestBuilders.get("/api/settings"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("excludeRepositories[0].owner", `is`("homuhomu")))
            .andExpect(jsonPath("excludeRepositories[0].name", nullValue()))
            .andExpect(jsonPath("excludeRepositories[1].owner", `is`("mami")))
            .andExpect(jsonPath("excludeRepositories[1].name", `is`("diary")))
            .andExpect(jsonPath("excludeForked", `is`(false)))
            .andExpect(jsonPath("excludeGist", `is`(true)))
    }

    @Test
    fun saveTest() {
        val repository1 = SettingExcludeRepository(
            owner = "homuhomu"
        )
        val repository2 = SettingExcludeRepository(
            owner = "mami",
            name = "diary"
        )
        val setting = Setting(
            excludeRepositories = listOf(repository1, repository2),
            excludeForked = false,
            excludeGist = true
        )
        doNothing().`when`(settingService).save(setting)

        val request = MockMvcRequestBuilders
            .post("/api/settings")
            .contentType(MediaType.APPLICATION_JSON)
            .content("""
                {
                    "excludeRepositories": [
                        {
                            "owner": "homuhomu"
                        },
                        {
                            "owner": "mami",
                            "name": "diary"
                        }
                    ],
                    "excludeForked": false,
                    "excludeGist": true
                }
            """)
        mvc.perform(request)
            .andExpect(status().isNoContent)

        verify(settingService).save(setting)
    }
}
