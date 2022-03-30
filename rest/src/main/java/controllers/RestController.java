package controllers;

import constants.SystemConstants;
import dto.CalculationDTO;
import entity.CalculationResult;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import services.RabbitService;
import util.EnumOperation;

import javax.validation.Valid;

@Slf4j
@org.springframework.web.bind.annotation.RestController
@RequestMapping(value = "operation")
public class RestController {
    @Autowired
    private RabbitService aRabbitmqService;

    /**
     * Send a division Rabbit message
     * @param pCalculationDTO dto instance
     * @return Rabbit message sent response
     */
    @GetMapping(
            value = "/division",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CalculationResult> division(@Valid CalculationDTO pCalculationDTO) {
        MDC.put(SystemConstants.ORIGIN, EnumOperation.DIVISION.getName());
        log.trace("Request received : {}", pCalculationDTO.toString());
        pCalculationDTO.setOperation(EnumOperation.DIVISION);

        String vResult = this.aRabbitmqService.sendMessage(pCalculationDTO);

        CalculationResult vResultOperation = new CalculationResult(vResult);

        return new ResponseEntity<CalculationResult>(vResultOperation, HttpStatus.OK);
    }

    /**
     * Send a multiplication Rabbit message
     * @param pCalculationDTO dto instance
     * @return Rabbit message sent response
     */
    @GetMapping(
            value = "/multiplication",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CalculationResult> multiplication(@Valid CalculationDTO pCalculationDTO) {
        MDC.put(SystemConstants.ORIGIN, EnumOperation.MULTIPLICATION.getName());
        log.trace("Request received : {}", pCalculationDTO.toString());
        pCalculationDTO.setOperation(EnumOperation.MULTIPLICATION);

        String vResult = this.aRabbitmqService.sendMessage(pCalculationDTO);

        CalculationResult vResultOperation = new CalculationResult(vResult);

        return new ResponseEntity<CalculationResult>(vResultOperation, HttpStatus.OK);
    }

    /**
     * Send a subtraction Rabbit message
     * @param pCalculationDTO dto instance
     * @return Rabbit message sent response
     */
    @GetMapping(
            value = "/subtraction",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<CalculationResult> subtraction(@Valid CalculationDTO pCalculationDTO) {
        MDC.put(SystemConstants.ORIGIN, EnumOperation.SUBTRACTION.getName());
        log.trace("Request received : {}", pCalculationDTO.toString());
        pCalculationDTO.setOperation(EnumOperation.SUBTRACTION);

        String vResult = this.aRabbitmqService.sendMessage(pCalculationDTO);

        CalculationResult vResultOperation = new CalculationResult(vResult);

        return new ResponseEntity<CalculationResult>(vResultOperation, HttpStatus.OK);
    }

    /**
     * Send a sum Rabbit message
     * @param pCalculationDTO dto instance
     * @return Rabbit message sent response
     */
    @GetMapping(
            value = "/sum",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CalculationResult> sum(@Valid CalculationDTO pCalculationDTO) {
        MDC.put(SystemConstants.ORIGIN, EnumOperation.SUM.getName());
        log.trace("Request received : {}", pCalculationDTO.toString());
        pCalculationDTO.setOperation(EnumOperation.SUM);

        String vResult = this.aRabbitmqService.sendMessage(pCalculationDTO);

        CalculationResult vResultOperation = new CalculationResult(vResult);

        return new ResponseEntity<CalculationResult>(vResultOperation, HttpStatus.OK);
    }
}
