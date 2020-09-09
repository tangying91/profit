package org.profit.app.seeker

import org.apache.http.HttpStatus
import org.apache.http.client.config.RequestConfig
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import java.io.IOException

open class StockSeeker(val code: String, private val seekerUrl: String) {

    companion object {
        private val LOG = LoggerFactory.getLogger(StockSeeker::class.java)
    }

    /**
     * 需要重写数据处理
     */
    open fun handle(doc: Document) {
        // TODO ...
    }

    /**
     * 对外执行接口
     */
    fun execute() {
        // 解析URL连接
        val url = seekerUrl.replace("#code", code)
        try {
            // 下载逻辑
            download(url)
        } catch (e1: Exception) {
            LOG.error("Exception in {}, {}", url, e1.message)
        }
    }

    /**
     * 下载方法
     *
     * @param url 获取数据地址
     * @return
     */
    @Throws(Exception::class)
    private fun download(url: String) {
        val httpClient = HttpClients.createDefault()
        try {
            val httpGet = HttpGet(url)
            // Request configuration can be overridden at the request level.
            // They will take precedence over the one set at the client level.
            val requestConfig = RequestConfig.custom()
                    .setSocketTimeout(30000)
                    .setConnectTimeout(30000)
                    .build()
            httpGet.config = requestConfig
            val response = httpClient.execute(httpGet)
            try {
                val statusCode = response.statusLine.statusCode
                if (statusCode == HttpStatus.SC_OK) {
                    val entity = response.entity
                    if (entity != null) {
                        val `in` = entity.content
                        try {
                            // 獲取對象內容
                            val doc = Jsoup.parse(`in`, "UTF-8", url)
                            handle(doc)
                        } catch (e: IOException) {
                            throw e
                        } finally {
                            `in`.close()
                        }
                    }
                }
            } finally {
                response.close()
            }
        } finally {
            httpClient.close()
        }
    }
}