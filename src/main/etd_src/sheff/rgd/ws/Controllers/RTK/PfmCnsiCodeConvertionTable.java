package sheff.rgd.ws.Controllers.RTK;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 * Таблица соответствия предприятий ЦНСИ и кодов ПФМ
 * 
 * @author user
 * 
 */
public class PfmCnsiCodeConvertionTable
{
	private static Map<String, PfmCNSIData> pfmCnsiCodesMap = new HashMap<String, PfmCNSIData>();
	private static Map<Integer, PfmCNSIData> cnsiPfmCodesMap = new HashMap<Integer, PfmCNSIData>();

	public static Integer getCnsiCode(String pfmCode)
	{
		return pfmCnsiCodesMap.get(pfmCode).cnsiCode;
	}

	public static String getPfmCode(Integer cnsiCode)
	{
		return cnsiPfmCodesMap.get(cnsiCode).pfmCode;
	}

	public static String getPfmShortDesc(String pfmCode)
	{
		return pfmCnsiCodesMap.get(pfmCode).shortDesc;
	}

	public static String getPfmShortDesc(Integer cnsiCode)
	{
		return cnsiPfmCodesMap.get(cnsiCode).shortDesc;
	}

	static
	{
		pfmCnsiCodesMap.put("0705", new PfmCNSIData("0705", 7284, "ЦЭКР", false));
		pfmCnsiCodesMap.put("0718", new PfmCNSIData("0718", 2622, "ЦТЕХ", false));
		pfmCnsiCodesMap.put("0707", new PfmCNSIData("0707", 2597, "ЦФ", false));
		pfmCnsiCodesMap.put("0737", new PfmCNSIData("0737", 8768, "ЦПРОТ", false));
		pfmCnsiCodesMap.put("0735", new PfmCNSIData("0735", 2557, "ЦН", false));
		pfmCnsiCodesMap.put("0730", new PfmCNSIData("0730", 2554, "ЦРИ", false));
		pfmCnsiCodesMap.put("0700", new PfmCNSIData("0700", 7403, "ЦА", false));
		pfmCnsiCodesMap.put("0701", new PfmCNSIData("0701", 2552, "ЦОС (ЦКК)", false));
		pfmCnsiCodesMap.put("0727", new PfmCNSIData("0727", 7381, "ЦРВ", false));
		pfmCnsiCodesMap.put("0733", new PfmCNSIData("0733", 6747, "ЦКИ", false));
		pfmCnsiCodesMap.put("0900", new PfmCNSIData("0900", 13859, "ЦБС", false));
		pfmCnsiCodesMap.put("0725", new PfmCNSIData("0725", 2559, "ЦИ", false));
		pfmCnsiCodesMap.put("0726", new PfmCNSIData("0726", 2436, "ЦБЗ", false));
		pfmCnsiCodesMap.put("0714", new PfmCNSIData("0714", 2391, "ЦЛ", false));
		pfmCnsiCodesMap.put("0731", new PfmCNSIData("0731", 2621, "ЦЮ", false));
		pfmCnsiCodesMap.put("0742", new PfmCNSIData("0742", 7385, "ЦУЭП", false));
		pfmCnsiCodesMap.put("0704", new PfmCNSIData("0704", 7261, "ЦТП", false));
		pfmCnsiCodesMap.put("0741", new PfmCNSIData("0741", 2624, "ЦБТ", false));
		pfmCnsiCodesMap.put("0747", new PfmCNSIData("0747", 8775, "ЦУИС", false));
		pfmCnsiCodesMap.put("0719", new PfmCNSIData("0719", 2629, "ЦРБ", false));
		pfmCnsiCodesMap.put("0721", new PfmCNSIData("0721", 2634, "ЦУКС", false));
		pfmCnsiCodesMap.put("0736", new PfmCNSIData("0736", 8773, "ЦИН", false));
		pfmCnsiCodesMap.put("0728", new PfmCNSIData("0728", 7382, "ЦУДЗ", false));
		pfmCnsiCodesMap.put("0732", new PfmCNSIData("0732", 8769, "ЦЗТ", false));
		pfmCnsiCodesMap.put("0724", new PfmCNSIData("0724", 2610, "ЦУВС", false));
		pfmCnsiCodesMap.put("0723", new PfmCNSIData("0723", 2632, "ЦСР", false));
		pfmCnsiCodesMap.put("0744", new PfmCNSIData("0744", 2619, "ЦО", false));
		pfmCnsiCodesMap.put("0720", new PfmCNSIData("0720", 7074, "ЦИНВ", false));
		pfmCnsiCodesMap.put("0743", new PfmCNSIData("0743", 2409, "ЦР", false));
		pfmCnsiCodesMap.put("0740", new PfmCNSIData("0740", 7384, "ЦУНР", false));
		pfmCnsiCodesMap.put("0722", new PfmCNSIData("0722", 2612, "ЦКАДР", false));
		pfmCnsiCodesMap.put("0754", new PfmCNSIData("0754", 13821, "ЦЭУ", false));
		pfmCnsiCodesMap.put("0709", new PfmCNSIData("0709", 7395, "ЦФК", false));
		pfmCnsiCodesMap.put("0762", new PfmCNSIData("0762", 13834, "ЦУТМ", false));
		pfmCnsiCodesMap.put("0755", new PfmCNSIData("0755", 13832, "ЦРСУ", false));
		pfmCnsiCodesMap.put("0756", new PfmCNSIData("0756", 13833, "ЦТЛБ", false));
		pfmCnsiCodesMap.put("0758", new PfmCNSIData("0758", 13822, "ЦБЗТБ", false));
		pfmCnsiCodesMap.put("0734", new PfmCNSIData("0734", 7399, "ЦУШ", false));
		pfmCnsiCodesMap.put("0761", new PfmCNSIData("0761", 5638, "ЦЧУ", false));
		pfmCnsiCodesMap.put("0746", new PfmCNSIData("0746", 7769, "ЦУО", false));
		pfmCnsiCodesMap.put("0763", new PfmCNSIData("0763", 7261, "ЦРТП", false));
		pfmCnsiCodesMap.put("0777", new PfmCNSIData("0777", 14464, "ЦУБП", false));
		pfmCnsiCodesMap.put("0764", new PfmCNSIData("0764", 14457, "ЦКО", false));
		pfmCnsiCodesMap.put("0766", new PfmCNSIData("0766", 14453, "ЦЖД", false));
		
		pfmCnsiCodesMap.put("0759", new PfmCNSIData("0759", 40000001, "ЦИВД", false));
		pfmCnsiCodesMap.put("0778", new PfmCNSIData("0778", 40000002, "ЦУЗПД", false));
		//ТФС
		pfmCnsiCodesMap.put("0784", new PfmCNSIData("0784", 14484, "Аппарат корпоративного секретаря ОАО «РЖД»", false));
//new
		//ЦФТО
		pfmCnsiCodesMap.put("1030", new PfmCNSIData("1030", 7383, "ЦФТО", true));
		pfmCnsiCodesMap.put("1110", new PfmCNSIData("1110", 2162, "ОКТЯБРЬСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1114", new PfmCNSIData("1114", 7481, "КАЛИНИНГРАДСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1116", new PfmCNSIData("1116", 2553, "МОСКОВСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1117", new PfmCNSIData("1117", 7482, "ГОРЬКОВСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1124", new PfmCNSIData("1124", 6982, "СЕВЕРНЫЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1128", new PfmCNSIData("1128", 3552, "СЕВЕРО-КАВКАЗСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1130", new PfmCNSIData("1130", 1274, "ЮГО-ВОСТОЧНЫЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1138", new PfmCNSIData("1138", 6928, "ПРИВОЛЖСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1166", new PfmCNSIData("1166", 7483, "КУЙБЫШЕВСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1313", new PfmCNSIData("1313", 6929, "СВЕРДЛОВСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1314", new PfmCNSIData("1314", 7484, "ЮЖНО-УРАЛЬСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1315", new PfmCNSIData("1315", 3028, "ЗАПАДНО-СИБИРСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1316", new PfmCNSIData("1316", 634, "КРАСНОЯРСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1317", new PfmCNSIData("1317", 8484, "ВОСТОЧНО-СИБИРСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1318", new PfmCNSIData("1318", 8485, "ЗАБАЙКАЛЬСКИЙ ТЦФТО", true));
		pfmCnsiCodesMap.put("1319", new PfmCNSIData("1319", 8486, "ДАЛЬНЕВОСТОЧНЫЙ ТЦФТО", true));
		//ЦД
		pfmCnsiCodesMap.put("2360", new PfmCNSIData("2360", 2643, "ЦД", true));
		pfmCnsiCodesMap.put("2364", new PfmCNSIData("2364", 9530, "ДАЛЬНЕВОСТОЧНАЯ ДУД", true));
		pfmCnsiCodesMap.put("2365", new PfmCNSIData("2365", 9532, "ЗАБАЙКАЛЬСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2366", new PfmCNSIData("2366", 9474, "ЗАПАДНО-СИБИРСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2367", new PfmCNSIData("2367", 9467, "ЮЖНО-УРАЛЬСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2368", new PfmCNSIData("2368", 9494, "СВЕРДЛОВСКАЯДУД", true));
		pfmCnsiCodesMap.put("2369", new PfmCNSIData("2369", 9461, "КУЙБЫШЕВСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2370", new PfmCNSIData("2370", 8982, "ОКТЯБРЬСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2371", new PfmCNSIData("2371", 9426, "КРАСНОЯРСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2372", new PfmCNSIData("2372", 9427, "ВОСТОЧНО-СИБИРСКАЯДУД", true));
		pfmCnsiCodesMap.put("2373", new PfmCNSIData("2373", 9533, "КАЛИНИНГРАДСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2374", new PfmCNSIData("2374", 9507, "МОСКОВСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2375", new PfmCNSIData("2375", 9455, "ГОРЬКОВСКАЯДУД", true));
		pfmCnsiCodesMap.put("2376", new PfmCNSIData("2376", 9468, "СЕВЕРНАЯ ДУД", true));
		pfmCnsiCodesMap.put("2377", new PfmCNSIData("2377", 9481, "СЕВЕРО-КАВКАЗСКАЯ ДУД", true));
		pfmCnsiCodesMap.put("2378", new PfmCNSIData("2378", 9482, "ЮГО-ВОСТОЧНАЯ ДУД", true));
		pfmCnsiCodesMap.put("2379", new PfmCNSIData("2379", 9462, "ПРИВОЛЖСКАЯДУД", true));
		//ЦМ
		pfmCnsiCodesMap.put("2046", new PfmCNSIData("2046", 13015, "ЦМ", true));
		pfmCnsiCodesMap.put("2048", new PfmCNSIData("2048", 7033, "ОКТЯБРЬСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2049", new PfmCNSIData("2049", 7246, "МОСКОВСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2050", new PfmCNSIData("2050", 7343, "ГОРЬКОВСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2051", new PfmCNSIData("2051", 7262, "СЕВЕРНАЯ ДИРЕКЦИЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2052", new PfmCNSIData("2052", 7220, "СЕВЕРО-КАВКАЗСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2053", new PfmCNSIData("2053", 7105, "ЮГО-ВОСТОЧНАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2054", new PfmCNSIData("2054", 7191, "ПРИВОЛЖСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2055", new PfmCNSIData("2055", 7174, "КУЙБЫШЕВСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2056", new PfmCNSIData("2056", 7162, "СВЕРДЛОВСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2057", new PfmCNSIData("2057", 7267, "ЮЖНО-УРАЛЬСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2058", new PfmCNSIData("2058", 7139, "ЗАП-СИБ ЦТСК", true));
		pfmCnsiCodesMap.put("2059", new PfmCNSIData("2059", 7130, "КРАСНОЯРСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2060", new PfmCNSIData("2060", 7269, "ВОСТОЧНО-СИБИРСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2061", new PfmCNSIData("2061", 7102, "ЗАБАЙКАЛЬСКАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2062", new PfmCNSIData("2062", 7066, "ДАЛЬНЕВОСТОЧНАЯ ЦТСК", true));
		pfmCnsiCodesMap.put("2064", new PfmCNSIData("2064", 7046, "КАЛИНИНГРАДСКАЯ ЦТСК", true));
		//ЦДРП 
		pfmCnsiCodesMap.put("2239", new PfmCNSIData("2239", 8609, "ЦДРП", true));
		pfmCnsiCodesMap.put("1916", new PfmCNSIData("1916", 2150, "ОКТЯБРЬСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("2780", new PfmCNSIData("2780", 6363, "МОСКОВСКАЯ ДРП ", true));
		pfmCnsiCodesMap.put("3200", new PfmCNSIData("3200", 6431, "ГОРЬКОВСКАЯ ДРП ", true));
		pfmCnsiCodesMap.put("3713", new PfmCNSIData("3713", 6422, "СЕВЕРНАЯ ДРП", true));
		pfmCnsiCodesMap.put("4361", new PfmCNSIData("4361", 6567, "СЕВЕРО-КАВКАЗСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("4874", new PfmCNSIData("4874", 6421, "ЮГО-ВОСТОЧНАЯ ДРП", true));
		pfmCnsiCodesMap.put("5322", new PfmCNSIData("5322", 6440, "ПРИВОЛЖСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("5778", new PfmCNSIData("5778", 6507, "КУЙБЫШЕВСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("6300", new PfmCNSIData("6300", 6805, "СВЕРДЛОВСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("6847", new PfmCNSIData("6847", 6423, "ЮЖНО-УРАЛЬСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("7271", new PfmCNSIData("7271", 6441, "ЗАПАДНО-СИБИРСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("7764", new PfmCNSIData("7764", 6506, "КРАСНОЯРСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("8244", new PfmCNSIData("8244", 6449, "ВОСТОЧНО-СИБИРСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("8816", new PfmCNSIData("8816", 6523, "ЗАБАЙКАЛЬСКАЯ ДРП", true));
		pfmCnsiCodesMap.put("9323", new PfmCNSIData("9323", 6438, "ДАЛЬНЕВОСТОЧНАЯ ДРП", true));
		//ЦДТВ
		pfmCnsiCodesMap.put("2071", new PfmCNSIData("2071", 13022, "ЦДТВ", true));
		pfmCnsiCodesMap.put("2072", new PfmCNSIData("2072", 8981, "ОКТЯБРЬСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2073", new PfmCNSIData("2073", 5668, "МОСКОВСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2074", new PfmCNSIData("2074", 7645, "ГОРЬКОВСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2075", new PfmCNSIData("2075", 7791, "СЕВЕРНАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2076", new PfmCNSIData("2076", 9910, "СЕВЕРО-КАВКАЗСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2077", new PfmCNSIData("2077", 5712, "ЮГО-ВОСТОЧНАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2078", new PfmCNSIData("2078", 8787, "ПРИВОЛЖСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2079", new PfmCNSIData("2079", 7994, "КУЙБЫШЕВСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2080", new PfmCNSIData("2080", 8153, "СВЕРДЛОВСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2081", new PfmCNSIData("2081", 8088, "ЮЖНО-УРАЛЬСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2082", new PfmCNSIData("2082", 5714, "ЗАПАДНО-СИБИРСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2083", new PfmCNSIData("2083", 8162, "КРАСНОЯРСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2084", new PfmCNSIData("2084", 7647, "ВОСТОЧНО-СИБИРСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2085", new PfmCNSIData("2085", 8980, "ЗАБАЙКАЛЬСКАЯ ДРТ", true));
		pfmCnsiCodesMap.put("2086", new PfmCNSIData("2086", 5699, "ДАЛЬНЕВОСТОЧНАЯ ДРТ", true));
		//ДЖВ
		pfmCnsiCodesMap.put("2174", new PfmCNSIData("2174", 7728, "ДЖВ", true));
		pfmCnsiCodesMap.put("2197", new PfmCNSIData("2197", 7723, "СЕВЕРО-ЗАПАДНАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2199", new PfmCNSIData("2199", 7722, "МОСКОВСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2200", new PfmCNSIData("2200", 7721, "ГОРЬКОВСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2201", new PfmCNSIData("2201", 7720, "СЕВЕРНАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2202", new PfmCNSIData("2202", 7719, "СЕВЕРО-КАВКАЗСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2203", new PfmCNSIData("2203", 7718, "ЮГО-ВОСТОЧНАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2204", new PfmCNSIData("2204", 7717, "ПРИВОЛЖСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2205", new PfmCNSIData("2205", 7716, "КУЙБЫШЕВСКАЯ РДЖВ", true));
		pfmCnsiCodesMap.put("2206", new PfmCNSIData("2206", 7715, "СВЕРДЛОВСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2207", new PfmCNSIData("2207", 7714, "ЮЖНО-УРАЛЬСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2208", new PfmCNSIData("2208", 7713, "ЗАПАДНО-СИБИРСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2209", new PfmCNSIData("2209", 7709, "КРАСНОЯРСКАЯ РДЖВ СП ДЖВ ОАО \"РЖД\"", true));
		pfmCnsiCodesMap.put("2210", new PfmCNSIData("2210", 7712, "ВОСТОЧНО-СИБИРСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2211", new PfmCNSIData("2211", 7711, "ЗАБАЙКАЛЬСКАЯ РДЖВ ", true));
		pfmCnsiCodesMap.put("2212", new PfmCNSIData("2212", 7710, "ДАЛЬНЕВОСТОЧНАЯ РДЖВ ", true));
//		pfmCnsiCodesMap.put("2911", new PfmCNSIData("2911", -1, "ОАО \"РЖД - РАЗВИТИЕ ВОКЗАЛОВ\"", true));
		//ДОСС
		pfmCnsiCodesMap.put("2881", new PfmCNSIData("2881", 8809, "ДОСС", true));
		pfmCnsiCodesMap.put("2882", new PfmCNSIData("2882", 9396, "СЕВЕРО-ЗАПАДНАЯ ДОСС ", true));
		pfmCnsiCodesMap.put("9924", new PfmCNSIData("9924", 13815, "ЦЕНТР ПОДГОТОВКИ ПЕРСОНАЛА ПО ОБСЛУЖИВАНИЮ ВЫСОКОСКОРОСТНЫХ ПОЕЗДОВ", true));
		pfmCnsiCodesMap.put("9524", new PfmCNSIData("9524", 13023, "ЮЖНАЯ ДОСС", true));
		pfmCnsiCodesMap.put("9938", new PfmCNSIData("9938", 14362, "МОСКОВСКАЯ ДОСС", true));
		//ЦТ
		pfmCnsiCodesMap.put("2045", new PfmCNSIData("2045", 13006, "ЦТ", true));
		pfmCnsiCodesMap.put("2400", new PfmCNSIData("2400", 13004, "Октябьская ДТ", true));
		pfmCnsiCodesMap.put("2401", new PfmCNSIData("2401", 13005, "Красноярская ДТ", true));
		pfmCnsiCodesMap.put("2402", new PfmCNSIData("2402", 9352, "Восточно-СибирскаяДТ", true));
		pfmCnsiCodesMap.put("2403", new PfmCNSIData("2403", 13007, "Калининградская ДТ", true));
		pfmCnsiCodesMap.put("2404", new PfmCNSIData("2404", 9500, "Московская ДТ", true));
		pfmCnsiCodesMap.put("2405", new PfmCNSIData("2405", 9599, "Горьковская ДТ", true));
		pfmCnsiCodesMap.put("2406", new PfmCNSIData("2406", 9601, "Северная ДТ", true));
		pfmCnsiCodesMap.put("2407", new PfmCNSIData("2407", 13008, "Северо-Кавказская ДТ", true));
		pfmCnsiCodesMap.put("2408", new PfmCNSIData("2408", 9598, "Юго-Восточная ДТ", true));
		pfmCnsiCodesMap.put("2409", new PfmCNSIData("2409", 9603, "Приволжская ДТ", true));
		pfmCnsiCodesMap.put("2410", new PfmCNSIData("2410", 9583, "Куйбышевская ДТ", true));
		pfmCnsiCodesMap.put("2411", new PfmCNSIData("2411", 9600, "Свердловск. ДТ", true));
		pfmCnsiCodesMap.put("2412", new PfmCNSIData("2412", 13011, "Южно- Уральская ДТ", true));
		pfmCnsiCodesMap.put("2413", new PfmCNSIData("2413", 13010, "Западно-Сибирская ДТ", true));
		pfmCnsiCodesMap.put("2414", new PfmCNSIData("2414", 13009, "Забайкальская ДТ", true));
		pfmCnsiCodesMap.put("2415", new PfmCNSIData("2415", 9602, "Дальневосточная ДТ", true));
		//ЦТР
		pfmCnsiCodesMap.put("2044", new PfmCNSIData("2044", 12077, "ЦТР", true));
		pfmCnsiCodesMap.put("2301", new PfmCNSIData("2301", 5669, "ЦТР Октябрьской ж.д.", true));
		pfmCnsiCodesMap.put("2302", new PfmCNSIData("2302", 9010, "ЦТР Московской ж.д.", true));
		pfmCnsiCodesMap.put("2303", new PfmCNSIData("2303", 13000, "ЦТР Горьковской ж.д.", true));
		pfmCnsiCodesMap.put("2304", new PfmCNSIData("2304", 9155, "ЦТР Северной ж.д.", true));
		pfmCnsiCodesMap.put("2305", new PfmCNSIData("2305", 13003, "ЦТР Северо-Кавказской ж.д.", true));
		pfmCnsiCodesMap.put("2306", new PfmCNSIData("2306", 9156, "ЦТР Юго-Восточной ж.д.", true));
		pfmCnsiCodesMap.put("2307", new PfmCNSIData("2307", 8986, "ЦТР Приволжской ж.д.", true));
		pfmCnsiCodesMap.put("2308", new PfmCNSIData("2308", 8993, "ЦТР Куйбышевской ж.д.", true));
		pfmCnsiCodesMap.put("2309", new PfmCNSIData("2309", 8987, "ЦТР Свердловскойж. д.", true));
		pfmCnsiCodesMap.put("2310", new PfmCNSIData("2310", 8823, "ЦТР Южно-Уральской ж.д.", true));
		pfmCnsiCodesMap.put("2311", new PfmCNSIData("2311", 6858, "ЦТР Западно-Сибирской ж.д", true));
		pfmCnsiCodesMap.put("2312", new PfmCNSIData("2312", 8820, "ЦTP Красноярской ж.д.", true));
		pfmCnsiCodesMap.put("2313", new PfmCNSIData("2313", 8822, "ЦТР Вост.-Сибирской ж.д", true));
		pfmCnsiCodesMap.put("2314", new PfmCNSIData("2314", 8815, "ЦТР Забайкальскойж.д.", true));
		pfmCnsiCodesMap.put("2315", new PfmCNSIData("2315", 8821, "ЦТР Дальневосточной ж.д.", true));
		pfmCnsiCodesMap.put("2316", new PfmCNSIData("2316", 12074, "ЦТР Калининградской ж.д.", true));
		pfmCnsiCodesMap.put("2317", new PfmCNSIData("2317", 13444, "Байкало-Амурская ЦТР", true));
		//РЖДС
		pfmCnsiCodesMap.put("1132", new PfmCNSIData("1132", 2625, "РЖДС", true));
		pfmCnsiCodesMap.put("1466", new PfmCNSIData("1466", 7384, "СПЕЦИАЛИЗИРОВАННОЕ УПРАВЛЕНИЕ \"СПЕЦЖЕЛДОРСНАБ\"", true));
		pfmCnsiCodesMap.put("2171", new PfmCNSIData("2171", 2204, "САНКТ-ПЕТЕРБУРГСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2172", new PfmCNSIData("2172", 6869, "КРАСНОЯРСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2240", new PfmCNSIData("2240", 1175, "ВОРОНЕЖСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2241", new PfmCNSIData("2241", 3992, "ЕКАТЕРИНБУРГСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2242", new PfmCNSIData("2242", 1500, "ИРКУТСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2243", new PfmCNSIData("2243", 1604, "КАЛИНИНГРАДСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2244", new PfmCNSIData("2244", 2692, "МОСКОВСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2245", new PfmCNSIData("2245", 1351, "НИЖЕГОРОДСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2246", new PfmCNSIData("2246", 3124, "НОВОСИБИРСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2247", new PfmCNSIData("2247", 3517, "РОСТОВСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2248", new PfmCNSIData("2248", 1943, "САМАРСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2249", new PfmCNSIData("2249", 3761, "САРАТОВСКАЯ ДМТО", true));
		pfmCnsiCodesMap.put("2250", new PfmCNSIData("2250", 588, "ХАБАРОВСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2251", new PfmCNSIData("2251", 4449, "ЧЕЛЯБИНСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2252", new PfmCNSIData("2252", 4659, "ЧИТИНСКАЯ ДМТО ", true));
		pfmCnsiCodesMap.put("2254", new PfmCNSIData("2254", 4705, "ЯРОСЛАВСКАЯ ДМТО", true));
		//Трансэнерго
		pfmCnsiCodesMap.put("1229", new PfmCNSIData("1229", 7390, "Трансэнерго", true));
		pfmCnsiCodesMap.put("2331", new PfmCNSIData("2331", 2224, "ОКТЯБРЬСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("7525", new PfmCNSIData("7525", 6746, "КРАСНОЯРСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9986", new PfmCNSIData("9986", 6386, "МОСКОВСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9987", new PfmCNSIData("9987", 6561, "ГОРЬКОВСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9988", new PfmCNSIData("9988", 6547, "СЕВЕРНАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9989", new PfmCNSIData("9989", 6564, "СЕВЕРО-КАВКАЗСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9990", new PfmCNSIData("9990", 6562, "ЮГО-ВОСТОЧНАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9991", new PfmCNSIData("9991", 6606, "ПРИВОЛЖСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9992", new PfmCNSIData("9992", 6693, "КУЙБЫШЕВСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9993", new PfmCNSIData("9993", 6365, "СВЕРДЛОВСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9994", new PfmCNSIData("9994", 6594, "ЮЖНО-УРАЛЬСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9995", new PfmCNSIData("9995", 6444, "ЗАПАДНО-СИБИРСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9996", new PfmCNSIData("9996", 6611, "ВОСТОЧНО-СИБИРСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9997", new PfmCNSIData("9997", 6614, "ЗАБАЙКАЛЬСКАЯ ДЭ", true));
		pfmCnsiCodesMap.put("9998", new PfmCNSIData("9998", 13200, "ДАЛЬНЕВОСТОЧНАЯ ДЭ", true));
		//ГВЦ
		pfmCnsiCodesMap.put("1232", new PfmCNSIData("1232", 2646, "ГВЦ", true));
		pfmCnsiCodesMap.put("2180", new PfmCNSIData("2180", 2180, "САНКТ-ПЕТЕРБУРГСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2181", new PfmCNSIData("2181", 1606, "КАЛИНИНГРАДСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2182", new PfmCNSIData("2182", 2635, "МОСКОВСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2183", new PfmCNSIData("2183", 1373, "НИЖЕГОРОДСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2184", new PfmCNSIData("2184", 4698, "ЯРОСЛАВСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2185", new PfmCNSIData("2185", 3486, "РОСТОВСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2186", new PfmCNSIData("2186", 1172, "ВОРОНЕЖСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2187", new PfmCNSIData("2187", 3773, "САРАТОВСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2188", new PfmCNSIData("2188", 1929, "САМАРСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2189", new PfmCNSIData("2189", 4001, "ЕКАТЕРИНБУРГСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2190", new PfmCNSIData("2190", 4393, "ЧЕЛЯБИНСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2191", new PfmCNSIData("2191", 3103, "НОВОСИБИРСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2192", new PfmCNSIData("2192", 349, "КРАСНОЯРСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2193", new PfmCNSIData("2193", 1561, "ИРКУТСКИЙ ИВЦ ", true));
		pfmCnsiCodesMap.put("2194", new PfmCNSIData("2194", 4524, "ЧИТИНСКИЙ ИВЦ", true));
		pfmCnsiCodesMap.put("2195", new PfmCNSIData("2195", 586, "ХАБАРОВСКИЙ ИВЦ", true));
		//ЦСС
		pfmCnsiCodesMap.put("1233", new PfmCNSIData("1233", 2433, "ЦСС", true));
		pfmCnsiCodesMap.put("2448", new PfmCNSIData("2448", 6722, "ОКТЯБРЬСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2449", new PfmCNSIData("2449", 6729, "САМАРСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2450", new PfmCNSIData("2450", 6732, "КРАСНОЯРСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2451", new PfmCNSIData("2451", 6726, "РОСТОВСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2452", new PfmCNSIData("2452", 6731, "НОВОСИБИРСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2453", new PfmCNSIData("2453", 6723, "МОСКОВСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2454", new PfmCNSIData("2454", 6735, "ХАБАРОВСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2455", new PfmCNSIData("2455", 6730, "ЕКАТЕРИНБУРГСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2466", new PfmCNSIData("2466", 7288, "КАЛИНИНГРАДСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2477", new PfmCNSIData("2477", 6724, "НИЖЕГОРОДСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2483", new PfmCNSIData("2483", 6725, "ЯРОСЛАВСКАЯ ДС", true));
		pfmCnsiCodesMap.put("2495", new PfmCNSIData("2495", 6727, "ВОРОНЕЖСКАЯ ДС", true));
		pfmCnsiCodesMap.put("9940", new PfmCNSIData("9940", 6728, "САРАТОВСКАЯ ДС", true));
		pfmCnsiCodesMap.put("9956", new PfmCNSIData("9956", 6737, "ЧЕЛЯБИНСКАЯ ДС", true));
		pfmCnsiCodesMap.put("9971", new PfmCNSIData("9971", 6733, "ИРКУТСКАЯ ДС", true));
		pfmCnsiCodesMap.put("9976", new PfmCNSIData("9976", 6734, "ЧИТИНСКАЯ ДС", true));
		//Трансинформ
		pfmCnsiCodesMap.put("1401", new PfmCNSIData("1401", 2623, "Трансинформ", true));
		//Центр ИССО
		pfmCnsiCodesMap.put("1231", new PfmCNSIData("1231", 7401, "Центр ИССО", true));
//		pfmCnsiCodesMap.put("1095", new PfmCNSIData("1095", -1, "ХАБАРОВСКАЯ МОСТОИСПЫТАТЕЛЬНАЯ СТАНЦИЯ ", true));
//		pfmCnsiCodesMap.put("1417", new PfmCNSIData("1417", -1, "САРАТОВСКАЯ МОСТОИСПЫТАТЕЛЬНАЯ СТАНЦИЯ ЦЕНТРА ИССО", true));
//		pfmCnsiCodesMap.put("1418", new PfmCNSIData("1418", -1, "МЕРЗЛОТНАЯ СТАНЦИЯ", true));
		//ЖДУ
		pfmCnsiCodesMap.put("1011", new PfmCNSIData("1011", 7387, "ЖДУ", true));
		pfmCnsiCodesMap.put("1902", new PfmCNSIData("1902", 8851, "КРАСНОЯРСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("2536", new PfmCNSIData("2536", 9287, "МОСКОВСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("2880", new PfmCNSIData("2880", 8811, "СЕВЕРО-ЗАПАДНЫЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("3305", new PfmCNSIData("3305", 9198, "ГОРЬКОВСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("3800", new PfmCNSIData("3800", 9253, "СЕВЕРНЫЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("4177", new PfmCNSIData("4177", 9280, "СЕВЕРО-КАВКАЗСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("4890", new PfmCNSIData("4890", 9273, "ЮГО-ВОСТОЧНЫЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("5401", new PfmCNSIData("5401", 8856, "ПРИВОЛЖСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("5555", new PfmCNSIData("5555", 9265, "КУЙБЫШЕВСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("6010", new PfmCNSIData("6010", 9259, "СВЕРДЛОВСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("6830", new PfmCNSIData("6830", 9300, "ЮЖНО-УРАЛЬСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("7061", new PfmCNSIData("7061", 9188, "ЗАПАДНО-СИБИРСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("8020", new PfmCNSIData("8020", 9193, "ВОСТОЧНО-СИБИРСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("8690", new PfmCNSIData("8690", 9205, "ЗАБАЙКАЛЬСКИЙ ОЦО-РЕГИОН", true));
		pfmCnsiCodesMap.put("9498", new PfmCNSIData("9498", 9306, "ДАЛЬНЕВОСТОЧНЫЙ ОЦО-РЕГИОН", true));
		//ЦОТЭН
		pfmCnsiCodesMap.put("1100", new PfmCNSIData("1100", 7388, "ЦОТЭН", true));
		//ЦИР
//		pfmCnsiCodesMap.put("2380", new PfmCNSIData("2380", -1, "ЦИР", true));
		//ЦНТИБ
		pfmCnsiCodesMap.put("1230", new PfmCNSIData("1230", 2402, "ЦНТИБ", true));
		//ЦДИ
		pfmCnsiCodesMap.put("2257", new PfmCNSIData("2257", 13419, "ЦДИ", true));
//		pfmCnsiCodesMap.put("4159", new PfmCNSIData("4159", -1, "СОЧИНСКАЯ ДИ", true));
//		pfmCnsiCodesMap.put("5053", new PfmCNSIData("5053", -1, "ОКТ ДИ СЕБЕЖСКИЕ МАСТЕРСКИЕ ЖБИ", true));
		pfmCnsiCodesMap.put("5059", new PfmCNSIData("5059", 13500, "ОКТ ДИ", true));
		pfmCnsiCodesMap.put("5063", new PfmCNSIData("5063", 2222, "ОКТ. ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5067", new PfmCNSIData("5067", 2176, "ОКТ. ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5068", new PfmCNSIData("5068", 2177, "ОКТ. ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5088", new PfmCNSIData("5088", 6457, "ОКТ. ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5093", new PfmCNSIData("5093", 13506, "КЛГ ДИ", true));
//		pfmCnsiCodesMap.put("5100", new PfmCNSIData("5100", -1, "МСК ДИЦДМ", true));
		pfmCnsiCodesMap.put("5123", new PfmCNSIData("5123", 6406, "КЛНГ ДИ- СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5124", new PfmCNSIData("5124", 6490, "КЛНГ ДИ-СЛУЖБА Ш", true));
//		pfmCnsiCodesMap.put("5125", new PfmCNSIData("5125", -1, "КЛНГ ДИ-СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5126", new PfmCNSIData("5126", 8751, "КЛНГ ДИ-СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5128", new PfmCNSIData("5128", 13502, "МОСКОВСКАЯ ДИ", true));
		pfmCnsiCodesMap.put("5131", new PfmCNSIData("5131", 6407, "МСК ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5132", new PfmCNSIData("5132", 6491, "МСК ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5133", new PfmCNSIData("5133", 6475, "МСК ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5134", new PfmCNSIData("5134", 6459, "МСК ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5137", new PfmCNSIData("5137", 13512, "ГОРЬК ДИ", true));
		pfmCnsiCodesMap.put("5138", new PfmCNSIData("5138", 6408, "ГОРЬК ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5139", new PfmCNSIData("5139", 6492, "ГОРЬК ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5146", new PfmCNSIData("5146", 6476, "ГОРЬК ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5147", new PfmCNSIData("5147", 6460, "ГОРЬК ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5149", new PfmCNSIData("5149", 13503, "СЕВ ДИ ", true));
		pfmCnsiCodesMap.put("5168", new PfmCNSIData("5168", 6409, "СЕВ ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5187", new PfmCNSIData("5187", 6493, "СЕВ ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5188", new PfmCNSIData("5188", 6477, "СЕВ ДИ-СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5189", new PfmCNSIData("5189", 6461, "СЕВ ДИ-СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5218", new PfmCNSIData("5218", 13515, "С-К ДИ", true));
		pfmCnsiCodesMap.put("5219", new PfmCNSIData("5219", 3569, "С-К ДИ - СЛУЖБА ПУТИ", true));
		pfmCnsiCodesMap.put("5266", new PfmCNSIData("5266", 6494, "С-К ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5267", new PfmCNSIData("5267", 6478, "С-К ДИ - СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5268", new PfmCNSIData("5268", 6462, "С-К ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5270", new PfmCNSIData("5270", 13510, "Ю-ВОСТ ДИ", true));
		pfmCnsiCodesMap.put("5271", new PfmCNSIData("5271", 6410, "Ю-ВОСТ ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5272", new PfmCNSIData("5272", 6495, "Ю-ВОСТ ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5273", new PfmCNSIData("5273", 6479, "Ю-ВОСТ ДИ - СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5274", new PfmCNSIData("5274", 6463, "Ю-ВОСТ ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5276", new PfmCNSIData("5276", 13504, "ПРИВ ДИ", true));
		pfmCnsiCodesMap.put("5277", new PfmCNSIData("5277", 6411, "ПРИВ ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5278", new PfmCNSIData("5278", 6496, "ПРИВ ДИ СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5279", new PfmCNSIData("5279", 6480, "ПРИВ ДИ СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5280", new PfmCNSIData("5280", 6464, "ПРИВ ДИ СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5283", new PfmCNSIData("5283", 13505, "КБШ ДИ", true));
		pfmCnsiCodesMap.put("5284", new PfmCNSIData("5284", 6412, "КБШ ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5285", new PfmCNSIData("5285", 6497, "КБШ ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5286", new PfmCNSIData("5286", 6481, "КБШ ДИ - СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5287", new PfmCNSIData("5287", 6465, "КБШ ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5289", new PfmCNSIData("5289", 13501, "СВЕРД ДИ", true));
		pfmCnsiCodesMap.put("5314", new PfmCNSIData("5314", 6413, "СВЕРД ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5317", new PfmCNSIData("5317", 6498, "СВЕРД ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5318", new PfmCNSIData("5318", 6482, "СВЕРД ДИ - СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5319", new PfmCNSIData("5319", 6466, "СВЕРД ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5348", new PfmCNSIData("5348", 13509, "Ю-УР ДИ", true));
		pfmCnsiCodesMap.put("5349", new PfmCNSIData("5349", 6414, "Ю-УР ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5351", new PfmCNSIData("5351", 6499, "Ю-УР ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5352", new PfmCNSIData("5352", 6483, "Ю-УР ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5353", new PfmCNSIData("5353", 6467, "Ю-УР ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5355", new PfmCNSIData("5355", 13507, "ЗАП-СИБ ДИ", true));
		pfmCnsiCodesMap.put("5356", new PfmCNSIData("5356", 6415, "ЗАП-СИБ - ДИ СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5357", new PfmCNSIData("5357", 6500, "ЗАП-СИБ - ДИ СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5358", new PfmCNSIData("5358", 6484, "ЗАП-СИБ - ДИ СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5359", new PfmCNSIData("5359", 6468, "ЗАП-СИБ - ДИ СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5362", new PfmCNSIData("5362", 13513, "КРАС ДИ", true));
		pfmCnsiCodesMap.put("5363", new PfmCNSIData("5363", 240, "КРАС ДИ-СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5364", new PfmCNSIData("5364", 6501, "КРАС ДИ-СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5365", new PfmCNSIData("5365", 6485, "КРАС ДИ-СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5366", new PfmCNSIData("5366", 6469, "КРАС ДИ-СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5368", new PfmCNSIData("5368", 13514, "ВС ДИ", true));
		pfmCnsiCodesMap.put("5369", new PfmCNSIData("5369", 6416, "ВС ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5370", new PfmCNSIData("5370", 6502, "ВС ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5371", new PfmCNSIData("5371", 6486, "ВС ДИ - СЛУЖБА Э", true));
		pfmCnsiCodesMap.put("5372", new PfmCNSIData("5372", 6470, "ВС ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5374", new PfmCNSIData("5374", 13511, "ЗАБАЙКАЛЬСКАЯ ДИ", true));
		pfmCnsiCodesMap.put("5375", new PfmCNSIData("5375", 6417, "ЗАБ ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5376", new PfmCNSIData("5376", 6503, "ЗАБ ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5377", new PfmCNSIData("5377", 6487, "ЗАБ ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5378", new PfmCNSIData("5378", 6471, "ЗАБ ДИ - СЛУЖБА В", true));
		pfmCnsiCodesMap.put("5380", new PfmCNSIData("5380", 13508, "ДВС ДИ", true));
		pfmCnsiCodesMap.put("5381", new PfmCNSIData("5381", 6418, "ДВС ДИ - СЛУЖБА П", true));
		pfmCnsiCodesMap.put("5382", new PfmCNSIData("5382", 6504, "ДВС ДИ - СЛУЖБА Ш", true));
		pfmCnsiCodesMap.put("5383", new PfmCNSIData("5383", 6488, "ДВС ДИ - СЛУЖБА ЭЛ", true));
		pfmCnsiCodesMap.put("5384", new PfmCNSIData("5384", 6472, "ДВС ДИ - СЛУЖБА В", true));
//		pfmCnsiCodesMap.put("5386", new PfmCNSIData("5386", -1, "ОКТ ДИ -ДПМ", true));
//		pfmCnsiCodesMap.put("5387", new PfmCNSIData("5387", -1, "ОКТ ДИ - ЦДМ", true));
//		pfmCnsiCodesMap.put("5389", new PfmCNSIData("5389", -1, "КЛНГ ДИ-ДПМ", true));
//		pfmCnsiCodesMap.put("5392", new PfmCNSIData("5392", -1, "МСК ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("5393", new PfmCNSIData("5393", -1, "ГОРЬК ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("5394", new PfmCNSIData("5394", -1, "ГОРЬК ДИ ЦДМ", true));
//		pfmCnsiCodesMap.put("5395", new PfmCNSIData("5395", -1, "СЕВ ДИ - ДПМ", true));
//		pfmCnsiCodesMap.put("5396", new PfmCNSIData("5396", -1, "СЕВ ДИ", true));
//		pfmCnsiCodesMap.put("5397", new PfmCNSIData("5397", -1, "С-К ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("5398", new PfmCNSIData("5398", -1, "ЭМП СЕВ ДИ", true));
//		pfmCnsiCodesMap.put("5399", new PfmCNSIData("5399", -1, "С-К ДИ", true));
		pfmCnsiCodesMap.put("5448", new PfmCNSIData("5448", 7378, "Ю-ВОСТ ДИ - ДПМ", true));
//		pfmCnsiCodesMap.put("5449", new PfmCNSIData("5449", -1, "Ю-ВОСТ ДИ - ЦДМ", true));
//		pfmCnsiCodesMap.put("6009", new PfmCNSIData("6009", -1, "ПРИВ ДИ-ДПМ", true));
//		pfmCnsiCodesMap.put("6024", new PfmCNSIData("6024", -1, "ПРИВ ДИ-ПЦД", true));
//		pfmCnsiCodesMap.put("6112", new PfmCNSIData("6112", -1, "КБШ ДИ - ДПМ", true));
//		pfmCnsiCodesMap.put("6134", new PfmCNSIData("6134", -1, "КБШ ДИ - ЦДМ", true));
		pfmCnsiCodesMap.put("6144", new PfmCNSIData("6144", 7377, "СВЕРД ДИ - ДПМ", true));
//		pfmCnsiCodesMap.put("6146", new PfmCNSIData("6146", -1, "СВЕРД ДИ - ЦДМ", true));
		pfmCnsiCodesMap.put("6206", new PfmCNSIData("6206", 7252, "ЮУР ДИ ДПМ", true));
		pfmCnsiCodesMap.put("6207", new PfmCNSIData("6207", 6443, "ЮУР ДИ ЦДМ", true));
		pfmCnsiCodesMap.put("6341", new PfmCNSIData("6341", 6512, "ЗАП-СИБ ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("6377", new PfmCNSIData("6377", -1, "ЗАП-СИБ ДИ ЦДМ", true));
		pfmCnsiCodesMap.put("6427", new PfmCNSIData("6427", 13856, "ЭМП ЗАП-СИБ ДИ", true));
//		pfmCnsiCodesMap.put("6429", new PfmCNSIData("6429", -1, "КРАС ДИ-ДПМ", true));
//		pfmCnsiCodesMap.put("6435", new PfmCNSIData("6435", -1, "КРАС ДИ-ЦДМ", true));
//		pfmCnsiCodesMap.put("6481", new PfmCNSIData("6481", -1, "ВС ДИ - ДПМ", true));
//		pfmCnsiCodesMap.put("6557", new PfmCNSIData("6557", -1, "ВС ДИ - ЦДМ", true));
//		pfmCnsiCodesMap.put("6660", new PfmCNSIData("6660", -1, "ЗАБ ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("6661", new PfmCNSIData("6661", -1, "ЗАБ ДИ ЦДМИ", true));
//		pfmCnsiCodesMap.put("6663", new PfmCNSIData("6663", -1, "ДВОСТ ДИ ДПМ", true));
//		pfmCnsiCodesMap.put("6664", new PfmCNSIData("6664", -1, "ДВОСТ ДИ ЦДМИ", true));
		//ЦДПО
		pfmCnsiCodesMap.put("3530", new PfmCNSIData("3530", 13317, "ЦДПО", true));
		pfmCnsiCodesMap.put("4421", new PfmCNSIData("4421", 13325, "ОКТЯБРЬСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4422", new PfmCNSIData("4422", 13318, "КАЛИНИНГРАДСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4423", new PfmCNSIData("4423", 13331, "МОСКОВСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4424", new PfmCNSIData("4424", 13323, "ГОРЬКОВСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4425", new PfmCNSIData("4425", 13405, "СЕВЕРНАЯ ДПО ", true));
		pfmCnsiCodesMap.put("4426", new PfmCNSIData("4426", 13319, "СЕВЕРО-КАВКАЗСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4427", new PfmCNSIData("4427", 13321, "ЮГО-ВОСТОЧНАЯ ДПО", true));
		pfmCnsiCodesMap.put("4428", new PfmCNSIData("4428", 13328, "ПРИВОЛЖСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4429", new PfmCNSIData("4429", 13352, "КУЙБЫШЕВСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4430", new PfmCNSIData("4430", 13349, "СВЕРДЛОВСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4431", new PfmCNSIData("4431", 13311, "ЮЖНО-УРАЛЬСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4432", new PfmCNSIData("4432", 13402, "ЗАП-СИБ ДПО", true));
		pfmCnsiCodesMap.put("4433", new PfmCNSIData("4433", 13407, "КРАСНОЯРСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4434", new PfmCNSIData("4434", 13306, "ВОСТОЧНО-СИБИРСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4435", new PfmCNSIData("4435", 13326, "ЗАБАЙКАЛЬСКАЯ ДПО", true));
		pfmCnsiCodesMap.put("4436", new PfmCNSIData("4436", 13409, "ДАЛЬНЕВОСТОЧНАЯ ДПО", true));
		//ЦДМВ
		pfmCnsiCodesMap.put("3590", new PfmCNSIData("3590", 3316, "ЦДМВ", true));
		pfmCnsiCodesMap.put("4471", new PfmCNSIData("4471", 198, "ОКТЯБРЬСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4472", new PfmCNSIData("4472", 3334, "КАЛИНИНГРАДСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4473", new PfmCNSIData("4473", 3330, "МОСКОВСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4474", new PfmCNSIData("4474", 3322, "ГОРЬКОВСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4475", new PfmCNSIData("4475", 3404, "СЕВЕРНАЯ ДМВ ", true));
		pfmCnsiCodesMap.put("4476", new PfmCNSIData("4476", 3320, "СЕВЕРО-КАВКАЗСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4477", new PfmCNSIData("4477", 3335, "ЮГО-ВОСТОЧНАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4478", new PfmCNSIData("4478", 3329, "ПРИВОЛЖСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4479", new PfmCNSIData("4479", 3400, "КУЙБЫШЕВСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4480", new PfmCNSIData("4480", 3348, "СВЕРДЛОВСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4481", new PfmCNSIData("4481", 3324, "ЮЖНО-УРАЛЬСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4482", new PfmCNSIData("4482", 3401, "ЗАПАДНО-СИБИРСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4483", new PfmCNSIData("4483", 3406, "КРАСНОЯРСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4484", new PfmCNSIData("4484", 3336, "ВОСТОЧНО-СИБИРСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4485", new PfmCNSIData("4485", 3340, "ЗАБАЙКАЛЬСКАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4486", new PfmCNSIData("4486", 3408, "ДАЛЬНЕВОСТОЧНАЯ ДМВ", true));
		pfmCnsiCodesMap.put("4487", new PfmCNSIData("4487", 544, "ТЦУ ППК", true));
		//ПКТБ ЦУНР
		pfmCnsiCodesMap.put("2909", new PfmCNSIData("2909", 9416, "ПКТБ ЦУНР", true));
		//ИЭРТ
		pfmCnsiCodesMap.put("9800", new PfmCNSIData("9800", 13367, "ИЭРТ", true));
		//АХУ
		pfmCnsiCodesMap.put("1234", new PfmCNSIData("1234", 2586, "АХУ", true));
			
		for (PfmCNSIData data : pfmCnsiCodesMap.values())
		{
			cnsiPfmCodesMap.put(data.cnsiCode, data);
		}
	}

	public static Integer correctPfmCode(String codeString)
	{
		long pmf = Long.parseLong(codeString) - 20000000;
		return (int) pmf;
	}

	public void insertintotable (NamedParameterJdbcTemplate npjt){
		HashMap<String, Object> pp = new HashMap<String, Object>();
		
		for (PfmCNSIData data : pfmCnsiCodesMap.values()){
			
			pp.put("PFMCODE", data.pfmCode);
			pp.put("ETDID", data.cnsiCode);
			pp.put("NAME", data.shortDesc);
			pp.put("ISFILIAL", data.isfilial?1:0);
			System.out.println("insert "+data.pfmCode+" "+data.cnsiCode+" "+data.shortDesc+" "+pp.get("ISFILIAL")+"");
			try{
//				sql.replaceAll(":PFMCODE", data.pfmCode);
//				sql.replaceAll(":ETDID", String.valueOf(data.cnsiCode));
//				sql.replaceAll(":NAME", data.shortDesc);
//				sql.replaceAll(":ISFILIAL", String.valueOf(pp.get("ISFILIAL")));
//				System.out.println(sql);
				
			npjt.update(getSql(data.pfmCode, String.valueOf(data.cnsiCode), data.shortDesc, String.valueOf(data.isfilial?1:0)), pp);
			} catch (Exception e){
				e.printStackTrace();
			}
		}
		
//		for (int i=0; i<pfmCnsiCodesMap.size(); i++){
//			pp.put("pfmcode", pfmCnsiCodesMap.get(i).pfmCode);
//			pp.put("etdid", pfmCnsiCodesMap.get(i).cnsiCode);
//			pp.put("name", pfmCnsiCodesMap.get(i).shortDesc);
//			
//			System.out.println(pp.get("pfmcode"));
//			System.out.println(pp.get("name"));
//		}
		
		
	}
	
	private String getSql (String pfmcode, String cnsi, String name, String filial){
	String sql = "MERGE INTO SNT.PFMCODES P USING(VALUES('"+pfmcode+"', "+cnsi+", '"+name+"',"+filial+")) "+
		 " AS NEW(PFMCODE,ETDID,NAME,ISFILIAL) "+
		 " ON P.PFMCODE = NEW.PFMCODE "+
		 " WHEN MATCHED THEN UPDATE SET P.ETDID = NEW.ETDID, P.NAME = NEW.NAME, P.ISFILIAL = NEW.ISFILIAL "+
		 " WHEN NOT MATCHED THEN "+
		 " INSERT (PFMCODE,ETDID,NAME,ISFILIAL) "+
		 " VALUES (NEW.PFMCODE,NEW.ETDID,NEW.NAME,NEW.ISFILIAL) ";
	return sql;
	}
	
	private static class PfmCNSIData
	{
		String pfmCode;
		int cnsiCode;
		String shortDesc;
		boolean isfilial;

	private PfmCNSIData(String pfmCode, int cnsiCode, String shortDesc, boolean isfilial)
	{
		this.pfmCode = pfmCode;
		this.cnsiCode = cnsiCode;
		this.shortDesc = shortDesc;
		this.isfilial = isfilial;
	}
	}
}
