package com.fitnation.workout.callbacks;

import android.support.annotation.Nullable;

import com.fitnation.model.ExerciseInstance;
import com.fitnation.model.ExerciseView;

/**
 * Callback for when an exercise gets updated
 */
public interface OnExerciseUpdatedCallback {
    /**
     * Notified when an exercise is finished being updated
     * @param updatedExerciseInstance - if null it wasn't updated
     */
    void exerciseUpdated(@Nullable ExerciseView updatedExerciseInstance);
}
