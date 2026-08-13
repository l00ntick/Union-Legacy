/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import static haven.Utils.getprop;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.Map;
import java.util.Iterator;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ender.GoogleTranslator;

import ender.CurioInfo;
import ender.HLInfo;

public class Config {
	public static byte[] authck;
	public static String authuser;
	public static String authserv;
	public static String defserv;
	public static URL resurl, mapurl;
	public static boolean fullscreen;
	public static boolean dbtext;
	public static boolean bounddb;
	public static boolean profile;
	public static boolean nolocalres;
	public static String resdir;
	public static boolean nopreload;
	public static String loadwaited, allused;
	public static boolean xray;
	public static boolean hide;
	public static boolean hide_all;
	public static boolean grid;
	public static boolean timestamp;
	public static boolean new_chat;
	public static boolean showDebug = false;
	public static boolean serverGrid = false;
	public static boolean use_smileys;
	public static boolean zoom;
	public static boolean noborders;
	public static boolean new_minimap;
	public static boolean new_menugrid;
	public static boolean simple_plants = false;
	public static boolean aimacrazyman = false;
	public static Set<String> hideObjectList;
	public static Set<String> highlightItemList;
	public static HashMap<Pattern, String> smileys;
	public static boolean nightvision;
	public static String currentCharName;
	public static Properties options, window_props, sounds;
	public static int sfxVol;
	public static int musicVol;
	public static int alertVol;
	public static boolean soundMemo = false;
	public static boolean isMusicOn = false;
	public static boolean isSoundOn = false;
	public static boolean showRadius = false;
	public static boolean showHidden = false;
	public static boolean showBeast = false;
	public static boolean showDirection;
	public static boolean showNames;
	public static boolean showOtherNames;
	public static boolean objectHighlighting; // Kerri: mouse obj highlighting
	public static boolean onlineNotifier; // Kerri: online/offline notifier
	public static boolean fastFlowerAnim = true; // Kerri: made it always to be true
	public static boolean mgridDebug = false; // Kerri: menu grid message debug
	public static boolean objectBlink; // Kerri: alt+LMB
	public static boolean disableMapSaving = true; // Kerri: minimap saving
	public static boolean useSimpleMap; // Kerri: tile based minimap
	public static boolean simplemap = false;
	public static boolean showMyAvatar = true; // Kerri: show avatar
	public static boolean toggleCA; //Kerri: criminal acts
	public static boolean toggleTR; //Kerri: tracking
	public static boolean toggleCL; //Kerri: claims
	public static boolean tileAA; //Kerri: tile anti alliasing
	public static boolean showDayTime;
	public static boolean sshot_compress;
	public static boolean sshot_noui;
	public static boolean sshot_nonames;
	public static boolean newclaim;
	public static boolean showq;
	public static boolean showpath;
	public static boolean showpathAll;
	public static boolean showFlavors;
	public static boolean drawIcons;
	public static Color hideColor = new Color(255, 0, 0, 128);
	public static int hideAlpha;
	
	public static boolean pathfinder = false;
	public static boolean pathfinderLine = false;
	public static boolean pathfinderRectangles = false;
	public static boolean autoAggro = false;
	public static boolean flaskMeters = false;
	public static boolean dontScaleMMIcons = true;
	public static boolean customNeg = false;
	public static boolean flaskFillOnly = false;
	public static int flaskNum = 113;
	public static int flaskFill = 1;
	public static boolean runFlaskSuppression = false;
	public static boolean debug = false;
	public static boolean forcemod = false;
	public static boolean showPclaim = false;
	public static boolean showVclaim = false;
	public static boolean overview = false;
	public static boolean hostileOverviewFilter = false;
	public static boolean autohearth;
    public static boolean hearthunknown;
    public static boolean hearthred;
	public static boolean villagePort = false;
	public static boolean fepbar = false;
	
	public static boolean yellowHalo = false;
	public static boolean objectTrans = false;
	public static boolean landMemo = false;
	public static boolean minerSafety = false;
	public static boolean miningDrop = false;
	public static boolean chatBoxInteraction = false;
	public static boolean enableSpaceHearth = false;
	public static boolean enableLiftClick = false;
	public static boolean singleAttack = false;
	public static boolean targetSwapDrink = false;
	public static boolean disableMouseActions = false;
	public static boolean highlightSkills;
	
	//FUKKEN SCENTS!!!
	
	public static boolean hideTressp;
	public static boolean hideTheft;
	public static boolean hideAsslt;
	public static boolean hideBatt;
	public static boolean hideVand;
	public static boolean hideMurd;

	public static boolean assign_to_tile = false;

	public static boolean show_minimap_profits = true;
	public static boolean show_minimap_players = true;
	public static boolean show_minimap_radius = true;
	public static boolean show_gob_health = true;
	public static boolean no_scent_smoke = true;
	public static boolean render_enable = true;
	public static boolean use_wasd = true;
	//public static ArrayList<Pair<String, Color>> minimap_highlights = new ArrayList<Pair<String, Color>>();
	//public static HashMap<String, HashMap<String, Float>> FEPMap = new HashMap<String, HashMap<String, Float>>();
	public static Map<String, Map<String, Float>> FEPMap = new HashMap<String, Map<String, Float>>();
	//public static HashMap<String, CuriosityStat> CurioMap = new HashMap<String, CuriosityStat>();
	public static HashMap<String, CurioInfo> curios = new HashMap<String, CurioInfo>();
	public static HashMap<String, Color> FEPColorMap = new HashMap<String, Color>();
	public static Map<String, String> beasts = new HashMap<String, String>();
	public static Map<String, HLInfo> hlcfg = new HashMap<String, HLInfo>();
    public static Map<String, Set<String>> hlgroups = new HashMap<String, Set<String>>();
    public static Map<String, Set<String>> hlcgroups = new HashMap<String, Set<String>>();

	public static boolean map_show_curiosities = true;
	public static Map<String, Boolean> confSounds = new HashMap<String, Boolean>();
	
	public static Map<Integer, String> resMap = new HashMap<Integer, String>(); // res id project

	static {
		FEPColorMap.put("STR", new Color(100, 100, 100));
		FEPColorMap.put("AGI", new Color(100, 100, 100));
		FEPColorMap.put("INT", new Color(100, 100, 100));
		FEPColorMap.put("CON", new Color(100, 100, 100));
		FEPColorMap.put("PER", new Color(100, 100, 100));
		FEPColorMap.put("CHA", new Color(100, 100, 100));
		FEPColorMap.put("DEX", new Color(100, 100, 100));
		FEPColorMap.put("PSY", new Color(100, 100, 100));
	}

	static {
		try {
			String p;
			if ((p = getprop("haven.authck", null)) != null)
				authck = Utils.hex2byte(p);
			authuser = getprop("haven.authuser", null);
			authserv = getprop("haven.authserv", null);
			defserv = getprop("haven.defserv", null);
			if (!(p = getprop("haven.resurl",
                    "http://127.0.0.1/res/")).equals(""))
                resurl = new URL(p);
            if (!(p = getprop("haven.mapurl",
                    "http://127.0.0.1/mm/")).equals(""))
				mapurl = new URL(p);
			fullscreen = getprop("haven.fullscreen", "off").equals("on");
			loadwaited = getprop("haven.loadwaited", null);
			allused = getprop("haven.allused", null);
			dbtext = getprop("haven.dbtext", "off").equals("on");
			bounddb = getprop("haven.bounddb", "off").equals("on");
			profile = getprop("haven.profile", "off").equals("on");
			nolocalres = getprop("haven.nolocalres", "").equals("yesimsure");
			resdir = getprop("haven.resdir", null);
			nopreload = getprop("haven.nopreload", "no").equals("yes");
			xray = false;
			hide = true;
			grid = false;
			timestamp = false;
			nightvision = true;
			zoom = false;
			new_minimap = true;
			GoogleTranslator.lang = "en";
			GoogleTranslator.turnedon = false;
			currentCharName = "";
			options = new Properties();
			window_props = new Properties();
			sounds = new Properties();
			hideObjectList = Collections.synchronizedSet(new HashSet<String>());
			highlightItemList = Collections.synchronizedSet(new HashSet<String>());
			loadOptions();
			loadWindowOptions();
			loadSmileys();
			loadFEP();
			//loadCurio();
			loadCurios();
			loadSounds();
			loadHighlight();
			loadCurrentHighlight();
			loadBeasts();
			loadRes(); // res id project
		} catch (java.net.MalformedURLException e) {
			throw (new RuntimeException(e));
		}
	}
	
	private static void loadRes() {  // res id project
		File f = new File("config/res.conf");
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (Exception e) {
				
			}
		} else {
			try {
				List<String> list = Files.readAllLines(Paths.get("config/res.conf"));
				for (String l : list) {
					String[] arr = l.split(" ");
					if (arr.length > 2) {
						try {
							Integer id = Integer.parseInt(arr[0]);
							String rest = "";
							for (int i = 1; i < arr.length; i++) {
								rest += (arr[i] + " ");
							}
							resMap.put(id, rest.trim());
						} catch (Exception e) {
							
						}
					}
				}
			} catch (Exception e) {
				
			}
		}
	}
	
	public static void addRes(int id, int ver, String name) {  // res id project
		if (!resMap.containsKey(id)) {
			String rest = (ver + " " + name);
			resMap.put(id, rest);
			rest = id + " " + rest + "\n";
			try {
				Files.write(Paths.get("config/res.conf"), rest.getBytes(), StandardOpenOption.APPEND);
			} catch (Exception e) {
				
			}
		}
	}
	
	private static void loadCurios() {
	try {
	    FileInputStream fstream;
	    fstream = new FileInputStream("config/curio.conf");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
	    String strLine;
	    while ((strLine = br.readLine()) != null)   {
		CurioInfo curio = new CurioInfo();
		String [] tmp = strLine.split(":");
		String name = tmp[0].toLowerCase();
		curio.LP = Integer.parseInt(tmp[1]);
		curio.time = (int) (Float.parseFloat(tmp[2]));
		curio.weight = Integer.parseInt(tmp[3]);
		curios.put(name, curio);
	    }
	    br.close();
	    fstream.close();
	} catch (Exception e) {}
    }
	
	private static void loadFEP() {
	try {
	    FileInputStream fstream;
	    fstream = new FileInputStream("config/fep.conf");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
	    String strLine;
	    while ((strLine = br.readLine()) != null)   {
		Map<String, Float> fep = new HashMap<String, Float>();
		String [] tmp = strLine.split("=");
		String name;
		name = tmp[0].toLowerCase();
		if(name.charAt(0)=='@'){
		    name = name.substring(1);
		    fep.put("isItem",(float) 1.0);
		}
		tmp = tmp[1].split(" ");
		for(String itm : tmp){
		    String tmp2[] = itm.split(":");
		    fep.put(tmp2[0], Float.valueOf(tmp2[1]).floatValue());
		}
		FEPMap.put(name, fep);
	    }
	    br.close();
	    fstream.close();
	} catch (FileNotFoundException e) {
	} catch (IOException e) {
	}
	}

	public static String mksmiley(String str) {
		synchronized (smileys) {
			for (Pattern p : Config.smileys.keySet()) {
				String res = Config.smileys.get(p);
				str = p.matcher(str).replaceAll(res);
			}
		}
		return str;
	}

	private static void usage(PrintStream out) {
		out.println("usage: haven.jar [-hdPf] [-u USER] [-C HEXCOOKIE] [-r RESDIR] [-U RESURL] [-A AUTHSERV] [SERVER]");
	}

	public static void cmdline(String[] args) {
		PosixArgs opt = PosixArgs.getopt(args, "hdPU:fr:A:u:C:");
		if (opt == null) {
			usage(System.err);
			System.exit(1);
		}
		boolean resurlset = false;
		for (char c : opt.parsed()) {
			switch (c) {
			case 'h':
				usage(System.out);
				System.exit(0);
				break;
			case 'd':
				dbtext = true;
				break;
			case 'P':
				profile = true;
				break;
			case 'f':
				fullscreen = true;
				break;
			case 'r':
				resdir = opt.arg;
				break;
			case 'A':
				authserv = opt.arg;
				break;
			case 'U':
				try {
					resurl = new URL(opt.arg);
					resurlset = true;
				} catch (java.net.MalformedURLException e) {
					System.err.println(e);
					System.exit(1);
				}
				break;
			case 'u':
				authuser = opt.arg;
				break;
			case 'C':
				authck = Utils.hex2byte(opt.arg);
				break;
			}
		}
		if (opt.rest.length > 0) {
			defserv = opt.rest[0];
			try {
				if (!resurlset)
					resurl = new URL("http://" + defserv + "/res/");
				mapurl = new URL("http://" + defserv + "/mm/");
			} catch (java.net.MalformedURLException e) {
				System.err.println(e);
				System.exit(1);
			}
		}
	}

	public static double getSFXVolume() {
		return (double) sfxVol / 100;
	}

	public static int getMusicVolume() {
		return isMusicOn ? musicVol : 0;
	}

	private static void loadSmileys() {
		smileys = new HashMap<Pattern, String>();
		try {
			FileInputStream fstream;
			fstream = new FileInputStream("config/smileys.conf");
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] tmp = strLine.split("\t");
				String smile, res;
				smile = tmp[0];
				res = "\\$img\\[smiley\\/" + tmp[1] + "\\]";
				smileys.put(
						Pattern.compile(smile, Pattern.CASE_INSENSITIVE
								| Pattern.LITERAL), res);
			}
			br.close();
			in.close();
			fstream.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

	}

	private static void loadWindowOptions() {
		File inputFile = new File("config/windows.conf");
		if (!inputFile.exists()) {
			return;
		}
		try {
			window_props.load(new FileInputStream(inputFile));
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void loadSounds() {
        File inputFile = new File("config/sound.conf");
        if (!inputFile.exists()) {
			try {
				inputFile.createNewFile();
			} catch (IOException e) {
				return;
			}
        }
		
		try {
            sounds.load(new FileInputStream("config/sound.conf"));
        }
        catch (IOException e) {
            System.out.println(e);
        }
		
		confSounds.put("white", sounds.getProperty("white", "false").equals("true") );
		confSounds.put("red", sounds.getProperty("red", "false").equals("true") );
		confSounds.put("troll", sounds.getProperty("troll", "false").equals("true") );
		confSounds.put("bear", sounds.getProperty("bear", "false").equals("true") );
		confSounds.put("bell", sounds.getProperty("bell", "false").equals("true") );
		confSounds.put("flotsam", sounds.getProperty("flotsam", "false").equals("true") );
		confSounds.put("pearl", sounds.getProperty("pearl", "false").equals("true") );
		confSounds.put("aggro", sounds.getProperty("aggro", "false").equals("true") );
		confSounds.put("death", sounds.getProperty("death", "false").equals("true") );
		confSounds.put("ram", sounds.getProperty("ram", "false").equals("true") );
		confSounds.put("timer", sounds.getProperty("timer", "false").equals("true") );
    }
	
    public static void saveSounds() {
		sounds.setProperty("white", confSounds.get("white").toString() );
        sounds.setProperty("red", confSounds.get("red").toString() );
		sounds.setProperty("troll", confSounds.get("troll").toString() );
		sounds.setProperty("bear", confSounds.get("bear").toString() );
		sounds.setProperty("bell", confSounds.get("bell").toString() );
		sounds.setProperty("flotsam", confSounds.get("flotsam").toString() );
		sounds.setProperty("pearl", confSounds.get("pearl").toString() );
		sounds.setProperty("aggro", confSounds.get("aggro").toString() );
		sounds.setProperty("death", confSounds.get("death").toString() );
		sounds.setProperty("ram", confSounds.get("ram").toString() );
		sounds.setProperty("timer", confSounds.get("timer").toString() );
		
        try {
            sounds.store(new FileOutputStream("config/sound.conf"), "Custom sound options");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

	private static void loadOptions() {
		File inputFile = new File("config/haven.conf");
		if (!inputFile.exists()) {
			return;
		}
		try {
			options.load(new FileInputStream("config/haven.conf"));
		} catch (IOException e) {
			System.out.println(e);
		}
		String hideObjects = options.getProperty("hideObjects", "");
		String hideHighlight = options.getProperty("hcolor", "255,0,0,128");
//		System.out.println(hideHighlight);
		String clist[] = hideHighlight.split(",");
		if(clist.length != 4) {
			clist[0] = "255"; clist[1] = "0"; clist[2] = "0"; clist[0] = "128";
		}
//		String salpha = hideHighlight.substring(8);
//		hideAlpha = Integer.parseInt(salpha, 16);
//		hideHighlight = hideHighlight.substring(0, 8);
//		hideColor = Color.decode(hideHighlight);
		hideColor = new Color(Integer.valueOf(clist[0]), Integer.valueOf(clist[1]),
				Integer.valueOf(clist[2]), Integer.valueOf(clist[3]));
		GoogleTranslator.apikey = options.getProperty("GoogleAPIKey",
				"AIzaSyCuo-ukzI_J5n-inniu2U7729ZfadP16_0");
		zoom = options.getProperty("zoom", "false").equals("true");
		noborders = options.getProperty("noborders", "false").equals("true");
		new_minimap = options.getProperty("new_minimap", "true").equals("true");
		new_chat = options.getProperty("new_chat", "true").equals("true");
		new_menugrid = options.getProperty("new_menugrid", "true").equals("true");
		use_smileys = options.getProperty("use_smileys", "true").equals("true");
		isMusicOn = options.getProperty("music_on", "true").equals("true");
		isSoundOn = options.getProperty("sound_on", "true").equals("true");
		showDirection = options.getProperty("show_direction", "true").equals(
				"true");
		showNames = options.getProperty("showNames", "true").equals("true");
		showOtherNames = options.getProperty("showOtherNames", "false").equals(
				"true");
		showBeast = options.getProperty("showBeast", "false").equals("true");
		showRadius = options.getProperty("showRadius", "false").equals("true");
		showHidden = options.getProperty("showHidden", "false").equals("true");
		simple_plants = options.getProperty("simple_plants", "false").equals(
				"true");
		objectHighlighting = options.getProperty("objMouseHLight", "false")
				.equals("true"); // Kerri
		onlineNotifier = options.getProperty("onlineNotifier", "false").equals(
				"true"); // Kerri
		showDayTime = options.getProperty("showDayTime", "false")
				.equals("true"); // Kerri
		objectBlink = options.getProperty("objectBlink", "false")
				.equals("true"); // Kerri
		disableMapSaving = options.getProperty("disableMapSaving", "false").equals("true"); // Kerri
		showMyAvatar = options.getProperty("showMyAvatar", "false").equals("true"); // Kerri
		sshot_compress = options.getProperty("sshot_compress", "false").equals(
				"true");
		sshot_noui = options.getProperty("sshot_noui", "false").equals("true");
		sshot_nonames = options.getProperty("sshot_nonames", "false").equals(
				"true");
		newclaim = options.getProperty("newclaim", "true").equals("true");
		showq = options.getProperty("showq", "true").equals("true");
		showpath = options.getProperty("showpath", "false").equals("true");
		showFlavors = options.getProperty("showFlavors", "false").equals("true");
		showpathAll = options.getProperty("showpathAll", "false").equals("true");
		tileAA = options.getProperty("tileAA", "false").equals("true"); // Kerri
		pathfinder = options.getProperty("pathfinder", "false").equals("true"); // new
		pathfinderLine = options.getProperty("pathfinderLine", "false").equals("true"); // new
		pathfinderRectangles = options.getProperty("pathfinderRectangles", "false").equals("true"); // new
		autoAggro = options.getProperty("autoAggro", "false").equals("true"); // new
		customNeg = options.getProperty("customNeg", "false").equals("true"); // new
		flaskFillOnly = options.getProperty("flaskFillOnly", "false").equals("true"); // new
		debug = options.getProperty("debug", "false").equals("true"); // new
		flaskNum = Integer.parseInt(options.getProperty("flaskNum", "100"));
		flaskFill = Integer.parseInt(options.getProperty("flaskFill", "100"));
		flaskMeters = options.getProperty("flaskMeters", "false").equals("true"); // new
		dontScaleMMIcons = options.getProperty("dontScaleMMIcons", "false").equals("true");
		showPclaim = options.getProperty("showPclaim", "false").equals("true"); // new
		showVclaim = options.getProperty("showVclaim", "false").equals("true"); // new
		overview = options.getProperty("overview", "false").equals("true"); // new
		hostileOverviewFilter = options.getProperty("hostileOverviewFilter", "false").equals("true"); // new
		villagePort = options.getProperty("villagePort", "false").equals("true"); // new
		fepbar = options.getProperty("FoodMeterWdg", "false").equals("true");
		autohearth = options.getProperty("autohearth", "false").equals("true");
        hearthunknown = options.getProperty("hearthunknown", "false").equals("true");
        hearthred = options.getProperty("hearthred", "false").equals("true");
		
		yellowHalo = options.getProperty("yellowHalo", "false").equals("true"); // new
		objectTrans = options.getProperty("objectTrans", "false").equals("true"); // new
		landMemo = options.getProperty("landMemo", "false").equals("true"); // new
		minerSafety = options.getProperty("minerSafety", "false").equals("true"); // new
		miningDrop = options.getProperty("miningDrop", "false").equals("true"); // new
		chatBoxInteraction = options.getProperty("chatBoxInteraction", "false").equals("true"); // new
		enableSpaceHearth = options.getProperty("enableSpaceHearth", "false").equals("true"); // new
		enableLiftClick = options.getProperty("enableLiftClick", "false").equals("true"); // new
		singleAttack = options.getProperty("singleAttack", "false").equals("true"); // new
		targetSwapDrink = options.getProperty("targetSwapDrink", "false").equals("true"); // new
		disableMouseActions = options.getProperty("disableMouseActions", "false").equals("true"); // new
		highlightSkills = options.getProperty("highlightSkills", "false").equals("true");
		
		drawIcons = options.getProperty("drawIcons", "false").equals("true"); // Kerri
		// Kerri
		hideTressp = options.getProperty("hideTressp", "false").equals("true");
		hideTheft = options.getProperty("hideTheft", "false").equals("true");
		hideAsslt = options.getProperty("hideAsslt", "false").equals("true");
		hideBatt = options.getProperty("hideBatt", "false").equals("true");
		hideVand = options.getProperty("hideVand", "false").equals("true");
		hideMurd = options.getProperty("hideMurd", "false").equals("true");
		//
		show_minimap_profits = options.getProperty("show_minimap_profits",
				"false").equals("true");
		toggleCA = options.getProperty("toggleCA", "false").equals("true");
		toggleTR = options.getProperty("toggleTR", "false").equals("true");
		toggleCL = options.getProperty("toggleCL", "false").equals("true");
		show_gob_health = options.getProperty("show_gob_health", "false")
				.equals("true");
		show_minimap_players = options.getProperty("show_minimap_players",
				"false").equals("true");
		show_minimap_radius = options.getProperty("show_minimap_radius",
				"false").equals("true");
		sfxVol = Integer.parseInt(options.getProperty("sfx_vol", "100"));
		musicVol = Integer.parseInt(options.getProperty("music_vol", "100"));
		alertVol = Integer.parseInt(options.getProperty("alert_vol", "100"));
		soundMemo = options.getProperty("soundMemo", "false").equals("true"); // new
		hideObjectList.clear();
		if (!hideObjects.isEmpty()) {
			for (String objectName : hideObjects.split(",")) {
				if (!objectName.isEmpty()) {
					hideObjectList.add(objectName);
				}
			}
		}
		String highlightObjects = options.getProperty("highlightObjects", "");
		if (!highlightObjects.isEmpty()) {
            for (String objectName : highlightObjects.split(",")) {
                if (!objectName.isEmpty()) {
                    highlightItemList.add(objectName);
                }
            }
        }
		Resource.checkhide();
		timestamp = options.getProperty("timestamp", "false").equals("true");

		/*try {
			INIFile ifile = new INIFile("config/haven.ini");
			minimap_highlights = ifile.getSectionColors("HIGHLIGHT", "");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
	}

	public static synchronized void setWindowOpt(String key, String value) {
		synchronized (window_props) {
			String prev_val = window_props.getProperty(key);
			if ((prev_val != null) && prev_val.equals(value))
				return;
			window_props.setProperty(key, value);
		}
		saveWindowOpt();
	}

	public static synchronized void setWindowOpt(String key, Boolean value) {
		setWindowOpt(key, value ? "true" : "false");
	}

	public static void saveWindowOpt() {
		synchronized (window_props) {
			try {
				window_props.store(new FileOutputStream("config/windows.conf"),
						"Window config options");
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	public static void addhide(String str) {
		hideObjectList.add(str);
		Resource.checkhide();
	}

	public static void remhide(String str) {
		hideObjectList.remove(str);
		Resource.checkhide();
	}

	public static void saveOptions() {
		String hideObjects = "";
		for (String objectName : hideObjectList) {
			hideObjects += objectName + ",";
		}
		String highlightObjects = "";
        for (String objectName : highlightItemList) {
            highlightObjects += objectName+",";
        }
		/*String scolor, mainc, alphac;
		mainc = Integer.toHexString(hideColor.getRGB()).substring(2);
		alphac = Integer.toHexString(hideColor.getRGB()).substring(0, 2);
		scolor = "0x" + mainc + alphac;*/
		String scolor = hideColor.getRed()+","+hideColor.getGreen()+","+hideColor.getBlue()+","+hideColor.getAlpha();
		options.setProperty("hcolor", scolor);
		options.setProperty("hideObjects", hideObjects);
		options.setProperty("highlightObjects", highlightObjects);
		options.setProperty("GoogleAPIKey", GoogleTranslator.apikey);
		options.setProperty("timestamp", (timestamp) ? "true" : "false");
		options.setProperty("zoom", zoom ? "true" : "false");
		options.setProperty("noborders", noborders ? "true" : "false");
		options.setProperty("new_minimap", new_minimap ? "true" : "false");
		options.setProperty("new_chat", new_chat ? "true" : "false");
		options.setProperty("new_menugrid", new_menugrid ? "true" : "false");
		options.setProperty("use_smileys", use_smileys ? "true" : "false");
		options.setProperty("sfx_vol", String.valueOf(sfxVol));
		options.setProperty("music_vol", String.valueOf(musicVol));
		options.setProperty("alert_vol", String.valueOf(alertVol));
		options.setProperty("soundMemo", soundMemo?"true":"false"); // new
		options.setProperty("music_on", isMusicOn ? "true" : "false");
		options.setProperty("sound_on", isSoundOn ? "true" : "false");
		options.setProperty("show_direction", showDirection ? "true" : "false");
		options.setProperty("showNames", showNames ? "true" : "false");
		options.setProperty("showOtherNames", showOtherNames ? "true" : "false");
		options.setProperty("showBeast", showBeast ? "true" : "false");
		options.setProperty("showRadius", showRadius ? "true" : "false");
		options.setProperty("showHidden", showHidden ? "true" : "false");
		options.setProperty("simple_plants", simple_plants ? "true" : "false");
		options.setProperty("objMouseHLight", objectHighlighting ? "true"
				: "false"); // Kerri
		options.setProperty("onlineNotifier", onlineNotifier ? "true" : "false"); // Kerri
		options.setProperty("disableMapSaving", disableMapSaving ? "true" : "false"); // Kerri
		options.setProperty("showMyAvatar", showMyAvatar ? "true" : "false"); // Kerri
		options.setProperty("showDayTime", showDayTime ? "true" : "false"); // Kerri
		options.setProperty("objectBlink", objectBlink ? "true" : "false"); // Kerri
		options.setProperty("toggleCA", toggleCA ? "true" : "false"); // Kerri
		options.setProperty("toggleTR", toggleTR ? "true" : "false"); // Kerri
		options.setProperty("toggleCL", toggleCL ? "true" : "false"); // Kerri
		options.setProperty("tileAA", tileAA ? "true" : "false"); // Kerri
		options.setProperty("pathfinder", pathfinder?"true":"false"); // new
		options.setProperty("pathfinderLine", pathfinderLine?"true":"false"); // new
		options.setProperty("pathfinderRectangles", pathfinderRectangles?"true":"false"); // new
		options.setProperty("autoAggro", autoAggro?"true":"false"); // new
		options.setProperty("customNeg", customNeg?"true":"false"); // new
		options.setProperty("flaskFillOnly", flaskFillOnly?"true":"false"); // new
		options.setProperty("debug", debug?"true":"false"); // new
		options.setProperty("flaskNum", String.valueOf(flaskNum)); // new
		options.setProperty("flaskFill", String.valueOf(flaskFill)); // new
		options.setProperty("flaskMeters", flaskMeters?"true":"false"); // new
		options.setProperty("dontScaleMMIcons", dontScaleMMIcons?"true":"false");
		options.setProperty("showPclaim", showPclaim?"true":"false"); // new
		options.setProperty("showVclaim", showVclaim?"true":"false"); // new
		options.setProperty("overview", overview?"true":"false"); // new
		options.setProperty("hostileOverviewFilter", hostileOverviewFilter?"true":"false"); // new
		options.setProperty("autohearth", autohearth?"true":"false");
		options.setProperty("villagePort", villagePort?"true":"false"); // new
		options.setProperty("FoodMeterWdg", fepbar ? "true" : "false");
        options.setProperty("hearthunknown", hearthunknown?"true":"false");
        options.setProperty("hearthred", hearthred?"true":"false");
		
		options.setProperty("yellowHalo", yellowHalo?"true":"false"); // new
		options.setProperty("objectTrans", objectTrans?"true":"false"); // new
		options.setProperty("landMemo", landMemo?"true":"false"); // new
		options.setProperty("minerSafety", minerSafety?"true":"false"); // new
		options.setProperty("miningDrop", miningDrop?"true":"false"); // new
		options.setProperty("chatBoxInteraction", chatBoxInteraction?"true":"false"); // new
		options.setProperty("enableSpaceHearth", enableSpaceHearth?"true":"false"); // new
		options.setProperty("enableLiftClick", enableLiftClick?"true":"false"); // new
		options.setProperty("singleAttack", singleAttack?"true":"false"); // new
		options.setProperty("targetSwapDrink", targetSwapDrink?"true":"false"); // new
		options.setProperty("disableMouseActions", disableMouseActions?"true":"false"); // new
		options.setProperty("highlightSkills", highlightSkills?"true":"false");
		
		options.setProperty("drawIcons", drawIcons ? "true" : "false"); // Kerri
		options.setProperty("sshot_compress", sshot_compress ? "true" : "false");
		options.setProperty("sshot_noui", sshot_noui ? "true" : "false");
		options.setProperty("sshot_nonames", sshot_nonames ? "true" : "false");
		options.setProperty("newclaim", newclaim ? "true" : "false");
		options.setProperty("showq", showq ? "true" : "false");
		options.setProperty("showpath", showpath ? "true" : "false");
		options.setProperty("showFlavors", showFlavors ? "true" : "false");
		options.setProperty("showpathAll", showpathAll ? "true" : "false");
		//
		options.setProperty("hideTressp", hideTressp ? "true" : "false");
		options.setProperty("hideTheft", hideTheft ? "true" : "false");
		options.setProperty("hideAsslt", hideAsslt ? "true" : "false");
		options.setProperty("hideBatt", hideBatt ? "true" : "false");
		options.setProperty("hideVand", hideVand ? "true" : "false");
		options.setProperty("hideMurd", hideMurd ? "true" : "false");
		// Kerri
		options.setProperty("show_minimap_profits",
				show_minimap_profits ? "true" : "false");
		options.setProperty("show_gob_health", show_gob_health ? "true"
				: "false");
		options.setProperty("show_minimap_players",
				show_minimap_players ? "true" : "false");
		options.setProperty("show_minimap_radius", show_minimap_radius ? "true"
				: "false");

		try {
			options.store(new FileOutputStream("config/haven.conf"),
					"Custom config options");
		} catch (IOException e) {
			System.out.println(e);
		}
	}
	
	private static void loadHighlight() {
	try {
	    FileInputStream fstream;
	    fstream = new FileInputStream("config/highlight.conf");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
	    String data = "";
	    String strLine;
	    while ((strLine = br.readLine()) != null)   {
		data += strLine;
	    }
	    try {
		JSONObject cfg = new JSONObject(data);
		Iterator<String> keys = cfg.keys();
		while(keys.hasNext()){
		    String key = keys.next();
		    Set<String> group = new HashSet<String>();
		    Set<String> group2 = new HashSet<String>();
		    hlgroups.put(key, group);
		    hlcgroups.put(key, group2);
		    JSONArray arr = cfg.getJSONArray(key);
		    for(int i=0; i<arr.length(); i++){
			JSONObject o = arr.getJSONObject(i);
			String name = o.getString("name");
			String icon = null;
			if(!o.isNull("icon")){
			    icon = o.getString("icon");
			}
			HLInfo inf = new HLInfo(name, icon);
			if(!o.isNull("color")){
			    inf.setColor(new Color(Integer.parseInt(o.getString("color"), 16)));
			}
			hlcfg.put(name, inf);
			group.add(name);
			group2.add(name);
		    }
		}
		
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	    br.close();
	    fstream.close();
	} catch (FileNotFoundException e) {
	} catch (IOException e) {
	}
    }
    
    private static void loadCurrentHighlight() {
	try {
	    FileInputStream fstream;
	    fstream = new FileInputStream("config/highlight.cfg");
	    BufferedReader br = new BufferedReader(new InputStreamReader(fstream, "UTF-8"));
	    String data = "";
	    String strLine;
	    while ((strLine = br.readLine()) != null)   {
		data += strLine;
	    }
	    try {
		JSONObject cfg = new JSONObject(data);
		Iterator<String> keys = cfg.keys();
		while(keys.hasNext()){
		    String key = keys.next();
		    Set<String> group = new HashSet<String>();
		    hlcgroups.put(key, group);
		    JSONArray arr = cfg.getJSONArray(key);
		    for(int i=0; i<arr.length(); i++){
			String name = arr.getString(i);
			group.add(name);
		    }
		}
		
	    } catch (JSONException e) {
		e.printStackTrace();
	    }
	    br.close();
	    fstream.close();
	} catch (FileNotFoundException e) {
	    //e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }
	
	public static void saveCurrentHighlights(){
	try {
	    JSONObject cfg = new JSONObject();
	    for(String group : hlcgroups.keySet()){
		cfg.put(group, hlcgroups.get(group));
	    }
	    try {
		FileWriter fw = new FileWriter("config/highlight.cfg");
		cfg.write(fw);
		fw.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	} catch (JSONException e) {
	    e.printStackTrace();
	}
    }
    
    private static void loadBeasts() {
	//bear
	String pat = "kritter/bear";
	HLInfo inf = new HLInfo(pat, "mmap/bear");
	Color col = new Color(0xff797c);
	inf.setColor(col);
	beasts.put(pat, "Bear");
	inf.show = true; // new
	hlcfg.put(pat, inf);
	//boar
	pat = "kritter/boar";
	inf = new HLInfo(pat, "mmap/boar");
	inf.setColor(col);
	beasts.put(pat, "Boar");
	inf.show = true; // new
	hlcfg.put(pat, inf);
	//deer
	pat = "kritter/deer";
	inf = new HLInfo(pat, "mmap/deer");
	inf.setColor(new Color(0x7BAF8E));
	beasts.put(pat, "Deer");
	inf.show = false; // new
	hlcfg.put(pat, inf);
	//fox
	pat = "kritter/fox";
	inf = new HLInfo(pat, "mmap/fox");
	inf.setColor(new Color(0xAF8E5B));
	beasts.put(pat, "Fox");
	inf.show = false;
	hlcfg.put(pat, inf);
	//rabbit
	pat = "kritter/hare";
	inf = new HLInfo(pat, "mmap/rabbit");
	inf.setColor(new Color(0x8E8E8E));
	beasts.put(pat, "Rabbit");
	inf.show = false; // new
	hlcfg.put(pat, inf);
	
	
	//aurochs ////// new
	pat = "kritter/aurochs";
	inf = new HLInfo(pat, "mmap/aurochs");
	inf.setColor(new Color(0x8E8E8E));
	beasts.put(pat, "Aurochs");
	inf.show = false; 
	hlcfg.put(pat, inf);
	//mufflon
	pat = "kritter/mufflon";
	inf = new HLInfo(pat, "mmap/mufflon");
	inf.setColor(new Color(0x8E8E8E));
	beasts.put(pat, "Mufflon");
	inf.show = false;
	hlcfg.put(pat, inf);
	//ram
	pat = "kritter/bram";
	inf = new HLInfo(pat, "paginae/build/bram");
	inf.setColor(new Color(0xFF66CC));
	beasts.put(pat, "Ram");
	inf.show = false;
	hlcfg.put(pat, inf);
	
	pat = "kritter/pig";
	inf = new HLInfo(pat, "gfx/kritter/pig/piglet/standing/standing-1");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Pig");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/cow";
	inf = new HLInfo(pat, "gfx/kritter/cow/calf/standing/standing-1");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Cow");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/sheep";
	inf = new HLInfo(pat, "gfx/kritter/sheep/lamb/standing/standing-1");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Sheep");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/hen";
	inf = new HLInfo(pat, "gfx/kritter/hen/body/standing/standing-0");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Hen");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/frog";
	inf = new HLInfo(pat, "gfx/kritter/frog/body/standing/standing-0");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Frog");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/rat";
	inf = new HLInfo(pat, "gfx/kritter/rat/body/standing/standing-0");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Rat");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/ladybug/ladybug";
	inf = new HLInfo(pat, "gfx/kritter/ladybug/ladybug");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Ladybug");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/dragonfly";
	inf = new HLInfo(pat, "gfx/invobjs/dragonfly-emerald");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Dragonfly");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/moth";
	inf = new HLInfo(pat, "gfx/invobjs/silkmoth");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Moth");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/dryad";
	inf = new HLInfo(pat, "gfx/kritter/dryad/body/walking/dryad-6");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Dryad");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/boat/boat";
	inf = new HLInfo(pat, "paginae/build/rowboat");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Boat");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/wagon";
	inf = new HLInfo(pat, "paginae/build/wagon");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Wagon");
	inf.show = false;
	hlcfg.put(pat, inf);
	pat = "kritter/cart";
	inf = new HLInfo(pat, "paginae/build/cart");
	inf.setColor(new Color(0x8e8e8e));
	beasts.put(pat, "Cart");
	inf.show = false;
	hlcfg.put(pat, inf);
    }
}
