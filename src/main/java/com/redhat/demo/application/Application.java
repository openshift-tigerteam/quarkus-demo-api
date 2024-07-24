package com.redhat.demo.application;

import jakarta.validation.constraints.NotEmpty;

public record Application(Long applicationId, @NotEmpty(message = "{Application.name.required}") String name) {

}