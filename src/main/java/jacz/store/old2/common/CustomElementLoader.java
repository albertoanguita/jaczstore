package jacz.store.old2.common;

/**
 * Loader for custom elements
 */
public interface CustomElementLoader<T> {

    T loadElement(String string);

    String saveElement(T element);
}
