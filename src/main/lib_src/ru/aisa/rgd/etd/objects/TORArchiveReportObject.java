package ru.aisa.rgd.etd.objects;

import java.io.Serializable;
import java.util.Date;

public class TORArchiveReportObject implements Serializable {

	private long id_doc;
	private String name_di;
	private String name_pred;
	private String id_pack;
	private String vagon;
	private String id_act;
	private Date repDate;
	private Date signDate;
	private String serviceName;
	private double cost;
	private double summ;
	
	public long getId_doc() {
		return id_doc;
	}
	public void setId_doc(long id_doc) {
		this.id_doc = id_doc;
	}
	public String getName_di() {
		return name_di;
	}
	public void setName_di(String name_di) {
		this.name_di = name_di;
	}
	public String getName_pred() {
		return name_pred;
	}
	public void setName_pred(String name_pred) {
		this.name_pred = name_pred;
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public double getCost() {
		return cost;
	}
	public void setCost(double cost) {
		this.cost = cost;
	}
	public double getSumm() {
		return summ;
	}
	public void setSumm(double summ) {
		this.summ = summ;
	}
	public Date getRepDate() {
		return repDate;
	}
	public void setRepDate(Date repDate) {
		this.repDate = repDate;
	}
	public Date getSignDate() {
		return signDate;
	}
	public void setSignDate(Date signDate) {
		this.signDate = signDate;
	}
	public String getId_pack() {
		return id_pack;
	}
	public void setId_pack(String id_pack) {
		this.id_pack = id_pack;
	}
	public String getVagon() {
		return vagon;
	}
	public void setVagon(String vagon) {
		this.vagon = vagon;
	}
	public String getId_act() {
		return id_act;
	}
	public void setId_act(String id_act) {
		this.id_act = id_act;
	}
	
	
	
	
}
