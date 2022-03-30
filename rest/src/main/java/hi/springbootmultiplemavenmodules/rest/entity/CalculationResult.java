package hi.springbootmultiplemavenmodules.rest.entity;

import lombok.Getter;

@Getter
public class CalculationResult {
    private String result;

    public CalculationResult(String result) {
        super();
        this.result = result;
    }
}
