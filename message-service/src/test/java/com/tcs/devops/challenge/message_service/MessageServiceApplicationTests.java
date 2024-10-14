package com.tcs.devops.challenge.message_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import org.springframework.http.MediaType;


@SpringBootTest
@AutoConfigureMockMvc
class MessageServiceApplicationTests {

	 @Autowired
    private MockMvc mockMvc;

    @Test
    public void testPostRequest() throws Exception {
        String requestJson = "{\"message”\":\"This is a test\", \"to\":\"Juan Perez\", \"from\":\"Rita Asturia\", \"timeToLifeSec\":45}";

        mockMvc.perform(post("/DevOps")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Hello Juan Perez your message will be send"));
    }

    @Test
    public void testGetRequest() throws Exception {
        mockMvc.perform(get("/DevOps"))
                .andExpect(status().isMethodNotAllowed())  
                .andExpect(content().string("ERROR"));     
    }

	@Test
    public void testPutRequest() throws Exception {
        mockMvc.perform(put("/DevOps"))
                .andExpect(status().isMethodNotAllowed())  
                .andExpect(content().string("ERROR"));     
    }

	@Test
    public void testDeleteRequest() throws Exception {
        mockMvc.perform(delete("/DevOps"))
                .andExpect(status().isMethodNotAllowed())  
                .andExpect(content().string("ERROR"));     
    }

}
