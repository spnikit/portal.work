package ru.aisa.etdadmin;

public class SqlForCnsi {
	
	public static final String sqlnew[] = {
		//admjd  0
		" MERGE INTO LETD.AdmJD P USING(VALUES(:ID, ':COUNTY', ':NAME',':SNAMER', ':SNAMEA')) "+
		 " AS NEW(ID,COUNTY,NAME,SNAMER,SNAMEA) "+
		 " ON P.ID = NEW.ID "+
		 " WHEN MATCHED THEN UPDATE SET P.COUNTY = NEW.COUNTY, P.Name = NEW.Name, P.sNameR = NEW.sNameR, P.sNameA = NEW.sNameA "+
		 " WHEN NOT MATCHED THEN "+
		 " INSERT (ID,COUNTY,NAME,SNAMER,SNAMEA) "+
		 " VALUES (NEW.ID,NEW.COUNTY,NEW.NAME,NEW.SNAMER,NEW.SNAMEA) "
//		 " NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		
		,
		//dor   1
		"MERGE INTO LETD.DOR P USING(VALUES(:DOR_KOD, :ADM_KOD, ':NAME',':SNAME')) "+
		"AS NEW(ID,ADMID,NAME,SNAME) "+
		"ON P.ID = NEW.ID "+
		"WHEN MATCHED THEN UPDATE SET P.ADMID = NEW.ADMID, P.Name = NEW.Name, P.sName = NEW.sName "+
		"WHEN NOT MATCHED THEN "+
		"INSERT (ID,ADMID,NAME,SNAME) "+
		"VALUES (NEW.ID,NEW.ADMID,NEW.NAME,NEW.SNAME) "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		
		,
		
		//stan  2
		"MERGE INTO LETD.PRED P USING(VALUES(:Stan_ID, 0, :Dor_Kod, :St_Kod, ':VName', ':Name', ' ','Y',CAST(NULL AS INT), 0)) "+
		"AS NEW(ID, Type, DorID, Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt) "+
		"ON P.ID = NEW.ID "+
		"WHEN MATCHED THEN UPDATE SET P.DorID=NEW.DorID, P.Kod = NEW.Kod, P.VName = NEW.VName, P.Name = NEW.Name "+
		"WHEN NOT MATCHED THEN "+
		"INSERT (ID, Type,DorID,Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt) "+
		"VALUES (NEW.ID, NEW.Type,NEW.DorID,NEW.Kod, NEW.VName,NEW.Name,NEW.URL,NEW.IsSelf,NEW.StPrim,NEW.ZoneCnt) "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		
		,
		//pred  3
	/*	"MERGE INTO LETD.PRED P USING(VALUES(:Pred_ID,2, :Dor_Kod, :Otr_Kod, ':Name', ':sName', ' ',':self', :stprim, 0, ':activ')) "+
		"AS NEW(ID, Type,DorID,Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ) "+ 
		"ON P.ID = NEW.ID  "+
		"WHEN MATCHED THEN UPDATE SET P.Kod = NEW.Kod, P.VName = NEW.VName, P.Name = NEW.Name, P.Activ = NEW.Activ "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (ID, Type,DorID,Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ)  "+
		"VALUES (NEW.ID, NEW.Type,NEW.DorID,NEW.Kod, NEW.VName,NEW.Name,NEW.URL,NEW.IsSelf,NEW.StPrim,NEW.ZoneCnt,NEW.Activ)  "+
		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "  */
		
		"MERGE INTO LETD.PRED P USING(VALUES(:Pred_ID,2, :Dor_Kod, :Otr_Kod, ':Name', ':sName', ' ',':self', :stprim, 0, ':activ', ':s2Name', :ZAV_TIP_ID )) "+
		"AS NEW(ID, Type, DorID, Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ,Oname,ZAV_TIP_ID) "+ 
		"ON P.ID = NEW.ID  "+
		"WHEN MATCHED THEN UPDATE SET P.DorID = NEW.DorID, P.Kod = NEW.Kod, P.VName = NEW.VName, P.Name = NEW.Name, P.Activ = NEW.Activ, P.Oname = NEW.Oname, P.ZAV_TIP_ID = NEW.ZAV_TIP_ID, P.StPrim=NEW.StPrim "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (ID, Type,DorID,Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ, Oname, ZAV_TIP_ID)  "+
		"VALUES (NEW.ID, NEW.Type,NEW.DorID,NEW.Kod, NEW.VName,NEW.Name,NEW.URL,NEW.IsSelf,NEW.StPrim,NEW.ZoneCnt,NEW.Activ, NEW.Oname, NEW.ZAV_TIP_ID)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
				
			
		,
		//UP   4
		"MERGE INTO LETD.UP P USING(VALUES(:up_id , :Mag_Kod, :up_kod, ':Name', :Stan1_ID, :Stan2_ID)) "+
		"AS NEW(ID,MagCode,Code,Name,StID1,StID2)  "+
		"ON P.ID = NEW.ID  "+
		"WHEN MATCHED THEN UPDATE SET P.MagCode = NEW.MagCode, P.Code = NEW.Code, P.Name = NEW.Name, P.StID1 = NEW.StID1  "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (ID,MagCode,Code,Name,StID1,StID2) "+
		"VALUES (NEW.ID,NEW.MagCode,NEW.Code,NEW.Name,NEW.StID1,NEW.StID2) "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		
		,
		
		
		//gidro  5
		"MERGE INTO LETD.GIDRO P USING(VALUES(:object_id ,:class_id, :object_kod, ':object_name')) "+
		"AS NEW(ID,ObjClass,kod,Name)  "+
		"ON P.ID = NEW.ID  "+
		"WHEN MATCHED THEN UPDATE SET P.ObjClass = NEW.ObjClass, P.kod = NEW.kod, P.Name = NEW.Name "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (ID,ObjClass,kod,Name) "+
		"VALUES (NEW.ID,NEW.ObjClass,NEW.kod,NEW.Name) "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		//okato 6
		"MERGE INTO LETD.OKATO P USING(VALUES(:okato_ID ,:stran_kod, :okato_kod, ':Name')) "+
		"AS NEW(okato_ID,stran_kod,okato_kod,Name)  "+ 
		"ON P.okato_ID = NEW.okato_ID   "+
		"WHEN MATCHED THEN UPDATE SET P.stran_kod = NEW.stran_kod, P.okato_kod = NEW.okato_kod, P.Name = NEW.Name  "+
		"WHEN NOT MATCHED THEN   "+
		"INSERT (okato_ID,stran_kod,okato_kod,Name)  "+
		"VALUES (NEW.okato_ID,NEW.stran_kod,NEW.okato_kod,NEW.Name)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION  "
		,
		
		//pred type = 1   7
		"MERGE INTO LETD.PRED P USING(VALUES(:VAG_UKP_ID,1, :Dor_Kod, :Ukp_Kod, ':Name', ':sName', ' ','Y', :stan_id, 0, ':activ')) "+
		"AS NEW(ID, Type, DorID, Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ) "+ 
		"ON P.ID = NEW.ID  "+
		"WHEN MATCHED THEN UPDATE SET P.DorID = NEW.DorID, P.Kod = NEW.Kod, P.VName = NEW.VName, P.Name = NEW.Name, P.Activ = NEW.Activ, P.StPrim = NEW.StPrim "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (ID, Type,DorID,Kod, VName,Name,URL,IsSelf,StPrim,ZoneCnt,Activ)  "+
		"VALUES (NEW.ID, NEW.Type,NEW.DorID,NEW.Kod, NEW.VName,NEW.Name,NEW.URL,NEW.IsSelf,NEW.StPrim,NEW.ZoneCnt,NEW.Activ)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
		// viddej	 8	
		"MERGE INTO LETD.VIDDEJ P USING(VALUES(:VD_ID,:VD_KOD, :PVD_KOD, ':Name', ':sName')) "+
		"AS NEW(VD_ID, VD_KOD,PVD_KOD, Name,sName) "+ 
		"ON P.VD_ID = NEW.VD_ID  "+
		"WHEN MATCHED THEN UPDATE SET P.VD_KOD = NEW.VD_KOD, P.PVD_KOD = NEW.PVD_KOD, P.Name = NEW.Name, P.sName = NEW.sName "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (VD_ID, VD_KOD,PVD_KOD, Name,sName)  "+
		"VALUES (NEW.VD_ID, NEW.VD_KOD,NEW.PVD_KOD, NEW.Name,NEW.sName)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,
		
//		 DISP_UCH	 9	
		"MERGE INTO LETD.DISP_UCH P USING(VALUES(:DISP_UCH_ID,':Name', ':vName', :DOR_KOD)) "+
		"AS NEW(DISP_UCH_ID,Name,vName,DOR_KOD) "+ 
		"ON P.DISP_UCH_ID = NEW.DISP_UCH_ID  "+
		"WHEN MATCHED THEN UPDATE SET P.Name = NEW.Name, P.vName = NEW.vName, P.DOR_KOD = NEW.DOR_KOD "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (DISP_UCH_ID, Name,vName, DOR_KOD)  "+
		"VALUES (NEW.DISP_UCH_ID,NEW.Name,NEW.vName, NEW.DOR_KOD)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,	
			
//		 DISP_UCH_MARSH	 10	
		"MERGE INTO LETD.DISP_UCH_MARSH P USING(VALUES(:DISP_UCH_ID,:MARSH_NOM, :STAN_NOM, :STAN_ID,':NODE_TYPE' )) "+
		"AS NEW(DISP_UCH_ID,MARSH_NOM,STAN_NOM,STAN_ID,NODE_TYPE ) "+ 
		"ON P.DISP_UCH_ID = NEW.DISP_UCH_ID AND P.MARSH_NOM = NEW.MARSH_NOM AND P.STAN_NOM = NEW.STAN_NOM "+ 
		"WHEN MATCHED THEN UPDATE SET P.STAN_ID = NEW.STAN_ID, P.NODE_TYPE = NEW.NODE_TYPE "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (DISP_UCH_ID,MARSH_NOM,STAN_NOM,STAN_ID,NODE_TYPE)  "+
		"VALUES (NEW.DISP_UCH_ID,NEW.MARSH_NOM,NEW.STAN_NOM,NEW.STAN_ID,NEW.NODE_TYPE)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "		
		,
		
//		 GRUZ	 11	
		"MERGE INTO LETD.GRUZ P USING(VALUES(:GRUZ_ETSNG2_ID,':KOD',':vName', ':sName', ':PR_ALF',:iKOD_INT, :TAR_CLASS)) "+
		"AS NEW(GRUZ_ETSNG2_ID,KOD,vName,sName,PR_ALF,KOD_INT,TAR_CLASS) "+ 
		"ON P.GRUZ_ETSNG2_ID = NEW.GRUZ_ETSNG2_ID  "+
		"WHEN MATCHED THEN UPDATE SET  P.KOD=NEW.KOD, P.vName=NEW.vName, P.sName=NEW.sName, P.PR_ALF=NEW.PR_ALF, P.KOD_INT=NEW.KOD_INT, P.TAR_CLASS=NEW.TAR_CLASS "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (GRUZ_ETSNG2_ID,KOD,vName,sName,PR_ALF,KOD_INT,TAR_CLASS)  "+
		"VALUES (NEW.GRUZ_ETSNG2_ID,NEW.KOD,NEW.vName,NEW.sName,NEW.PR_ALF,NEW.KOD_INT,NEW.TAR_CLASS)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,
		
//		PRED_REM 12                                                   
		"MERGE INTO LETD.PRED_REM P USING(VALUES(:PRED_ID,:UKP_KOD, ':activ')) "+
		"AS NEW(PRED_ID,UKP_KOD,activ) "+ 
		"ON P.PRED_ID = NEW.PRED_ID AND P.UKP_KOD = NEW.UKP_KOD "+
		"WHEN MATCHED THEN UPDATE SET  P.activ=NEW.activ "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (PRED_ID,UKP_KOD,activ)  "+
		"VALUES (NEW.PRED_ID,NEW.UKP_KOD,NEW.activ)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,
		
		//VERTSV 13                                                                              null         
		"MERGE INTO LETD.VERTSV P USING(VALUES(:V_ID, :N_ID, :VERT_SV_ID, :Vid_Podch_Priz, :VERTSV_VID_ID, ':activ' )) "+
		"AS NEW(V_ID,N_ID,VERT_SV_ID,Vid_Podch_Priz,VERTSV_VID_ID,activ) "+ 
		"ON P.V_ID = NEW.V_ID AND P.N_ID = NEW.N_ID AND P.VERT_SV_ID = NEW.VERT_SV_ID "+
		"WHEN MATCHED THEN UPDATE SET  P.Vid_Podch_Priz = NEW.Vid_Podch_Priz, P.VERTSV_VID_ID = NEW.VERTSV_VID_ID, P.activ = NEW.activ "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (V_ID,N_ID,VERT_SV_ID,Vid_Podch_Priz,VERTSV_VID_ID,activ)  "+
		"VALUES (NEW.V_ID,NEW.N_ID,NEW.VERT_SV_ID,NEW.Vid_Podch_Priz,NEW.VERTSV_VID_ID,NEW.activ)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		OBJ_OSN_INF 14                                                                                      
		"MERGE INTO LETD.OBJ_OSN_INF P USING(VALUES(:OBJ_OSN_ID, :V_OBJ_ID, :OKATO_ID, ':DOP_RAZM', ':VNAME', ':NAME', :OBJ_OSN_N, :OBJ_OSN_K, ':ACTIV' )) "+
		"AS NEW(OBJ_OSN_ID, V_OBJ_ID, OKATO_ID, DOP_RAZM, VNAME, NAME, OBJ_OSN_N, OBJ_OSN_K, ACTIV ) "+ 
		"ON P.OBJ_OSN_ID = NEW.OBJ_OSN_ID "+
		"WHEN MATCHED THEN UPDATE SET P.V_OBJ_ID=NEW.V_OBJ_ID, P.OKATO_ID=NEW.OKATO_ID, P.DOP_RAZM=NEW.DOP_RAZM, P.VNAME=NEW.VNAME, P.NAME=NEW.NAME, P.OBJ_OSN_N=NEW.OBJ_OSN_N, P.OBJ_OSN_K=NEW.OBJ_OSN_K, P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (OBJ_OSN_ID, V_OBJ_ID, OKATO_ID, DOP_RAZM, VNAME, NAME, OBJ_OSN_N, OBJ_OSN_K, ACTIV)  "+
		"VALUES (NEW.OBJ_OSN_ID, NEW.V_OBJ_ID, NEW.OKATO_ID, NEW.DOP_RAZM, NEW.VNAME, NEW.NAME, NEW.OBJ_OSN_N, NEW.OBJ_OSN_K, NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
,
		
//		PRED 15                                                                                      
		"MERGE INTO LETD.PRED_CNSI P USING(VALUES(:PRED_ID, :DOR_KOD, :GR_ID, :VD_ID, :OTR_KOD, :MPS_PRIZ, :STRAN_KOD, :OKATO_ID, :OKPO_KOD, :OKONH_KOD, ':VNAME', ':NAME', ':SNAME', ':S2NAME', :KR, ':MESTO', :NOM, :STAN_M_ID, :ZAV_TIP_ID, ':ACTIV' )) "+
		"AS NEW(PRED_ID,DOR_KOD,GR_ID,VD_ID,OTR_KOD,MPS_PRIZ,STRAN_KOD,OKATO_ID,OKPO_KOD,OKONH_KOD,VNAME,NAME,SNAME,SNAME2,KR,MESTO,NOM,STAN_M_ID,ZAV_TIP_ID,ACTIV) "+ 
		"ON P.PRED_ID = NEW.PRED_ID "+
		"WHEN MATCHED THEN UPDATE SET P.DOR_KOD=NEW.DOR_KOD,P.GR_ID=NEW.GR_ID,P.VD_ID=NEW.VD_ID,P.OTR_KOD=NEW.OTR_KOD,P.MPS_PRIZ=NEW.MPS_PRIZ,P.STRAN_KOD=NEW.STRAN_KOD,P.OKATO_ID=NEW.OKATO_ID,P.OKPO_KOD=NEW.OKPO_KOD,P.OKONH_KOD=NEW.OKONH_KOD,P.VNAME=NEW.VNAME,P.NAME=NEW.NAME,P.SNAME=NEW.SNAME,P.SNAME2=NEW.SNAME2,P.KR=NEW.KR,P.MESTO=NEW.MESTO,P.NOM=NEW.NOM,P.STAN_M_ID=NEW.STAN_M_ID,P.ZAV_TIP_ID=NEW.ZAV_TIP_ID,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (PRED_ID,DOR_KOD,GR_ID,VD_ID,OTR_KOD,MPS_PRIZ,STRAN_KOD,OKATO_ID,OKPO_KOD,OKONH_KOD,VNAME,NAME,SNAME,SNAME2,KR,MESTO,NOM,STAN_M_ID,ZAV_TIP_ID,ACTIV)  "+
		"VALUES (NEW.PRED_ID,NEW.DOR_KOD,NEW.GR_ID,NEW.VD_ID,NEW.OTR_KOD,NEW.MPS_PRIZ,NEW.STRAN_KOD,NEW.OKATO_ID,NEW.OKPO_KOD,NEW.OKONH_KOD,NEW.VNAME,NEW.NAME,NEW.SNAME,NEW.SNAME2,NEW.KR,NEW.MESTO,NEW.NOM,NEW.STAN_M_ID,NEW.ZAV_TIP_ID,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,
		
//		STAN 16                                                                                      
		"MERGE INTO LETD.STAN P USING(VALUES(:STAN_ID, :DOR_KOD, :PRED_ID, :OKATO_ID, :ST_KOD, ':VNAME', ':NAME', :STAN_TIP_ID, ':MNEM', ':ACTIV' )) "+
		"AS NEW(STAN_ID,DOR_KOD,PRED_ID,OKATO_ID,ST_KOD,VNAME,NAME,STAN_TIP_ID,MNEM,ACTIV) "+ 
		"ON P.STAN_ID = NEW.STAN_ID "+
		"WHEN MATCHED THEN UPDATE SET P.DOR_KOD=NEW.DOR_KOD,P.PRED_ID=NEW.PRED_ID,P.OKATO_ID=NEW.OKATO_ID,P.ST_KOD=NEW.ST_KOD,P.VNAME=NEW.VNAME,P.NAME=NEW.NAME,P.STAN_TIP_ID=NEW.STAN_TIP_ID,P.MNEM=NEW.MNEM,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (STAN_ID,DOR_KOD,PRED_ID,OKATO_ID,ST_KOD,VNAME,NAME,STAN_TIP_ID,MNEM,ACTIV)  "+
		"VALUES (NEW.STAN_ID,NEW.DOR_KOD,NEW.PRED_ID,NEW.OKATO_ID,NEW.ST_KOD,NEW.VNAME,NEW.NAME,NEW.STAN_TIP_ID,NEW.MNEM,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "	
		,
		
//		OBJ_DIS 17                                                                                      
		"MERGE INTO LETD.OBJ_DIS P USING(VALUES(:PRED_ID, :OBJ_OSN_ID, ':PRIM', ':ACTIV' )) "+
		"AS NEW(PRED_ID,OBJ_OSN_ID,PRIM,ACTIV) "+ 
		"ON P.PRED_ID = NEW.PRED_ID AND P.OBJ_OSN_ID = NEW.OBJ_OSN_ID "+
		"WHEN MATCHED THEN UPDATE SET P.PRIM=NEW.PRIM, P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (PRED_ID,OBJ_OSN_ID,PRIM,ACTIV)  "+
		"VALUES (NEW.PRED_ID,NEW.OBJ_OSN_ID,NEW.PRIM,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		DIC_CLASS 18                                                                                      
		"MERGE INTO LETD.DIC_CLASS P USING(VALUES(:CLASS_ID, ':CLASS_NAME', ':ACTIV' )) "+
		"AS NEW(CLASS_ID,CLASS_NAME,ACTIV) "+ 
		"ON P.CLASS_ID = NEW.CLASS_ID "+
		"WHEN MATCHED THEN UPDATE SET P.CLASS_NAME=NEW.CLASS_NAME, P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (CLASS_ID,CLASS_NAME,ACTIV)  "+
		"VALUES (NEW.CLASS_ID,NEW.CLASS_NAME,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		DIC_OBJECTS 19                                                                                      
		"MERGE INTO LETD.DIC_OBJECTS P USING(VALUES(:OBJECT_ID, :CLASS_ID, :OBJECT_KOD, ':OBJECT__KODSTR', ':OBJECT_VNAME', ':OBJECT_NAME', ':OBJECT_SNAME', :REFER, ':S1', :I1, ':ACTIV' )) "+
		"AS NEW(OBJECT_ID,CLASS_ID,OBJECT_KOD,OBJECT_KODSTR,OBJECT_VNAME,OBJECT_NAME,OBJECT_SNAME,REFER,S1,I1,ACTIV) "+ 
		"ON P.OBJECT_ID = NEW.OBJECT_ID "+
		"WHEN MATCHED THEN UPDATE SET P.CLASS_ID=NEW.CLASS_ID,P.OBJECT_KOD=NEW.OBJECT_KOD,P.OBJECT_KODSTR=NEW.OBJECT_KODSTR,P.OBJECT_VNAME=NEW.OBJECT_VNAME,P.OBJECT_NAME=NEW.OBJECT_NAME,P.OBJECT_SNAME=NEW.OBJECT_SNAME,P.REFER=NEW.REFER,P.S1=NEW.S1,P.I1=NEW.I1,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (OBJECT_ID,CLASS_ID,OBJECT_KOD,OBJECT_KODSTR,OBJECT_VNAME,OBJECT_NAME,OBJECT_SNAME,REFER,S1,I1,ACTIV)  "+
		"VALUES (NEW.OBJECT_ID,NEW.CLASS_ID,NEW.OBJECT_KOD,NEW.OBJECT_KODSTR,NEW.OBJECT_VNAME,NEW.OBJECT_NAME,NEW.OBJECT_SNAME,NEW.REFER,NEW.S1,NEW.I1,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		VAG_REM_VU22 20                                                                                      
		"MERGE INTO LETD.VAG_REM_VU22 P USING(VALUES(:Vag_Rem_VU22_Id, :Vag_Rem_Kod, ':VNAME', ':NAME', ':Presk_#_new', ':Presk_#_old', :Uzl_ID, :TR1, :TR2, :DR, :KR, ':ACTIV' )) "+
		"AS NEW(Vag_Rem_VU22_Id,Vag_Rem_Kod,VNAME,NAME,Presk_#_new,Presk_#_old,Uzl_ID,TR1,TR2,DR,KR,ACTIV) "+ 
		"ON P.Vag_Rem_VU22_Id = NEW.Vag_Rem_VU22_Id "+
		"WHEN MATCHED THEN UPDATE SET P.Vag_Rem_Kod=NEW.Vag_Rem_Kod,P.VNAME=NEW.VNAME,P.NAME=NEW.NAME,P.Presk_#_new=NEW.Presk_#_new,P.Presk_#_old=NEW.Presk_#_old,P.Uzl_ID=NEW.Uzl_ID,P.TR1=NEW.TR1,P.TR2=NEW.TR2,P.DR=NEW.DR,P.KR=NEW.KR,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (Vag_Rem_VU22_Id,Vag_Rem_Kod,VNAME,NAME,Presk_#_new,Presk_#_old,Uzl_ID,TR1,TR2,DR,KR,ACTIV)  "+
		"VALUES (NEW.Vag_Rem_VU22_Id,NEW.Vag_Rem_Kod,NEW.VNAME,NEW.NAME,NEW.Presk_#_new,NEW.Presk_#_old,NEW.Uzl_ID,NEW.TR1,NEW.TR2,NEW.DR,NEW.KR,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		VAG_REM_ROD 21                                                                                      
		"MERGE INTO LETD.VAG_REM_ROD P USING(VALUES(:Vag_Rem_VU22_Id, :Vag_Rod_ID, ':ACTIV' )) "+
		"AS NEW(Vag_Rem_VU22_Id,Vag_Rod_ID,ACTIV) "+ 
		"ON P.Vag_Rem_VU22_Id = NEW.Vag_Rem_VU22_Id AND P.Vag_Rod_ID = NEW.Vag_Rod_ID "+
		"WHEN MATCHED THEN UPDATE SET P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (Vag_Rem_VU22_Id,Vag_Rod_ID,ACTIV)  "+
		"VALUES (NEW.Vag_Rem_VU22_Id,NEW.Vag_Rod_ID,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
		
//		VAG_UKP 22                                                                                      
		"MERGE INTO LETD.VAG_UKP P USING(VALUES(:VAG_UKP_ID, :UKP_KOD,:STRAN_KOD,:DOR_KOD,:STAN_ID,':TP',':NAME',':SNAME',:PRED_ID,':DR',':KR1',':POSTR'," +
		                        "':KRP',':DPS1',':DPS2',':DPS3',':TEK_REM',':OSI',':AMA',':BALKA',':REM_KOL_PARI',':KOL_PARA',':KOLESA',':TORM_PROD',':ACTIV' )) "+
		"AS NEW(VAG_UKP_ID,UKP_KOD,STRAN_KOD,DOR_KOD,STAN_ID,TP,NAME,SNAME,PRED_ID,DR,KR,POSTR," +
		       "KRP,DPS1,DPS2,DPS3,TEK_REM,OSI,AMA,BALKA,REM_KOL_PARI,KOL_PARA,KOLESA,TORM_PROD,ACTIV) "+ 
		"ON P.VAG_UKP_ID = NEW.VAG_UKP_ID "+
		"WHEN MATCHED THEN UPDATE SET P.UKP_KOD=NEW.UKP_KOD,P.STRAN_KOD=NEW.STRAN_KOD,P.DOR_KOD=NEW.DOR_KOD,P.STAN_ID=NEW.STAN_ID,P.TP=NEW.TP,P.NAME=NEW.NAME,P.SNAME=NEW.SNAME,P.PRED_ID=NEW.PRED_ID,P.DR=NEW.DR,P.KR=NEW.KR,P.POSTR=NEW.POSTR," +
		       "P.KRP=NEW.KRP,P.DPS1=NEW.DPS1,P.DPS2=NEW.DPS2,P.DPS3=NEW.DPS3,P.TEK_REM=NEW.TEK_REM,P.OSI=NEW.OSI,P.AMA=NEW.AMA,P.BALKA=NEW.BALKA,P.REM_KOL_PARI=NEW.REM_KOL_PARI,P.KOL_PARA=NEW.KOL_PARA,P.KOLESA=NEW.KOLESA,P.TORM_PROD=NEW.TORM_PROD,P.ACTIV=NEW.ACTIV"+
		" WHEN NOT MATCHED THEN  "+
		"INSERT (VAG_UKP_ID,UKP_KOD,STRAN_KOD,DOR_KOD,STAN_ID,TP,NAME,SNAME,PRED_ID,DR,KR,POSTR," +
		       "KRP,DPS1,DPS2,DPS3,TEK_REM,OSI,AMA,BALKA,REM_KOL_PARI,KOL_PARA,KOLESA,TORM_PROD,ACTIV)  "+
		"VALUES (NEW.VAG_UKP_ID,NEW.UKP_KOD,NEW.STRAN_KOD,NEW.DOR_KOD,NEW.STAN_ID,NEW.TP,NEW.NAME,NEW.SNAME,NEW.PRED_ID,NEW.DR,NEW.KR,NEW.POSTR," +
		       "NEW.KRP,NEW.DPS1,NEW.DPS2,NEW.DPS3,NEW.TEK_REM,NEW.OSI,NEW.AMA,NEW.BALKA,NEW.REM_KOL_PARI,NEW.KOL_PARA,NEW.KOLESA,NEW.TORM_PROD,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		VAG_SOB 23                                                                                      
		"MERGE INTO LETD.VAG_SOB P USING(VALUES(:VAG_SOB_ID, :LOC_KOD,:OKPO_KOD,':SNAME',':REG_NOM',:ADM_KOD,':ACTIV' )) "+
		"AS NEW(VAG_SOB_ID,LOC_KOD,OKPO_KOD,SNAME,REG_NOM,ADM_KOD,ACTIV) "+ 
		"ON P.VAG_SOB_ID = NEW.VAG_SOB_ID "+
		"WHEN MATCHED THEN UPDATE SET P.LOC_KOD=NEW.LOC_KOD,P.OKPO_KOD=NEW.OKPO_KOD,P.SNAME=NEW.SNAME,P.REG_NOM=NEW.REG_NOM,P.ADM_KOD=NEW.ADM_KOD,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (VAG_SOB_ID,LOC_KOD,OKPO_KOD,SNAME,REG_NOM,ADM_KOD,ACTIV)  "+
		"VALUES (NEW.VAG_SOB_ID,NEW.LOC_KOD,NEW.OKPO_KOD,NEW.SNAME,NEW.REG_NOM,NEW.ADM_KOD,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		PEREG_MS 24                                                                                      
		"MERGE INTO LETD.PEREG_MS P USING(VALUES(:PEREG_MS_ID, :UP_ID,:STAN1_ID,:STAN2_ID,:EXPL,:CHET,':ACTIV' )) "+
		"AS NEW(PEREG_MS_ID, UP_ID,STAN1_ID,STAN2_ID,EXPL,CHET,ACTIV) "+ 
		"ON P.PEREG_MS_ID = NEW.PEREG_MS_ID "+
		"WHEN MATCHED THEN UPDATE SET P.PEREG_MS_ID=NEW.PEREG_MS_ID,P.UP_ID=NEW.UP_ID,P.STAN1_ID=NEW.STAN1_ID,P.STAN2_ID=NEW.STAN2_ID,P.EXPL=NEW.EXPL,P.CHET=NEW.CHET,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (PEREG_MS_ID, UP_ID,STAN1_ID,STAN2_ID,EXPL,CHET,ACTIV)  "+
		"VALUES (NEW.PEREG_MS_ID, NEW.UP_ID,NEW.STAN1_ID,NEW.STAN2_ID,NEW.EXPL,NEW.CHET,NEW.ACTIV)  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,

//		VAG_NUM 25                                                                                      
		"MERGE INTO LETD.VAG_NUM P USING(VALUES(:VAG_NUM_ID,:NG3,:VG3,:NG4,:VG4,:NG5,:VG5,:NG6,:VG6,:NG7,:VG7,:KO,:VT,:UD,:DV,:PG,:UTV,:RV,:PRDO,:ACTIV )) "+
		"AS NEW(VAG_NUM_ID,NG3,VG3,NG4,VG4,NG5,VG5,NG6,VG6,NG7,VG7,KO,VT,UD,DV,PG,UTV,RV,PRDO,ACTIV ) "+ 
		"ON P.VAG_NUM_ID = NEW.VAG_NUM_ID "+
		"WHEN MATCHED THEN UPDATE SET  P.NG3=NEW.NG3,P.VG3=NEW.VG3,P.NG4=NEW.NG4,P.VG4=NEW.VG4,P.NG5=NEW.NG5,P.VG5=NEW.VG5,P.NG6=NEW.NG6,P.VG6=NEW.VG6,P.NG7=NEW.NG7,P.VG7=NEW.VG7,P.KO=NEW.KO,P.VT=NEW.VT,P.UD=NEW.UD,P.DV=NEW.DV,P.PG=NEW.PG,P.UTV=NEW.UTV,P.RV=NEW.RV,P.PRDO=NEW.PRDO,P.ACTIV=NEW.ACTIV "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (VAG_NUM_ID,NG3,VG3,NG4,VG4,NG5,VG5,NG6,VG6,NG7,VG7,KO,VT,UD,DV,PG,UTV,RV,PRDO,ACTIV )  "+
		"VALUES (NEW.VAG_NUM_ID,NEW.NG3,NEW.VG3,NEW.NG4,NEW.VG4,NEW.NG5,NEW.VG5,NEW.NG6,NEW.VG6,NEW.NG7,NEW.VG7,NEW.KO,NEW.VT,NEW.UD,NEW.DV,NEW.PG,NEW.UTV,NEW.RV,NEW.PRDO,NEW.ACTIV )  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		STPPRJ 26                                                                                      
		"MERGE INTO LETD.STP_PRJ P USING(VALUES(:Stp_Prj_Id,:Kod,:SKodS,:Id_rel,:Id_MK,:LMM,:V_BOK,:V_PR,:PRIM )) "+
		"AS NEW(Stp_Prj_Id,Kod,SKodS,Id_rel,Id_MK,LMM,V_BOK,V_PR,PRIM) "+ 
		"ON P.Stp_Prj_Id = NEW.Stp_Prj_Id "+
		"WHEN MATCHED THEN UPDATE SET  P.Kod=NEW.Kod,P.SKodS=NEW.SKodS,P.Id_rel=NEW.Id_rel,P.Id_MK=NEW.Id_MK,P.LMM=NEW.LMM,P.V_BOK=NEW.V_BOK,P.V_PR=NEW.V_PR,P.PRIM=NEW.PRIM "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (Stp_Prj_Id,Kod,SKodS,Id_rel,Id_MK,LMM,V_BOK,V_PR,PRIM )  "+
		"VALUES (NEW.Stp_Prj_Id,NEW.Kod,NEW.SKodS,NEW.Id_rel,NEW.Id_MK,NEW.LMM,NEW.V_BOK,NEW.V_PR,NEW.PRIM )  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
		,
		
//		STANPARK 27                                                                                      
		"MERGE INTO LETD.STANPARK P USING(VALUES(:STAN_ID,:STANPARK_ID,:NAME,:SPC_PARK_ID,:SNAME,:ACTIV )) "+
		"AS NEW(STAN_ID,STANPARK_ID,NAME,SPC_PARK_ID,SNAME,ACTIV ) "+ 
		"ON P.STAN_ID = NEW.STAN_ID AND P.STANPARK_ID = NEW.STANPARK_ID "+
		"WHEN MATCHED THEN UPDATE SET P.STAN_ID=NEW.STAN_ID,P.STANPARK_ID=NEW.STANPARK_ID,P.NAME=NEW.NAME,P.SPC_PARK_ID=NEW.SPC_PARK_ID,P.SNAME=NEW.SNAME,P.ACTIV=NEW.ACTIV  "+
		"WHEN NOT MATCHED THEN  "+
		"INSERT (STAN_ID,STANPARK_ID,NAME,SPC_PARK_ID,SNAME,ACTIV )  "+
		"VALUES (NEW.STAN_ID,NEW.STANPARK_ID,NEW.NAME,NEW.SPC_PARK_ID,NEW.SNAME,NEW.ACTIV  )  "
//		"NOT ATOMIC CONTINUE ON SQLEXCEPTION "
			
		};

}
