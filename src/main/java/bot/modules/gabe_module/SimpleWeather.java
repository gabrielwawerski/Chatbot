package bot.modules.gabe_module;

import bot.core.Chatbot;
import bot.core.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.module_library.SimpleModule;
import bot.core.helper.misc.Message;
import com.github.prominence.openweathermap.api.HourlyForecastRequester;
import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.exception.InvalidAuthTokenException;
import com.github.prominence.openweathermap.api.model.response.HourlyForecast;
import com.github.prominence.openweathermap.api.model.response.Weather;

import java.text.SimpleDateFormat;
import java.util.List;

public class SimpleWeather extends SimpleModule {
    private OpenWeatherMapManager openWeatherManager;
    /** current weather */
    private WeatherRequester weatherRequester;
    /** weather from last 3 hours */
    private HourlyForecastRequester hourlyRequester;

    private static final String API_KEY = "996851e7e9d015a4a9ff48dee77dd9c9";
    private static final String LANGUAGE = Language.POLISH;
    private static final String UNIT_SYSTEM = Unit.METRIC_SYSTEM;
    private static final String ACCURACY = Accuracy.ACCURATE;
    private static final String LUBLIN_ID = "765876";


    public SimpleWeather(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
        openWeatherManager = new OpenWeatherMapManager(API_KEY);
        System.out.println(getClass().getSimpleName() + " online.");
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : regexList) {
            if (match.equals(command)) {
                try {
                    Weather weatherResponse = new Weather();
                    StringBuilder builder = new StringBuilder();
                    hourlyRequester = openWeatherManager.getHourlyForecastRequester();

                    // init openweather with desired settings
                    weatherResponse = openWeatherManager.getWeatherRequester()
                            .setLanguage(LANGUAGE)
                            .setUnitSystem(UNIT_SYSTEM)
                            .setAccuracy(ACCURACY)
                            .getByCityId(LUBLIN_ID);

                    HourlyForecast hourlyForecast = hourlyRequester
                            .setLanguage(LANGUAGE)
                            .setUnitSystem(UNIT_SYSTEM)
                            .setAccuracy(ACCURACY)
                            .getByCityId(LUBLIN_ID);

                    // tells messenger to format the message as a block of code
//                    builder.append("```\n");

//                    String header = String.format("%20s%12s%1s%n",
//                            weatherResponse.getCityName(), "*" + new java.text.DecimalFormat("#").format(weatherResponse.getTemperature()) + "*",
//                            Character.toString(weatherResponse.getTemperatureUnit()));

                    // name of the city, weather description
                    builder.append("*").append(weatherResponse.getCityName()).append("*")
                            // current temperature
                            .append("                   *")
                            .append(new java.text.DecimalFormat("#").format(weatherResponse.getTemperature()))
                            .append(weatherResponse.getTemperatureUnit())
                            .append("*")
                            .append(System.getProperty("line.separator"));

                    // current weather's brief description
                    builder.append("_")
                            .append(weatherResponse.getWeatherDescription().substring(0, 1).toUpperCase()
                                    // .substring(1) will throw an exception if getWeatherDescription() will be <1
                            + weatherResponse.getWeatherDescription().substring(1))
                            .append("_")
                            .append(System.getProperty("line.separator"))
                            .append("==================")
                            .append(System.getProperty("line.separator"));

                    builder.append("_Maksymalna_          *")
                            .append(new java.text.DecimalFormat("#").format(hourlyForecast.getMaximumTemperature()))
                            .append(weatherResponse.getWeatherInfo().getTemperatureUnit())
                            .append("*")
                            .append(System.getProperty("line.separator"));

                    builder.append("_Minimalna_            *")
                            .append(new java.text.DecimalFormat("#").format(hourlyForecast.getMinimumTemperature()))
                            .append(weatherResponse.getWeatherInfo().getTemperatureUnit())
                            .append("*")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    builder.append("_Wiatr_               *")
                            .append(new java.text.DecimalFormat("###").format(weatherResponse.getWind().getSpeed() * 3.6f)).append(" km/h*")
                            .append(System.getProperty("line.separator"));

                    builder.append("_Zachmurzenie_       *")
                            .append(weatherResponse.getClouds().getCloudiness())
                            .append("%*")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    builder.append("_Barometr_       *")
                            .append(weatherResponse.getPressure()).append(" ")
                            .append(weatherResponse.getPressureUnit())
                            .append("*")
                            .append(System.getProperty("line.separator"));

                    builder.append("_Wilgotność_             *")
                            .append(weatherResponse.getHumidityPercentage())
                            .append("%*")
                            .append(System.getProperty("line.separator"));

                    builder.append("==================")
                            .append(System.getProperty("line.separator"));

                    String dataCalculationDate = new SimpleDateFormat("dd.MM.yy hh:mm")
                            .format(weatherResponse.getDataCalculationDate());
                    // tells when data was gathered
                    builder.append("Dane z: ")
                            .append(dataCalculationDate);

//                    builder.append("\n```");

                    builder.trimToSize();
                    chatbot.sendMessage(builder.toString());
                    return true;
                } catch (DataNotFoundException e) {
                    e.printStackTrace();
                } catch (InvalidAuthTokenException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}
