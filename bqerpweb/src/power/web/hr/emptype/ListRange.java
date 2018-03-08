package power.web.hr.emptype;

import java.util.ArrayList;
import java.util.List;

public class ListRange<T> {
	boolean success;
	String message;
	long totalProperty;
	List<T> root;

	public ListRange() {
		this.totalProperty = 0;
		this.root = new ArrayList<T>();
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

	/**
	 * @return the totalProperty
	 */
	public long getTotalProperty() {
		return totalProperty;
	}

	/**
	 * @param totalProperty the totalProperty to set
	 */
	public void setTotalProperty(long totalProperty) {
		this.totalProperty = totalProperty;
	}


	/**
	 * @return the root
	 */
	public List<T> getRoot() {
		return root;
	}


	/**
	 * @param root the root to set
	 */
	public void setRoot(List<T> root) {
		this.root = root;
	}

}
