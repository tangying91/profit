package org.profit.server.handler.communication;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.profit.server.handler.communication.data.impl.ResponsePagingData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TangYing on 14-4-25.
 */
public class ResponsePaging<T> {

    private ResponsePagingData INSTANCE;

    public ResponsePaging(String response) {
        try {
            INSTANCE =  new Gson().fromJson(response, new TypeToken<ResponsePagingData<T>>(){}.getType());
        } catch (Exception e) {
            INSTANCE = new ResponsePagingData();
        }
    }

    public List<T> getItems() {
        if (INSTANCE == null) {
            return new ArrayList<T>();
        }
        return INSTANCE.getData();
    }

    public int getTotalCount() {
        if (INSTANCE == null) {
            return 0;
        }
        return INSTANCE.getTotalCount();
    }
}
