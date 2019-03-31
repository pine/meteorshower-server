package moe.pine.meteorshower.auth

import org.apache.commons.text.RandomStringGenerator
import java.util.Random

object RandomStringGeneratorFactory {
    fun newInstance(random: Random): RandomStringGenerator {
        return RandomStringGenerator.Builder()
            .withinRange(
                charArrayOf('a', 'z'),
                charArrayOf('A', 'Z'),
                charArrayOf('0', '9'))
            .usingRandom(random::nextInt)
            .build()
    }
}