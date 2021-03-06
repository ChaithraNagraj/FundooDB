package com.bridgelabz.fundoonotes.configuration;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.bridgelabz.fundoonotes.response.MailObject;

/**
 * 
 * @author chaithra B N
 *
 */
@Component
public class RabbitmqSender {

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("rmq.rube.exchange")
	private String exchange;

	@Value("rube.key")
	private String routingkey;

	public void send(MailObject message) {
		rabbitTemplate.convertAndSend(exchange, routingkey, message);
	}
}
