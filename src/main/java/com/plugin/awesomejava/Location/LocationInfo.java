package com.plugin.awesomejava.Location;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.*;
import java.net.HttpURLConnection;
import org.json.JSONObject;


public class LocationInfo {

    private static final String STRING_URL = "http://checkip.amazonaws.com";

    public static String getIp() {
        BufferedReader in = null;
        URL whatismyip = null;
        String ip = null;
        try {
            whatismyip = new URL(STRING_URL);

            in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));
            ip = in.readLine();
         
            return ip;
        } catch (Exception ex) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ip;
    }

    public static String DayName() {
        try {
            DateObjects localdate = GetLocalDate();
            String dateString = String.format("%d-%d-%d", localdate.year, localdate.month, localdate.day);

            Date date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int day = cal.get(Calendar.DAY_OF_WEEK);

            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

            return dayOfWeek + " " + day + "TH";
        } catch (ParseException ex) {
            System.out.println(ex.toString());
            return Error.LISTVALUES.getDescription();
        }
    }

    public static int DayCode() {
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek;
    }

    private static DateObjects GetLocalDate() {
        final Date date = new Date();
        final LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        final int year = localDate.getYear();
        final int month = localDate.getMonthValue();
        final int day = localDate.getDayOfMonth();

        return new DateObjects(year, month, day);

    }

    private static class DateObjects {

        private int year;
        private int month;
        private int day;

        public DateObjects() {

        }

        public DateObjects(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }

    }
    
     public static String getUserInputLocation() {
        Scanner s = new Scanner(System.in);
        System.out.print("Enter a location for a weather forecast you would like to see: ");
        return s.nextLine();
        
    }
    public static String getWeatherData(String location) {
        try {
            String urlString = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + "a19c55c36ed66d550c2cce7bdf03eb99";
            URL url = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line.trim());
                }
                return response.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
 public static void parseWeatherData(String jsonData) {
        JSONObject jsonObject = new JSONObject(jsonData);

        String weather = jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
        double temperature = jsonObject.getJSONObject("main").getDouble("temp");
        
        // Convert Kelvin to Celsius (since OpenWeather API returns temperature in Kelvin)
        double tempInCelsius = temperature - 273.15;
        double tempInFahreheit = ((temperature - 273.15)*1.8) + 32;

        System.out.println("Weather: " + weather);
        System.out.println("Temperature in celsius: " + tempInCelsius + "°C");
        System.out.println("Temperature in fahreheit: " + tempInFahreheit + "°F");
    }

    // In your main method or where you call these functions
   /*  public static void main(String[] args) {
        String location = getUserInputLocation();
        String weatherData = getWeatherData(location);
        if (weatherData != null) {
            parseWeatherData(weatherData);
        } 
        else {
            System.out.println("Failed to get weather data.");
        }
    } */
    
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        while (true) {
            String location = getUserInputLocation();
            String weatherData = getWeatherData(location);

            if (weatherData != null) {
                parseWeatherData(weatherData);
            } else {
                System.out.println("Failed to get weather data.");
            }

            // Ask the user if they want to continue or exit
            System.out.print("Do you want to check another location? (yes/no): ");
            String userInput = s.nextLine().toLowerCase();
            
            if (!userInput.equals("yes")) {
                break; // Exit the loop if the user doesn't want to continue
            }
        }

        
}
    
}
    


