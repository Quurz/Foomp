package org.quurz.foomp.plugins;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.TypeCache;
import net.bytebuddy.description.type.TypeDescription;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.quurz.foomp.base.functions.Fun;
import org.quurz.foomp.base.util.ThrowingSupplier;

import java.util.Objects;

import static net.bytebuddy.implementation.MethodDelegation.to;
import static net.bytebuddy.implementation.MethodDelegation.toField;
import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static org.quurz.foomp.base.localisation.BaseMessages.nullSuppliedFrom;
import static org.quurz.foomp.base.localisation.BaseMessages.nullValue;
import static org.quurz.foomp.base.util.Util.mustBeConcrete;
import static org.quurz.foomp.base.util.Util.mustBeInterface;
import static org.quurz.foomp.plugins.localisation.PluginsMessages.failedToCreateProxyClass;
import static org.quurz.foomp.plugins.localisation.PluginsMessages.failedToCreateProxyInstance;
import static org.quurz.foomp.plugins.localisation.PluginsMessages.notAConcreteClass;
import static org.quurz.foomp.plugins.localisation.PluginsMessages.notAnInterface;
import static org.quurz.foomp.plugins.Argument.extractTypesAndValues;
import static org.quurz.foomp.plugins.DelegatingProxy.DELEGATE_FIELD_NAME;

/**
 * <div>
 *     <p>
 *         Ein Hilfswerkzeug zur dynamischen Erzeugung von Proxy-Klassen zur Laufzeit
 *         mithilfe von ByteBuddy.
 *     </p>
 *     <p>
 *         Der erzeugte Proxy implementiert ein gegebenes Interface und basiert auf einer
 *         konkreten Implementierungsklasse, die über einen {@link ThrowingSupplier}
 *         zur Verfügung gestellt wird.
 *     </p>
 *     <p>
 *         Der zugrundeliegende Bytecode wird gecached, um unnötige Klassengenerierung zu vermeiden.
 *     </p>
 * </div>
 *
 * @param <A> der Typ des zu implementierenden Interfaces
 *
 * @since 1.0.0
 *
 * @author Alexander Schell
 */
public class ProxyBuilder<A> {

    private static final TypeCache<String> TYPE_CACHE
        = new TypeCache<>();
    private static final ByteBuddy BYTE_BUDDY
        = new ByteBuddy();
    private static final Argument<?>[] EMPTY_ARGUMENTS
        = new Argument[0];


    /**
     * <div>
     *     <p>
     *         Erstellt einen {@code ProxyBuilder} für die angegebene Schnittstelle
     *         auf Basis einer konkreten Implementierungsklasse.
     *     </p>
     *     <p>
     *         Die generierte Proxy-Klasse wird über einen internen Cache gespeichert
     *         und muss die Schnittstelle {@code contract} implementieren.
     *     </p>
     * </div>
     *
     * @param canonicalName der eindeutige Name des Proxys (für den Cache); darf nicht {@code null} sein
     * @param contract die zu implementierende Schnittstelle; darf nicht {@code null} sein
     * @param implementationResolver ein Lieferant für die konkrete Implementierungsklasse;
     *                                darf nicht {@code null} sein
     * @param proxyClassLoader der {@code ClassLoader}, in dem die Klasse geladen werden soll;
     *                         darf nicht {@code null} sein
     * @param <A> der Typ der Schnittstelle
     * @return eine neue {@code ProxyBuilder}-Instanz
     * @throws NullPointerException wenn ein Argument {@code null} ist
     * @throws ProxyBuilderException wenn die Klasse nicht generiert werden konnte oder ungültig ist
     *
     * @since 1.0.0
     */
    @SuppressWarnings({"resource", "unchecked"})
    public static <A> ProxyBuilder<A> proxyBuilder(final @NonNull String canonicalName,
                                                   final @NonNull Class<A> contract,
                                                   final @NonNull ThrowingSupplier<Class<? extends A>> implementationResolver,
                                                   final @NonNull ClassLoader proxyClassLoader) {
        Objects.requireNonNull(canonicalName, nullValue("canonicalName"));
        Objects.requireNonNull(contract, nullValue("contract"));
        Objects.requireNonNull(implementationResolver, nullValue("implementationResolver"));
        Objects.requireNonNull(proxyClassLoader, nullValue("proxyClassLoader"));
        mustBeInterface(contract, () -> new ProxyBuilderException(notAnInterface(contract.getCanonicalName())));

        try {
            final var implementation
                = Objects.requireNonNull(implementationResolver.get(), nullSuppliedFrom("implementationResolver"));
            mustBeConcrete(implementation, () -> new ProxyBuilderException(notAConcreteClass(implementation.getCanonicalName())));

            final var proxy
                = (Class<A>) TYPE_CACHE.findOrInsert(
                    proxyClassLoader,
                    canonicalName,
                    () -> BYTE_BUDDY
                            .subclass(implementation)
                            .implement(contract)
                            .make()
                            .load(proxyClassLoader)
                            .getLoaded()
                );

            return new ProxyBuilder<>(
                arguments -> {
                    final A instance;
                    try {
                        if (arguments.length == 0) {
                            instance
                                = proxy.getConstructor().newInstance();
                        } else {
                            final var typesAndValues
                                = extractTypesAndValues(arguments);
                            instance
                                = proxy
                                    .getConstructor(typesAndValues.get1())
                                    .newInstance(typesAndValues.get2());
                        }
                        return instance;
                    } catch (final Exception exception) {
                        throw new ProxyBuilderException(failedToCreateProxyInstance(proxy), exception);
                    }
                }
            );
        } catch (final ProxyBuilderException proxyBuilderException) {
            throw proxyBuilderException;
        } catch (final Exception exception) {
            throw new ProxyBuilderException(failedToCreateProxyClass(contract, canonicalName), exception);
        }
    }

    @SuppressWarnings({"unchecked", "resource"})
    public static <A> ProxyBuilder<A> delegatingProxyBuilder(final @NonNull String canonicalName,
                                                             final @NonNull Class<A> contract,
                                                             final @NonNull ThrowingSupplier<Class<? extends A>> implementationResolver,
                                                             final @NonNull ClassLoader proxyClassLoader,
                                                             final @NonNull LockingMode lockingMode) {
        Objects.requireNonNull(canonicalName, nullValue("canonicalName"));
        Objects.requireNonNull(contract, nullValue("contract"));
        Objects.requireNonNull(implementationResolver, nullValue("implementationResolver"));
        Objects.requireNonNull(proxyClassLoader, nullValue("proxyClassLoader"));
        Objects.requireNonNull(lockingMode, nullValue("lockingMode"));
        mustBeInterface(contract, () -> new ProxyBuilderException(notAnInterface(contract.getCanonicalName())));

        try {
            final var implementation
                = Objects.requireNonNull(implementationResolver.get(), nullSuppliedFrom("implementationResolver"));
            mustBeConcrete(implementation, () -> new ProxyBuilderException(notAConcreteClass(implementation.getCanonicalName())));

            final var genericDelegatingProxyTypeDescription
                = TypeDescription.Generic.Builder
                    .parameterizedType(DelegatingProxy.class, contract)
                    .build();

            final var delegatingProxy
                = (Class<A>) TYPE_CACHE.findOrInsert(
                    proxyClassLoader,
                    canonicalName,
                    () -> BYTE_BUDDY
                            .subclass(genericDelegatingProxyTypeDescription)
                            .implement(contract)
                            .name(canonicalName)
                            .method(isDeclaredBy(contract))
                            .intercept(
                                lockingMode.isLockMethodCalls()
                                    ? to(MethodLockingInterceptor.class)
                                    : toField(DELEGATE_FIELD_NAME)
                            )
                            .make()
                            .load(proxyClassLoader)
                            .getLoaded()
                );

            return new ProxyBuilder<>(
                arguments -> {
                    try {
                        final A delegate;
                        if (arguments.length == 0) {
                            delegate
                                = implementation.getConstructor().newInstance();
                        } else {
                            final var typesAndValues
                                = extractTypesAndValues(arguments);
                            delegate
                                = implementation
                                    .getConstructor(typesAndValues.get1())
                                    .newInstance(typesAndValues.get2());
                        }
                        return delegatingProxy
                            .getConstructor(contract, boolean.class)
                            .newInstance(delegate, lockingMode.isLockDelegate());
                    } catch (final Exception exception) {
                        throw new ProxyBuilderException(failedToCreateProxyInstance(delegatingProxy), exception);
                    }
                }
            );
        } catch (final ProxyBuilderException proxyBuilderException) {
            throw proxyBuilderException;
        } catch (final Exception exception) {
            throw new ProxyBuilderException(failedToCreateProxyClass(contract, canonicalName), exception);
        }
    }

    private final Fun<Argument<?>[], A> constructor;

    private ProxyBuilder(final Fun<Argument<?>[], A> constructor) {
        this.constructor
            = constructor;
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz des generierten Proxys über den Standardkonstruktor.
     *     </p>
     * </div>
     *
     * @return eine neue Proxy-Instanz
     * @throws ProxyBuilderException wenn die Instanz nicht erzeugt werden konnte
     *
     * @since 1.0.0
     */
    public A newInstance() {
        return this.constructor.apply(EMPTY_ARGUMENTS);
    }

    /**
     * <div>
     *     <p>
     *         Erstellt eine neue Instanz des generierten Proxys mit dem gegebenen Argumenten,
     *         basierend auf einem passenden Konstruktor.
     *     </p>
     * </div>
     *
     * @param arguments die Argumente für den Konstruktor; dürfen keine {@code null}-Werte enthalten
     * @return eine neue Proxy-Instanz
     * @throws NullPointerException wenn {@code arguments} oder eines der enthaltenen Argumente {@code null} ist
     * @throws ProxyBuilderException wenn keine passende Konstruktor-Signatur gefunden oder die Instanziierung fehlgeschlagen ist
     *
     * @since 1.0.0
     */
    public A newInstance(final @NonNull Argument<?>... arguments) {
        Objects.requireNonNull(arguments, nullValue("arguments"));

        return this.constructor.apply(arguments);
    }

}
