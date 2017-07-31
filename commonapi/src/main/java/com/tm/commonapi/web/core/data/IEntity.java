package com.tm.commonapi.web.core.data;

import java.io.Serializable;

public interface IEntity<T extends Serializable> extends Serializable {

    T getId();

    void setId(T id); 
}
