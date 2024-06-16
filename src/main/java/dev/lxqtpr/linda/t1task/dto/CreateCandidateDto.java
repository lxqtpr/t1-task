package dev.lxqtpr.linda.t1task.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;


@Builder
public record CreateCandidateDto (
    @JsonProperty("last_name")
    String lastName,
    @JsonProperty("first_name")
    String firstName,
    @JsonProperty("email")
    String email,
    @JsonProperty("role")
    String role
) {}
