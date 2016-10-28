package com.github.dmitriylamzin.service;

import com.github.dmitriylamzin.domain.Head;

public interface HeadService {
    boolean saveHead(Head head);
    Head getHead();
}
