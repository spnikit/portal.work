package sheff.rgd.ws.Controllers.PPS;

import java.util.List;


public class VU20a {
			
			private List<Long> idList1;
			private List<Long> idList2;
			private String okpo_perf;
			private String perf;
			private String nameProdTo;
			private Integer priznak;
			private List<RowTable1> list;
			private String customer;
			private String okpo_customer;
			
			
			public Integer getPriznak() {
				return priznak;
			}
			
			
			public void setPriznak(Integer priznak) {
				this.priznak = priznak;
			}
			
			
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



			public List<RowTable1> getList() {
				return list;
			}



			public void setList(List<RowTable1> list) {
				this.list = list;
			}


			public List<Long> getIdList1() {
				return idList1;
			}


			public void setIdList1(List<Long> idList1) {
				this.idList1 = idList1;
			}


			public List<Long> getIdList2() {
				return idList2;
			}


			public void setIdList2(List<Long> idList2) {
				this.idList2 = idList2;
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


}
