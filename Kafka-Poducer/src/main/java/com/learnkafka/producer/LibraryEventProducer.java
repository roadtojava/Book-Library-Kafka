package com.learnkafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.learnkafka.domain.LibraryEvent;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Component
@Slf4j
public class LibraryEventProducer {

    @Autowired
    KafkaTemplate<Integer, String> kafkaTemplate;

    String topic = "library-events";

    @Autowired
    ObjectMapper objectMapper;

    String sentMes;
    boolean isSendCompleted = false;

    public void sendLibraryEvent(LibraryEvent libraryEvent) throws JsonProcessingException {
        isSendCompleted = false;
        sentMes = null;
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
                isSendCompleted = true;
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handlesSuccess(key, value, result);
                sentMes = "Okk";
                System.out.println("12131232131321212");
                isSendCompleted = true;
            }
        });
    }

    public CompletableFuture<Void> sendLibraryEventRealAsync(LibraryEvent libraryEvent) {
        return CompletableFuture.runAsync(() -> {
                    isSendCompleted = false;
                    try {
                        sentMes = null;
                        Integer key = libraryEvent.getLibraryEventId();
                        String value = objectMapper.writeValueAsString(libraryEvent);
                        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.sendDefault(key, value);
                        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
                            @Override
                            public void onFailure(Throwable ex) {
                                handleFailure(key, value, ex);
                                isSendCompleted = true;
                            }

                            @Override
                            public void onSuccess(SendResult<Integer, String> result) {
                                handlesSuccess(key, value, result);
                                sentMes = "Okk";
                                System.out.println("12131232131321212");
                                isSendCompleted = true;
                            }
                        });
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        isSendCompleted = true;
                    }
                    while (!isSendCompleted) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException ex) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }

        );
    }

    public SendResult<Integer, String> sendLibrarySynchronoslyEvent(LibraryEvent libraryEvent) throws JsonProcessingException, ExecutionException, InterruptedException {
        sentMes = null;
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        SendResult<Integer, String> sendResult = null;
        try {
            sendResult = kafkaTemplate.sendDefault(key, value).get();
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error while sending message is {} !-----! key: {} - value: {}", e.getMessage(), key, value);
            throw e;
        } catch (Exception e) {
            log.error("Error while sending message is {} !-----! key: {} - value: {}", e.getMessage(), key, value);
            throw e;
        }
        return sendResult;
    }

    private void handleFailure(Integer key, String value, Throwable ex) {
        log.error("Error while sending message is {} !-----! key: {} - value: {}", ex.getMessage(), key, value);
        try {
            throw ex;
        } catch (Throwable throwable) {
            log.error("Error on failure {}", throwable.getMessage());
        }
    }

    private void handlesSuccess(Integer key, String value, SendResult<Integer, String> result) {
        log.info("Message successfully  for the key: {} and the values id: {} , partition : {}", key, value, result.getRecordMetadata().partition());
    }

    public String getSentMes() {
        return sentMes;
    }

    public void sendLibraryEvent2(LibraryEvent libraryEvent) throws JsonProcessingException {
        sentMes = null;
        Integer key = libraryEvent.getLibraryEventId();
        String value = objectMapper.writeValueAsString(libraryEvent);
        ProducerRecord<Integer, String> producerRecord = buildProducerRecord(key, value, topic);
        ListenableFuture<SendResult<Integer, String>> listenableFuture = kafkaTemplate.send(producerRecord);
        listenableFuture.addCallback(new ListenableFutureCallback<SendResult<Integer, String>>() {
            @Override
            public void onFailure(Throwable ex) {
                handleFailure(key, value, ex);
            }

            @Override
            public void onSuccess(SendResult<Integer, String> result) {
                handlesSuccess(key, value, result);
                sentMes = "Okk";
            }
        });

    }

    private ProducerRecord<Integer, String> buildProducerRecord(Integer key, String value, String topic) {
        List<Header> recordHeaders = List.of(new RecordHeader("event-source", "scanner".getBytes()));
        return new ProducerRecord<>(topic, null, key, value, recordHeaders);
    }
}

