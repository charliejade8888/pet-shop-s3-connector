package com.tyrell.replicant.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
class Request {
    private String hello;
    private String bla;
    private String blabla;
    private String cheese;
}
