package com.example.namematcher.domain;

import java.util.Set;
import lombok.Data;

@Data
public class MatchingResult {

  String exactMatch;
  Set<String> partialMatches;
}
