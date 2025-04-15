package com.aktic.indussahulatbackend.model.response;

import com.aktic.indussahulatbackend.model.request.LocationDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveEventUpdateDTO {
    private Long eventId;
    private String eventStatus;
    private LocationDTO location;
}
