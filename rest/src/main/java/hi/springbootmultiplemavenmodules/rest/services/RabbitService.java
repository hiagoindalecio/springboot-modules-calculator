package hi.springbootmultiplemavenmodules.rest.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import hi.springbootmultiplemavenmodules.library.constants.RabbitConstants;
import hi.springbootmultiplemavenmodules.library.constants.SystemConstants;
import hi.springbootmultiplemavenmodules.library.dto.CalculationDTO;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import hi.springbootmultiplemavenmodules.library.util.JsonCalculationTools;

import java.util.HashMap;

@Slf4j
@Service
public class RabbitService {
    @Autowired
    private RabbitTemplate aRabbitTemplate;

    /**
     * Generate Rabbit message
     * @param pMessage dto instance
     * @return generated message
     */
    private Message rabbitMessage(CalculationDTO pMessage) throws JsonProcessingException {
        String vMdcCorrelationId = MDC.get(SystemConstants.CORRELATION_ID);
        String vMdcOperation = MDC.get(SystemConstants.ORIGIN);

        Message vMessage = MessageBuilder.withBody(JsonCalculationTools.serialize(pMessage).getBytes()).build();
        vMessage.getMessageProperties().setHeader(SystemConstants.CORRELATION_ID, vMdcCorrelationId);
        vMessage.getMessageProperties().setHeader(SystemConstants.ORIGIN, vMdcOperation);

        return vMessage;
    }

    /**
     * Send Rabbit message
     * @param pMessage dto instance
     * @return sent response
     */
    public String sendMessage(CalculationDTO pMessage) {
        try {
            Message vMessage = rabbitMessage(pMessage);

            log.trace("client service send：{}", vMessage.toString());

            Message vResult = aRabbitTemplate.sendAndReceive(RabbitConstants.RPC_EXCHANGE, RabbitConstants.QUEUE_OP_MSG,
                    vMessage);

            String vResponse = "";
            if (vResult != null) {
                String vCorrelationId = vMessage.getMessageProperties().getCorrelationId();

                HashMap<String, Object> vHeaders = (HashMap<String, Object>) vResult.getMessageProperties().getHeaders();

                String vMsgId = (String) vHeaders.get("spring_returned_message_correlation");

                if (vMsgId.equals(vCorrelationId)) {
                    vResponse = new String(vResult.getBody());
                    log.trace("Client service receive：{}", vResponse);
                }
            }
            return vResponse;

        } catch (Exception pException) {
            pException.printStackTrace();
            log.trace("Error", pException.toString());
            return "Error";
        }
    }
}
