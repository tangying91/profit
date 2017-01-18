package org.profit.server.handler.communication.data.impl;

import org.profit.server.handler.communication.data.AbstractResponseData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TangYing on 14-4-25.
 */
public class ResponsePagingData<T> extends AbstractResponseData {

    private List<T> data = new ArrayList<T>();
    private int totalCount = 0;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
