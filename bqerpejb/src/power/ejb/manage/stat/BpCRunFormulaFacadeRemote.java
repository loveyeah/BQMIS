package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCRunFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCRunFormulaFacadeRemote {

	public PageObject getRunFormulaList(String itemCode, String deriveDataType,
			String enterpriseCode, int... rowStartIdxAndCount);

	public String getRunFormulaVchar(String itemCode, String enterpriseCode);

	public boolean saveRunFormulaList(String itemCode,
			List<BpCRunFormula> addList);

}