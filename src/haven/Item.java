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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ender.CurioInfo;

public class Item extends Widget implements DTarget {
	static Coord shoff = new Coord(1, 3);
	static final Pattern patt = Pattern.compile("quality (\\d+) ",
			Pattern.CASE_INSENSITIVE);
	static final Pattern pattVal = Pattern.compile("\\(([0-9.]+)/([0-9.]+)",
			Pattern.CASE_INSENSITIVE);
	static final Pattern pattTray = Pattern.compile("quality \\d+ cheese tray: quality \\d+ (.+)",
			Pattern.CASE_INSENSITIVE);
	static Map<Integer, Tex> qmap;
	public static Resource missing = Resource.load("gfx/invobjs/missing");
	static Color outcol = new Color(0,0,0,255);
	static Color clrWater = new Color(48, 48, 154,210);
    static Color clrWine = new Color(139, 71, 137,210);
	static Color clrHoney = new Color(238, 173, 14,210);
	static Color clrWort = new Color(168, 47, 26,210);
	public boolean isDragging = false;
	public int quality, q2;
	boolean hq;
	Coord doff;
	public String tooltip;
	public int num = -1;
	public Indir<Resource> res;
	Tex sh;
	public Color olcol = null;
	Tex mask = null;
	public int meter = 0;
	String curioStr = null;
	public double count_of_value = -1;
	public double count_of_maximum = -1;
	
	public static int idCounter = 0; // new
	public int id = 0; // new

	public static class ItemQualityComparator implements Comparator<Item> {
		int desc = -1;

		@Override
		public int compare(Item itema, Item itemb) {
			return desc * (itema.get_quality() - itemb.get_quality());
		}

		public ItemQualityComparator() {

		}

		public ItemQualityComparator(Boolean bdesc) {
			desc = (bdesc) ? 1 : -1;
		}
	}

	static {
		Widget.addtype("item", new WidgetFactory() {
			public Widget create(Coord c, Widget parent, Object[] args) {
				int res = (Integer) args[0];
				int q = (Integer) args[1];
				int num = -1;
				String tooltip = null;
				int ca = 3;
				Coord drag = null;
				if ((Integer) args[2] != 0)
					drag = (Coord) args[ca++];
				if (args.length > ca)
					tooltip = (String) args[ca++];
				if ((tooltip != null) && tooltip.equals(""))
					tooltip = null;
				if (args.length > ca)
					num = (Integer) args[ca++];
				Item item = new Item(c, res, q, parent, drag, num);
				item.settip(tooltip);
				return (item);
			}
		});
		missing.loadwait();
		qmap = new HashMap<Integer, Tex>();
	}

	void calcTray(){
		try{
			String name = this.GetResName();
			String cheeseName = "";
			if(name.equals("gfx/invobjs/cheese-tray-cheese") ){
				cheeseName = cheeseTrayName();
			}else if(name.equals("gfx/invobjs/cheese-tray-curd") ){
				cheeseName = "curd";
			}else{
				return;
			}
			
			String getName = getNextCheeseStage(cheeseName);
			CurioInfo curio;
			if((curio = Config.curios.get(getName.toLowerCase())) != null){
				int time = (int)(curio.time*(100 - meter)/100);
				int d = time/1440;
				int h = (time%1440)/60;
				int m = time%60;
				curioStr = String.format("\n%s: %dd %dh %2dm",getName,d,h,m);
			}
		}catch(Exception e){}
	}
	
	String cheeseTrayName(){
		try{
			Matcher m = pattTray.matcher(tooltip); 
			while(m.find()){
				return m.group(1);
			}
	    } catch(Exception e){}
		
		return "";
	}
	
	public String GetResName(){
		if (this.res.get() != null) {
			return ((Resource)this.res.get()).name;
		}
		return "";
	}
	
	String getNextCheeseStage(String tray){
		int idType = getPlayerTileID(); // 1 outside 2 cabin 3 cellar 4 mine
		
		if(tray.equals("curd") ){
			if(idType == 1)
				return "Creamy Camembert";
			else if(idType == 2)
				return "Tasty Emmentaler";
			else if(idType == 3)
				return "Cellar Cheddar";
			else if(idType == 4)
				return "Mothzarella";
		}else if(tray.equals("Brodgar Blue Cheese") ){
			if(idType == 4)
				return "Jorbonzola";
		}else if(tray.equals("Mothzarella") ){
			if(idType == 2 || idType == 3)
				return "Harmesan Cheese";
		}else if(tray.equals("Cellar Cheddar") ){
			if(idType == 1 || idType == 2)
				return "Brodgar Blue Cheese";
		}else if(tray.equals("Jorbonzola") ){
			if(idType == 3)
				return "Midnight Blue Cheese";
		}else if(tray.equals("Harmesan Cheese") ){
			if(idType == 1)
				return "Sunlit Stilton";
		}else if(tray.equals("Tasty Emmentaler") ){
			if(idType == 4)
				return "Musky Milben";
		}else if(tray.equals("Generic Gouda") ){
			return "";
		}
		
		return "Generic Gouda";
	}
	
	int getPlayerTileID(){
		try{
			int id = ui.mainview.map.gettilen(ui.mainview.glob.oc.getgob(ui.mainview.playergob).getc().div(11) );
			
			if(id == 21)
				return 2;
			if(id == 22)
				return 3;
			if(id == 23 || id == 24 || id == 25)
				return 4;
		}catch(Exception e){}
		
		return 1;
	}

	public int get_inner_quality() {
		return q2;
	}

	public int get_quality() {
		return (q2 > 0) ? q2 : quality;
	}

	public boolean isEatable() {
		String s = GetResName();
		if (s.indexOf("gfx/invobjs/bread") >= 0)
			return true;
		if (s.indexOf("gfx/invobjs/meat") >= 0)
			return true;
		if (s.indexOf("gfx/invobjs/mussel-boiled") >= 0)
			return true;
		return false;
	}

	public int coord_x() {
		return c.div(31).x;
	}

	public int coord_y() {
		return c.div(31).y;
	}
	
	public Coord coord() {
		return c.div(31);
	}

	public void settip(String t) {
		tooltip = t;
		q2 = -1;
		if (tooltip != null) {
			try {
				Matcher m = patt.matcher(tooltip);
				while (m.find()) {
					q2 = Integer.parseInt(m.group(1));
				}
				Matcher valm = pattVal.matcher(tooltip);
				if (valm.find()) {
					count_of_value = Double.parseDouble(valm.group(1));
					count_of_maximum = Double.parseDouble(valm.group(2));
				}
			} catch (IllegalStateException e) {
				System.out.println(e.getMessage());
			}
		}
		calcFEP();
		calcCurio();
	}

	private void fixsize() {
		if (res.get() != null) {
			Tex tex = res.get().layer(Resource.imgc).tex();
			sz = tex.sz().add(shoff);
		} else {
			sz = new Coord(30, 30);
		}
	}
	
	public Coord size() {
		if (res.get() != null) {
			Tex tex = res.get().layer(Resource.imgc).tex();
			if(tex == null)
				return new Coord(1, 1);
			else
				return tex.sz().div(30);
		} else {
			return new Coord(1, 1);
		}
	}

	public void draw(GOut g) {
		final Resource ttres;
		if (res.get() == null) {
			sh = null;
			sz = new Coord(30, 30);
			g.image(missing.layer(Resource.imgc).tex(), Coord.z, sz);
			ttres = missing;
		} else {
			Tex tex = res.get().layer(Resource.imgc).tex();
			fixsize();
			if (isDragging) {
				g.chcolor(255, 255, 255, 128);
				g.image(tex, Coord.z);
				g.chcolor();
			} else {
				if(res.get().name.equals("gfx/invobjs/silkmoth") && tooltip.contains("Female") )
				g.chcolor(255, 100, 255, 255);
				else
					g.chcolor();
				
				g.image(tex, Coord.z);
			}
			if (num >= 0) {
				// g.chcolor(Color.WHITE);
				// g.atext(Integer.toString(num), new Coord(0, 30), 0, 1);
				g.aimage(getqtex(num), Coord.z, 0, 0);
			}
			if (meter > 0) {
				double a = ((double) meter) / 100.0;
				int r = (int) ((1 - a) * 255);
				int gr = (int) (a * 255);
				int b = 0;
				g.chcolor(r, gr, b, 255);
				// g.fellipse(sz.div(2), new Coord(15, 15), 90, (int)(90 + (360
				// * a)));
				g.frect(new Coord(sz.x - 5, (int) ((1 - a) * sz.y)), new Coord(
						5, (int) (a * sz.y)));
				g.chcolor();
			}
			int tq = (q2 > 0) ? q2 : quality;
			if (Config.showq && (tq > 0)) {
				tex = getqtex(tq);
				g.aimage(tex, sz.sub(1, 1), 1, 1);
			}
			ttres = res.get();
		}
		if (olcol != null) {
			Tex bg = ttres.layer(Resource.imgc).tex();
			if ((mask == null) && (bg instanceof TexI)) {
				mask = ((TexI) bg).mkmask();
			}
			if (mask != null) {
				g.chcolor(olcol);
				g.image(mask, Coord.z);
				g.chcolor();
			}
		}
		
		if(FEP == null){calcFEP();}
		if(curioStr == null){calcCurio();}
		
		if(Config.flaskMeters){
		if (ttres.name.lastIndexOf("waterflask") > 0) {
			drawBar(g, 2, clrWater, 7);
		} else if (ttres.name.lastIndexOf("glass-winef") > 0) {
			drawBar(g, 0.2, clrWine, 7);
		} else if (ttres.name.lastIndexOf("bottle-winef") > 0) {
			drawBar(g, 0.6, clrWine, 7);
		} else if (ttres.name.lastIndexOf("bottle-wine-weißbier") > 0) {
			drawBar(g, 0.6, clrWine, 7);
		} else if (ttres.name.lastIndexOf("tankardf") > 0) {
			drawBar(g, 0.4, clrWine, 7);
		} else if (ttres.name.lastIndexOf("waterskin") > 0) {
			drawBar(g, 3, clrWater, 7);
		} else if (ttres.name.lastIndexOf("bucket-") > 0 || ttres.name.lastIndexOf("waterflask-") > 0) {
			Color clr;
			if (ttres.name.lastIndexOf("water") > 0)
				clr = clrWater;
			else if (ttres.name.lastIndexOf("wine") > 0 || ttres.name.lastIndexOf("vinegar") > 0 || ttres.name.lastIndexOf("grapejuice") > 0)
				clr = clrWine;
			else if (ttres.name.lastIndexOf("honey") > 0)
				clr = clrHoney;
			else if (ttres.name.lastIndexOf("wort") > 0 )
				clr = clrWort;
			else
				clr = Color.LIGHT_GRAY;

				drawBar(g, 10, clr, 10);
		}
	}
	
	}
	
	private void drawBar(GOut g, double capacity, Color clr, int width) {
		try {
			String valStr = tooltip.substring(tooltip.indexOf('(')+1, tooltip.indexOf('/'));
			double val = Double.parseDouble(valStr);        
			int h = (int)(val/capacity*sz.y);
			g.chcolor(clr);        
			int barH = h-shoff.y;
			g.frect(new Coord(0, sz.y-h), new Coord(width, barH < 0 ? 0 : barH));
			g.chcolor();
		} catch (Exception e) {} // fail silently.
	}

	static Tex getqtex(int q) {
		synchronized (qmap) {
			if (qmap.containsKey(q)) {
				return qmap.get(q);
			} else {
				BufferedImage img = Text.render(Integer.toString(q)).img;
				img = Utils.outline2(img, outcol, true);
				Tex tex = new TexI(img);
				qmap.put(q, tex);
				return tex;
			}
		}
	}

	static Tex makesh(Resource res) {
		BufferedImage img = res.layer(Resource.imgc).img;
		Coord sz = Utils.imgsz(img);
		BufferedImage sh = new BufferedImage(sz.x, sz.y,
				BufferedImage.TYPE_INT_ARGB);
		for (int y = 0; y < sz.y; y++) {
			for (int x = 0; x < sz.x; x++) {
				long c = img.getRGB(x, y) & 0x00000000ffffffffL;
				int a = (int) ((c & 0xff000000) >> 24);
				sh.setRGB(x, y, (a / 2) << 24);
			}
		}
		return (new TexI(sh));
	}

	public String name() {
	Resource res = this.res.get();
	if(res != null){
	    if(res.layer(Resource.tooltip) != null) {
		return res.layer(Resource.tooltip).t;
	    } else {
		return(this.tooltip);
	    }
	}
	return null;
    }
	
	public String tooltip() {
		return(this.tooltip);
    }

	public double qmult;
	private String FEP = null;

	/*private void calcFEP() {
		Map<String, Float> fep;
		String name = name();
		if ((name != null) && (fep = Config.FEPMap.get(name().toLowerCase())) != null) {
			FEP = "\n";
			for (String key : fep.keySet()) {
				FEP += String.format("%s:%.1f ", key, (float) fep.get(key)
						* qmult);
			}
		}
	}*/
	
	private void calcFEP() {
		Map<String, Float> fep;
		String name = name();
		double weapon = 1;
		if(name == null){return;}
		if(name.equals("Ring of Brodgar")){
			if(res.get().name.equals("gfx/invobjs/bread-brodgar")){name = "Ring of Brodgar (Baking)";}
			if(res.get().name.equals("gfx/invobjs/feast-rob")){name = "Ring of Brodgar (Seafood)";}
		}
		
		name = name.toLowerCase();
		boolean isItem = false;
		if((fep = Config.FEPMap.get(name)) != null){
			if(fep.containsKey("isItem")){
			isItem = true;
			}
			FEP = "\n";
			for(String key:fep.keySet()){
				double k = fep.get(key);
				float val = (float)(k*qmult);
				boolean hunger = false;
				if(key.equals("HUNGER")){
					val = fep.get(key);
					hunger = true;
				} else if (key.equals("Industry") || key.equals("Day") || key.equals("Night") || key.equals("Tradition")) {
					val = fep.get(key);
				}
				if(key.equals("isItem")){continue;}
				
				if (!key.equals("AGI")) {
					if(name.contains("sword") || name.contains("axe")){
						int str = ui.sess.glob.cattr.get("str").comp;
						double marsh = 1 + (((double)ui.sess.glob.cattr.get("martial").comp * 4) / 100);
						weapon = Math.sqrt(Math.sqrt((double)quality * (double)str)/10) * marsh;
						val = (float)(weapon * k);
					}else if(name.contains("bow") || name.contains("sling")){
						double marsh = 1 + (((double)ui.sess.glob.cattr.get("martial").comp * 4) / 100);
						val = (float)(marsh * val);
					}
				}
				
				if(isItem || hunger){
					val = (float) Math.floor(val);
					FEP += String.format("%s:%.0f ", key, val);
				} else {
					val = (float) Math.floor(val * 10);
					FEP += String.format("%s:%.1f ", key, val/10);
				}
			}
			shorttip = longtip = null;
		}
    }

	public String shorttip() {
		if (this.tooltip != null)
			return (this.tooltip);
		Resource res = this.res.get();
		if ((res != null) && (res.layer(Resource.tooltip) != null)) {
			String tt = res.layer(Resource.tooltip).t;
			if (tt != null) {
				if (quality > 0) {
					tt = tt + ", quality " + quality;
					if (hq)
						tt = tt + "+";
				}
				if (FEP == null) {
					calcFEP();
				}
				if (curios == null && name() != null && Config.curios.get(name().toLowerCase()) != null) {
					curios = Config.curios.get(name().toLowerCase());
				}
				return (tt);
			}
		}
		return (null);
	}

	long hoverstart;
	Text shorttip = null, longtip = null;

	
	public static String min2hours(int minutes) {
		String ret = "";
		int h = (int)(minutes / 60);
		int m = minutes - h * 60;
		if (h > 0) ret += h + "h ";
		ret += m + "min";
		return ret;
	}
	
	private void calcCurio(){
	String name = name();
	if(name == null){return;}
	name = name.toLowerCase();
	CurioInfo curio;
	if((curio = Config.curios.get(name)) != null){
		if(GetResName().contains("goldegg")) qmult = 1;
	    int LP = (int) (curio.LP*qmult*ui.sess.glob.cattr.get("expmod").comp/100);
	    int time = (int)(curio.time*(100 - meter)/100);
	    int h = time/60;
	    int m = time%60;
		int LPM = (int)( (double)LP / (double)(curio.time) );
		int LPH = (int)( (double)LPM * (double)(60) );
		if(LP != 0)
			curioStr = String.format("\nLP: %d, Weight: %d\nStudy time: %dh %2dm\nLPH: %d", LP,curio.weight,h,m,LPH);
		else
			curioStr = String.format("\nPrep time: %dh %2dm",h,m);
	    shorttip = longtip = null;
	}
    }
	
	private String shrtTip() {
		String tt = shorttip();
		if (tt != null) {
			tt = RichText.Parser.quote(tt);
			if (meter > 0) {
				tt = tt + " (" + meter + "%)";
			}
			if (FEP != null) {
				tt += FEP;
			}
			if(curioStr != null){
				tt += curioStr;
		    }
			/*if (curio_stat != null && qmult > 0) {
				if(UI.instance.wnd_char != null)
					tt += "\nLP: $col[205,205,0]{" + Math.round(curio_stat.baseLP * qmult * UI.instance.wnd_char.getExpMode()) + "}";
				if (meter > 0) {
					double time = (double)meter / 100;
					tt += " in " + min2hours(curio_stat.studyTime - (int)(curio_stat.studyTime * time));
				}
				tt += " Att: " + curio_stat.attention;
				double curioTime =  curio_stat.studyTime / 60;
				tt += "\nLPH: $col[205,205,0]{" + Math.round((curio_stat.baseLP * qmult * UI.instance.wnd_char.getExpMode())/curioTime) + "}";
			}*/
		}
		return tt;
	}
	
	private String lngTip() {
		String tt = shrtTip();
		if (tt != null) {
			tt += "\nResource: " + GetResName();
		}
		return tt;
	}
	
	public Object tooltip(Coord c, boolean again) {
		long now = System.currentTimeMillis();
		if (!again)	hoverstart = now;
		
		if ((now - hoverstart) < 1000) {
			if (shorttip == null) {
				String tip = shrtTip();
				if (tip == null) return null;
				shorttip = RichText.render(tip, 200);
			}
			return (shorttip);
		} else {
			if (longtip == null) {
				String tip = lngTip();
				if (tip == null) return null;
				longtip = RichText.render(tip, 200);
			}
			return (longtip);
		}
	}

	private void resettt() {
		shorttip = null;
		longtip = null;
	}

	private void decq(int q) {
		if (q < 0) {
			this.quality = q;
			hq = false;
		} else {
			int fl = (q & 0xff000000) >> 24;
			this.quality = (q & 0xffffff);
			hq = ((fl & 1) != 0);
		}
	}

	public CurioInfo curios = null;
	
	public Item(Coord c, Indir<Resource> res, int q, Widget parent, Coord drag,
			int num) {
		super(c, Coord.z, parent);
		this.res = res;
		idCounter++; // new
	    id = idCounter; // new
		decq(q);
		fixsize();
		this.num = num;
		if (drag == null) {
			isDragging = false;
		} else {
			isDragging = true;
			doff = drag;
			ui.grabmouse(this);
			this.c = ui.mc.add(doff.inv());
		}
		qmult = Math.sqrt((double) q / 10);
		calcFEP();
		calcCurio();
		if (curios == null && name() != null && Config.curios.get(name().toLowerCase()) != null) {
			curios = Config.curios.get(name().toLowerCase());
		}
	}

	public Item(Coord c, int res, int q, Widget parent, Coord drag, int num) {
		this(c, parent.ui.sess.getres(res), q, parent, drag, num);
	}

	public Item(Coord c, Indir<Resource> res, int q, Widget parent, Coord drag) {
		this(c, res, q, parent, drag, -1);
	}

	public Item(Coord c, int res, int q, Widget parent, Coord drag) {
		this(c, parent.ui.sess.getres(res), q, parent, drag);
	}
	
	public int getLP() {
	String name = name();
	if(name == null){return 0;}
	name = name.toLowerCase();
	CurioInfo curio;
	if((curio = Config.curios.get(name)) != null){
	    return (int) (curio.LP*qmult*ui.sess.glob.cattr.get("expmod").comp/100);
	}
	return 0;
    }
	
	public int getLPMinut() {
	int LP = 0;
	int LPM = 0;
	String name = name();
	if(name == null){return 0;}
	name = name.toLowerCase();
	CurioInfo curio;
	if((curio = Config.curios.get(name)) != null){
		if(GetResName().contains("goldegg")) qmult = 1;
	    LP = (int) (curio.LP*qmult*ui.sess.glob.cattr.get("expmod").comp/100);
		LPM = (int)((double)LP / (double)curio.time);
	}
	return LPM;
    }

	public boolean dropon(Widget w, Coord c) {
		for (Widget wdg = w.lchild; wdg != null; wdg = wdg.prev) {
			if (wdg == this)
				continue;
			Coord cc = w.xlate(wdg.c, true);
			if (c.isect(cc, (wdg.hsz == null) ? wdg.sz : wdg.hsz)) {
				if (dropon(wdg, c.add(cc.inv())))
					return (true);
			}
		}
		if (w instanceof DTarget) {
			if (((DTarget) w).drop(c, c.add(doff.inv())))
				return (true);
		}
		if (w instanceof DTarget2) {
			if (((DTarget2) w).drop(c, c.add(doff.inv()), this))
				return (true);
		}
		return (false);
	}

	public boolean interact(Widget w, Coord c) {
		for (Widget wdg = w.lchild; wdg != null; wdg = wdg.prev) {
			if (wdg == this)
				continue;
			Coord cc = w.xlate(wdg.c, true);
			if (c.isect(cc, (wdg.hsz == null) ? wdg.sz : wdg.hsz)) {
				if (interact(wdg, c.add(cc.inv())))
					return (true);
			}
		}
		if (w instanceof DTarget) {
			if (((DTarget) w).iteminteract(c, c.add(doff.inv())))
				return (true);
		}
		return (false);
	}

	public void chres(Indir<Resource> res, int q) {
		this.res = res;
		sh = null;
		decq(q);
	}

	public void uimsg(String name, Object... args) {
		if (name == "num") {
			num = (Integer) args[0];
		} else if (name == "chres") {
			chres(ui.sess.getres((Integer) args[0]), (Integer) args[1]);
			resettt();
		} else if (name == "color") {
			olcol = (Color) args[0];
		} else if (name == "tt") {
			if ((args.length > 0) && (((String) args[0]).length() > 0))
				settip((String) args[0]);
			else
				settip(null);
			resettt();
		} else if (name == "meter") {
			meter = (Integer) args[0];
			shorttip = null;
			longtip = null;
			calcCurio();
			calcTray();
		}
	}

	public boolean mousedown(Coord c, int button) {
		if (!isDragging) {
			if(button == 1) {
				if(ui.modflags() == 1){
					wdgmsg("transfer", c);
				}else if(ui.modflags() == 2){
					wdgmsg("drop", c);
				}else if(ui.modflags() == 4){
					/*if (!GetResName().contains("cheese-tray") && !GetResName().contains("bucket-"))
						wdgmsg("transfer-same", name(), false);
					else*/ 
						wdgmsg("transfer_such_all_ql", GetResName());
				}else{
					wdgmsg("take", c);
				}
				return(true);
			}else if(button == 3){
                if(ui.modflags() == 4){
					/*if (!GetResName().contains("cheese-tray") && !GetResName().contains("bucket-"))
						wdgmsg("transfer-same", name(), true);
					else*/ 
						wdgmsg("transfer_such_all_qldesc", GetResName());
				}else if(ui.modflags() == 6){
					wdgmsg("drop_such_all", GetResName());
				}else{
					wdgmsg("iact", c);
				}
				return(true);
			}
		} else {
			if(button == 1){
				dropon(parent, c.add(this.c));
			}else if(button == 3){
				interact(parent, c.add(this.c));
			}
			return(true);
		}
		return(false);
	}

	public void mousemove(Coord c) {
		if (isDragging)
			this.c = this.c.add(c.add(doff.inv()));
	}

	public boolean drop(Coord cc, Coord ul) {
		return (false);
	}

	public boolean iteminteract(Coord cc, Coord ul) {
		wdgmsg("itemact", ui.modflags());
		return (true);
	}
	
	public void binded(){
		itemAction(res);
	}
	
	void itemAction(Indir<Resource> res){
		if(parent instanceof Inventory){
			try{
				String resname = res.get().name;
				
				if(((Window)parent.parent).cap.text.equals("Inventory")){
					if(resname.equals("gfx/invobjs/pearl") && ui.m_util.hasHourglass() && Sound.soundCheck(id)){
						Sound.safePlay("pearl");
					}else if(Config.minerSafety && Config.miningDrop && (resname.contains("ore-iron") || resname.contains("petrifiedseashell") || resname.contains("catgold") || resname.contains("strangecrystal")) ){
						wdgmsg("drop", Coord.z);
					}
				}
			}catch(Exception e){}
		}
	}
}
