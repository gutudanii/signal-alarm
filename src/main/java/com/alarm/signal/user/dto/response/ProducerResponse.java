package com.alarm.signal.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProducerResponse {
    private UUID id;
    private UUID userId;
    private String name;
    private Instant createdAt;
}

