package ru.aisa.rgd.ws.dao;

import ru.aisa.rgd.ws.domain.Template;

public interface TemplateDao {
	
	/**
	 * @param name Название шаблона
	 * @return Объект шаблона
	 */
	Template getByName(String name);

}
