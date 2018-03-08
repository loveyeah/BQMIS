package power.ejb.resource.test;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCTempMaterial;
import power.ejb.resource.InvCTempMaterialFacadeRemote;

public class InvCTempMaterialFacadeRemoteTest {

	private static InvCTempMaterialFacadeRemote remote;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(InvCTempMaterialFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("InvCTempMaterialFacade");
	}
	
	@Test
	public void testAdd()
	{
		InvCTempMaterial model = new InvCTempMaterial();
		model.setEnterpriseCode("hfdc");
		model.setLastModifiedBy("999999");
		model.setMaertialClassId(1l);
		model.setMaterialName("测试");
		remote.save(model);
	}
	
	@Test
	public void testUpdate()
	{
		InvCTempMaterial model = remote.findById(1L);
		model.setMaterialName("aaaa");
		model.setLastModifiedBy("777777");
		model.setEnterpriseCode("hfdc");
		remote.update(model);
	}
	
	@Test
	public void testDelete()
	{
		String ids="1,2";
		remote.deleteMulti(ids);
	}
	
	@Test
	public void findAllList()
	{
		PageObject result=remote.findTempMaterialList("999999","hfdc", null, null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
	@Test
	public void findApproveList()
	{
		PageObject result = remote.findApprovelList("hfdc", null);
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}
}
