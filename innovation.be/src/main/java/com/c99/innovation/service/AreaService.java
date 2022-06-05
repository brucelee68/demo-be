package com.c99.innovation.service;

import com.c99.innovation.entity.Area;

import java.util.List;
import java.util.Map;

public interface AreaService {
    List<Area> getAll();

//    List<Area> getAllActiveArea();

    boolean deleteArea(Long id);

    String createArea(String area);

    String updateArea(Long id, String newName);

    Map<String, Object> getAllActiveArea(int page, int size);
}
