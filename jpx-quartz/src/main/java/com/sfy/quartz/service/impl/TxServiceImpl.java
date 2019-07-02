package com.sfy.quartz.service.impl;

import com.lorne.tx.annotation.TxTransaction;
import com.sfy.quartz.service.IAuthTestServiceClient;
import com.sfy.quartz.service.IConfigServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TxServiceImpl {

    @Autowired
    private IConfigServiceClient configServiceClient;

    @TxTransaction
    @Transactional
    public int testSave(){
        configServiceClient.save();
        int v = 100/0;
        return 0;
    }
}
