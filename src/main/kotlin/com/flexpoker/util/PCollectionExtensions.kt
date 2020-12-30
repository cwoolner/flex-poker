package com.flexpoker.util

import org.pcollections.HashTreePMap
import org.pcollections.HashTreePSet
import org.pcollections.PMap
import org.pcollections.PSet
import org.pcollections.PVector
import org.pcollections.TreePVector

fun <K, V> Map<K, V>.toPMap(): PMap<K, V> {
    return HashTreePMap.from(this)
}

fun <E> Collection<E>.toPSet(): PSet<E> {
    return HashTreePSet.from(this)
}

fun <E> Collection<E>.toPVector(): PVector<E> {
    return TreePVector.from(this)
}