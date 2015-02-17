package app;


import java.util.logging.Level;
import java.util.logging.Logger;

import model.Model;
import utils.LogUtil;
import view.ViewPanel;

public class BeastMain {

	public static void main(String[] args) throws Exception {

		Logger.getLogger("").setLevel(Level.INFO);
		LogUtil.init();
		
		Model model = new Model(500, 300);
		
		final MainFrame frame = new MainFrame();
		frame.init(model);
		
		ViewPanel view = frame.getViewPanel();
		
		while (true) {
			model.universe.update();
			view.repaint();
		}
	}
	
}
