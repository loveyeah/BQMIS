package power.ejb.workticket.test;

import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.RunCWorkticketDangerType;
import power.ejb.workticket.RunCWorkticketDangerTypeFacadeRemote;

public class RunCWorkticketDangerTypeFacadeRemoteTest {

	protected static RunCWorkticketDangerTypeFacadeRemote remote;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote = (RunCWorkticketDangerTypeFacadeRemote) Ejb3Factory
				.getInstance()
				.getFacadeRemote("RunCWorkticketDangerTypeFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		RunCWorkticketDangerType dangerType = new RunCWorkticketDangerType();
	
		dangerType.setDangerTypeName("ttt");
		dangerType.setEnterpriseCode("hfdc");
		dangerType.setModifyBy("刘应文");
		dangerType.setOrderBy(1L);
		dangerType.setWorkticketTypeCode("1");
		remote.save(dangerType);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		Long dangerTypeId = 2l;
		remote.delete(dangerTypeId);
	}

	@Test
	public void testDeleteMulti() {
		String ids = "1,2";
		remote.deleteMulti(ids);
	}

	@Test
	public void testUpdate() throws CodeRepeatException {
		RunCWorkticketDangerType dangerType = remote.findById(1l);
		dangerType.setDangerTypeName("");
		dangerType.setEnterpriseCode("911");
		dangerType.setModifyBy("liuyingwen");
		dangerType.setIsUse("N");
		remote.update(dangerType);
	}

	@Test
	public void testFindAll() {
		PageObject result=remote.findAll("hfdc","", "");
		System.out.println("当前的list.size():"+result.getList().size()+"   总数:"+result.getTotalCount());
	}

}
