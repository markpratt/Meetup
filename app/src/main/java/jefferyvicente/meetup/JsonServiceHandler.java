package jefferyvicente.meetup;

import java.io.IOException;
import java.io.UnsupportedEncodingException;



import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.os.StrictMode;
import java.lang.Object;

public class JsonServiceHandler {

    static String response ="";
    public final static int GET = 1;
    public final static int POST = 2;
    StringBuilder buf;


    public JsonServiceHandler() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

    }

    /**
     * Making service call
     * @url - url to make request
     * @method - http request method
     * @params - http request params
     * */
    public String makeServiceCall(String url, int method) {
        try {
            InputStream is = null;
            BufferedReader reader = null;
            String data=null;



            URL url1 = new URL(url);
            System.out.println(url1);
            URLConnection conn = (URLConnection) url1.openConnection();
            System.out.println("conn: "+conn);
            is = conn.getInputStream();
            System.out.println("is: "+is.toString());
            reader =new BufferedReader(new InputStreamReader(is, "UTF-8"));
            System.out.println("reader: " + reader.toString());
            buf=new StringBuilder();


            reader.mark(0);
            //is.mark(0);
            while ((data = reader.readLine()) != null){
                //response += data;
                buf.append(data);
            }
            is.close();
            reader.close();
            reader.reset();
            //is.reset();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("response: " + buf.toString());

        return buf.toString();

    }
}


