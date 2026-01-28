package com.concertfinder.concertfinder.sse.DTO;

import lombok.*;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SseSendRequestDTO {

    private String eventName;

    private Object data;
}
