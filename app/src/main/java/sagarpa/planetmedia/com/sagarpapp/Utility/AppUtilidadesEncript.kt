package sagarpa.planetmedia.com.sagarpapp.Utility

import java.util.*
import java.security.MessageDigest
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec
import org.apache.commons.codec.binary.Base64


class AppUtilidadesEncript {

    fun Encriptar(texto: String): String {

        val secretKey = "planetmedia" //llave para encriptar datos
        var base64EncryptedString = ""

        try {

            val md = MessageDigest.getInstance("MD5")
            val digestOfPassword = md.digest(secretKey.toByteArray(charset("utf-8")))
            val keyBytes = Arrays.copyOf(digestOfPassword, 24)

            val key = SecretKeySpec(keyBytes, "DESede")
            val cipher = Cipher.getInstance("DESede")
            cipher.init(Cipher.ENCRYPT_MODE, key)

            val plainTextBytes = texto.toByteArray(charset("utf-8"))
            val buf = cipher.doFinal(plainTextBytes)
            val base64Bytes = Base64.encodeBase64(buf)
            base64EncryptedString = String(base64Bytes)

        } catch (ex: Exception) {
        }

        return base64EncryptedString
    }

    @Throws(Exception::class)
    fun Desencriptar(textoEncriptado: String): String {

        val secretKey = "planetmedia" //llave para encriptar datos
        var base64EncryptedString = ""

        try {
            val message = Base64.decodeBase64(textoEncriptado.toByteArray(charset("utf-8")))
            val md = MessageDigest.getInstance("MD5")
            val digestOfPassword = md.digest(secretKey.toByteArray(charset("utf-8")))
            val keyBytes = Arrays.copyOf(digestOfPassword, 24)
            val key = SecretKeySpec(keyBytes, "DESede")

            val decipher = Cipher.getInstance("DESede")
            decipher.init(Cipher.DECRYPT_MODE, key)

            val plainText = decipher.doFinal(message)

            base64EncryptedString = String(plainText, Charsets.UTF_8)

        } catch (ex: Exception) {
        }

        return base64EncryptedString
    }



}