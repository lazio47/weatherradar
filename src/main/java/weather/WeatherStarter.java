package weather;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import weather.ipma_client.IpmaCityForecast;
import weather.ipma_client.IpmaService;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * demonstrates the use of the IPMA API for weather forecast
 */
public class WeatherStarter {

    private static Logger logger = LogManager.getLogger(WeatherStarter.class);

    public static void  main(String[] args ) {
        logger.info("Program starts!");
        // get a retrofit instance, loaded with the GSon lib to convert JSON into objects
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.ipma.pt/open-data/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // create a typed interface to use the remote API (a client)
        IpmaService service = retrofit.create(IpmaService.class);

        int cityId = 0;
        try {
            // Processing the arg
            cityId = Integer.parseInt(args[0]);
            logger.info("Valid argument");
        } catch (Exception e) {
            System.err.println("The city ID must be an integer Number.");
            logger.error("Type conversion Error: ", e);
        }
        // prepare the call to remote endpoint
        Call<IpmaCityForecast> callSync = service.getForecastForACity(cityId);

        try {
            Response<IpmaCityForecast> apiResponse = callSync.execute();
            IpmaCityForecast forecast = apiResponse.body();

            if (forecast != null) {
                var firstDay = forecast.getData().listIterator().next();
                System.out.println("------------------------------------------------");
                System.out.println(firstDay.toString());
                System.out.println("------------------------------------------------");
                logger.info("Program executed successfully!");
            } else {
                System.out.println( "No results for this request!");
                logger.info("No results for this request!");
            }
        } catch (Exception ex) {
            logger.error("Error: ", ex);
            ex.printStackTrace();
        }
        logger.info("Program ends!");

    }
}