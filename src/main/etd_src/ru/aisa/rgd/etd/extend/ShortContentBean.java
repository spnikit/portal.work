package ru.aisa.rgd.etd.extend;

import java.util.List;

public class ShortContentBean {
	List<ShortContentDescriptor> descriptions;

	public ShortContentBean() {
		super();
	}

	public ShortContentBean(List<ShortContentDescriptor> descriptions) {
		super();
		this.descriptions = descriptions;
	}

	public List<ShortContentDescriptor> getDescriptions() {
		return descriptions;
	}

	public void setDescriptions(List<ShortContentDescriptor> descriptions) {
		this.descriptions = descriptions;
	}

}
