package com.example.mvvmbaseproject.api

import android.util.Log
import okhttp3.TlsVersion
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import kotlin.collections.ArrayList

class TLSSocketFactory @Throws(KeyManagementException::class, NoSuchAlgorithmException::class)
internal constructor() : SSLSocketFactory() {
    private val delegate: SSLSocketFactory

    init {
        val context = SSLContext.getInstance("TLS")
        context.init(null, null, null)
        delegate = context.socketFactory
    }

    override fun getDefaultCipherSuites(): Array<String> {
        return delegate.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return delegate.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket? {
        return enableTLSOnSocket(delegate.createSocket(s, host, port, autoClose))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port, localHost, localPort))
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(host, port))
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket? {
        return enableTLSOnSocket(delegate.createSocket(address, port, localAddress, localPort))
    }

    private fun enableTLSOnSocket(socket: Socket): Socket {
        if (socket is SSLSocket) {
            try {
                val sslParameters = SSLContext.getDefault().defaultSSLParameters
                var isTLS3 = false
                var protocolList: ArrayList<String>? = null
                if (sslParameters != null && sslParameters.protocols != null) {
                    protocolList = ArrayList(Arrays.asList(*sslParameters.protocols))
                }

                if (protocolList != null) {
                    for (protocol in protocolList) {
                        if (protocol.equals(TlsVersion.TLS_1_3.javaName(), ignoreCase = true)) {
                            isTLS3 = true
                            break
                        }
                    }
                }

                if (protocolList != null && protocolList.size > 0 && isTLS3) {
                    socket.enabledProtocols = arrayOf(
                        TlsVersion.TLS_1_2.javaName(),
                        TlsVersion.TLS_1_3.javaName(),
                        TlsVersion.TLS_1_1.javaName(),
                        TlsVersion.TLS_1_0.javaName()
                    )
                    Log.e(TLSSocketFactory::class.java.name, "TLS 1.3")
                } else {
                    socket.enabledProtocols =
                        arrayOf(
                            TlsVersion.TLS_1_2.javaName(),
                            TlsVersion.TLS_1_1.javaName(),
                            TlsVersion.TLS_1_0.javaName()
                        )
                }

            } catch (e: Exception) {
                e.printStackTrace()
                try {
                    socket.enabledProtocols =
                        arrayOf(
                            TlsVersion.TLS_1_2.javaName(),
                            TlsVersion.TLS_1_1.javaName(),
                            TlsVersion.TLS_1_0.javaName()
                        )
                } catch (e1: Exception) {
                    e1.printStackTrace()
                    try {
                        socket.enabledProtocols = arrayOf(TlsVersion.TLS_1_2.javaName(), TlsVersion.TLS_1_1.javaName())
                    } catch (e2: Exception) {
                        e2.printStackTrace()
                        try {
                            socket.enabledProtocols =
                                arrayOf(TlsVersion.TLS_1_1.javaName(), TlsVersion.TLS_1_0.javaName())
                        } catch (e4: Exception) {
                            e4.printStackTrace()
                            try {
                                socket.enabledProtocols = arrayOf(TlsVersion.TLS_1_2.javaName())
                            } catch (e5: Exception) {
                                e5.printStackTrace()
                                try {
                                    socket.enabledProtocols = arrayOf(TlsVersion.TLS_1_0.javaName())
                                } catch (e6: Exception) {
                                    e6.printStackTrace()
                                }

                            }

                        }

                    }

                }

            }

        }
        return socket
    }
}
