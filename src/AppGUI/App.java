package AppGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Objects;

//png files from: https://www.vecteezy.com/search?qterm=weather-png-free&content_type=png
//png files authors: ucen cemong, manasthep, Ovidiu Timplaru, Jawad Ali, Luz Eugenia Velasquez
//<a href="https://www.vecteezy.com/free-png/error-404">Error 404 PNGs by Vecteezy</a>
//<a href="https://www.vecteezy.com/free-png/wind">Wind PNGs by Vecteezy</a>
//<a href="https://www.vecteezy.com/free-png/water-drop-clipart">Water Drop Clipart PNGs by Vecteezy</a>
//<a href="https://www.vecteezy.com/free-png/weather">Weather PNGs by Vecteezy</a>
//all png files are free

public class App extends JFrame implements ActionListener {
    JTextField enterText;
    JButton confirmButton;
    JLabel labelTemp, labelHumT, labelHum, labelWindT, labelWind, labelType, labelCity, labelPhoto;
    JButton resetB;
    String city;

    public App() {
        super("Pogoda");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setSize(420, 550);
        setLocation(350, 100);
        getContentPane().setBackground(new Color(70, 149, 207));
        setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/pCloudy.png"))).getImage());
        setLayout(null);

        //TEXT_FIELD
        enterText = new JTextField();
        enterText.setBounds(25, 25, 290, 50);
        enterText.setFont(new Font("Verdana", Font.PLAIN, 23));
        enterText.addActionListener(this);
        enterText.setBackground(new Color (41, 136, 203));
        enterText.setForeground(new Color(240, 240, 240));
        enterText.setBorder(BorderFactory.createLineBorder(new Color (240, 240, 240), 2));
        //TEXT_FIELD

        //CONFIRM BUTTON
        confirmButton = new JButton();
        confirmButton.setBounds(330, 25, 50, 50);
        confirmButton.addActionListener(this);
        confirmButton.setText("\uD83D\uDD0D"); //unicode dla emotki lupa
        confirmButton.setBackground(new Color(41, 136, 203));
        confirmButton.setForeground(new Color(240, 240, 240));
        confirmButton.setBorder(BorderFactory.createLineBorder(new Color (240, 240, 240), 2));
        //CONFIRM BUTTON

        //LABELS
        labelCity = new JLabel();
        labelCity.setFont(new Font("Verdana", Font.BOLD, 25));
        labelCity.setHorizontalAlignment(SwingConstants.CENTER);
        labelCity.setForeground(new Color(240, 240, 240));
        labelCity.setBounds(95, 100, 200, 27);

        labelTemp = new JLabel();
        labelTemp.setFont(new Font("Verdana", Font.BOLD, 25));
        labelTemp.setForeground(new Color(240, 240, 240));
        labelTemp.setBounds(125, 335, 150, 30);
        labelTemp.setHorizontalAlignment(SwingConstants.CENTER);

        labelPhoto = new JLabel();
        labelPhoto.setBounds(95, 130, 215, 210);

        labelHumT = new JLabel();
        labelHumT.setBounds(330, 430, 100, 20);
        labelHumT.setFont(new Font("Verdana", Font.BOLD, 15));
        labelHumT.setForeground(new Color(240, 240, 240));

        labelHum = new JLabel();
        labelHum.setBounds(300, 350, 100, 75);

        labelWindT = new JLabel();
        labelWindT.setBounds(25, 430, 100, 20);
        labelWindT.setFont(new Font("Verdana", Font.BOLD, 15));
        labelWindT.setForeground(new Color(240, 240, 240));

        labelWind = new JLabel();
        labelWind.setBounds(20, 350, 100, 75);

        labelType = new JLabel();
        labelType.setFont(new Font("Verdana", Font.BOLD, 10));
        labelType.setForeground(new Color(240, 240, 240));
        labelType.setBounds(150, 370, 120, 15);
        labelType.setHorizontalAlignment(SwingConstants.CENTER);
        //LABELS

        //RESET
        resetB = new JButton();
        resetB.setText("RESET");
        resetB.setBounds(160, 475, 75, 20);
        resetB.setFont(new Font("Verdana", Font.BOLD, 15));
        resetB.addActionListener(this);
        resetB.setBackground(new Color (41, 136, 203));
        resetB.setForeground(new Color(240, 240, 240));
        resetB.setBorder(BorderFactory.createLineBorder(new Color (240, 240, 240), 2));
        //RESET

        add(resetB);
        add(labelType);
        add(labelWind);
        add(labelHum);
        add(labelHumT);
        add(labelWindT);
        add(labelCity);
        add(labelTemp);
        add(labelPhoto);
        add(confirmButton);
        add(enterText);
        setVisible(true);
    }

    private String getWeather(String city, String what) {
        String format;
        switch(what) {
            case "temp" -> format = "?format=%25t&T";
            case "type" -> format = "?format=%25C&T";
            case "hum" -> format = "?format=%25h&T";
            case "wind" -> format = "?format=%25w&T";
            default -> format = "?format=%25t+%25C+%25w+%25h&T";
        }

        String url_p1 = "https://wttr.in/";
        String weatherUrl = url_p1 + city + format;

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(weatherUrl))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return response.body().trim();
            } else {
                return "Error " + response.statusCode();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error 404";
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == confirmButton || e.getSource() == enterText) {
            city = enterText.getText();
            String newCity = city.replace(" ", "");
            if (!newCity.equals("")) {
                newCity = newCity.substring(0, 1).toUpperCase() + newCity.substring(1);
            }
            enterText.setText("");
            if (getWeather(newCity, "type").equals("Error 404") || newCity.equals("")) {
                clearData();
                labelTemp.setText("Error");
                labelType.setText("City not found :(");
                Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/error.png"))).getImage();
                Image scaledIcon = icon.getScaledInstance(180, 180, Image.SCALE_SMOOTH);
                labelPhoto.setIcon(new ImageIcon(scaledIcon));
            } else {
                labelCity.setText(newCity);
                String weatherTemp = getWeather(newCity, "temp");
                labelTemp.setText(weatherTemp);
                Image icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(chooseIcon(getWeather(newCity, "type"))))).getImage();
                Image scaledIcon = icon.getScaledInstance(215, 210, Image.SCALE_SMOOTH);
                labelPhoto.setIcon(new ImageIcon(scaledIcon));
                labelHumT.setText(getWeather(newCity, "hum"));
                labelWindT.setText(getWeather(newCity, "wind").substring(1));
                labelType.setText(getWeather(newCity, "type"));

                Image iconHum = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/hum.png"))).getImage();
                Image scaledIconHum = iconHum.getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                labelHum.setIcon(new ImageIcon(scaledIconHum));

                Image iconWind = new ImageIcon(Objects.requireNonNull(getClass().getResource("/img/wind.png"))).getImage();
                Image scaledIconWind = iconWind.getScaledInstance(100, 75, Image.SCALE_SMOOTH);
                labelWind.setIcon(new ImageIcon(scaledIconWind));
            }
        }
        if (e.getSource() == resetB) {
            clearData();
        }
    }

    private String chooseIcon(String weatherType) {
        String ret = "/img/";

        switch(weatherType) {
            case "Sunny" -> ret += "sunny";
            case "Clear" -> ret += "sunny";
            case "Cloudy" -> ret += "cloudy";
            case "Overcast" -> ret += "cloudy";
            case "Partly cloudy" -> ret += "pCloudy";
            case "Light rain" -> ret += "rain";
            case "Rain" -> ret += "rain";
            case "Heavy rain" -> ret += "rain";
            case "Drizzle" -> ret += "rain";
            case "Hail" -> ret += "rain";
            case "Freezing rain" -> ret += "rain";
            case "Light snow" -> ret += "snow";
            case "Snow" -> ret += "snow";
            case "Heavy snow" -> ret += "snow";
            case "Sleet" -> ret += "snow";
            case "Blizzard" -> ret += "snow";
            case "Thunderstorm" -> ret += "storm";
            default -> ret += "cloudy";
        }
        if (weatherType.contains("Rain")  || weatherType.contains("rain")) {
            ret = "/img/rain";
        }

        ret += ".png";
        return ret;
    }

    private void clearData() {
        enterText.setText("");
        city = "";
        labelTemp.setText("");
        labelWindT.setText("");
        labelHumT.setText("");
        labelCity.setText("");
        labelType.setText("");
        labelHum.setIcon(null);
        labelWind.setIcon(null);
        labelPhoto.setIcon(null);
    }
}
