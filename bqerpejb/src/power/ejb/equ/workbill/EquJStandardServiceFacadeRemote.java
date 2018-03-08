package power.ejb.equ.workbill;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

@Remote
public interface EquJStandardServiceFacadeRemote
{
public void save(EquJStandardService entity);
	
	public boolean save(List<EquJStandardService> addList,
			List<EquJStandardService> updateList, String delIds);

	public boolean delete(String ids);

	
	public EquJStandardService update(EquJStandardService entity);

	public EquJStandardService findById(Long id);

	
	public List<EquJStandardService> findByProperty(String propertyName,
			Object value);

	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
	public PageObject getEquCStandardService(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);

	}