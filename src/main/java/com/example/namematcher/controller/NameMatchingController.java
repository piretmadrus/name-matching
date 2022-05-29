package com.example.namematcher.controller;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.example.namematcher.domain.MatchingResult;
import com.example.namematcher.service.NameMatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/match", produces = APPLICATION_JSON_VALUE)
public class NameMatchingController {

  private final NameMatchingService nameMatchingService;

  @ResponseStatus(OK)
  @PostMapping(consumes = {MULTIPART_FORM_DATA})
  @Operation(summary = "Checks name matching against names in provided files")
  public ResponseEntity<MatchingResult> findMatchingNames(
      @Parameter(description = "Name must not be blank, max length 100") @RequestParam @NotBlank @Size(max = 100) String name,
      @Parameter(description = "Possible to add max 2 files") @RequestParam @Size(max = 2) List<MultipartFile> multipartFile) {

    MatchingResult matchingResult = nameMatchingService.findMatches(name, multipartFile);
    return new ResponseEntity<>(matchingResult, OK);
  }

}
