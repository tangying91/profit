package org.profit.app.service

import org.profit.app.seeker.StockHistorySeeker

class DownloadHistoryService(val code: String, val date: String): StockService {

    override fun execute() {
        val stockHistorySeeker = StockHistorySeeker(code)
        stockHistorySeeker.execute()
    }
}