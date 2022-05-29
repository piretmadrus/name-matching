package com.example.namematcher.controller;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.util.StreamUtils.copyToString;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class NameMatchingControllerIntegrationTest {

  private static final String NAME_MATCHING_URL = "/match";

  @Getter
  @Autowired
  private MockMvc mockMvc;

  @Value("classpath:data/blackList.txt")
  private Resource blackList;

  @Value("classpath:data/matchingResult.json")
  private Resource matchingResult;

  @Test
  void findMatchingNamesReturnsExactMatchAndPartialMatches() throws Exception {
    mockMvc.perform(multipart(NAME_MATCHING_URL)
            .file(getMultipartFile())
            .characterEncoding(UTF_8)
            .param("name", "Osama Bin Laden"))
        .andExpect(status().isOk())
        .andExpect(content().json(getContent(matchingResult)));
  }

  private MockMultipartFile getMultipartFile() throws Exception {
    return new MockMultipartFile(
        "multipartFile",
        getContent(blackList).getBytes(UTF_8));
  }

  private static String getContent(Resource resource) throws IOException {
    return copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
  }
}