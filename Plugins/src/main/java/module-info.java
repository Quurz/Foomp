module foomp.plugins {
    exports org.quurz.foomp.plugins;
    exports org.quurz.foomp.plugins.proxybuilder;

    requires foomp.base;

    requires static org.checkerframework.checker.qual;
    requires static lombok;
    requires net.bytebuddy;
}