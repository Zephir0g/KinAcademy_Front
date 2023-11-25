package com.fo4ik.kinacademy.core;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Response {

    boolean isSuccess;
    String message;
    HttpStatus httpStatus;
}
