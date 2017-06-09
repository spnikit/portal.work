package sheff.rjd.ws.OCO.BeforeOpen;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.transform.TransformerException;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.w3c.dom.DOMException;

import ru.aisa.rgd.etd.dao.ETDFacade;
import ru.aisa.rgd.ws.documents.ETDForm;
import ru.aisa.rgd.ws.domain.RequisitesWithName;
import ru.aisa.rgd.ws.exeption.InternalException;
import ru.aisa.rgd.ws.exeption.ServiceException;
import ru.aisa.rgd.ws.utility.DataBinder;

public class CreateGU2b {
	private static final String GET_REQUISITES_SQL = "select inn, kpp, (okpo_kod) okpo, "+
"(select rtrim(name) from snt.pred where id = (case when(select headid from snt.pred where id = :PREDID) "+
" is null then :PREDID else (select headid from snt.pred where id = :PREDID) end) ) name from snt.pred where id = :PREDID";
	
	public byte[] CreateGU2b(byte[] template, int predid,ETDFacade facade) throws ServiceException, IOException, DOMException, InternalException, TransformerException{
		
		ETDForm GU2bform=new ETDForm(template);
		DataBinder gu2bdb = GU2bform.getBinder();
		gu2bdb.setNodeValue("sposob", "2");
		
		RequisitesWithName req = getRequisitesByPredId(predid, facade);
		gu2bdb.setNodeValue("P_4", req.getHeadname());
		gu2bdb.setNodeValue("P_4a", req.getOkpo());
		gu2bdb.setNodeValue("P_4b", req.getInn());
		gu2bdb.setNodeValue("P_4v", req.getKpp());
		return GU2bform.encodeToArchiv();
		}
	
	class RequisitesWithNameWrapper implements ParameterizedRowMapper<RequisitesWithName> {
		public RequisitesWithName mapRow(ResultSet rs, int n) throws SQLException {
			RequisitesWithName req = new RequisitesWithName();
			req.setInn(rs.getString("INN"));
			req.setKpp(rs.getString("KPP"));
			req.setOkpo(rs.getInt("OKPO"));
			req.setHeadname(rs.getString("NAME"));
			return req;
		}

	}
	private RequisitesWithName getRequisitesByPredId(int predid, ETDFacade facade)
			throws IncorrectResultSizeDataAccessException {
		RequisitesWithName req = null;

		try {
			req = (RequisitesWithName) facade.getNpjt().queryForObject(
					GET_REQUISITES_SQL,
					new MapSqlParameterSource("PREDID", predid),
					new RequisitesWithNameWrapper());
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException(new Exception(), -1,
					"Внутренняя ошибка сервиса");

		}

		return req;

	}
}
