package com.ids.inpoint.model.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ResponseDevice {

    @SerializedName("Id")
    @Expose
    var id: Int? = null
    @SerializedName("Model")
    @Expose
    var model: String? = null
    @SerializedName("OsVersion")
    @Expose
    var osVersion: String? = null
    @SerializedName("DeviceToken")
    @Expose
    var deviceToken: String? = null
    @SerializedName("DeviceTypeID")
    @Expose
    var deviceTypeID: String? = null
    @SerializedName("IMEI")
    @Expose
    var imei: String? = null
    @SerializedName("RegistrationDate")
    @Expose
    var registrationDate: String? = null
    @SerializedName("GeneralNotification")
    @Expose
    var generalNotification: Int? = null
    @SerializedName("AppVersion")
    @Expose
    var appVersion: String? = null
    @SerializedName("IsProduction")
    @Expose
    var isProduction: Int? = null
    @SerializedName("UserId")
    @Expose
    var userId: Int? = null
    @SerializedName("Lang")
    @Expose
    var lang: String? = null


    constructor(
        id: Int?,
        model: String?,
        osVersion: String?,
        deviceToken: String?,
        deviceTypeID: String?,
        imei: String?,
        registrationDate: String?,
        generalNotification: Int?,
        appVersion: String?,
        isProduction: Int?,
        userId: Int?,
        lang: String?
    ) {
        this.id = id
        this.model = model
        this.osVersion = osVersion
        this.deviceToken = deviceToken
        this.deviceTypeID = deviceTypeID
        this.imei = imei
        this.registrationDate = registrationDate
        this.generalNotification = generalNotification
        this.appVersion = appVersion
        this.isProduction = isProduction
        this.userId = userId
        this.lang = lang
    }
}
