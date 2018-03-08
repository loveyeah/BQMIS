package power.ejb.system.testcase;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.system.SysCUlFacadeRemote;

public class SysCUlFacadeRemoteTest {
	
	static SysCUlFacadeRemote remote;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (SysCUlFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("SysCUlFacade");
	}
	
//	@Test
//	public void testSave() {
//		SysCUl m = new SysCUl();
//		m.setWorkerId(300L);
//		m.setWorkerCode("sfsfsfa"); 
//		remote.save(m); 
//	}

	@Test
	public void testFindAll() throws  Exception{
//        PageObject o =  remote.findUsers("%", 0,8);
        
	}
	@Test
	public void testFindByroleIdAndCodeOrName(){
		PageObject o1 =  remote.findByroleIdAndCodeOrName(Long.valueOf(1), "2", true);
		PageObject o2 =  remote.findByroleIdAndCodeOrName(Long.valueOf(1), "", false);
		System.out.print(o1.getTotalCount()+"####"+o2.getTotalCount());
	}
	@Test
	public void testFindUserByDeptAndRole(){
		PageObject o=remote.findUserByDeptAndRole(105l,"", 31l);
		System.out.print(o.getTotalCount());
	}

}
