package sheff.rjd.mappers;

public class SqlSync {
    public static final String sqlnew[] = {
        //0   -    EGRPO
        "MERGE INTO SNT.EGRPO P USING(VALUES(#OKPO_KOD, '#NAME', '#INN', '#ADR', '#TEL', '#EMAIL', '#FAX', '#KPP')) " +
        "AS " +
            "NEW(OKPO_KOD,NAME,INN, ADR, TEL,EMAIL, FAX, KPP) ON P.OKPO_KOD = NEW.OKPO_KOD " +
        "WHEN MATCHED THEN " +
            "UPDATE " +
            "SET " +
             "P.NAME = NEW.NAME, " +
             "P.INN = NEW.INN, " +
             "P.ADR = NEW.ADR, " +
             "P.TEL = NEW.TEL, " +
             "P.EMAIL = NEW.EMAIL, " +
             "P.FAX = NEW.FAX, " +
             "P.KPP = NEW.KPP " +
        "WHEN NOT MATCHED THEN " +
            "INSERT " +
                "( " +
                    "OKPO_KOD, " +
                    "NAME, " +
                    "INN, " +
                    "ADR, " +
                    "TEL, " +
                    "EMAIL, " +
                    "FAX, " +
                    "KPP "+
                ") " +
                "VALUES " +
                "( " +
                    "NEW.OKPO_KOD, " +
                    "NEW.NAME, " +
                    "NEW.INN, " +
                    "NEW.ADR, " +
                    "NEW.TEL, " +
                    "NEW.EMAIL, " +
                    "NEW.FAX, " +
                    "NEW.KPP" +
                ")"
    	};
    }
