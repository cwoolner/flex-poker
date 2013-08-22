package com.flexpoker.dto;

import java.util.ArrayList;
import java.util.List;

public class Result {

    private List<String> errors;

    private List<String> warnings;

    public Result() {
        errors = new ArrayList<>();
        warnings = new ArrayList<>();
    }

    public void addError(String error) {
        errors.add(error);
    }

    public void addWarning(String warning) {
        warnings.add(warning);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public boolean hasWarnings() {
        return !warnings.isEmpty();
    }

    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }

}
