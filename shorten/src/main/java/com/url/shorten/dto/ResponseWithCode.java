package com.url.shorten.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResponseWithCode {
    String errorMessage;
    int errorCode;
}
