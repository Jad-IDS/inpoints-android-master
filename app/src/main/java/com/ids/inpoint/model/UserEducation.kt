package com.ids.inpoint.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class UserEducation {
    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Description")
    @Expose
    var description: String? = null
    @SerializedName("School")
    @Expose
    var school: String? = null
    @SerializedName("DegreeId")
    @Expose
    var degreeId: Int? = null
    @SerializedName("DegreeDescription")
    @Expose
    var degreeDescription: String? = null
    @SerializedName("SpecificationId")
    @Expose
    var specificationId: Int? = null
    @SerializedName("SpecificationDescription")
    @Expose
    var specificationDescription: String? = null
    @SerializedName("StartYear")
    @Expose
    var startYear: Int? = null
    @SerializedName("EndYear")
    @Expose
    var endYear: Int? = null
    @SerializedName("Grade")
    @Expose
    var grade: Int? = null
    @SerializedName("Activities")
    @Expose
    var activities: String? = null

}