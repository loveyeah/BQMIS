package power.ejb.equ.base;

import java.text.ParseException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * 
 */

@Remote
public interface EquLibraryManage {
	public PageObject find(String fuzzy,String fuzzy1, String enterpriseCode, final int... rowStartIdxAndCount);

	//add by ypan 20100919
	public PageObject findQueryList(String enterpriseCode,String materialName, String modelName, final int... rowStartIdxAndCount);
}
