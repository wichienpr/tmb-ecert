package th.co.baiwa.buckwaframework.common.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DatatableRequest implements Serializable {

	private static final long serialVersionUID = -2101666256658867880L;
	int start;
	int length;
	List<DatatableSort> sort = new ArrayList<DatatableSort>();

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<DatatableSort> getSort() {
		return sort;
	}

	public void setSort(List<DatatableSort> sort) {
		this.sort = sort;
	}
	
	public boolean isSort() {
		return !this.sort.isEmpty();
	}
}
