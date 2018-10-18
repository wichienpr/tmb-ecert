package th.co.baiwa.buckwaframework.common.bean;

public class ResponseData<T> {
	private T data;

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
