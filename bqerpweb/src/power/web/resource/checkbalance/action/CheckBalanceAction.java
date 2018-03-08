/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.web.resource.checkbalance.action;

import java.util.List;

import power.ejb.resource.InvCMaterialFacadeRemote;
import power.web.comm.AbstractAction;

/**
 * 盘点损益表Action
 * @author zhujie
 *
 */
public class CheckBalanceAction extends AbstractAction{

	/** serialVersionUID */
	private static final long serialVersionUID = 1L;
	/** 远程 */
	private InvCMaterialFacadeRemote remote;
	/**
	 * 构造函数
	 */
	public CheckBalanceAction() {
		remote = (InvCMaterialFacadeRemote) factory.getFacadeRemote("InvCMaterialFacade");
	}
	
	public void checkCheckBalanceNo(){
		String bookNo = request.getParameter("bookNo");
		List<String> list = remote.fillAllCheckBalanceNo();
		boolean flag = false;
		if(list!=null&&list.size()>0){
			for(int i =0;i<list.size();i++){
				if(bookNo.equals(list.get(i))){
					flag = true;
				}
			}
		}
		if(flag==true){
			write("1");
		}else{
			write("0");
		}
	}
}
