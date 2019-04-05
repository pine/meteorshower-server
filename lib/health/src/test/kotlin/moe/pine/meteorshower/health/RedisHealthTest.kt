package moe.pine.meteorshower.health


import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.any
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnit
import org.mockito.junit.MockitoRule
import org.springframework.data.redis.connection.RedisConnection
import org.springframework.data.redis.core.RedisCallback
import org.springframework.data.redis.core.RedisTemplate


class RedisHealthTest {
    @Rule
    @JvmField
    val mockitoRule: MockitoRule = MockitoJUnit.rule()

    @Mock
    lateinit var redisTemplate: RedisTemplate<String, String>

    @Mock
    lateinit var redisConnection: RedisConnection

    @InjectMocks
    lateinit var redisHealth: RedisHealth

    @Captor
    lateinit var callbackCaptor: ArgumentCaptor<RedisCallback<String>>

    @Test
    fun alive_OK() {
        `when`(redisTemplate.execute(callbackCaptor.capture()))
            .then { "PONG" }

        assertTrue(redisHealth.alive())


        `when`(redisConnection.ping()).thenReturn("PONG")

        val callback = callbackCaptor.value
        assertEquals("PONG", callback.doInRedis(redisConnection))

        verify(redisConnection).ping()
    }

    @Test
    fun alive_NG() {
        `when`(redisTemplate.execute(any<RedisCallback<String>>()))
            .then { "" }

        assertFalse(redisHealth.alive())
    }
}
