package sheff.rgd.ws.Controllers.PPS;

public class VU20 {
	
	private Long id;
	private Integer priznak;
	private String okpo_perf;
	private String perf;
	private String nameProdTo;
	private String vagnum;
	private String nameProdFrom;
	private String numOperation;
	private int idClaim ;
	private String customer;
	private String okpo_customer;
	private String numClaim;
	private String dateClaim;

	
	public String getOkpo_perf() {
		return okpo_perf;
	}



	public void setOkpo_perf(String okpo_perf) {
		this.okpo_perf = okpo_perf;
	}



	public String getPerf() {
		return perf;
	}



	public void setPerf(String perf) {
		this.perf = perf;
	}



	public String getNameProdTo() {
		return nameProdTo;
	}



	public void setNameProdTo(String nameProdTo) {
		this.nameProdTo = nameProdTo;
	}
	
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
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getOkpo_customer() {
		return okpo_customer;
	}
	public void setOkpo_customer(String okpo_customer) {
		this.okpo_customer = okpo_customer;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPriznak() {
		return priznak;
	}
	public void setPriznak(Integer priznak) {
		this.priznak = priznak;
	}



	@Override
	public String toString() {
		return "VU20 [okpo_perf=" + okpo_perf + ", perf=" + perf
				+ ", nameProdTo=" + nameProdTo + ", vagnum=" + vagnum
				+ ", nameProdFrom=" + nameProdFrom + ", numOperation="
				+ numOperation + ", idClaim=" + idClaim + ", customer="
				+ customer + ", okpo_customer=" + okpo_customer
				+ ", numClaim=" + numClaim + ", dateClaim=" + dateClaim
				+ "]";
	}

}


