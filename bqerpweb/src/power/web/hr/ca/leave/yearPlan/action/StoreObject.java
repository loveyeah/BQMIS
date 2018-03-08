/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.hr.ca.leave.yearPlan.action;

import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class StoreObject implements Serializable{
	private MetaData metaData;
	private Long totalCount;
	@SuppressWarnings("unchecked")
	private List list;

	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	@SuppressWarnings("unchecked") 
	public List getList() {
		return list;
	}
	@SuppressWarnings("unchecked")
	public void setList(List list) {
		this.list = list;
	}
	public MetaData getMetaData() {
		return metaData;
	}
	public void setMetaData(MetaData metaData) {
		this.metaData = metaData;
	}
}
