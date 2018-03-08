/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.birt.bean.hr;

import java.util.ArrayList;
import java.util.List;

import power.ejb.hr.ContractQuery;

/**
 * 合同台帐Bean
 * @author zhujie
 *
 */
public class ContractBean {
	
	/** 合同账票Table用数据 */
	private List<ContractQuery> list = new ArrayList<ContractQuery>();

	/**
	 * @return the list
	 */
	public List<ContractQuery> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(List<ContractQuery> list) {
		this.list = list;
	} 
}
