package com.example.namematcher.service;

import com.example.namematcher.config.FileConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

  private static final String LINE_TOO_LONG_MSG = "Line number: {} is too long. Not reading it";
  private static final String FILE_READING_FAILED_MSG = "Problem occured when reading file";

  private final FileConfig fileConfig;

  public List<String> readLines(MultipartFile file) {
    List<String> lines = new ArrayList<>();
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()));
      String line;
      int lineNumber = 0;
      while ((line = br.readLine()) != null) {
        lineNumber++;

        if (fileConfig.getFileLength() != 0) {
          if (lineNumber > fileConfig.getFileLength()) {
            break;
          }
        }

        if (fileConfig.getLineLength() != 0) {
          if (line.length() > fileConfig.getLineLength()) {
            log.info(LINE_TOO_LONG_MSG, lineNumber);
            continue;
          }
        }

        if (!line.isBlank()) {
          lines.add(line.trim());
        }
      }
    } catch (IOException e) {
      log.info(FILE_READING_FAILED_MSG);
      throw new RuntimeException(FILE_READING_FAILED_MSG);
    }
    return lines;
  }
}
