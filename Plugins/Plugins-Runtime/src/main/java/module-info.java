module foomp.plugins.runtime {
    exports org.quurz.foomp.plugins;

    requires plugins.test.shared;

    requires foomp.base;
    requires net.bytebuddy;

    requires static org.checkerframework.checker.qual;
}