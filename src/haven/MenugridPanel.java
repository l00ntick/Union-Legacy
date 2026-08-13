package haven;

import static haven.MenuGrid.bgsz;
import static haven.MenuGrid.gsz;

public class MenugridPanel extends Window {
	boolean rsm = false;
	private static final Coord MRGS = new Coord(3, 3);
	private static final Coord PAD = MRGS.mul(2).add(0, Utils.imgsz(fbtni[0]).x).add(gzsz);
	static final Coord minsz = bgsz.mul(gsz).add(PAD);

	MenuGrid menu;
    
	public MenugridPanel(Coord c, Widget parent) {
		super(c, minsz, parent, "Menu");
		menu = new MenuGrid(MRGS.add(0, Utils.imgsz(fbtni[0]).x), this);
		mrgn = Coord.z;
		fbtn.visible = true;
		cbtn.visible = false;
		loadpos();
	}

	private void loadpos(){
		synchronized (Config.window_props) {
			c = new Coord(Config.window_props.getProperty("menugrid_pos", c.toString()));
			changeSize(new Coord(Config.window_props.getProperty("menugrid_sz", minsz.toString())));
		}
	}

	protected void placecbtn() {
		fbtn.c = new Coord(wsz.x - 3 - Utils.imgsz(cbtni[0]).x, 3).add(mrgn.inv().add(wbox.tloff().inv()));
		//fbtn.c = new Coord(cbtn.c.x - 1 - Utils.imgsz(fbtni[0]).x, cbtn.c.y);
	}

	public void draw(GOut g) {
		super.draw(g);
		if(!folded)
			g.image(grip, sz.sub(gzsz));
	}

	public boolean mousedown(Coord c, int button) {
		if (folded) {
			return super.mousedown(c, button);
		}
		parent.setfocus(this);
		raise();
		if (button == 1) {
			ui.grabmouse(this);
			doff = c;
			if(c.isect(sz.sub(gzsz), gzsz)){
				rsm = true;
				return true;
			}
		}

		return super.mousedown(c, button);
	}
	
	public boolean mouseup(Coord c, int button) {
		if(dm){
			Config.setWindowOpt("menugrid_pos", this.c.toString());
		}
		if (rsm){
			ui.grabmouse(null);
			rsm = false;
			Config.setWindowOpt("menugrid_sz", this.sz.toString());
		} else {
			super.mouseup(c, button);
		}

		return true;
	}

	public void mousemove(Coord c) {
		if (rsm){
			Coord d = c.sub(doff);
			doff = c;
			changeSize(this.sz.add(d));
		} else {
			super.mousemove(c);
		}
	}

	public boolean type(char key, java.awt.event.KeyEvent ev) {
		if(key == 27) {
			wdgmsg(fbtn, "click");
			return(true);
		}
		return(super.type(key, ev));
	}

	private void changeSize(Coord newSize) {
		if (newSize.x < minsz.x || newSize.y < minsz.y) {
			newSize = minsz;
		}
		this.sz = newSize;

		Coord tempGSZ = this.sz.sub(PAD).div(bgsz);
		gsz.x = tempGSZ.x;
		gsz.y = tempGSZ.y;
		menu.sz = bgsz.mul(gsz).add(1);

		oldSize = this.sz;
		recalcSize(this.sz);
		pack();
	}
}
