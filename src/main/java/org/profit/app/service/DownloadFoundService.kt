package org.profit.app.service

import org.profit.app.seeker.StockFoundSeeker

class DownloadFoundService(val code: String, val date: String): StockService {

    override fun execute() {
        val stockFoundSeeker = StockFoundSeeker(code)
        stockFoundSeeker.execute()
    }
}