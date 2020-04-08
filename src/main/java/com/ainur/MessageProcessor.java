package com.ainur;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * blockingQueue <- message
 * 10 обработчиков обрабаотывают сообщения
 */

@Component
public class MessageProcessor {
    private ArrayList<Worker> workers = new ArrayList<>();
    private Logger log;

    public MessageProcessor() {
        TokensStorage.getTokenStorage();
        this.log = Logger.getLogger(MessageProcessor.class.getName());
        log.info("Конструктор процессора");
    }

    @PostConstruct
    public void init() {
        startWorkers();
    }


    @Autowired
    WorkerFactory workerFactory;

    public void startWorkers() {
        for (int i = 0; i < 10; i++) {
            Worker worker =
                    workerFactory.getWorker();
            workers.add(worker);
            worker.start();
            log.info("worker " + i + " запущен");
        }
    }

    public void stopWorkers() {
        for (Worker worker : workers) {
            worker.stopWorker();
        }
    }

}
