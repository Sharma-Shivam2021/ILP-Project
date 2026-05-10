package com.hwscs.backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestHistoryResponseDto {

    private Integer id;

    private String actorUsername;

    private String actorRole;

    private String action;

    private String remarks;

    private LocalDateTime actedAt;
}