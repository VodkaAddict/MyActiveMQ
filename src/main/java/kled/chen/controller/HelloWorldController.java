/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kled.chen.controller;

import kled.chen.JNDIActivemqSender;
import kled.chen.form.HelloPlainRequest;
import kled.chen.form.HelloRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.Topic;

@RestController
public class HelloWorldController {

	@Autowired
	private Queue myQueue;

	@Autowired
	private Queue myJacksonQueue;

	@Autowired
	private Queue mySerializableQueue;

	@Autowired
	private Queue myRpcQueue;

	@Autowired
	private Topic myTopic;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	private JmsMessagingTemplate jmsMessagingTemplate;

	@Autowired
	private JNDIActivemqSender jndiActivemqSender;

	@PostMapping("/sendP2PMsg/{msg}")
	public String sendP2PMsg(@PathVariable String msg) {
		jmsTemplate.convertAndSend(myQueue, msg);
		return "SUCCESS";
	}

	@PostMapping("/sendP2PMsgAndReceive/{msg}")
	public String sendP2PMsgAndReceive(@PathVariable String msg) throws JMSException {
		String rpcResponse = jmsMessagingTemplate.convertSendAndReceive(myRpcQueue.getQueueName(), msg, String.class);
		return rpcResponse;
	}

	@PostMapping("/sendP2PMsgByJNDI/{msg}")
	public String sendP2PMsgByJNDI(@PathVariable String msg) throws JMSException {
		jndiActivemqSender.produceMsg(msg, myQueue);
		return "SUCCESS";
	}

	@PostMapping("/sendP2PSerializableMsg")
	public String sendP2PSerializableMsg(@RequestBody HelloRequest request) {
		System.out.println("sendP2PSerializableMsg:" + request);
		jmsTemplate.convertAndSend(mySerializableQueue, request);
		return "SUCCESS";
	}

	//消息实体不需要序列化，通过JsonConverter转化
	@PostMapping("/sendP2PPlainMsg")
	public String sendP2PPlainMsg(@RequestBody HelloPlainRequest request) {
		System.out.println("sendP2PPlainMsg:" + request);
		jmsTemplate.convertAndSend(myJacksonQueue, request);
		return "SUCCESS";
	}

	@PostMapping("/sendTopicMsg/{msg}")
	public String sendTopicMsg(@PathVariable String msg) {
		jmsTemplate.convertAndSend(myTopic, msg);
		return "SUCCESS";
	}
}
