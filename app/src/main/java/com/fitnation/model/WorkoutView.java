package com.fitnation.model;

/**
 * Created by Ryan on 4/15/2017.
 */

public interface WorkoutView extends Comparable, Cloneable {
    String getName();

    String getLastUpdated();

    String getNotes();

    Long getId();

    Long getWorkoutTemplateId();
}
