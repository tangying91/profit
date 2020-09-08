package org.profit.server.handler.communication.data.impl

import org.profit.server.handler.communication.data.AbstractResponseData
import java.util.*

/**
 * Created by TangYing on 14-4-25.
 */
class ResponsePagingData<T> : AbstractResponseData() {
    var data: List<T> = ArrayList()
    var totalCount = 0

}