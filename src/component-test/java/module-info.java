
// follow -
// https://stackoverflow.com/questions/46991022/junit-5-java-9-and-gradle-how-to-pass-add-modules
open module componentTest {
    requires spring.beans;
    requires org.assertj.core;
    requires freemarker;
    requires io.cucumber.core;
    requires io.cucumber.datatable;
    requires io.cucumber.java;
    requires spring.boot;
    requires org.json;
    requires spring.web;
    requires spring.test;
    requires io.cucumber.junit;
    requires junit;
    requires rest.assured;
    requires testcontainers;
}


