package com.c99.innovation.service.impl;

import com.c99.innovation.entity.Type;
import com.c99.innovation.repository.TypeRepository;
import com.c99.innovation.service.TypeService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    private TypeRepository typeRepository;

    public TypeServiceImpl(TypeRepository typeRepository) {
        this.typeRepository = typeRepository;
    }

    @Override
    public List<Type> getAll() {
        return this.typeRepository.findAll();
    }
}
