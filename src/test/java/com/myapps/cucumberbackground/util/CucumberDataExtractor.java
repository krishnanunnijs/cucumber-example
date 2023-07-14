package com.myapps.cucumberbackground.util;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.core.gherkin.DataTableArgument;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Step;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestStep;

import java.lang.reflect.Field;
import java.util.List;

public class CucumberDataExtractor {

    public static List<List<String>> extractDatatableArguments(Scenario scenario){
        List<List<String>> args = null;
        try {
            TestCaseState testCaseState = (TestCaseState)extractField(scenario,"delegate");
            TestCase testCase = (TestCase)extractField(testCaseState,"testCase");
            List<TestStep> testSteps = (List<TestStep>) extractField(testCase,"testSteps");
            //since we are fetching data from first Given phrase
            Step step = (Step)extractField(testSteps.get(0),"step");
            DataTableArgument dataTableArgument = (DataTableArgument) step.getArgument();
            final List<List<String>> cells = dataTableArgument.cells();
            System.out.println("** Datatable Arguments **");
            cells.stream().flatMap(a -> a.stream()).forEach(System.out::println);
            System.out.println("** **");
            args = cells;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return args;
    }

    private static <T> Object extractField(T t,String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = t.getClass()
                .getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(t);
    }

}
