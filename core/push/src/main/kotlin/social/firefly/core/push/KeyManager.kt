package social.firefly.core.push

import android.util.Base64
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec

class KeyManager {
    private fun generateAuthSecret(): String = ByteArray(AUTH_SECRET_BYTE_SIZE).apply {
        SecureRandom().nextBytes(this)
    }.encode()

    private fun generateKeyPair(): KeyPair = KeyPairGenerator.getInstance("EC").apply {
        initialize(
            ECGenParameterSpec("prime256v1")
        )
    }.generateKeyPair()

    private fun ByteArray.encode(): String = Base64.encodeToString(
        this,
        Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
    )

    fun generatePushKeys(): EncodedPushKeys {
        val keyPair = generateKeyPair()
        return EncodedPushKeys(
            privateKey = keyPair.private.encoded.encode(),
            publicKey = keyPair.public.encoded.encode(),
            authSecret = generateAuthSecret(),
        )
    }

    companion object {
        private const val AUTH_SECRET_BYTE_SIZE = 16
    }
}
