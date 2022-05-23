package com.example.examplemod.util;

public interface Self<T> {

    default T self() {
        return (T) this;
    }
}
