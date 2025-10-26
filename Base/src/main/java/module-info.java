module foomp.base {
    exports org.quurz.foomp.base.util;
    exports org.quurz.foomp.base.functions;
    exports org.quurz.foomp.base.types;
    exports org.quurz.foomp.base.localisation;

    requires transitive foomp.higher;

    requires static org.checkerframework.checker.qual;
    requires static lombok;
    requires foomp.base;
}
