package ssg.osd.osapi.dto;

import lombok.Data;

@Data
public class CancelForm {
    private Long orderId;
    private Long productId;
}
