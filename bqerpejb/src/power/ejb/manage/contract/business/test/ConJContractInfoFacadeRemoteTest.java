package power.ejb.manage.contract.business.test;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJConDoc;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.form.ContractFullInfo;

public class ConJContractInfoFacadeRemoteTest {
	protected static ConJContractInfoFacadeRemote remote;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		remote=(ConJContractInfoFacadeRemote)Ejb3Factory.getInstance().getFacadeRemote("ConJContractInfoFacade");
	}

	@Test
	public void testSave() throws CodeRepeatException {
		ConJContractInfo model=new ConJContractInfo();
		model.setActAmount(12d);
		model.setAppliedAmount(11d);
		model.setApprovedAmount(10d);
		model.setCliendId(11L);
		model.setConAbstract("abc");
		model.setContractName("合同");
		model.setConTypeId(1l);
		model.setEnterpriseCode("hfdc");
		model.setConYear("2009");
		model.setCurrencyType(1L);
		model.setEntryBy("999999");
		model.setEntryDate(new Date());
		model.setExecFlag(1l);
		model.setFileStatus("Y");
		model.setFileBy("abc");
		model.setFileDate(new Date());
		model.setFileNo("000");
		model.setIsSum("Y");
		model.setIsInstant("Y");
		model.setIsSign("Y");
		model.setItemId("8");
		ConJConDoc doc=new ConJConDoc();
//		doc.setDocContent("");
		doc.setDocMemo("ff");
		doc.setDocName("cs");
		remote.save("abc", model,doc,null);
	}

	@Test
	public void testDelete() throws CodeRepeatException {
		remote.delete(remote.findById(1L));
	}

	@Test
	public void testUpdate() {
//		fail("Not yet implemented");
		remote.getConFullInfoById(1L);
	}

	@Test
	public void testFindContractInfos() {
		PageObject pg=remote.findContractInfos(Long.parseLong("2"),"hfdc", "2006-10-01", "2009-12-30", "%", "", "","", 0,10);
		System.out.print(pg.getTotalCount());
	}
	
	@Test
	public void testGetConFullInfoById(){
		ContractFullInfo m=remote.getConFullInfoById(1L);
		List list=m.getMconAttlist();
		System.out.print(list.size());
	}

	//===========================drdu=============================
	
	@Test
	public void testfindContractTerminateList()
	{
		PageObject po = remote.findContractTerminateList(Long.parseLong("2"),"hfdc", "%");
		System.out.print(po.getTotalCount());
	}
	
	@Test
	public void testfindConIntegrateList()
	{
		PageObject pg  = remote.findConIntegrateList(Long.parseLong("2"),"hfdc", null, null, null, null, null, null, null, null);
		System.out.print(pg.getTotalCount());
	}
}
