package org.profit.server.handler.communication.data

import java.util.*

class ApiResponse<T> {
    var isSuccess: Boolean
    var event = 0
    var msg: String? = null
    var count: Long = 0
    var items: List<T>

    constructor(success: Boolean, msg: String?) {
        isSuccess = success
        this.msg = msg
        items = ArrayList()
    }

    constructor(success: Boolean, count: Long, items: List<T>) {
        isSuccess = success
        this.count = count
        this.items = items
    }

}