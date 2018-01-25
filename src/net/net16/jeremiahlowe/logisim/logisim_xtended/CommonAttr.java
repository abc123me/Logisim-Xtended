package net.net16.jeremiahlowe.logisim.logisim_xtended;

import java.awt.Color;

import com.cburch.logisim.data.Attribute;
import com.cburch.logisim.data.Attributes;

public class CommonAttr {
	public static final Attribute<Integer> COLOR_DETAIL = Attributes.forIntegerRange("Color detail", 1, 8);
	public static final Attribute<?> SCREEN_SIZE = Attributes.forOption("Size", new Object[]{64, 128, 256, 512, 1024});
	public static final Attribute<Color> ON_COLOR = Attributes.forColor("On color");
	public static final Attribute<Color> OFF_COLOR = Attributes.forColor("Off color");
	public static final Attribute<String> LABEL_ON = Attributes.forString("Label on");
	public static final Attribute<String> LABEL_OFF = Attributes.forString("Label off");
	public static final Attribute<Color> LABEL_COLOR = Attributes.forColor("Label color");
}
