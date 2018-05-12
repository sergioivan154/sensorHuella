package sagarpa.planetmedia.com.sagarpapp.Model.Steganography

private class MessageEncodingStatus {
    var isMessageEncoded: Boolean = false
    var currentMessageIndex: Int = 0
    var byteArrayMessage: ByteArray? = null
    var message: String? = null

    fun incrementMessageIndex() {
        currentMessageIndex++
    }
}