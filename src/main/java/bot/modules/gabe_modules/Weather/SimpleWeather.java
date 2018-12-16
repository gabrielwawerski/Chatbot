package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.ModuleBase;
import bot.utils.bot.helper.helper_class.Message;
import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.exception.InvalidAuthTokenException;
import com.github.prominence.openweathermap.api.model.response.Weather;

import java.util.List;

public class SimpleWeather extends ModuleBase {
    private OpenWeatherMapManager openWeatherManager;
    WeatherRequester weatherRequester;

    public static final String API_KEY = "996851e7e9d015a4a9ff48dee77dd9c9";
    private static final String LANGUAGE = Language.POLISH;
    private static final String UNIT_SYSTEM = Unit.METRIC_SYSTEM;
    private static final String ACCURACY = Accuracy.ACCURATE;
    public static final String LUBLIN_ID = "765876";


    public SimpleWeather(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
        openWeatherManager = new OpenWeatherMapManager(API_KEY);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        for (String command : commands) {
            if (match.equals(command)) {
                try {
                    Weather weatherResponse = new Weather();
                    StringBuilder builder = new StringBuilder();

                    // init openweather with desired settings
                    weatherResponse = openWeatherManager.getWeatherRequester()
                            .setLanguage(LANGUAGE)
                            .setUnitSystem(UNIT_SYSTEM)
                            .setAccuracy(ACCURACY)
                            .getByCityId(LUBLIN_ID);

                    // tells messenger to format the message as a block of code
                    builder.append("```\n");

                    // name of the city, weather description
                    builder.append(weatherResponse.getCityName())
                            .append(" - ")
                            .append(weatherResponse.getWeatherDescription())

                            .append(System.getProperty("line.separator"));

                    builder.append("Temperatura: ")
                            .append(new java.text.DecimalFormat("#").format(weatherResponse.getTemperature()))
                            .append(weatherResponse.getTemperatureUnit())

                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    if (weatherResponse.getRain().toString().isEmpty()) {
                        builder.append("Brak opadów deszczu");
                    } else {
                        builder.append("Opady: ").append(weatherResponse.getRain().toString());
                    }

                    // F12, DB2D, B2, DB2U, Forward jump, B22DB2
                    builder.append(System.getProperty("line.separator"));

                    if (weatherResponse.getSnow() == null) {
                        builder.append("Brak opadow sniegu");
                    } else {
                        builder.append("Opady sniegu: ")
                                .append(weatherResponse.getSnow().toString()).append(weatherResponse.getSnow().getUnit());
                    }
                    builder.append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"))
                            .append("Zachmurzenie: ")
                            .append(weatherResponse.getClouds().getCloudiness()).append("%");

                    builder.append(System.getProperty("line.separator"));

                    builder.append("Wiatr: ")
                            .append(weatherResponse.getWind().getSpeed()).append(" m/s")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    builder.append("Minimalna: ")
                            .append(new java.text.DecimalFormat("#").format(weatherResponse.getWeatherInfo().getMinimumTemperature()))
                            .append(weatherResponse.getWeatherInfo().getTemperatureUnit())

                            .append(System.getProperty("line.separator"))

                            .append("Maksymalna: ")
                            .append(new java.text.DecimalFormat("#").format(weatherResponse.getWeatherInfo().getMaximumTemperature()))
                            .append(weatherResponse.getWeatherInfo().getTemperatureUnit())

                            .append(System.getProperty("line.separator"));

                    // tells when data was gathered
                    builder.append(System.getProperty("line.separator"))
                            .append("Dane z ")
                            .append(weatherResponse.getDataCalculationDate().toString());

                    builder.append("\n```");

                    builder.trimToSize();
                    chatbot.sendMessage(builder.toString());
                    return true;

                } catch (DataNotFoundException e) { e.printStackTrace(); }
                catch (InvalidAuthTokenException e) { e.printStackTrace(); }
            }
        }
        return false;
    }
}