package util;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum EnumOperation {
    SUM('+', "Sum"),
    SUBTRACTION('-', "Subtraction"),
    MULTIPLICATION('*', "Multiplication"),
    DIVISION('/', "Division");

    private Character character;
    private String operationName;

    EnumOperation(Character pCharacter, String pOperationName) {
        this.character = pCharacter;
        this.operationName = pOperationName;
    }

    @JsonValue
    public String getName() {
        return operationName;
    }

    @JsonValue
    public Character getCharacter() {
        return character;
    }

    /**
     * @param pCharacter operation character
     * @return needed operation
     */
    public static EnumOperation getEnum(Character pCharacter) {
        for (EnumOperation vTemp : EnumOperation.values()) {
            if (vTemp.getCharacter().equals(pCharacter)) {
                return vTemp;
            }
        }
        return null;
    }

    /**
     * @param pName operation name
     * @return needed operation
     */
    @JsonCreator
    public static EnumOperation getEnum(String pName) {
        for (EnumOperation vTemp : EnumOperation.values()) {
            if (vTemp.getName().equalsIgnoreCase(pName)) {
                return vTemp;
            }
        }
        return EnumOperation.getEnum(pName.charAt(0));
    }
}
