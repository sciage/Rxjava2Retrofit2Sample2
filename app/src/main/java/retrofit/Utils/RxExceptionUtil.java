package retrofit.Utils;


import org.json.JSONException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import retrofit2.HttpException;

/**
 * Exception handling
 */
public class RxExceptionUtil {
    public static String exceptionHandler(Throwable e){
        String errorMsg = "unknown mistake";
        if (e instanceof UnknownHostException) {
            errorMsg = "Network Unavailable";
        } else if (e instanceof SocketTimeoutException) {
            errorMsg = "Request network timeout";
        } else if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            errorMsg = convertStatusCode(httpException);
        } else if (e instanceof ParseException || e instanceof JSONException
                || e instanceof JSONException) {
            errorMsg = "Data parsing error";
        }
        return errorMsg;
    }

    private static String convertStatusCode(HttpException httpException) {
        String msg;
        if (httpException.code() >= 500 && httpException.code() < 600) {
            msg = "Server processing request error";
        } else if (httpException.code() >= 400 && httpException.code() < 500) {
            msg = "The server cannot process the request";
        } else if (httpException.code() >= 300 && httpException.code() < 400) {
            msg = "The request was redirected to another page";
        } else {
            msg = httpException.message();
        }
        return msg;
    }
}
