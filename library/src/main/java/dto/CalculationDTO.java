package dto;

import lombok.Getter;
import lombok.Setter;
import util.EnumOperation;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

public class CalculationDTO implements Serializable {
    /** */
    private static final long serialVersionUID = -3774290452726264152L;

    @NotNull
    @Getter
    @Setter
    private BigDecimal firstValue;

    @NotNull
    @Getter
    @Setter
    private BigDecimal secondValue;

    @Getter
    @Setter
    private EnumOperation operation;

    @Override
    public String toString() {
        return "---CalculationDTO---\nfirstValue=" + firstValue + "\nsecondvalue=" + secondValue + "\noperation=" + operation + "]";
    }
}
