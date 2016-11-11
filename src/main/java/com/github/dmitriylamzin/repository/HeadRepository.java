package com.github.dmitriylamzin.repository;

import com.github.dmitriylamzin.domain.Head;

public interface HeadRepository {
    boolean saveHead(Head head);
    Head getHead();
}
