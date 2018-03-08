/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.materialstatus.action;

import java.util.Date;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterialStatus;
import power.ejb.resource.InvCMaterialStatusFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 物料状态码维护action
 * 
 * @author zhengzhipeng
 * @version 1.0
 */
public class LogisticsMaterialStatusAction extends AbstractAction {   
	  
    private static final long serialVersionUID = 1L;
    // ejb之been
    private InvCMaterialStatus statusBeen;
    // ejb之remote 
    private InvCMaterialStatusFacadeRemote statusRemote;

	public InvCMaterialStatus getStatusBeen() {
		return statusBeen;
	}

	public void setStatusBeen(InvCMaterialStatus statusBeen) {
		this.statusBeen = statusBeen;
	}
    
    /**
     * 构造函数
     */
    public LogisticsMaterialStatusAction() {
    	statusRemote = (InvCMaterialStatusFacadeRemote) factory.getFacadeRemote("InvCMaterialStatusFacade");
    }

    /**
     * 页面加载
     * 
     * @throws JSONException
     */
    public void getMaterialStatusList() throws JSONException {
		// 取得查询参数: 开始行
		int intStart = Integer.parseInt(request.getParameter("start"));
		// 取得查询参数: 结束行
		int intLimit = Integer.parseInt(request.getParameter("limit"));	
		
    	PageObject obj = statusRemote.getMaterialStatusList(employee.getEnterpriseCode(),intStart,intLimit);
    	// 查询结果为null,设置页面显示
		if(obj == null) {
		String str = "{\"list\":[],\"totalCount\":0}";
			write(str);
		 // 不为null
		} else {
			if(obj.getList() == null) {
				String str = "{\"list\":[],\"totalCount\":0}";
				write(str);
			}
			String str = JSONUtil.serialize(obj);			
			write(str);
		}
    }

    /**
     * 增加
     * 
     * @throws CodeRepeatException
     */
    public void addMaterialStatus() throws CodeRepeatException {
        try {
    		// 增加的编码与名称
        	String newStatusNo = statusBeen.getStatusNo();
    		String newStatusName = statusBeen.getStatusName();
    		// 获取企业编码
            String enterpriseCode = employee.getEnterpriseCode();
    		// 检查增加的编码与名称是否已经存在
    		if(statusRemote.isStatusNoExist(enterpriseCode, newStatusNo)) {
    			// 编码重复
    			write("{success:true,flag:'2'}");
    			return;
    		}    		
    		if(statusRemote.isStatusNameExist(enterpriseCode, newStatusName)) {    			
    		    // 名称重复
    	        write("{success:true,flag:'1'}");
    	        return;
    		}
    		
    		// 设定企业编码
        	statusBeen.setEnterpriseCode(enterpriseCode);
        	// 设定修改者        
        	statusBeen.setLastModifiedBy(employee.getWorkerCode());
        	// 设定是否可用
        	statusBeen.setIsUse(Constants.IS_USE_Y);
        	// 修改时间
        	statusBeen.setLastModifiedDate(new Date());
        	// 流水号在保存时设定
        	statusRemote.save(statusBeen);
        	
        	// 保存成功	     		
     		write("{success:true,flag:'0'}");           
        } catch (Exception e) {
            write(Constants.ADD_FAILURE);
        }
    }

    /**
     * 更新
     * 
     * @throws CodeRepeatException
     */
    public void updateMaterialStatus() throws CodeRepeatException {
    	try {
    		// 根据画面选择项目的流水号找到类型实体
    		InvCMaterialStatus oldStatusBeen = statusRemote.findById(statusBeen.getMaterialStatusId()); 		
    		// 获取企业编码
            String enterpriseCode = employee.getEnterpriseCode();
    		// 修改前后的名称
    		String newStatusName = statusBeen.getStatusName();
    		String oldStatusName = oldStatusBeen.getStatusName();
    		// check修改后的名称是否已经存在,如已存在，则必须与修改前的名称相同
    		if(statusRemote.isStatusNameExist(enterpriseCode, newStatusName)) {
    			if(!oldStatusName.equals(newStatusName)) {
    				// 名称重复
    	     		write("{success:true,flag:'1'}");
    	     		return;
    			}
    		}
    		// 更新内容    		
	    	// 1.名称
    		oldStatusBeen.setStatusName(statusBeen.getStatusName());	
    		// 2.描述
    		oldStatusBeen.setStatusDesc(statusBeen.getStatusDesc());
    		// 3.设定修改者   
    		oldStatusBeen.setLastModifiedBy(employee.getWorkerCode());
    		// 4.修改日期
    		oldStatusBeen.setLastModifiedDate(new Date());
    		statusRemote.update(oldStatusBeen);
    		// 保存成功	     		
     		write("{success:true,flag:'0'}");
     	} catch (Exception e) {
	    	write(Constants.MODIFY_FAILURE_CARD);
	    }
    }

    /**
     * 删除 通过流水号找到实体，然后进行逻辑删除
     */
    public void deleteMaterialStatus() {
    	// 根据画面选择项目的流水号找到类型实体
		InvCMaterialStatus oldStatusBeen = statusRemote.findById(statusBeen.getMaterialStatusId()); 		

    	// 1.设定修改者   
		oldStatusBeen.setLastModifiedBy(employee.getWorkerCode());
		// 2.修改日期
		oldStatusBeen.setLastModifiedDate(new Date());
    	// 3.设定不可用
		oldStatusBeen.setIsUse(Constants.IS_USE_N);
		// 逻辑删除
		statusRemote.update(oldStatusBeen);		
    }
}