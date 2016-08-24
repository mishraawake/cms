package com.sp.dao.api.hooks;

import java.util.List;
import java.util.Set;

/**
 * Created by pankajmishra on 24/08/16.
 */
public interface HookManager {
    List<BeforeWrite> getOrderedBefore();
    List<AfterWrite> getOrderedAfter();
}
