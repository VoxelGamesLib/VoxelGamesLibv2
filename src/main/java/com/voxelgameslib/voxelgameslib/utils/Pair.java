package com.voxelgameslib.voxelgameslib.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Simple pair class
 *
 * @param <S> first type
 * @param <T> second type
 */
@Data
@AllArgsConstructor
public class Pair<S, T> {

    S first;
    T second;
}
