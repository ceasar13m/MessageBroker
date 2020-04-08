package com.ainur;

import com.ainur.repository.MySQLRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WorkerFactory {
    @Autowired
    MySQLRepository mySQLRepository;

    public Worker getWorker() {
        return new Worker(mySQLRepository);
    }
}
