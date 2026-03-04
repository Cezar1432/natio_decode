package org.firstinspires.ftc.teamcode.util.groups;

import java.util.Iterator;
import java.util.Spliterator;
import java.util.function.Consumer;

public interface Iterable<T>{
@RecentlyNonNull
Iterator<T> iterator();

default void forEach(@RecentlyNonNull Consumer<? super T> action) {
    throw new RuntimeException("Stub!");
}


default Spliterator<T> spliterator() {
    throw new RuntimeException("Stub!");
}
}
