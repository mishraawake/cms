package com.sp.dao.api.exchange;

import javax.jcr.Node;
import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
public interface ExchangeProviderJcrToSp<T> extends Function<Node, JcrToSp<T>> {
    @Override
    JcrToSp<T> apply(Node node);
}
