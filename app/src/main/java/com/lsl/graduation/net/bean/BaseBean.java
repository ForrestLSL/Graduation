package com.lsl.graduation.net.bean;

import java.io.Serializable;

/**
 * 基类序列化
 * @ClassName: Base
 * @author qulq
 * @date 2014年8月20日下午4:51:37
 * @version V1.0
 *
 */
public class BaseBean<T> implements Serializable{
	
	private int id;
	private boolean success;
	private String message;
	private int retcode;
	private T data;
	private int msgCount;
	private int page;
	private int perPage;
	private int total;
	private int totalPages;
	private boolean hasNext;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getRetcode() {
		return retcode;
	}

	public void setRetcode(int retcode) {
		this.retcode = retcode;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPerPage() {
		return perPage;
	}

	public void setPerPage(int perPage) {
		this.perPage = perPage;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public boolean isHasNext() {
		return hasNext;
	}

	public void setHasNext(boolean hasNext) {
		this.hasNext = hasNext;
	}

}
