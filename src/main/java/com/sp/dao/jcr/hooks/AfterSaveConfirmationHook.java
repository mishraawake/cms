package com.sp.dao.jcr.hooks;

import com.sp.dao.api.hooks.AfterWrite;
import com.sp.dao.jcr.model.JCRItem;
import org.springframework.stereotype.Service;

/**
 * Created by pankajmishra on 24/08/16.
 */
@Service
public class AfterSaveConfirmationHook implements AfterWrite<JCRItem, Integer> {

    @Override
    public void doThis(JCRItem spObject) {
    }

    @Override
    public Integer rank() {
        return 1;
    }
}
