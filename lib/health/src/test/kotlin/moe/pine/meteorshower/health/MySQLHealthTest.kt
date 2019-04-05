package moe.pine.meteorshower.health

import moe.pine.meteorshower.health.repositories.HealthRepository
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule

class MySQLHealthTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var healthRepository: HealthRepository

    @InjectMocks
    lateinit var mysqlHealth: MySQLHealth

    @Test
    fun aliveTest_OK() {
        `when`(healthRepository.alive()).thenReturn(1L)
        assertTrue(mysqlHealth.alive())
    }

    @Test
    fun aliveTest_NG() {
        `when`(healthRepository.alive()).thenReturn(0L)
        assertFalse(mysqlHealth.alive())
    }
}