package power.ejb.system.testcase;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.system.SysCRolesFacadeRemote;
import power.ejb.system.SysJUr;

public class SysCRolesFacadeRemoteTest {
	protected static  SysCRolesFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote =(SysCRolesFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("SysCRolesFacade");
	} 
	@Test
	public void testFindAll() {
		 remote.findAll("%", "%");
		 
	} 
	
	@Test
	public void testGrantRoleUsers(){
		List<SysJUr> roleUsers = new ArrayList<SysJUr>();
		for(int i = 1;i<37;i++)
		{
			SysJUr ur = new SysJUr(); 
			ur.setModifyBy("111"+i);
			roleUsers.add(ur);
		}
		remote.grantRoleUsers(roleUsers);
	}
	@Test
	public void testFindByRoleName(){
		List list=remote.findByRoleName("票负责人");
		System.out.print(list.size());
	}
}
