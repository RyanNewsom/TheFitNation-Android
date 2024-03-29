package com.fitnation.model;

import android.util.Log;

import com.fitnation.model.enums.SkillLevel;
import com.fitnation.model.enums.UnitOfMeasure;
import com.fitnation.networking.UserLogins;
import com.fitnation.profile.ProfileDataManager;
import com.fitnation.utils.PrimaryKeyFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Information about a user and their data
 */
public class UserDemographic extends RealmObject implements Cloneable {
    private static final String TAG = UserDemographic.class.getSimpleName();
    @PrimaryKey
    private Long androidId;
    private Long id;  //userDemographicId
    private Long userId; //user Id
    private String firstName;
    private String lastName;
    private String gender;
    private Float height;
    private String skillLevelLevel;
    private Long skillLevelId;
    private String unitOfMeasure;
    private Boolean isActive;
    private RealmList<Gym> gyms;
    private Long user_weight_id;
    private RealmList<WorkoutTemplate> workoutTemplates;
    private String dateOfBirth;
    private String createdOn;
    private String lastLogin;
    private String userLogin;


    public UserDemographic() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateOfBirth = dateFormat.format(new Date());
        createdOn = dateFormat.format(new Date());
        lastLogin = dateFormat.format(new Date());
        skillLevelLevel = SkillLevel.BEGINNER;
        unitOfMeasure = UnitOfMeasure.IMPERIAL;
    }

    /**
     * Sets the androidId to the next available
     */
    public void setAndroidIdToNextAvailable() {
        androidId = PrimaryKeyFactory.getInstance().nextKey(this.getClass());
    }

    public void setAndroidId(Long id) {
        androidId = id;
    }

    public Long getAndroidId() {
        return androidId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
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

    public void setFirstName(String name){
        firstName = name;
    }

    public String getFirstName(){
        return firstName;
    }

    public void setLastName(String name){
        lastName = name;
    }

    public String getLastName(){
        return lastName;
    }

    public void setGender(String gender){ this.gender = gender; }

    public String getGender(){
        return gender;
    }

    public void setDateOfBirth(Date dateOfBirth){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        this.dateOfBirth = dateFormat.format(dateOfBirth);
    }

    public void setDateOfBirth(String dob){ this.dateOfBirth = dob;}

    public String getDateOfBirth(){ return dateOfBirth; }

    public void setHeight(String pHeight){
        try {
            height = Float.parseFloat(pHeight);
        } catch (Exception e){
            Log.d(TAG, e.toString());
            height = 0.0f;
        }
    }

    public Float getHeight(){
        return height;
    }

    public void setSkillLevelLevel(String skillLevel) { skillLevelLevel = skillLevel; }

    public String getSkillLevelLevel(){
        return skillLevelLevel;
    }

    public void setUnitOfMeasure(String unit){
        unitOfMeasure = unit;
    }

    public String getUnitOfMeasure(){
        return unitOfMeasure;
    }

    public String getUserLogin(){ return userLogin; }

    public void setUserLogin(String userLogin) {this.userLogin = userLogin;}

    @Override
    public String toString() {
        return "UserDemographic{" +
            "id=" + id +
                "firstName=" + firstName +
                "lastName=" + lastName +
            ", gender='" + gender + "'" +
            ", dateOfBirth='" + dateOfBirth + "'" +
            ", height='" + height + "'" +
            ", skill_level='" + skillLevelLevel + "'" +
            ", unit_of_measure='" +  unitOfMeasure + "'" +
            ", is_active='" + isActive + "'" +
            '}';
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setSkillLevelId(Long skillLevelId) {
        this.skillLevelId = skillLevelId;
    }
}
