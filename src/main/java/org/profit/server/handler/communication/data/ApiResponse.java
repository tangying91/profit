package org.profit.server.handler.communication.data;

import java.util.ArrayList;
import java.util.List;

public class ApiResponse<T> {

	private boolean success;
	private int event;
	private String msg;
	private long count;
	private List<T> items;

	public ApiResponse(boolean success, String msg) {
		this.success = success;
		this.msg = msg;
		this.items = new ArrayList<T>();
	}

	public ApiResponse(boolean success, long count, List<T> items) {
		this.success = success;
		this.count = count;
		this.items = items;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getEvent() {
		return event;
	}

	public void setEvent(int event) {
		this.event = event;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public List<T> getItems() {
		return items;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}
}
