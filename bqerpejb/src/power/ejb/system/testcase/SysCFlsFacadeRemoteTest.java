package power.ejb.system.testcase; 
import java.util.List;

import junit.framework.Assert;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.system.SysCFls;
import power.ejb.system.SysCFlsFacadeRemote;

public class SysCFlsFacadeRemoteTest { 
	protected static  SysCFlsFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 remote =(SysCFlsFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("SysCFlsFacade");
	} 
	@Test
	public void testFindAll() {
		List <SysCFls> result = remote.findAll(0L);  
		if(result==null || result.size()==0)
		{
			Assert.fail("没有查询到数据");
		}  
	} 
	
	@Test
	public void testSave()
	{
		SysCFls model = new SysCFls(); 
		remote.save(model);
	}
	
//	@Test
//	public final void testSave() {
//		SysCFls model = new SysCFls();
//		model.setFileId(102L);
//		model.setFileName("工作流管理");
//		remote.save(model);  
//	} 
//	@Test
//	public final void testDelete() {
//		SysCFls model = remote.findById(102L);
//		remote.delete(model);
//	}
//	@Test
//	public final void testUpdate() {
//		SysCFls model = remote.findById(100L);
//		model.setFileAddr("http://www.baidu.com");
//		remote.update(model);
//	} 
}
