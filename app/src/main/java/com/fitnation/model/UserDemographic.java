package com.fitnation.model;

import com.fitnation.model.enums.SkillLevel;
import com.fitnation.model.enums.UnitOfMeasure;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Information about a user and their data
 */
public class UserDemographic extends RealmObject {
    @PrimaryKey
    private Long androidId;
    private Long id;
    private String firstName;
    private String lastName;
    private String gender;
    private Date dob;
    private Integer height;
    private String skillLevel;
    private String unitOfMeasure;
    private Boolean isActive;
    private RealmList<Gym> gyms;
    private RealmList<UserWeight> userWeights;
    private WorkoutLog workoutLog;
    private RealmList<WorkoutTemplate> workoutTemplates;

    public UserDemographic() {
        dob = new Date();
        skillLevel = SkillLevel.BEGINNER;
        unitOfMeasure = UnitOfMeasure.IMPERIAL;
    }

    /**
     * Sets the androidId to the next available
     */
    public void setAndroidIdToNextAvailable() {
        androidId = PrimaryKeyFactory.getInstance().nextKey(this.getClass());
    }

    public void setId(Long id) {
        androidId = id;
    }

    public Long getAndroidId() {
        return androidId;
    }

    public Long getId() {
        return id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDemographic that = (UserDemographic) o;

        if (androidId != null ? !androidId.equals(that.androidId) : that.androidId != null)
            return false;
        return id != null ? id.equals(that.id) : that.id == null;

    }

    @Override
    public int hashCode() {
        int result = androidId != null ? androidId.hashCode() : 0;
        result = 31 * result + (id != null ? id.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "UserDemographic{" +
            "id=" + id +
            ", first_name='" + firstName + "'" +
            ", last_name='" + lastName + "'" +
            ", gender='" + gender + "'" +
            ", dob='" + dob + "'" +
            ", height='" + height + "'" +
            ", skill_level='" + skillLevel + "'" +
            ", unit_of_measure='" +  unitOfMeasure + "'" +
            ", is_active='" + isActive + "'" +
            '}';
    }
}
