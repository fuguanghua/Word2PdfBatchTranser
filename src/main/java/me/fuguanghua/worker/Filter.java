package me.fuguanghua.worker;

/*
 * @deprecated 废弃
 * @author fuguanghua
 * @date {2022.02.14}
 * @version 1.0
 */
@FunctionalInterface
public interface Filter<E> {
    boolean accept(E e);
}
