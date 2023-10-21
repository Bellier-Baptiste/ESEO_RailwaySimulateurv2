package data;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Random;

import model.Event;

/**
 * Data Singleton class which stores all informations relative to stations and
 * lines
 * 
 * @author arthu
 *
 */
public class Data extends Observable {

	// attributes
	private static Data instance;
	private static final String[] STATIONS_NAMES = { "Agapanthe", "Bleuet", "Coquelicot", "Dahlia", "Edelweiss",
			"Ficaire", "Grebera", "Hortensia", "Iris", "Jasmin", "Kalmie", "Lys", "Marguerite", "Narcisse", "Ophrys",
			"Petunia", "Quinoa", "Renoncule", "Scabieuse", "Tulipe", "Ursinia", "Violet", "Waterlily", "Xeranthemum",
			"Yarrow", "Zenobia", "Amaranthus", "Begonia", "Cyclamen", "Daphne", "Echinacea", "Ursinia", "Violet",
			"Waterlily", "Xeranthemum", "Yarrow", "Zenobia", "Amaranthus", "Begonia", "Cyclamen", "Daphne", "Echinacea",
			"Fumeterre", "Gaillet", "Heliantheme", "Immortelle", "Jacinthe", "Lamier", "Magnolia", "Nenuphar",
			"Oeillet", "Paquerette", "Raiponce", "Sainfoin", "Tournesol", "Veronique", "Xanthium", "Ylang-ylang",
			"Zinnia", "Ancolie", "Boutons d or", "Calla", "Dame d onze heures", "Epiaire", "Genet", "Hellebore",
			"Jarosse", "Lavande", "Marjolaine", "Oiseau du paradis", "Patience", "Renouee", "Sanve", "Trefle",
			"Viperine", "Arum", "Buglose", "Callune", "Digitale", "Epilobe", "Gentiane", "Hortense", "Jonc", "Lilas",
			"Mauve", "Onagre", "Pavot", "Reseda", "Saponaire", "Tussilage", "Zielik", "Asdel", "Ariel", "Achiel",
			"Aziel", "Nielach", "Bielach", "Deja", "Sacha", "Kusha", "Amael", "Althah", "Aran", "Kasdiel", "Ireul",
			"Kiridw", "Jeha", "Zadkir", "Ibil", "Barbeh", "Vepha", "Mono", "Hema", "Lisu", "Zago", "Balbach", "Gono",
			"Neusa", "Arin", "Aelex", "Lisya", "Chasya", "Mara", "Chema", "Olos", "Bery", "Alech", "Sama", "Shatari",
			"Haneeri", "Minahouka", "Rehohreha", "Yafshegah", "Ranedola", "Nehomahni", "Ghidehama", "Mehrouzana",
			"Soumahtaroya", "Sattehodsa", "Osatilyna", "Mahsaghina", "Mahtaheeva", "Faranaba", "Shareha", "Timina",
			"Sattarina", "Suramehou", "Shorsharata", "Placentia", "Pallena", "Ruscina", "Sinorata", "Pientala",
			"Serona", "Cinicinnia", "Julupicia", "Natacia", "Vinata", "Ucictomnan", "Postellumna", "Virgenia",
			"Laraterna", "Vernicata", "Lucissala", "Sabilia", "Prixeda", "Miolara", "Cillucoscia", "Mamminia",
			"Sulicenta", "Miniana", "Olavistix", "Felena", "Centurna", "Cronetia", "Joverina", "Ticima", "Atalvaxes",
			"Alikakur", "Atalinit", "Karaja", "Anambulit", "Bakumati", "Kanikara", "Bulapari", "Kunjusti", "Hadrika",
			"Atipamar", "Balapara", "Apishantar", "Chendravi", "Kamatii", "Kamalavi", "Atimaniz", "Kamanandi",
			"Avindranit", "Amatinish", "Kanakanayi", "Ritahadri", "Anikabhit", "Bandhatri", "Bhavathandri", "Januruma",
			"Likola", "Jalusi", "Anatarit", "Alakraksayam", "Kalavara", "Ochenelmas", "Parthene", "Dorycle", "Ioxaris",
			"Astyochisas", "Dorisa", "Alesidos", "Henippane", "Asteressas", "Gonisa", "Dicube", "Ainanilton",
			"Menacasto", "Gliasisapphe", "Menikallo", "Atatheagas", "Tharmachima", "Anthethusas", "Carpersebe",
			"Pheribe", "Monippyle", "Hyrmykata", "Eroperis", "Ersenenias", "Pervince", "Antikemis", "Alladitos",
			"Anthousanen", "Perisa", "Amialeris", "Sumilka", "Nebaalu", "Sadaga", "Amupon", "Amzuudit", "Saditu",
			"Nabazzi", "Ninabi", "Bonaki", "Sunina", "Bera", "Duki", "Daresu", "Ammabiel", "Sheburu", "Mabiuma",
			"Hazzera", "Gilga", "Naburna", "Taaninga", "Lagduf", "Rishnag", "Mauhak", "Golurtz", "Rodush", "Lukil",
			"Buga", "Golug", "Agral", "Lurtzog", "Othrog", "Adbug", "Uglurtz", "Gashur", "Azolg", "Golfimb", "Grodush",
			"Snaga", "Buga", "Gashagr", "Bolga", "Mega", "Luga", "Ufthag", "Alcmeg", "Ancis", "Aerert", "Wulfa",
			"Genwy", "Enbeod", "Ethel", "Gyles", "Eadher", "Wolda", "Aewulf", "Chany", "Dreder", "Frichye", "Gyleon",
			"Ricio", "Alard", "Hamath", "Burhre", "Lasym", "Ealrer", "Kater", "Phely", "Burga", "Eongorn", "Eryn",
			"Odleld", "Hilda", "Ennet", "Inen", "Elgith", "Aenburh", "Jane", "Hrethe", "Hreda", "Marey", "Bridger",
			"Withiua", "Abet", "Wena", "Dohe", "Patun", "Shealey", "Cradun", "Asgate", "Batun", "Cawic", "Knedon",
			"Diwood", "Gamor", "Hlabrycg", "Lefield", "Shaithorp", "Heystone", "Wayminster", "Degrove", "Hatun",
			"Choford", "Heyley", "Heabluff", "Mileah", "Hamor", "Graham", "Weedale", "Fiheath", "Tacot", "Heaminster",
			"Thigate", "Orwood", "Hadun", "Bahyrst", "Catun", "Hwadun", "Mahyrst", "Castow", "Wawold", "Griford",
			"Raythorp", "Mewold", "Maburh", "Addleah", "Hrahyrst", "Theminster", "Tastow", "Cawold", "Fiwick",
			"Inhyrst", "Mewick", "Braypool", "Orin", "Irin", "Theli", "Bali", "Alim", "Furi", "Kurdu", "Umur", "Askar",
			"Grinarv", "Ukal", "Thrinarv", "Arvin", "Arud", "Sanzagh", "Amin", "Ukhul", "Grumin", "Ugmar", "Ziri",
			"Narvi", "Aghar", "Khakun", "Dainan", "Zuri", "Arkund", "Kunan", "Frinan", "Orim", "Gimli", "Urin", "Geda",
			"Gwali", "Gwoinan", "Gili", "Bizanar", "Kharbilgun", "Narukthund", "Nulukkhizd", "Kinbarakz", "Buzundin",
			"Gabizah", "Nala", "Kharakinb", "Umulbin", "Baragzir", "Zahazir", "Bizinbiz", "Nula", "Zinbilgil",
			"Nargunarg", "Gunala", "Nulbizinb", "Arar", "Gabaraz", "Naramunz", "Zaramunz", "Bilnulbund", "Badusharb",
			"Zarakinb", "Zaragzinb", "Undar", "Baramunz", "Diranthil", "Borufin", "Ophellas", "Alanduin", "Eglil",
			"Miroduil", "Galenwe", "Celenwe", "Ingon", "Dorophil", "Edhror", "Celore", "Motheli", "Enlor", "Oron",
			"Herendaer", "Enemmir", "Gildire", "Thrufinwe", "Eledran", "Rolallian", "Oislion", "Anfelir", "Ethilith",
			"Ethilorith", "Firedept", "Atal", "Belial", "Diablo", "Asmodan", "Pandore", "Baal", "Koivu", "Ornigold",
			"Mahyar", "Tanimlye", "Enderith", "Nerdalye", "Anith", "Inden", "Serianye", "Enwel", "Erdas", "Earien",
			"Nellothien", "Dolothlond", "Ellothlon", "Eimlarnin", "Enorlon", "Gondolond", "Gondolor", "Helline",
			"Mariathon", "Ellondon", "Negrasea", "Golgal", "Londone", "Edhenon", "Dorwine", "Hellothlion", "Formene",
			"Evrargond", "Lindoladr", "Vallonde", "Marastir", "Lorasea", "Athyes", "Jago", "Ferib", "Reward", "Andes",
			"Tharles", "Folco", "Bardo", "Hany", "Willes", "Wilhye", "Piersym", "Erin", "Thurey", "Engar", "Giles",
			"Pippin", "Palac", "Gery", "Yansham", "Cemoor", "Pewood", "Spemoor", "Beminster", "Autwich", "Sefalls",
			"Beybrook", "Hibrook", "Wywood", "Opton", "Baybluff", };

	private final static Color[] LINES_COLORS = new Color[50];
	Random rand = new Random();

	private int areaId;
	private List<String> availableStationNames;
	public static final String AREA_TOURIST = "Tourist";
	public static final String AREA_STUDENT = "Student";
	public static final String AREA_BUSINESSMAN = "Businessman";
	public static final String AREA_WORKER = "Worker";
	public static final String AREA_CHILD = "Child";
	public static final String AREA_RETIRED = "Retired";
	public static final String AREA_UNEMPLOYED = "Unemployed";
	
	//event attributes
	private List<Event> eventList;
	public static final String STATION_START = "stationStart";
	public static final String STATION_END = "stationEnd";
	public static final String STATION_CONCERNED = "stationConcerned";
	private int stationStartId;
	private int stationEndId;
	private int stationConcernedId;
	private String selectType;
	
	//map Attribute
	private String currentCity;

	/**
	 * Private Constructor
	 * 
	 */
	private Data() {
		super();
		this.availableStationNames = new LinkedList<String>(Arrays.asList(STATIONS_NAMES));
		for (int i = 0; i < 50; i++) {
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			LINES_COLORS[i] = new Color(r, g, b);
			this.selectType = null;
			this.currentCity = "";
		}
		areaId = 0;
		this.eventList = new ArrayList<>();
	}

	// accessors
	/**get the list of available stations name.
	 * @return list of strings stations names
	 */
	public List<String> getAvailableStationNames() {
		return availableStationNames;
	}

	/**set the list of available stations name.
	 * @param availableStationNames name available for stations
	 */
	public void setAvailableStationNames(List<String> availableStationNames) {
		this.availableStationNames = availableStationNames;
	}

	/**get the available lines color.
	 * @return Color[] lines color
	 */
	public static Color[] getLinesColors() {
		return LINES_COLORS;
	}
	

	/**get the current city displayed by opentStreetMap.
	 * @return String currentCity
	 */
	public String getCurrentCity() {
		return currentCity;
	}

	/**get the current city displayed by opentStreetMap.
	 * @param currentCity current city name displayed by opentStreetMap
	 */
	public void setCurrentCity(String currentCity) {
		this.currentCity = currentCity;
	}

	/**
	 * Singleton creation
	 * 
	 * @return Data instance
	 */
	public static Data getInstance() {
		if (instance == null) {
			instance = new Data();
		}
		return instance;
	}

	/**get all stations names.
	 * @return String[] stationNames
	 */
	public static String[] getStationsNames() {
		return STATIONS_NAMES;
	}

	/** generate a new area Id.
	 * @return int newAreaId
	 */
	public int getNewAreaId() {
		this.areaId += 1;
		return areaId;
	}

	/**get event stationStart id
	 * @return int StationStartId
	 */
	public int getStationStartId() {
		return stationStartId;
	}

	/**set event stationStart id
	 * @param stationStartId event stationStart id
	 */
	public void setStationStartId(int stationStartId) {
		this.stationStartId = stationStartId;
		setChanged();
		notifyObservers("start");
	}

	/**get event stationEnd id
	 * @return int stationEndId
	 */
	public int getStationEndId() {
		return stationEndId;
	}

	/**set event stationStart id
	 * @param stationEndId event stationEnd id
	 */
	public void setStationEndId(int stationEndId) {
		this.stationEndId = stationEndId;
		setChanged();
		notifyObservers("end");
	}
	
	/**get event stationConcerned id
	 * @return int stationConcernedId
	 */
	public int getStationConcernedId() {
		return stationConcernedId;
	}

	/**set event stationConcerned id
	 * @param stationConcernedId event stationConcerned id
	 */
	public void setStationConcernedId(int stationConcernedId) {
		this.stationConcernedId = stationConcernedId;
		setChanged();
		notifyObservers("concerned");
	}

	/**get Event selected type.
	 * @return String selectedType
	 */
	public String getSelectType() {
		return selectType;
	}

	/**set Event selected type.
	 * @param selectType eventSelected type
	 */
	public void setSelectType(String selectType) {
		this.selectType = selectType;
		
	}

	/**get the list of all event created.
	 * @return List of events eventList
	 */
	public List<Event> getEventList() {
		return eventList;
	}

	/**set the list of all event created.
	 * @param eventList list of events
	 */
	public void setEventList(List<Event> eventList) {
		this.eventList = eventList;
	}
}
