package com.sfy.demo.service.impl;

import com.lorne.tx.annotation.TxTransaction;
import com.sfy.demo.service.IAuthTestServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class TxServiceImpl {

    @Resource
    private IAuthTestServiceClient authTestServiceClient;

    @TxTransaction
    @Transactional
    public int testSave(){
        authTestServiceClient.save();
        int v = 100/0;

        return 0;
    }
}
