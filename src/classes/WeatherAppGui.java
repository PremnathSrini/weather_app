package classes;

import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.json.simple.JSONObject;

public class WeatherAppGui extends JFrame{
	private JSONObject weatherData;
	public WeatherAppGui()
	{
		super("Weather App");
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		setSize(450,650);
		
		setLocationRelativeTo(null);
		
		setLayout(null);
		
		setResizable(false);
		
		addGuiComponents();
	}

	private void addGuiComponents() {
		// TODO Auto-generated method stub
		JTextField searchTextField = new JTextField();
		searchTextField.setBounds(15,15,351,45);
		searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
		add(searchTextField);
		
	
		//weather image
		JLabel weatherConditionImage = new JLabel(loadImage("src/assets/cloudy.png"));
		weatherConditionImage.setBounds(0,125,450,217);
		add(weatherConditionImage);
		
		//temperature text
		JLabel tempText = new JLabel("10 C");
		tempText.setBounds(0,350,450,54);
		tempText.setFont(new Font("Dialog",Font.BOLD,48));
		tempText.setHorizontalAlignment(SwingConstants.CENTER);
		add(tempText);
		
		//weather Condition
		JLabel weatherConditionDesc = new JLabel("Cloudy");
		weatherConditionDesc.setBounds(0,405,450,36);
		weatherConditionDesc.setFont(new Font("Dialog",Font.PLAIN,32));
		weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
		add(weatherConditionDesc);
		
		//humidity
		JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
		humidityImage.setBounds(15,500,74,66);
		add(humidityImage);
		JLabel humidityText = new JLabel("<html><b>Humidity</b> 100% </html>");
		humidityText.setBounds(90,500,85,55);
		humidityText.setFont(new Font("Dialog",Font.PLAIN,16));
		add(humidityText);
	
		//wind speed
		JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
		windSpeedImage.setBounds(220,500,74,66);
		add(windSpeedImage);
		JLabel windSpeedText = new JLabel("<html> <b> Windspeed </b> 15km/h </html>");
		windSpeedText.setBounds(310,500,85,55);
		windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
		add(windSpeedText);
		
		//searchImage
		JButton searchButton = new JButton(loadImage("src/assets/search.png"));
		searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		searchButton.setBounds(375,13,47,45);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String userInput = searchTextField.getText();
				if(userInput.replaceAll("\\s", "").length() <= 0) {
					return;
				}
				
				weatherData = WeatherApp.getWeatherData(userInput);
				String weatherCondition = (String)weatherData.get("weatherCondition");
				
				switch(weatherCondition)
				{
				case "Clear":
					weatherConditionImage.setIcon(loadImage("src/assests/clear.png"));
					break;
				case "Cloudy":
					weatherConditionImage.setIcon(loadImage("src/assests/cloudy.png"));
					break;
				case "Rain":
					weatherConditionImage.setIcon(loadImage("src/assests/rain.png"));
					break;
				case "Snow":
					weatherConditionImage.setIcon(loadImage("src/assests/snow.png"));
					break;
				}
				
				double temperature = (double)weatherData.get("temperature");
				tempText.setText(temperature+" C");
				
				//update weather
				weatherConditionDesc.setText(weatherCondition);
				
				//update humidity
				long humidity =(long)weatherData.get("humidity");
				humidityText.setText("<html><b>Humidity</b> "+ humidity +"%</html>");
				
				//update windspeed

				double windSpeed =(double)weatherData.get("windSpeed");
				windSpeedText.setText("<html><b>WindSpeed</b> "+ windSpeed +"km/hr</html>");
			}
			
		});
		add(searchButton);
	}

	private ImageIcon loadImage(String resourcePath) {
		// TODO Auto-generated method stub
		try {
			BufferedImage image = ImageIO.read(new File(resourcePath));
			return new ImageIcon(image);
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
		System.out.println("Could not find resource");
		return null;
	}
}
