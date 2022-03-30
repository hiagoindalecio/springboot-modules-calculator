package consumer;

import constants.RabbitConstants;
import constants.SystemConstants;
import dto.CalculationDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.EnumOperation;
import util.JsonCalculationTools;

import java.io.IOException;
import java.math.RoundingMode;

@Slf4j
@Component
public class CalculatorConsumer {

    @Autowired
    private RabbitTemplate aRabbitTemplate;

    @RabbitListener(queues = RabbitConstants.QUEUE_OP_MSG)
    public void process(Message pMessage) {
        try {
            logMdc(pMessage);

            log.trace("Server consumer received: {}" + pMessage.toString());

            String vResult = null;

            try {
                CalculationDTO vCalculationDTO = JsonCalculationTools.deserialize(new String(pMessage.getBody()));
                EnumOperation vOperation = vCalculationDTO.getOperation();
                switch (vOperation) {
                    case SUM:
                        vResult = vCalculationDTO.getFirstValue().add(vCalculationDTO.getSecondValue()).toString();
                        break;
                    case SUBTRACTION:
                        vResult = vCalculationDTO.getFirstValue().subtract(vCalculationDTO.getSecondValue()).toString();
                        break;
                    case DIVISION:
                        vResult = vCalculationDTO.getFirstValue().divide(vCalculationDTO.getSecondValue()).toString();
                        break;
                    case MULTIPLICATION:
                        vResult = vCalculationDTO.getFirstValue().multiply(vCalculationDTO.getSecondValue()).toString();
                        break;
                    default:
                        log.info("Operation not found!");
                }
            } catch (IOException pException) {
                triggerException(pException);
                vResult = "Input/Output management failure!";
            } catch (Exception pException) {
                triggerException(pException);
                vResult = "Operation failed!";
            }

            log.trace("Result {}", vResult);

            Message response = MessageBuilder.withBody((vResult).getBytes()).build();

            CorrelationData correlationData = new CorrelationData(pMessage.getMessageProperties().getCorrelationId());
            aRabbitTemplate.sendAndReceive(RabbitConstants.RPC_EXCHANGE, RabbitConstants.QUEUE_OP_REPLY, response,
                    correlationData);
        } finally {
            MDC.remove(SystemConstants.CORRELATION_ID);
            MDC.remove(SystemConstants.ORIGIN);
        }
    }

    private void triggerException(Exception pException) {
        log.trace("Error", pException.toString());
        pException.printStackTrace();
    }

    private void logMdc(Message pMessage) {
        String vHeaderCorrelationId = pMessage.getMessageProperties().getHeader(SystemConstants.CORRELATION_ID);
        String vHeaderOperation = pMessage.getMessageProperties().getHeader(SystemConstants.ORIGIN);

        MDC.put(SystemConstants.CORRELATION_ID, vHeaderCorrelationId);
        MDC.put(SystemConstants.ORIGIN, vHeaderOperation);
    }
}
