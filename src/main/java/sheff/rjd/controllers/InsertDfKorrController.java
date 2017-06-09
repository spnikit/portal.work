package sheff.rjd.controllers;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.aisa.portal.invoice.integration.dao.IntegrationDao;

public class InsertDfKorrController extends AbstractController{
	private IntegrationDao integrationDao;
	
	public IntegrationDao getIntegrationDao() {
		return integrationDao;
	}

	public void setIntegrationDao(IntegrationDao integrationDao) {
		this.integrationDao = integrationDao;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String guid  = UUID.randomUUID().toString();
//		System.out.println("in insertDfCorr");
		String docid = request.getParameter("docid");
//		System.out.println("docid "+docid);
		long id = Long.parseLong(docid);
	/*	int stat = integrationDao.GetKorrStat(di);
		System.out.println("stat "+stat);*/
		integrationDao.insertDFKorr(id, guid, 0, null);
		integrationDao.updtGfsgnForKorr(id);
		return null;
	}

}
