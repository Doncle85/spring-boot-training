/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package demo;

import com.solacesystems.jcsmp.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Component
    static class Runner implements CommandLineRunner {

        private static final Logger logger = LoggerFactory.getLogger(Runner.class);

        private final Topic topic = JCSMPFactory.onlyInstance().createTopic("broadcast/topic");
        private final Queue queue = JCSMPFactory.onlyInstance().createQueue("leah");

        @Autowired
        private SpringJCSMPFactory solaceFactory;

        private MessageConsumer msgConsumer = new MessageConsumer();
        private PublishEventHandler pubEventHandler = new PublishEventHandler();
        private QueueConsumer queueConsumer = new QueueConsumer();

        public void run(String... strings) throws Exception {

            final JCSMPSession session = solaceFactory.createSession();

            session.addSubscription(topic);
            logger.info("Connected. Awaiting message...");

            // consumer starting
            XMLMessageConsumer messageConsumer = session.getMessageConsumer(msgConsumer);
            messageConsumer.start();

            XMLMessageProducer producer = session.getMessageProducer(pubEventHandler);
            producer.send(createTextMessage("Hello", DeliveryMode.DIRECT), topic);
            waitForMessage();
            producer.send(createTextMessage("This is Leah", DeliveryMode.DIRECT), topic);
            waitForMessage();


            // queue
            final EndpointProperties endpointProps = new EndpointProperties();
            endpointProps.setPermission(EndpointProperties.PERMISSION_CONSUME);
            endpointProps.setAccessType(EndpointProperties.ACCESSTYPE_EXCLUSIVE);
            session.provision(queue, endpointProps, JCSMPSession.FLAG_IGNORE_ALREADY_EXISTS);

            // flow props
            final ConsumerFlowProperties flowProperties = new ConsumerFlowProperties();
            flowProperties.setEndpoint(queue);
            flowProperties.setAckMode(JCSMPProperties.SUPPORTED_MESSAGE_ACK_CLIENT);

            FlowReceiver flowReceiver = session.createFlow(queueConsumer, flowProperties, endpointProps);
            flowReceiver.start();

            producer.send(createTextMessage("@dishand How is the weather ?", DeliveryMode.PERSISTENT), queue);
            waitForMessage();

            // Close messageConsumer / flow receiver
            logger.info("Closing consumer and closing session.");
            messageConsumer.close();
            flowReceiver.close();
            session.closeSession();
        }

        private void waitForMessage() throws InterruptedException {
            Thread.sleep(1000L);
        }
    }

    private static TextMessage createTextMessage(String msg, DeliveryMode deliveryMode) {
        TextMessage jcsmpMsg = JCSMPFactory.onlyInstance().createMessage(TextMessage.class);
        jcsmpMsg.setText(msg);
        jcsmpMsg.setDeliveryMode(deliveryMode);
        return jcsmpMsg;
    }
}
