package com.c99.innovation.dto.statistic;

import com.c99.innovation.dto.response.TypesCountingInCertainTime;

public class CombiningTypeCountingAndIndex {

    private TypesCountingInCertainTime typesCounting;

    private int index;

    public CombiningTypeCountingAndIndex(TypesCountingInCertainTime typesCounting, int index) {
        this.typesCounting = typesCounting;
        this.index = index;
    }

    public TypesCountingInCertainTime getTypesCounting() {
        return typesCounting;
    }

    public int getIndex() {
        return index;
    }
}
