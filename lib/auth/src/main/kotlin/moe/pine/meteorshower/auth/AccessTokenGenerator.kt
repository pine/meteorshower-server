package moe.pine.meteorshower.auth

import com.google.common.annotations.VisibleForTesting
import org.apache.commons.text.RandomStringGenerator
import java.security.SecureRandom

class AccessTokenGenerator {
    companion object {
        const val TOKEN_LENGTH = 64
        val DEFAULT_RANDOM_STRING_GENERATOR: RandomStringGenerator =
            {
                val random = SecureRandom.getInstanceStrong()
                val randomStringGenerator =
                    RandomStringGenerator.Builder()
                        .withinRange(
                            charArrayOf('a', 'z'),
                            charArrayOf('A', 'Z'),
                            charArrayOf('0', '9'))
                        .usingRandom(random::nextInt)
                        .build()
                randomStringGenerator
            }()
    }

    private val randomStringGenerator: RandomStringGenerator

    constructor() : this(DEFAULT_RANDOM_STRING_GENERATOR)

    @VisibleForTesting
    internal constructor(randomStringGenerator: RandomStringGenerator) {
        this.randomStringGenerator = randomStringGenerator
    }

    fun generate(): String {
        return randomStringGenerator.generate(TOKEN_LENGTH)
    }
}
