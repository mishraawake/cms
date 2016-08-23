package com.sp.dao.api.exchange;

import com.sp.model.FieldValue;

import javax.jcr.Node;
import java.util.function.Function;

/**
 * Created by pankajmishra on 23/08/16.
 */
public interface JcrToSp<T> extends Function<Node, FieldValue<T>> {
    @Override
    FieldValue<T> apply(Node node);
}
