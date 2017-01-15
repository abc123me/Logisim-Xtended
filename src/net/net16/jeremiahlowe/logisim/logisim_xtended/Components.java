package net.net16.jeremiahlowe.logisim.logisim_xtended;

import java.util.Arrays;
import java.util.List;

import com.cburch.logisim.tools.AddTool;
import com.cburch.logisim.tools.Library;
import com.cburch.logisim.tools.Tool;

import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.BinToBCD;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.ColorScreen;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.RGBLed;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.RGBSquareLed;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.Screen;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.io.SquareLED;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic.SquareAND;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic.SquareOR;
import net.net16.jeremiahlowe.logisim.logisim_xtended.parts.logic.SquareXOR;

public class Components extends Library {
    private List<Tool> tools;
    
    public Components() {
    	tools = Arrays.asList(new Tool[]{
    		new AddTool(new RGBSquareLed()),
        	new AddTool(new RGBLed()),
        	new AddTool(new SquareLED()),
        	new AddTool(new Screen()),
        	new AddTool(new ColorScreen()),
        	new AddTool(new BinToBCD()),
        	new AddTool(new SquareAND()),
        	new AddTool(new SquareOR()),
        	new AddTool(new SquareXOR())
    	});
    }
    
    public String getDisplayName() { return "Logisim Xtended"; }
    @Override
	public List<Tool> getTools() { return tools; }
}

