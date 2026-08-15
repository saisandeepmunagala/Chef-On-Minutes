package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDTO {
    private Long id;
    private String line1;
    private String line2;
    private String city;
    private String state;
    private String pincode;
    private String label;
    private Boolean isDefault;
}
