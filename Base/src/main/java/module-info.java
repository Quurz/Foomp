module foomp.base {
    exports org.quurz.foomp.base.util;
    exports org.quurz.foomp.base.functions;
    exports org.quurz.foomp.base.types;
    exports org.quurz.foomp.base.localisation;
    exports org.quurz.foomp.base.misc;
    exports org.quurz.foomp.base.functions.concurrent;
    exports org.quurz.foomp.base.concurrent;

    requires transitive foomp.higher;

    requires static org.checkerframework.checker.qual;
}
