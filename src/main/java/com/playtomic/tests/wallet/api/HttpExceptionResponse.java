package com.playtomic.tests.wallet.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HttpExceptionResponse {
    private String code;
    private String genericMessage;
    private String clarifiedMessage;
}
