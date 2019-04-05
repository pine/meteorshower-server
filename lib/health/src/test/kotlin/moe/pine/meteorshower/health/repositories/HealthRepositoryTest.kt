package moe.pine.meteorshower.health.repositories

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class HealthRepositoryTest : TestBase() {
    private lateinit var healthRepository: HealthRepository

    @Before
    override fun setUp() {
        super.setUp()
        healthRepository =
            sqlSession.getMapper(HealthRepository::class.java)
    }

    @Test
    fun aliveTest() {
        assertEquals(1L, healthRepository.alive())
    }
}