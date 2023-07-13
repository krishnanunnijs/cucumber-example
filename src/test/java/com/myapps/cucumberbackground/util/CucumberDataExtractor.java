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
            Field delegate = scenario.getClass()
                    .getDeclaredField("delegate");
            delegate.setAccessible(true);
            TestCaseState testCaseState = (TestCaseState) delegate.get(scenario);

            Field testCaseField = testCaseState.getClass()
                    .getDeclaredField("testCase");
            testCaseField.setAccessible(true);
            TestCase testCase = (TestCase) testCaseField.get(testCaseState);
            Field testStepsField = testCase.getClass()
                    .getDeclaredField("testSteps");
            testStepsField.setAccessible(true);
            List<TestStep> testSteps = (List<TestStep>) testStepsField.get(testCase);
            TestStep testStep = testSteps.get(0);
            Field testStepField = testStep.getClass().getDeclaredField("step");
            testStepField.setAccessible(true);
            Step step = (Step)testStepField.get(testStep);
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

}
