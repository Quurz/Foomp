package org.quurz.foomp.plugins;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.bind.annotation.AllArguments;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.mustBeConcrete;
import static org.quurz.foomp.base.util.Util.mustBeInterface;
import static org.quurz.foomp.plugins.TypedValue.extractTypesAndValues;

@FunctionalInterface
public interface ProxyFactory<A> {

    @SuppressWarnings({"unchecked", "resource"})
    public static <A> ProxyFactory<A> proxyFactory(final @NonNull Class<A> contract,
                                                   final @NonNull Class<? extends A> implementation,
                                                   final @NonNull String fullyQualifiedName,
                                                   final @NonNull ClassLoader classLoader) {
        mustBeInterface(
            Objects.requireNonNull(contract, nullValue("contract")),
            () -> new IllegalArgumentException("The contract class must be an interface.")    // TODO: Localise
        );
        mustBeConcrete(
            Objects.requireNonNull(implementation, nullValue("implementation")),
            () -> new IllegalArgumentException("The implementation class must be concrete.")    // TODO: Localise
        );
        Objects.requireNonNull(fullyQualifiedName, nullValue("fullyQualifiedName"));
        Objects.requireNonNull(classLoader, nullValue("classLoader"));

        final var builder
            = new ByteBuddy()
                .subclass(Proxy.class)
                .implement(contract)
                .method(net.bytebuddy.matcher.ElementMatchers.isDeclaredBy(contract))
                .intercept(net.bytebuddy.implementation.MethodDelegation.to(LockedDelegator.class))
                .name(fullyQualifiedName);

        final var unloadedType
            = builder.make();
        final var loadedType
            = unloadedType.load(classLoader, net.bytebuddy.dynamic.loading.ClassLoadingStrategy.Default.INJECTION)
                .getLoaded();
        return arguments -> {
            try {
                final var typesAndValues
                    = extractTypesAndValues(arguments);
                final var pluginConstructor
                    = implementation.getDeclaredConstructor(typesAndValues.get1());
                final var plugin
                    = pluginConstructor.newInstance(typesAndValues.get2());
                final var proxy
                    = loadedType.getDeclaredConstructor().newInstance();
                proxy.$__set_plugin(plugin);
                return (A) proxy;
            } catch (final NoSuchMethodException
                         | InstantiationException
                         | IllegalAccessException
                         | IllegalArgumentException
                         | InvocationTargetException exception) {
                throw new IllegalArgumentException(exception);    // TODO
            }
        };
    }

    @NonNull
    A construct(final @NonNull TypedValue<?>... arguments);

    // Nested interceptor class for locked delegation
    class LockedDelegator {
        @RuntimeType
        public static Object intercept(@This Proxy<?> proxy,
                                      @Origin java.lang.reflect.Method method,
                                      @AllArguments Object[] args) throws Throwable {
            proxy.$__access_lock.writeLock().lock();
            try {
                final var plugin = proxy.$__get_plugin();
                return method.invoke(plugin, args);
            } finally {
                proxy.$__access_lock.writeLock().unlock();
            }
        }
    }

}
