package bot.modules.gabe_modules.Weather;

import bot.Chatbot;
import bot.utils.exceptions.MalformedCommandException;
import bot.utils.gabe_modules.modules_base.SimpleModule;
import bot.utils.helper_class.Message;
import com.github.prominence.openweathermap.api.OpenWeatherMapManager;
import com.github.prominence.openweathermap.api.WeatherRequester;
import com.github.prominence.openweathermap.api.constants.Accuracy;
import com.github.prominence.openweathermap.api.constants.Language;
import com.github.prominence.openweathermap.api.constants.Unit;
import com.github.prominence.openweathermap.api.exception.DataNotFoundException;
import com.github.prominence.openweathermap.api.exception.InvalidAuthTokenException;
import com.github.prominence.openweathermap.api.model.response.Weather;

import java.util.List;

public class WeatherModule extends SimpleModule {
    private OpenWeatherMapManager openWeatherManager = new OpenWeatherMapManager(API_KEY);
    WeatherRequester weatherRequester;

    String urlCall = jsonCall + LUBLIN_ID + "&appid=" + API_KEY;

    private static final String jsonCall = "http://api.openweathermap.org/data/2.5/weather?id=";
    public static final String API_KEY = "996851e7e9d015a4a9ff48dee77dd9c9";
    public static final String LUBLIN_ID = "765876";

    public WeatherModule(Chatbot chatbot, List<String> commands) {
        super(chatbot, commands);
    }

    public WeatherModule(Chatbot chatbot, List<String> commands, String cityId) {
        super(chatbot, commands);
    }

    @Override
    public boolean process(Message message) throws MalformedCommandException {
        updateMatch(message);

        Weather weatherResponse = new Weather();
        StringBuilder builder = new StringBuilder();

        builder.append("Lublin\n");

        for (String command : commands) {
            if (match.equals(command)) {
                try {
                    weatherResponse = openWeatherManager.getWeatherRequester()
                            .setLanguage(Language.POLISH)
                            .setUnitSystem(Unit.METRIC_SYSTEM)
                            .setAccuracy(Accuracy.ACCURATE)
                            .getByCityId(LUBLIN_ID);

                    builder.append(weatherResponse.getTemperature()).append(weatherResponse.getTemperatureUnit())
                            .append("\nOpady: ").append(weatherResponse.getRain().getRainVolumeLast3Hrs())
                            .append("\nWiatr: ").append(weatherResponse.getWind().getSpeed()).append(weatherResponse.getWind().getUnit());

                } catch (DataNotFoundException e) { e.printStackTrace(); }
                catch (InvalidAuthTokenException e) { e.printStackTrace(); }

                builder.toString();
                chatbot.sendMessage(builder.toString());
                return true;
            }
        }
        return false;
    }


}
