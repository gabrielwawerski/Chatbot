package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.bot.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.BaseModule;
import bot.utils.bot.helper_class.Message;
import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.exception.InvalidAuthTokenException;
import com.github.prominence.openweathermap.api.model.response.Weather;

import java.io.IOException;
import java.util.List;

public class WeatherModule extends BaseModule {
    private OpenWeatherMapManager openWeatherManager;
    WeatherRequester weatherRequester;

    public static final String API_KEY = "996851e7e9d015a4a9ff48dee77dd9c9";
    public static final String LUBLIN_ID = "765876";

    public WeatherModule(Chatbot chatbot, List<String> commands) {
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

                    weatherResponse = openWeatherManager.getWeatherRequester()
                            .setLanguage(Language.POLISH)
                            .setUnitSystem(Unit.METRIC_SYSTEM)
                            .setAccuracy(Accuracy.ACCURATE)
                            .getByCityId(LUBLIN_ID);

                    builder.append(weatherResponse.getCityName());

                    builder.append(" - ")
                            .append(weatherResponse.getWeatherDescription())
                            .append(System.getProperty("line.separator"));

                    builder.append("Temperatura: ")
                            .append(weatherResponse.getTemperature()).append(" ").append(weatherResponse.getTemperatureUnit())
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    builder.append("Zachmurzenie: ")
                            .append(weatherResponse.getClouds().getCloudiness()).append("%")
                            .append(System.getProperty("line.separator"));

                    if (weatherResponse.getRain() == null) {
                        builder.append("Brak opadów")
                                .append(System.getProperty("line.separator"));
                    } else {
                        builder.append("Opady: ").append(weatherResponse.getRain().toString())
                                .append(System.getProperty("line.separator"));
                    }

                    builder.append("Wiatr: ")
                            .append(weatherResponse.getWind().getSpeed()).append("m/s")
                            .append(System.getProperty("line.separator"))
                            .append(System.getProperty("line.separator"));

                    builder.append("Minimalna: ")
                            .append(weatherResponse.getWeatherInfo().getMinimumTemperature())
                            .append(System.getProperty("line.separator"))
                            .append("Maksymalna: ")
                            .append(weatherResponse.getWeatherInfo().getMaximumTemperature())
                            .append(System.getProperty("line.separator"));

                    builder.append("Dane z ")
                            .append(weatherResponse.getDataCalculationDate().toString());

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
