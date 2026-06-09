package org.quurz.foomp.base.types;

import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class SeqDefaultMethodsTest {

    @Test
    void isEmpty_true_when_no_elements() {
        Seq<Integer> seq = proxySeq(List.of());
        assertTrue(seq.isEmpty(), "isEmpty() sollte true liefern, wenn keine Elemente vorhanden sind");
        assertFalse(seq.isNotEmpty(), "isNotEmpty() sollte false liefern, wenn keine Elemente vorhanden sind");
    }

    @Test
    void isEmpty_false_when_elements_present() {
        Seq<Integer> seq = proxySeq(List.of(1, 2, 3));
        assertFalse(seq.isEmpty(), "isEmpty() sollte false liefern, wenn Elemente vorhanden sind");
        assertTrue(seq.isNotEmpty(), "isNotEmpty() sollte true liefern, wenn Elemente vorhanden sind");
    }


    // Hilfsfunktion: Erzeugt einen Proxy-basierten Seq-Stub, der nur die für die Tests
    // benötigten Wege implementiert:
    // - isNotEmpty(): basierend auf der Größe der Elements-Liste
    // - toCollection(Supplier): nutzt den Supplier, fügt alle Elemente hinzu und gibt die Collection zurück
    // - iterator(): liefert Iterator über die Elemente (weil Seq Iterable ist)
    //
    // Default-Methoden des Interfaces (isEmpty, toList, toSet) werden über MethodHandles korrekt aufgerufen.
    private static <A> Seq<A> proxySeq(final List<A> elements) {
        Objects.requireNonNull(elements, "elements");

        InvocationHandler handler = (proxy, method, args) -> {
            // Default-Methoden des Interface korrekt invokieren
            if (method.isDefault()) {
                return invokeDefaultMethod(proxy, method, args);
            }

            String name = method.getName();
            switch (name) {
                case "isNotEmpty":
                    return !elements.isEmpty();

                case "toCollection": {
                    @SuppressWarnings("unchecked")
                    Supplier<Collection<? super A>> init = (Supplier<Collection<? super A>>) args[0];
                    Collection<? super A> c = Objects.requireNonNull(init.get(), "Supplier#get darf kein null liefern");
                    c.addAll(elements);
                    return c;
                }

                case "iterator":
                    return elements.iterator();

                // Der Rest der Seq-API wird in diesen Tests nicht benötigt
                case "head":
                case "tail":
                case "cons":
                case "consAll":
                case "decons":
                case "split":
                case "filter":
                    throw new UnsupportedOperationException("Nicht benötigt in Default-Methoden-Tests: " + name);

                // Basis-Objektmethoden sicher behandeln
                case "toString":
                    return "SeqProxy" + elements;
                case "hashCode":
                    return System.identityHashCode(proxy);
                case "equals":
                    return proxy == args[0];

                default:
                    // Für alle nicht erwarteten Methoden lieber fail-fast
                    throw new UnsupportedOperationException("Nicht implementiert im Test-Stub: " + method);
            }
        };

        Object proxy = Proxy.newProxyInstance(
                Seq.class.getClassLoader(),
                new Class<?>[]{Seq.class},
                handler
        );
        @SuppressWarnings("unchecked")
        Seq<A> seq = (Seq<A>) proxy;
        return seq;
    }

    // Ruft eine Default-Methode eines Interfaces auf (für Proxy).
    // Nutzt MethodHandles.privateLookupIn + unreflectSpecial.
    private static Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable {
        final Class<?> declaringClass = method.getDeclaringClass();
        MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(declaringClass, MethodHandles.lookup());
        MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
        MethodHandle handle = lookup.findSpecial(declaringClass, method.getName(), methodType, declaringClass)
                .bindTo(proxy);
        return handle.invokeWithArguments(args == null ? new Object[0] : args);
    }
}
