package com.chefonminutes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** No token yet - real JWT/session issuing is a later phase (see plan scope boundaries). */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponseDTO {
    private UserDTO user;
}
