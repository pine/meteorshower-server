package moe.pine.meteorshower.github.auth

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.Random

class RandomStringGeneratorFactoryTest {
    @Test
    fun newInstanceTest() {
        val random = Random()
        val randomStringGenerator = RandomStringGeneratorFactory.newInstance(random)

        assertEquals("", randomStringGenerator.generate(0))
        assertEquals(10, randomStringGenerator.generate(10).length)

        val generatedText = randomStringGenerator.generate(1000)
        val regex = "^[a-zA-Z0-9]+$".toRegex()
        assertTrue(regex.matches(generatedText))
    }
}
