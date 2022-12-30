package com.learnkafka.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.learnkafka.domain.LibraryEvent;
import com.learnkafka.domain.LibraryEventType;
import com.learnkafka.producer.LibraryEventProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

@RestController
public class LibraryEventsController {

    @Autowired
    LibraryEventProducer libraryEventProducer;

    @PostConstruct
    public void init() {

    }

    @PostMapping("/v1/libraryevent")
    public ResponseEntity<LibraryEvent> postLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, InterruptedException, ExecutionException {
        System.out.println("before");
        libraryEventProducer.sendLibraryEventRealAsync(libraryEvent).join(); //async
//        SendResult<Integer, String> sendResult = libraryEventProducer.sendLibrarySynchronoslyEvent(libraryEvent);
//        System.out.println(sendResult.toString());
        libraryEvent.setLibraryEventType(LibraryEventType.NEW);
//        libraryEventProducer.sendLibraryEvent2(libraryEvent);
        System.out.println("after");
        libraryEvent.getBook().setBookAuthor(libraryEventProducer.getSentMes());
        return ResponseEntity.status(HttpStatus.CREATED).body(libraryEvent);
    }

    @PutMapping("/v1/libraryevent")
    public ResponseEntity<?> putLibraryEvent(@RequestBody LibraryEvent libraryEvent) throws JsonProcessingException, InterruptedException, ExecutionException {
        if (libraryEvent.getLibraryEventId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please pass the libraryeventid");

        }
        System.out.println("before");
        libraryEvent.setLibraryEventType(LibraryEventType.UPDATE);
        libraryEventProducer.sendLibraryEvent2(libraryEvent);
        System.out.println("after");
        libraryEvent.getBook().setBookAuthor(libraryEventProducer.getSentMes());
        return ResponseEntity.status(HttpStatus.OK).body(libraryEvent);
    }

}
