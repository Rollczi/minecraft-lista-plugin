package dev.rollczi.minecraftlista.vote;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class VoteHttpRequestReceived {

    private static final String RECEIVE_AWARD_URL = "https://minecraft-lista.pl/api/receive/%s";

    private final OkHttpClient client;
    private final VoteHttpSettings voteHttpSettings;

    VoteHttpRequestReceived(OkHttpClient client, VoteHttpSettings voteHttpSettings) {
        this.client = client;
        this.voteHttpSettings = voteHttpSettings;
    }

    List<VoteHttpDto> receive() {
        Request request = new Request.Builder()
                .url(String.format(RECEIVE_AWARD_URL, voteHttpSettings.serverId()))
                .header("User-Agent", "MinecraftListaPlugin/1.1.0")
                .header("Authorization", voteHttpSettings.apiKey())
                .get()
                .build();

        try (Response response = client.newCall(request).execute()) {
            return this.handleResponse(response);
        } catch (IOException | JSONException exception) {
            throw new VoteException(exception);
        }
    }

    private List<VoteHttpDto> handleResponse(Response response) {
        try {
            if (!response.isSuccessful()) {
                throw VoteException.newException(response);
            }

            ResponseBody responseBody = response.body();

            if (responseBody == null) {
                throw new IOException("Response body is null");
            }

            JSONArray list = new JSONArray(responseBody.string());
            List<VoteHttpDto> votes = new ArrayList<>();

            for (Object vote : list) {
                votes.add(this.extractVote(vote));
            }

            return votes;
        } catch (IOException | JSONException exception) {
            throw new VoteException(exception);
        }
    }

    private VoteHttpDto extractVote(Object obj) {
        if (!(obj instanceof JSONObject)) {
            throw new JSONException("Unexpected type " + obj.getClass());
        }

        JSONObject jsonObject = (JSONObject) obj;

        return new VoteHttpDto(jsonObject.getInt("id"), jsonObject.getString("nickname"));
    }

}
