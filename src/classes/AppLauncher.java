package classes;

import javax.swing.SwingUtilities;

public class AppLauncher {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new WeatherAppGui().setVisible(true);
				
				//System.out.println(WeatherApp.getLocationData("Tokyo"));
				
				//System.out.println(WeatherApp.getCurrentTime());
			}
		});
	}

}
