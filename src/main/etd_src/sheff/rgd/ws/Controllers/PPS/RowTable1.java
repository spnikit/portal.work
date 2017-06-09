package sheff.rgd.ws.Controllers.PPS;

public class RowTable1 {
	private String vagnum;
	private String nameProdFrom;
	private String numOperation;
	private int idClaim ;
	private String numClaim;
	private String dateClaim;
	
	public String getVagnum() {
		return vagnum;
	}
	public void setVagnum(String vagnum) {
		this.vagnum = vagnum;
	}
	public String getNameProdFrom() {
		return nameProdFrom;
	}
	public void setNameProdFrom(String nameProdFrom) {
		this.nameProdFrom = nameProdFrom;
	}
	public String getNumOperation() {
		return numOperation;
	}
	public void setNumOperation(String numOperation) {
		this.numOperation = numOperation;
	}
	public int getIdClaim() {
		return idClaim;
	}
	public void setIdClaim(int idClaim) {
		this.idClaim = idClaim;
	}
	public String getNumClaim() {
		return numClaim;
	}
	public void setNumClaim(String numClaim) {
		this.numClaim = numClaim;
	}
	public String getDateClaim() {
		return dateClaim;
	}
	public void setDateClaim(String dateClaim) {
		this.dateClaim = dateClaim;
	}
	
	@Override
	public String toString() {
		return "Row [vagnum=" + vagnum + ", nameProdFrom=" + nameProdFrom
				+ ", numOperation=" + numOperation + ", idClaim=" + idClaim
			    + ", numClaim=" + numClaim + ", dateClaim="
				+ dateClaim + "]";
	}

}
