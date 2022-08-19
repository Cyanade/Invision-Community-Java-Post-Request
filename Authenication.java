package xyz.edge.ac.auth;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class Authenication {

    private final String AUTH_URL = "https://yoursite.com";

    public void sendRequest() {
        try {
            final byte[] urlParamsBytes = ("key=" + "Your licence key from config here").getBytes(StandardCharsets.UTF_8);
            int postDataParamLength = urlParamsBytes.length;
            final URL url = new URL(AUTH_URL + "/applications/nexus/interface/licenses/?info");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(urlParamsBytes);
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            final JsonElement parser = (new JsonParser()).parse(in);
            final JsonObject json = parser.getAsJsonObject();

            final String customerName = json.get("customer_name").getAsString();
            final String email = json.get("customer_email").getAsString();
            final String productName = json.get("purchase_name").getAsString();
            final int purchaseActive = json.get("purchase_active").getAsInt();

            connection.disconnect();

        } catch (Exception e) {

            /*
            You may want to remove this or else you may leak your backend information.
             */

            e.printStackTrace();
        }
    }

}
