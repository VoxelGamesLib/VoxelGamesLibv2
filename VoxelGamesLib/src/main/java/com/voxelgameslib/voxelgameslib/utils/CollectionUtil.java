package com.voxelgameslib.voxelgameslib.utils;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;

/**
 * Small util that offers small method to do stuff related to collections
 */
public class CollectionUtil {

    public static StringCreator<Object> OBJECT_TO_STRING = Object::toString;

    /**
     * Converts an array to a string list using a given string creator
     *
     * @param arr     the array to convert
     * @param creator the string creator that is used to convert the objects to strings
     * @param <T>     the type
     * @return the converted list
     */
    @Nonnull
    public static <T> List<String> toStringList(@Nonnull T[] arr, @Nonnull StringCreator<T> creator) {
        List<String> list = new ArrayList<>(arr.length);
        for (T t : arr) {
            list.add(creator.toString(t));
        }
        return list;
    }

    /**
     * Converts an array to a string list by calling Object#toString on all of the element.
     *
     * @param arr the array to convert
     * @return the converted list
     */
    @Nonnull
    public static List<String> toStringList(@Nonnull Object[] arr) {
        return toStringList(arr, OBJECT_TO_STRING);
    }

    /**
     * Small functional interface to convert a object into a string
     *
     * @param <T> the type of the object
     */
    public interface StringCreator<T> {

        /**
         * converts a object of type t to a string
         *
         * @param t the object to convert
         * @return the resulting string
         */
        @Nonnull
        String toString(@Nonnull T t);
    }
}
