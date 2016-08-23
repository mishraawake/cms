package com.sp.dao.api.exchange;

import javax.jcr.Node;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
public interface SpToJcr<T> extends BiConsumer<T, Node> {
    @Override
    void accept(T spObject, Node node);
}
