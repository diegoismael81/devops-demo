package com.tcs.devops.challenge.auth_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO {
    private String message;
    private boolean status;
}
