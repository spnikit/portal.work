package sheff.rjd.services.syncutils;
import rzd8888.gvc.etd.was.etd.synchronizeXfdlDoc.AddParams;
public class GetSyncParams {

	public SyncParamObject getparams(SyncParamObject obj, AddParams params){
		
		
		for (int i = 0; i < params.getParamArray().length; i++) {

			if (params.getParamArray(i).getName()
					.equals(obj.getIsokpo()))

				obj.setOkpo(Integer.parseInt(params
						.getParamArray(i).getStringValue().trim()));

			if (params.getParamArray(i).getName()
					.equals(obj.getIsinn())){
				if (!params.getParamArray(i)
						.getStringValue().equals("-"))
				obj.setInn(params.getParamArray(i)
						.getStringValue().trim());
			}
			if (params.getParamArray(i).getName()
					.equals(obj.getIskpp())){
				if (!params.getParamArray(i)
						.getStringValue().equals("-"))
				obj.setKpp(params.getParamArray(i)
						.getStringValue().trim());
			}
			if (params.getParamArray(i).getName()
					.equals("id_pak"))

				obj.setId_pak(params.getParamArray(i)
						.getStringValue());

			if (params.getParamArray(i).getName()
					.equals(obj.getIsdecline())){

				obj.setDecline(params.getParamArray(i)
						.getStringValue());
			}
			if (params.getParamArray(i).getName()
					.equals("_shortContent"))

				obj.setContent(params.getParamArray(i)
						.getStringValue());

			if (params.getParamArray(i).getName()
					.equals("_mark"))

				obj.setMark(Integer.parseInt(params
						.getParamArray(i).getStringValue()));
			if (params.getParamArray(i).getName()
					.equals("_f_gu45_noPortalSign"))

				obj.setNo_sign(params.getParamArray(i)
						.getStringValue());

			if (params.getParamArray(i).getName()
					.equals(" _f_gu23_timeout"))

				obj.setNo_sign(params.getParamArray(i)
						.getStringValue());
			
			if (params.getParamArray(i).getName()
					.equals("_f_VU-36M_O_sendASETD")){
				obj.setNo_sign("true");
				obj.setEtdSecondVU36(true);
			}
			if (params.getParamArray(i).getName()
					.equals("_f_VU-36M_O_result1354message")){
				obj.setNo_sign("true");
				obj.setMess1354(params.getParamArray(i).getStringValue());
			}
			if (params.getParamArray(i).getName()
					.equals("_f_VU-36M_O_result4624message")){
				obj.setNo_sign("true");
				obj.setMess4624(params.getParamArray(i).getStringValue());
			}
			if (params.getParamArray(i).getName()
					.equals("_f_VU-36M_O_etdid")){
				obj.setVu36_etdid(Long.parseLong(params.getParamArray(i).getStringValue()));
			}
		}
		
		
		
		return obj;
	}
	
	
}
