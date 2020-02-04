package com.web_kabinet.service;


import com.web_kabinet.domain.Contragent;
import com.web_kabinet.domain.Ttn;
import com.web_kabinet.domain.User;
import com.web_kabinet.repos.TtnRepo;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TtnService {

    private final TtnRepo ttnRepo;

    private String indexOfNumber = "ТТН";

    public TtnService(TtnRepo ttnRepo) {
        this.ttnRepo = ttnRepo;
    }

    public String getIndexOfNumber() {
        return indexOfNumber;
    }

    public void setIndexOfNumber(String indexOfNumber) {
        this.indexOfNumber = indexOfNumber;
    }

    public TtnRepo getTtnRepo() {
        return ttnRepo;
    }

    public List<Ttn> loadTtnByContragent(User user) {

        Contragent userContragent = user.getContragent();
        return ttnRepo.findAllByContragentId(userContragent.getId());
    }


    public Long getNumber() {
        Long num = 0L;
        Long max = 0L;

        max = ttnRepo.findMaxNum();
        if (max == null) {

            return 1L;
        }

        num = max + 1L;
        return num;
    }
}