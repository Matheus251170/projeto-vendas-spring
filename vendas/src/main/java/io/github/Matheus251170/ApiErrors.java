package io.github.Matheus251170;

import lombok.Data;

import java.util.Arrays;
import java.util.List;

@Data
public class ApiErrors {

    private List<String> errors;

    public ApiErrors(String msgErrors) {
        this.errors = Arrays.asList(msgErrors);
    }

    public ApiErrors(List<String> errors) {
        this.errors = errors;
    }
}
