package guru.hakandurmaz.ratelimiting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import guru.hakandurmaz.ratelimiting.dto.SendOtpRequest;
import guru.hakandurmaz.ratelimiting.utils.RedisClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = EmbeddedRedisServerConfig.class)
@AutoConfigureMockMvc
class SendOtpControllerTest {

    @Autowired
    RedisClient redisClient;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        redisClient.clean();
    }

    @Test
    void when_send_otp_request_then_return_ok() throws Exception {
        Faker faker = new Faker();
        SendOtpRequest sendOtpRequest = new SendOtpRequest(faker.phoneNumber()
                .phoneNumber());
        ResultActions perform = mockMvc.perform(post("/api/send-otp")
                .content(objectMapper.writeValueAsString(sendOtpRequest))
                .contentType(MediaType.APPLICATION_JSON));
        perform.andExpect(status().isOk());
    }

    @Test
    void when_sending_too_many_otp_request_then_throw_too_many_request() throws Exception {
        List<MvcResult> responses = new ArrayList<>();
        Faker faker = new Faker();
        SendOtpRequest sendOtpRequest = new SendOtpRequest(faker.phoneNumber()
                .phoneNumber());
        for (int i = 0; i < 3; i++) {
            MvcResult mvcResult = mockMvc.perform(post("/api/send-otp")
                            .content(objectMapper.writeValueAsString(sendOtpRequest))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andReturn();
            responses.add(mvcResult);
        }
        assertThat(responses.get(0)
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses.get(1)
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(responses.get(2)
                .getResponse()
                .getStatus()).isEqualTo(HttpStatus.TOO_MANY_REQUESTS.value());
    }
}