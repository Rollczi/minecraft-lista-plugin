package dev.rollczi.minecraftlista.request;

import dev.rollczi.minecraftlista.award.AwardException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class RequestReceivedVote {

    private static final String RECEIVE_AWARD_URL = "https://minecraft-lista.pl/api/receive/%s";

    private final OkHttpClient client;
    private final RequestSettings requestSettings;

    RequestReceivedVote(OkHttpClient client, RequestSettings requestSettings) {
        this.client = client;
        this.requestSettings = requestSettings;
    }

    List<Vote> receive() {
        Request request = new Request.Builder()
                .url(String.format(RECEIVE_AWARD_URL, requestSettings.serverId()))
                .header("User-Agent", "MinecraftListaPlugin/1.0.0")
                .header("Authorization", requestSettings.apiKey())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return this.handleResponse(response);
        } catch (IOException | JSONException exception) {
            throw new AwardException(exception);
        }
    }

    private List<Vote> handleResponse(Response response) {
        try {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new IOException("Response body is null");
            }

            JSONArray list = new JSONArray(responseBody.string());
            List<Vote> votes = new ArrayList<>();

            for (Object vote : list) {
                votes.add(this.extractVote(vote));
            }

            return votes;
        } catch (IOException | JSONException exception) {
            throw new AwardException(exception);
        }
    }

    private Vote extractVote(Object obj) {
        if (!(obj instanceof JSONObject)) {
            throw new JSONException("Unexpected type " + obj.getClass());
        }

        JSONObject jsonObject = (JSONObject) obj;

        return new Vote(jsonObject.getInt("id"), jsonObject.getString("nickname"));
    }

}
