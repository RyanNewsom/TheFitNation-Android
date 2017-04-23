package com.fitnation.workout.parent;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.util.Log;

import com.fitnation.R;
import com.fitnation.model.ExerciseView;
import com.fitnation.model.UserWorkoutInstance;
import com.fitnation.model.UserWorkoutTemplate;
import com.fitnation.networking.tasks.callbacks.ExercisesRequestCallback;
import com.fitnation.workout.callbacks.OnExerciseUpdatedCallback;
import com.fitnation.workout.callbacks.SaveDialogCallback;
import com.fitnation.workout.callbacks.SaveWorkoutCallback;
import com.fitnation.workout.common.ExerciseAlertDialogFactory;
import com.fitnation.navigation.Navigator;
import com.fitnation.workout.exercise.ExerciseType;
import com.fitnation.model.ExerciseInstance;
import com.fitnation.model.enums.ExerciseAction;

import java.util.List;

/**
 * Presenter for the ExercisesParent Fragment
 */
public class ExercisesParentPresenter implements ExercisesParentContract.Presenter, ExercisesRequestCallback, SaveDialogCallback, OnExerciseUpdatedCallback {
    private static final String TAG = ExercisesParentPresenter.class.getSimpleName();
    private ExercisesManager mExerciseManager;
    private ExercisesParentContract.View mView;
    private ExerciseInstance mExerciseInstanceBeingEdited;

    public ExercisesParentPresenter(Context context, ExercisesParentContract.View view) {
        mView = view;
        mExerciseManager = new ExercisesManager(context);
    }

    @Override
    public void onViewReady() {
        //get the exercises
        mView.showProgress();
        mExerciseManager.getExercises(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void onEditPressed(ExerciseInstance exercise) {
        mExerciseInstanceBeingEdited = exercise;
        Navigator.navigateToEditExercise(mView.getBaseActivity(), exercise, ExerciseType.TEMPLATE, this, R.id.content_main_container);
    }

    //----------------------------------ExercisesRequestCallback----------------------------------//

    @Override
    public void onExercisesRetrieved(List<ExerciseInstance> exerciseList1, List<ExerciseInstance> exerciseList2, List<ExerciseInstance> exerciseList3) {
        mView.stopProgress();
        mView.displayExercises(exerciseList1, exerciseList2, exerciseList3);
    }

    @Override
    public void onError() {
        //TODO re-authenticate user if 401
        mView.stopProgress();
    }

    @Override
    public void onActionButtonClicked(ExerciseAction action) {
        Log.i(TAG, "ExerciseActionButtonClicked: " + action);
        if(action == ExerciseAction.SAVE) {
            mView.promptUserToSave(SaveWorkoutDialogFragment.newInstance(this));

        } else if(action == ExerciseAction.LAUNCH) {
            //TODO Launch a new instance of this workout
        }
    }

    @Override
    public void onExerciseSelected(ExerciseInstance exerciseInstance, boolean isSelected) {
        mExerciseManager.exerciseInstanceSelected(exerciseInstance, isSelected);
        if(mExerciseManager.atLeastOneExerciseSelected()) {
            mView.setSaveButtonEnabled(true);
        } else {
            mView.setSaveButtonEnabled(false);
        }
    }

    //----------------------------------SaveDialogCallback----------------------------------//

    @Override
    public void onSaveRequested(String name) {
        Log.i(TAG, "User requested to save workout with name: " + name);
        if(name != null && !name.isEmpty()) {
            mView.showProgress();
            mExerciseManager.createWorkoutAndSave(name, new SaveWorkoutCallback() {
                @Override
                public void onSuccess() {
                    mView.stopProgress();
                    mView.showSuccess(ExerciseAlertDialogFactory.getBuildWorkoutSuccess(mView.getBaseActivity(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            UserWorkoutInstance userWorkoutInstance = new UserWorkoutInstance(mExerciseManager.getWorkoutInstance(), UserWorkoutInstance.getNextAndroidKey());
                            UserWorkoutTemplate userWorkoutTemplate = WorkoutTemplateManager.getSingletonUserWorkoutTemplate(mExerciseManager.getWorkoutTemplate());
                            Navigator.navigateToEditWorkout(mView.getBaseActivity(), userWorkoutInstance, userWorkoutTemplate, R.id.content_main_container);
                        }
                    }));
                }

                @Override
                public void onFailure(String error) {
                    mView.stopProgress();
                    mView.showFailure(ExerciseAlertDialogFactory.getBuildWorkoutError(error, mView.getBaseActivity()));
                }
            });
        } else {
            mView.showFailure(ExerciseAlertDialogFactory.getBuildWorkoutError("A Workout name must be provided", mView.getBaseActivity()));
        }
    }

    //----------------------------------OnExerciseUpdatedCallback----------------------------------//

    @Override
    public void exerciseUpdated(@Nullable ExerciseView updatedExerciseView) {
        ExerciseInstance updatedExerciseInstance = (ExerciseInstance) updatedExerciseView;

        if(updatedExerciseInstance != null) {
            mExerciseInstanceBeingEdited = (ExerciseInstance) updatedExerciseInstance.clone();
            mExerciseManager.updateExerciseList(mExerciseInstanceBeingEdited, updatedExerciseInstance, mView.getSelectedTab());
            mView.displayUpdatedExercises(mExerciseManager.getExercisesTab1(), mExerciseManager.getExercisesTab2(), mExerciseManager.getExercisesTab3());
        }
    }
}
