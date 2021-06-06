package com.laurentiuspilca.liveproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.laurentiuspilca.liveproject.entities.HealthMetric;
import com.laurentiuspilca.liveproject.entities.HealthProfile;
import com.laurentiuspilca.liveproject.repositories.HealthMetricRepository;
import com.laurentiuspilca.liveproject.repositories.HealthProfileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StreamUtils;


import java.nio.charset.Charset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class Milestone2ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("classpath:health-advice-list.json")
    private Resource healthAdviceListJson;

    @Value("classpath:health-profile-post-request.json")
    private Resource healthProfilePostRequestJson;

    @Value("classpath:health-metric-post-request.json")
    private Resource healthMetricPostRequestJson;

    @Value("classpath:sample-jwt-token.txt")
    private Resource sampleJwtTokenTxt;

    @Autowired
    private HealthProfileRepository healthProfileRepository;

    @Autowired
    private HealthMetricRepository healthMetricRepository;

    @Test
    @DirtiesContext
    public void testPostHealthProfileShouldPass() throws Exception {
        //act & assert
        this.mockMvc.perform(post("/profile")
            .with(jwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                    .subject("ken")
                    .header("typ", "JWT")
                    .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthProfilePostRequestJson.getInputStream()))
        ).andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void testPostHealthProfileShouldFail() throws Exception {
        //act & assert
        this.mockMvc.perform(post("/profile")
                .with(jwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                        .subject("tom")
                        .header("typ", "JWT")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthProfilePostRequestJson.getInputStream()))
        ).andExpect(status().isForbidden());

        //act & assert
        this.mockMvc.perform(post("/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthProfilePostRequestJson.getInputStream()))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    public void testGetHealthProfileShouldPass() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        //act & assert
        this.mockMvc.perform(get("/profile/ken")
                .with(jwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                        .subject("ken")
                        .header("typ", "JWT")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(healthProfile.getId()))
                .andExpect(jsonPath("$.username").value("ken"));

        this.mockMvc.perform(get("/profile/ken")
                .with(jwt().authorities(() -> "admin"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(healthProfile.getId()))
                .andExpect(jsonPath("$.username").value("ken"));
    }

    @Test
    public void testGetHealthProfileShouldFail() throws Exception {
        this.mockMvc.perform(get("/profile/ken")
                .with(jwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                        .subject("tom")
                        .header("typ", "JWT")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(get("/profile/ken")
                .with(jwt().authorities(() -> "tester"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(get("/profile/ken")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    public void testDeleteHealthProfileShouldPass() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        //act & assert
        this.mockMvc.perform(delete("/profile/ken")
                .with(jwt().authorities(() -> "admin"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DirtiesContext
    public void testDeleteHealthProfileShouldFail() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        //act & assert
        this.mockMvc.perform(delete("/profile/ken")
                .with(jwt().authorities(() -> "tester"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(delete("/profile/ken")
                .with(jwt().jwt(Jwt.withTokenValue(StreamUtils.copyToString(this.sampleJwtTokenTxt.getInputStream(), Charset.defaultCharset()))
                        .subject("ken")
                        .header("typ", "JWT")
                        .build()))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(delete("/profile/ken")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    public void testPostHealthMetricShouldPass() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        this.healthProfileRepository.save(healthProfile);

        //act & assert
        this.mockMvc.perform(post("/metric")
            .with(jwt().authorities(() -> "SCOPE_advice"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthMetricPostRequestJson.getInputStream()))
        ).andExpect(status().isOk());
    }

    @Test
    public void testPostHealthMetricShouldFail() throws Exception {
        //act & assert
        this.mockMvc.perform(post("/metric")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthMetricPostRequestJson.getInputStream()))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetHealthMetricShouldPass() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        HealthMetric healthMetric = this.objectMapper.readValue(
                healthMetricPostRequestJson.getInputStream(),
                HealthMetric.class
        );
        healthMetric.getProfile().setId(healthProfile.getId());
        this.healthMetricRepository.save(healthMetric);

        //act & assert
        this.mockMvc.perform(get("/metric/ken")
            .with(jwt().authorities(() -> "SCOPE_advice"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].value").value(2.0))
            .andExpect(jsonPath("$[0].type").value("HEART_RATE"))
            .andExpect(jsonPath("$[0].profile.username").value("ken"))
            .andExpect(jsonPath("$[0].profile.id").value(healthProfile.getId()));
    }

    @Test
    public void testGetHealthMetricShouldFail() throws Exception {
        //act & assert
        this.mockMvc.perform(get("/metric/ken")
            .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @DirtiesContext
    public void testDeleteHealthMetricShouldPass() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        HealthMetric healthMetric = this.objectMapper.readValue(
                healthMetricPostRequestJson.getInputStream(),
                HealthMetric.class
        );
        healthMetric.getProfile().setId(healthProfile.getId());
        this.healthMetricRepository.save(healthMetric);

        //act & assert
        this.mockMvc.perform(delete("/metric/ken")
                .with(jwt().authorities(() -> "admin"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        MvcResult result = this.mockMvc.perform(get("/metric/ken")
                .with(jwt().authorities(() -> "SCOPE_advice"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        assertThat(responseBody).isEqualTo("[]");
    }

    @Test
    @DirtiesContext
    public void testDeleteHealthMetricShouldFail() throws Exception {
        //arrange
        HealthProfile healthProfile = this.objectMapper.readValue(
                healthProfilePostRequestJson.getInputStream(),
                HealthProfile.class
        );
        healthProfile = this.healthProfileRepository.save(healthProfile);

        HealthMetric healthMetric = this.objectMapper.readValue(
                healthMetricPostRequestJson.getInputStream(),
                HealthMetric.class
        );
        healthMetric.getProfile().setId(healthProfile.getId());
        this.healthMetricRepository.save(healthMetric);

        //act & assert
        this.mockMvc.perform(delete("/metric/ken")
                .with(jwt().authorities(() -> "read"))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(delete("/metric/ken")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isUnauthorized());
    }

    @Test
    public void testPostHealthAdviceShouldPass() throws Exception {
        //act & assert
        this.mockMvc.perform(post("/advice")
            .with(jwt().authorities(() -> "SCOPE_advice"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthAdviceListJson.getInputStream()))
        ).andExpect(status().isOk());
    }

    @Test
    public void testPostHealthAdviceShouldFail() throws Exception {
        //act & assert
        this.mockMvc.perform(post("/advice")
                .with(jwt().authorities(() -> "SCOPE_user"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthAdviceListJson.getInputStream()))
        ).andExpect(status().isForbidden());

        this.mockMvc.perform(post("/advice")
                .contentType(MediaType.APPLICATION_JSON)
                .content(StreamUtils.copyToByteArray(healthAdviceListJson.getInputStream()))
        ).andExpect(status().isUnauthorized());
    }
}
