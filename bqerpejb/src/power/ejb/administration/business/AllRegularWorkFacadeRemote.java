/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.business;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AllRegularWorkFacadeRemote {
	/**
	 * 全部定期工作
	 * 	
	 * @return PageObject
	 */
	public PageObject getAllRegularWorkList();

}