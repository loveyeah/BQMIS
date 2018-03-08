package power.ejb.productiontec.report;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

@Remote
public interface ptJDependReportFacadeRemote {
	public PageObject findAllOnlyYear(String dependType,String dependYear,int... rowStartIdxAndCount);
	public void save(ptJDependReport entity);
	public ptJDependReport findById(String Id);
	public ptJDependReport update(ptJDependReport entity);
	public void delete(ptJDependReport entity);
	public void deleteMulti(String ids);
}
