package moe.pine.meteorshower.auth

import com.google.common.annotations.VisibleForTesting
import org.apache.commons.text.RandomStringGenerator
import java.util.Random

class AccessTokenGenerator {
    companion object {
        const val TOKEN_LENGTH = 64
    }

    private val randomStringGenerator: RandomStringGenerator

    constructor(random: Random)
        : this(RandomStringGeneratorFactory.newInstance(random))

    @VisibleForTesting
    internal constructor(randomStringGenerator: RandomStringGenerator) {
        this.randomStringGenerator = randomStringGenerator
    }

    fun generate(): String {
        return randomStringGenerator.generate(TOKEN_LENGTH)
    }
}
