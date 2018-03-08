package power.web.productiontec.dependability.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxGeneratorInfo;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxGeneratorInfoFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class GeneratorInfoAction extends AbstractAction {
	private PtKkxGeneratorInfoFacadeRemote remote;
	private PtKkxGeneratorInfo pgInfo;

	public GeneratorInfoAction() {
		remote = (PtKkxGeneratorInfoFacadeRemote) factory
				.getFacadeRemote("PtKkxGeneratorInfoFacade");
	}

	public void getGeneratorInfo() throws JSONException {
		String pBlockId = request.getParameter("pBlockId");
		List<PtKkxGeneratorInfo> list = remote.findInfoByBlockId(pBlockId);
		System.out.println(JSONUtil.serialize(list));
		write(JSONUtil.serialize(list));
	}

	public void saveGeneratorInfo() {
		if (pgInfo.getGeneratorId() == null) {
			pgInfo.setEnterpriseCode("hfdc");
			PtKkxGeneratorInfo model = remote.save(pgInfo);
			write("{success : true,generatorId : " + model.getGeneratorId()
					+ "}");
		} else {
			PtKkxGeneratorInfo entity = new PtKkxGeneratorInfo();
			pgInfo.setEnterpriseCode(entity.getEnterpriseCode());
			pgInfo.setIsUse(entity.getIsUse());
			PtKkxGeneratorInfo model = remote.update(pgInfo);
			write("{success : true,generatorId : " + model.getGeneratorId()
					+ "}");
		}
	}

	public PtKkxGeneratorInfo getPgInfo() {
		return pgInfo;
	}

	public void setPgInfo(PtKkxGeneratorInfo pgInfo) {
		this.pgInfo = pgInfo;
	}

}
