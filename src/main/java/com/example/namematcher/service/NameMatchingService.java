package com.example.namematcher.service;

import static java.util.stream.Collectors.toList;

import com.example.namematcher.domain.MatchingResult;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class NameMatchingService {

  private static final String NAME_SPLITTING_REGEX = "\\s+|-|,|'";

  private final FileService fileService;

  public MatchingResult findMatches(String name, List<MultipartFile> files) {
    List<String> namesToCompare = getNamesFromFiles(files);

    Set<String> partialMatches = new HashSet<>();
    MatchingResult matchingResult = new MatchingResult();
    matchingResult.setPartialMatches(partialMatches);

    namesToCompare.stream()
        .forEach(nameToCompare -> {
          if (isExactMatch(name, nameToCompare)) {
            matchingResult.setExactMatch(name);
          } else if (isPartialMatch(name, nameToCompare)) {
            matchingResult.getPartialMatches().add(nameToCompare);
          }
        });
    return matchingResult;
  }

  private List<String> getNamesFromFiles(List<MultipartFile> files) {
    List<String> names = new ArrayList<>();
    files.stream()
        .forEach(file -> names.addAll(fileService.readLines(file)));
    return names;
  }

  private boolean isExactMatch(String name, String nameToCompare) {
    return name.equalsIgnoreCase(nameToCompare);
  }

  private boolean isPartialMatch(String name, String nameToCompare) {
    List<String> nameParts = getNameParts(name);
    List<String> namePartsToCompare = getNameParts(nameToCompare);
    List<String> matches = nameParts.stream()
        .filter(namePart -> !namePart.isBlank())
        .filter(namePart -> !namePartsToCompare.stream()
            .filter(namePartToCompare -> namePartToCompare.equalsIgnoreCase(namePart)).collect(toList()).isEmpty())
        .collect(toList());
    return matches.size() >= 2 ? true : false;
  }

  private List<String> getNameParts(String name) {
    return Arrays.asList(name.toLowerCase().split(NAME_SPLITTING_REGEX));
  }
}
