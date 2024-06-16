package dev.lxqtpr.linda.t1task.clients;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.lxqtpr.linda.t1task.dto.CreateCandidateDto;
import dev.lxqtpr.linda.t1task.dto.RoleResponse;
import dev.lxqtpr.linda.t1task.dto.SetStatusDto;
import dev.lxqtpr.linda.t1task.exceptions.RequestException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@RequiredArgsConstructor
@Slf4j
public class T1Client {

    @Value("${clients.url.t1}")
    private String baseUrl;

    private final RestTemplate restTemplate;

    public RoleResponse getRoles() {
        return restTemplate.getForObject("/get-roles", RoleResponse.class);
    }

    public String getCode(@NonNull String email){
        var builder = UriComponentsBuilder
                .fromUriString("/get-code")
                .queryParam("email", email);
        return restTemplate.getForObject(builder.toUriString(), String.class);
    }

    @SneakyThrows
    public String sighUp(@NonNull CreateCandidateDto createCandidateDto){
        var url = new URL(baseUrl.concat("/sign-up"));

        var connection = configureRequestConnection(url, createCandidateDto);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getRequestResponse(connection);
        } else {
            throw new RequestException("Ошибка при отправке запроса. Код ответа:" + responseCode);
        }
    }

    @SneakyThrows
    public String setStatus(@NonNull SetStatusDto setStatusDto){
        var url = new URL(baseUrl.concat("/set-status"));

        var connection = configureRequestConnection(url, setStatusDto);

        var responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            return getRequestResponse(connection);
        } else {
            throw new RequestException("Ошибка при отправке запроса. Код ответа:" + responseCode);
        }
    }

    @SneakyThrows
    private String getRequestResponse(@NonNull HttpURLConnection connection){
        try(BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
            String inputLine;
            var response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString().replaceAll("^\"|\"$", "");
        }
    }

    @SneakyThrows
    private <T> HttpURLConnection configureRequestConnection(@NonNull URL url, @NonNull T objectToSend ){
        var mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        var json = mapper.writeValueAsString(objectToSend);

        var connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json; utf-8");
        connection.setRequestProperty("Accept", "application/json");
        connection.setDoOutput(true);

        try(OutputStream os = connection.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
        }

        return connection;
    }
}
