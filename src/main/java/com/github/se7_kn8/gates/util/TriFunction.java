package com.github.se7_kn8.gates.util;

@FunctionalInterface
public interface TriFunction<T, U, V, R> {
	R apply(T t, U u, V v);
}
