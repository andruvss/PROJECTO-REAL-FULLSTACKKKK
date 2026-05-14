package com.redsalud.waiting_list.service;

import com.redsalud.waiting_list.model.WaitingList;
import com.redsalud.waiting_list.repository.WaitingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WaitingListService {

    @Autowired
    private WaitingListRepository repository;

    public WaitingList addToWaitingList(WaitingList entry) {
        return repository.save(entry);
    }

    public List<WaitingList> getAllEntries() {
        return repository.findAll();
    }
}