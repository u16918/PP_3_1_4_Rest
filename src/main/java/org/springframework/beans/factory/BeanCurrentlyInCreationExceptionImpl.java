package org.springframework.beans.factory;

public class BeanCurrentlyInCreationExceptionImpl extends BeanCurrentlyInCreationException {
    public BeanCurrentlyInCreationExceptionImpl(String beanName) {
        super(beanName);
    }
}
