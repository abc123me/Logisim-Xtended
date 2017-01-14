package net.net16.jeremiahlowe.logisim.logisim_xtended.assets.icons;

import javax.swing.Icon;
import javax.swing.ImageIcon;

public class IconGetter {
	public static final IconGetter iconGetterInstance = new IconGetter();
	public static Icon getIcon(String in){
		return new ImageIcon(iconGetterInstance.getClass().getResource(in));
	}
}
