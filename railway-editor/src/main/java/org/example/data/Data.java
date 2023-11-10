/*
 * License : MIT License
 *
 * Copyright (c) 2023 Team PFE_2023_16
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.example.data;

import org.example.model.Event;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/**
 * Data Singleton class which stores all information relative to the metro
 * network configuration.
 *
 * @see org.example.model.Line
 * @see org.example.model.Station
 * @see org.example.model.Event
 *
 * @author Arthur Lagarce
 * @author Aurélie Chamouleau
 * @file Data.java
 * @date N/A
 * @since 2.0
 */
public final class Data {
  // constants
  /**
   * Number of colors available for lines.
   */
  public static final int NB_COLORS = 50;
  // attributes
  /** Observer of the data. */
  private Consumer<Object> observer;
  /** Singleton instance of the class. */
  private static Data instance;
  /** List of all stations names. */
  private static final String[] STATIONS_NAMES = {"Agapanthe", "Bleuet",
      "Coquelicot", "Dahlia", "Edelweiss", "Ficaire", "Grebera", "Hortensia",
      "Iris", "Jasmin", "Kalmie", "Lys", "Marguerite", "Narcisse", "Ophrys",
      "Petunia", "Quinoa", "Renoncule", "Scabieuse", "Tulipe", "Ursinia",
      "Violet", "Waterlily", "Xeranthemum", "Yarrow", "Zenobia", "Amaranthus",
      "Begonia", "Cyclamen", "Daphne", "Echinacea", "Ursinia", "Violet",
      "Waterlily", "Xeranthemum", "Yarrow", "Zenobia", "Amaranthus", "Begonia",
      "Cyclamen", "Daphne", "Echinacea", "Fumeterre", "Gaillet", "Heliantheme",
      "Immortelle", "Jacinthe", "Lamier", "Magnolia", "Nenuphar", "Oeillet",
      "Paquerette", "Raiponce", "Sainfoin", "Tournesol", "Veronique",
      "Xanthium", "Ylang-ylang", "Zinnia", "Ancolie", "Boutons d or", "Calla",
      "Dame d onze heures", "Epiaire", "Genet", "Hellebore", "Jarosse",
      "Lavande", "Marjolaine", "Oiseau du paradis", "Patience", "Renouee",
      "Sanve", "Trefle", "Viperine", "Arum", "Buglose", "Callune", "Digitale",
      "Epilobe", "Gentiane", "Hortense", "Jonc", "Lilas", "Mauve", "Onagre",
      "Pavot", "Reseda", "Saponaire", "Tussilage", "Zielik", "Asdel", "Ariel",
      "Achiel", "Aziel", "Nielach", "Bielach", "Deja", "Sacha", "Kusha",
      "Amael", "Althah", "Aran", "Kasdiel", "Ireul", "Kiridw", "Jeha", "Zadkir",
      "Ibil", "Barbeh", "Vepha", "Mono", "Hema", "Lisu", "Zago", "Balbach",
      "Gono", "Neusa", "Arin", "Aelex", "Lisya", "Chasya", "Mara", "Chema",
      "Olos", "Bery", "Alech", "Sama", "Shatari", "Haneeri", "Minahouka",
      "Rehohreha", "Yafshegah", "Ranedola", "Nehomahni", "Ghidehama",
      "Mehrouzana", "Soumahtaroya", "Sattehodsa", "Osatilyna", "Mahsaghina",
      "Mahtaheeva", "Faranaba", "Shareha", "Timina", "Sattarina", "Suramehou",
      "Shorsharata", "Placentia", "Pallena", "Ruscina", "Sinorata", "Pientala",
      "Serona", "Cinicinnia", "Julupicia", "Natacia", "Vinata", "Ucictomnan",
      "Postellumna", "Virgenia", "Laraterna", "Vernicata", "Lucissala",
      "Sabilia", "Prixeda", "Miolara", "Cillucoscia", "Mamminia", "Sulicenta",
      "Miniana", "Olavistix", "Felena", "Centurna", "Cronetia", "Joverina",
      "Ticima", "Atalvaxes", "Alikakur", "Atalinit", "Karaja", "Anambulit",
      "Bakumati", "Kanikara", "Bulapari", "Kunjusti", "Hadrika", "Atipamar",
      "Balapara", "Apishantar", "Chendravi", "Kamatii", "Kamalavi", "Atimaniz",
      "Kamanandi", "Avindranit", "Amatinish", "Kanakanayi", "Ritahadri",
      "Anikabhit", "Bandhatri", "Bhavathandri", "Januruma", "Likola", "Jalusi",
      "Anatarit", "Alakraksayam", "Kalavara", "Ochenelmas", "Parthene",
      "Dorycle", "Ioxaris", "Astyochisas", "Dorisa", "Alesidos", "Henippane",
      "Asteressas", "Gonisa", "Dicube", "Ainanilton", "Menacasto",
      "Gliasisapphe", "Menikallo", "Atatheagas", "Tharmachima", "Anthethusas",
      "Carpersebe", "Pheribe", "Monippyle", "Hyrmykata", "Eroperis",
      "Ersenenias", "Pervince", "Antikemis", "Alladitos", "Anthousanen",
      "Perisa", "Amialeris", "Sumilka", "Nebaalu", "Sadaga", "Amupon",
      "Amzuudit", "Saditu", "Nabazzi", "Ninabi", "Bonaki", "Sunina", "Bera",
      "Duki", "Daresu", "Ammabiel", "Sheburu", "Mabiuma", "Hazzera", "Gilga",
      "Naburna", "Taaninga", "Lagduf", "Rishnag", "Mauhak", "Golurtz", "Rodush",
      "Lukil", "Buga", "Golug", "Agral", "Lurtzog", "Othrog", "Adbug",
      "Uglurtz", "Gashur", "Azolg", "Golfimb", "Grodush", "Snaga", "Buga",
      "Gashagr", "Bolga", "Mega", "Luga", "Ufthag", "Alcmeg", "Ancis", "Aerert",
      "Wulfa", "Genwy", "Enbeod", "Ethel", "Gyles", "Eadher", "Wolda", "Aewulf",
      "Chany", "Dreder", "Frichye", "Gyleon", "Ricio", "Alard", "Hamath",
      "Burhre", "Lasym", "Ealrer", "Kater", "Phely", "Burga", "Eongorn", "Eryn",
      "Odleld", "Hilda", "Ennet", "Inen", "Elgith", "Aenburh", "Jane",
      "Hrethe", "Hreda", "Marey", "Bridger", "Withiua", "Abet", "Wena", "Dohe",
      "Patun", "Shealey", "Cradun", "Asgate", "Batun", "Cawic", "Knedon",
      "Diwood", "Gamor", "Hlabrycg", "Lefield", "Shaithorp", "Heystone",
      "Wayminster", "Degrove", "Hatun", "Choford", "Heyley", "Heabluff",
      "Mileah", "Hamor", "Graham", "Weedale", "Fiheath", "Tacot", "Heaminster",
      "Thigate", "Orwood", "Hadun", "Bahyrst", "Catun", "Hwadun", "Mahyrst",
      "Castow", "Wawold", "Griford", "Raythorp", "Mewold", "Maburh", "Addleah",
      "Hrahyrst", "Theminster", "Tastow", "Cawold", "Fiwick", "Inhyrst",
      "Mewick", "Braypool", "Orin", "Irin", "Theli", "Bali", "Alim", "Furi",
      "Kurdu", "Umur", "Askar", "Grinarv", "Ukal", "Thrinarv", "Arvin", "Arud",
      "Sanzagh", "Amin", "Ukhul", "Grumin", "Ugmar", "Ziri", "Narvi", "Aghar",
      "Khakun", "Dainan", "Zuri", "Arkund", "Kunan", "Frinan", "Orim", "Gimli",
      "Urin", "Geda", "Gwali", "Gwoinan", "Gili", "Bizanar", "Kharbilgun",
      "Narukthund", "Nulukkhizd", "Kinbarakz", "Buzundin", "Gabizah", "Nala",
      "Kharakinb", "Umulbin", "Baragzir", "Zahazir", "Bizinbiz", "Nula",
      "Zinbilgil", "Nargunarg", "Gunala", "Nulbizinb", "Arar", "Gabaraz",
      "Naramunz", "Zaramunz", "Bilnulbund", "Badusharb", "Zarakinb",
      "Zaragzinb", "Undar", "Baramunz", "Diranthil", "Borufin", "Ophellas",
      "Alanduin", "Eglil", "Miroduil", "Galenwe", "Celenwe", "Ingon",
      "Dorophil", "Edhror", "Celore", "Motheli", "Enlor", "Oron",
      "Herendaer", "Enemmir", "Gildire", "Thrufinwe", "Eledran", "Rolallian",
      "Oislion", "Anfelir", "Ethilith", "Ethilorith", "Firedept", "Atal",
      "Belial", "Diablo", "Asmodan", "Pandore", "Baal", "Koivu", "Ornigold",
      "Mahyar", "Tanimlye", "Enderith", "Nerdalye", "Anith", "Inden",
      "Serianye", "Enwel", "Erdas", "Earien", "Nellothien", "Dolothlond",
      "Ellothlon", "Eimlarnin", "Enorlon", "Gondolond", "Gondolor", "Helline",
      "Mariathon", "Ellondon", "Negrasea", "Golgal", "Londone", "Edhenon",
      "Dorwine", "Hellothlion", "Formene", "Evrargond", "Lindoladr",
      "Vallonde", "Marastir", "Lorasea", "Athyes", "Jago", "Ferib", "Reward",
      "Andes", "Tharles", "Folco", "Bardo", "Hany", "Willes", "Wilhye",
      "Piersym", "Erin", "Thurey", "Engar", "Giles", "Pippin", "Palac", "Gery",
      "Yansham", "Cemoor", "Pewood", "Spemoor", "Beminster", "Autwich",
      "Sefalls", "Beybrook", "Hibrook", "Wywood", "Opton", "Baybluff", };

  /** List of all lines colors. */
  private final Color[] lineColors = new Color[NB_COLORS];
  /** Random object to generate random colors. */
  private final Random rand = new Random();

  /** Area id. */
  private int areaId;
  /** List of all available stations names. */
  private List<String> availableStationNames;
  /** String for tourist label. */
  public static final String AREA_TOURIST = "Tourist";
  /** String for student label. */
  public static final String AREA_STUDENT = "Student";
  /**
   * String for businessman label.
   */
  public static final String AREA_BUSINESSMAN = "Businessman";
  /**
   * String for worker label.
   */
  public static final String AREA_WORKER = "Worker";
  /**
   * String for child label.
   */
  public static final String AREA_CHILD = "Child";
  /**
   * String for retired label.
   */
  public static final String AREA_RETIRED = "Retired";
  /**
   * String for unemployed label.
   */
  public static final String AREA_UNEMPLOYED = "Unemployed";
  public static final String AREA_RESIDENTIAL = "Residential";
  public static final String AREA_COMMERCIAL = "Commercial";
  public static final String AREA_OFFICE = "Office";
  public static final String AREA_INDUSTRIAL = "Industrial";
  public static final String AREA_EDUCATIONAL = "Educational";
  public static final String AREA_TOURISTIC = "Touristic";
  public static final String AREA_LEISURE = "Leisure";

  //event attributes
  /** List of all event created. */
  private List<Event> eventList;
  /**
   * String for event starting station.
   */
  public static final String STATION_START = "stationStart";
  /** String for event ending station. */
  public static final String STATION_END = "stationEnd";
  /** String for event concerned station. */
  public static final String STATION_CONCERNED = "stationConcerned";
  /** String for starting station id. */
  private int stationStartId;
  /** String for ending station id. */
  private int stationEndId;
  /** String for concerned station id. */
  private int stationConcernedId;
  /** String for event selected type. */
  private String selectType;

  //map Attribute
  /** String for map type. */
  private String currentCity;

  /**
   * Private Data Constructor.
   */
  private Data() {
    super();
    this.availableStationNames = new LinkedList<>(Arrays.asList(
        STATIONS_NAMES));
    this.initLineColors();
    this.selectType = null;
    this.currentCity = "";
    areaId = 0;
    this.eventList = new ArrayList<>();
  }

  private void initLineColors() {
    for (int i = 0; i < NB_COLORS; i++) {
      float r = rand.nextFloat();
      float g = rand.nextFloat();
      float b = rand.nextFloat();
      lineColors[i] = new Color(r, g, b);
    }
  }

  // accessors

  /**
   * Set the observer.
   *
   * @param observerToSet the observer
   */
  public void setObserver(final Consumer<Object> observerToSet) {
    this.observer = observerToSet;
  }

  /**
   * get the list of available stations name.
   *
   * @return list of strings stations names
   */
  public List<String> getAvailableStationNames() {
    return availableStationNames;
  }

  /**
   * set the list of available stations name.
   *
   * @param availableStationNamesToSet name available for stations
   */
  public void setAvailableStationNames(
      final List<String> availableStationNamesToSet) {
    this.availableStationNames = availableStationNamesToSet;
  }

  /**
   * get the available lines color.
   *
   * @return Color[] lines color
   */
  public Color[] getLinesColors() {
    return lineColors;
  }


  /**
   * get the current city displayed by opentStreetMap.
   *
   * @return String currentCity
   */
  public String getCurrentCity() {
    return currentCity;
  }

  /**
   * get the current city displayed by opentStreetMap.
   *
   * @param currentCityToSet current city name displayed by opentStreetMap
   */
  public void setCurrentCity(final String currentCityToSet) {
    this.currentCity = currentCityToSet;
  }

  /**
   * Singleton creation.
   *
   * @return Data instance
   */
  public static Data getInstance() {
    if (instance == null) {
      instance = new Data();
    }
    return instance;
  }

  /**
   * get all stations names.
   *
   * @return String[] stationNames
   */
  public static String[] getStationsNames() {
    return STATIONS_NAMES;
  }

  /**
   * generate a new area Id.
   *
   * @return int newAreaId
   */
  public int getNewAreaId() {
    this.areaId += 1;
    return areaId;
  }

  /**
   * get event stationStart id.
   *
   * @return int StationStartId
   */
  public int getStationStartId() {
    return stationStartId;
  }

  /**
   * set event stationStart id.
   *
   * @param stationStartIdToSet event stationStart id
   */
  public void setStationStartId(final int stationStartIdToSet) {
    this.stationStartId = stationStartIdToSet;
    observer.accept("start");
  }

  /**
   * get event stationEnd id.
   *
   * @return int stationEndId
   */
  public int getStationEndId() {
    return stationEndId;
  }

  /**
   * set event stationStart id.
   *
   * @param stationEndIdToSet event stationEnd id
   */
  public void setStationEndId(final int stationEndIdToSet) {
    this.stationEndId = stationEndIdToSet;
    observer.accept("end");
  }

  /**
   * get event stationConcerned id.
   *
   * @return int stationConcernedId
   */
  public int getStationConcernedId() {
    return stationConcernedId;
  }

  /**
   * set event stationConcerned id.
   *
   * @param stationConcernedIdToSet event stationConcerned id
   */
  public void setStationConcernedId(final int stationConcernedIdToSet) {
    this.stationConcernedId = stationConcernedIdToSet;
    observer.accept("concerned");
  }

  /**
   * get Event selected type.
   *
   * @return String selectedType
   */
  public String getSelectType() {
    return selectType;
  }

  /**
   * set Event selected type.
   *
   * @param selectTypeToSet eventSelected type
   */
  public void setSelectType(final String selectTypeToSet) {
    this.selectType = selectTypeToSet;

  }

  /**
   * get the list of all event created.
   *
   * @return List of events eventList
   */
  public List<Event> getEventList() {
    return eventList;
  }

  /**
   * set the list of all event created.
   *
   * @param eventListToSet list of events
   */
  public void setEventList(final List<Event> eventListToSet) {
    this.eventList = eventListToSet;
  }
}
