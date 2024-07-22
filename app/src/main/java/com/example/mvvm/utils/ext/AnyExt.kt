package com.example.mvvm.utils.ext

inline fun <T : Any> T?.notNull(f: (it: T) -> Unit) {
    if (this != null) f(this)
}

inline fun <T : Any> T?.isNull(f: () -> Unit) {
    if (this == null) f()
}

inline fun <T> List<T>.indexOf(
    notFoundIndex: Int = -1,
    predicate: (T) -> Boolean,
): Int {
    for ((index, item) in this.withIndex()) {
        if (predicate(item)) {
            return index
        }
    }
    return notFoundIndex
}
