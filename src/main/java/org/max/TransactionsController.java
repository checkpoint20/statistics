package org.max;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
public class TransactionsController {

    @Autowired
    private StatisticsService servise;

    @RequestMapping(value = "/transactions", method = RequestMethod.POST)
    public ResponseEntity<Void> add(@Valid @NotNull @RequestBody Transaction transaction) {
        if(transaction.isInvalid()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            servise.add(transaction);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
    }
}
