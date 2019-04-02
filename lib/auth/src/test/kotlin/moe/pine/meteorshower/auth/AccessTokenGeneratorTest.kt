package moe.pine.meteorshower.auth

import org.apache.commons.text.RandomStringGenerator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.reflect.Whitebox
import java.util.Random

@RunWith(PowerMockRunner::class)
@PrepareForTest(RandomStringGenerator::class)
class AccessTokenGeneratorTest {
    @Mock
    lateinit var randomStringGenerator: RandomStringGenerator

    @Test
    fun constructorTest() {
        val random = Random()
        val accessTokenGenerator = AccessTokenGenerator(random)

        val randomStringGenerator =
            Whitebox.getInternalState<RandomStringGenerator?>(
                accessTokenGenerator, "randomStringGenerator")
        assertNotNull(randomStringGenerator)
    }

    @Test
    fun generateTest() {
        val accessTokenGenerator = AccessTokenGenerator(randomStringGenerator)

        `when`(randomStringGenerator.generate(AccessTokenGenerator.TOKEN_LENGTH))
            .thenReturn("TOKEN")

        assertEquals("TOKEN", accessTokenGenerator.generate())

        verify(randomStringGenerator).generate(AccessTokenGenerator.TOKEN_LENGTH);
    }
}