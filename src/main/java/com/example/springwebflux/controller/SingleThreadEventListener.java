package com.example.springwebflux.controller;

import java.util.List;

public interface SingleThreadEventListener {

    void onDataChunk(List<String> chunk);

    void processComplete();

    void processError(Throwable e);
}
