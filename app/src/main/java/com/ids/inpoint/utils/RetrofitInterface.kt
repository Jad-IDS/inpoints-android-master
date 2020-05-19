package com.ids.inpoint.utils

import com.ids.inpoint.controller.Fragments.ResponseTeamRequest
import com.ids.inpoint.model.*
import com.ids.inpoint.model.response.*
import com.ids.inpoint.model.response.Resources
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    @GET("/video/{video_id}/config")
    fun getVideoInfo(
        @Path(value="video_id",encoded = true) video_id: String): Call<ResponseVimeo>


    @GET("Api/Login/ValidateUser")
    fun validateUser(
        @Query(ApiParameters.EMAIL) email: String,
        @Query(ApiParameters.PASSWORD) password: String,
        @Query(ApiParameters.LANG) lang: String

    ): Call<ResponseUserInfos>

    @GET("Api/Login/ValidateUser")
    fun validateUserMessage(
        @Query(ApiParameters.EMAIL) email: String,
        @Query(ApiParameters.PASSWORD) password: String,
        @Query(ApiParameters.LANG) lang: String

    ): Call<String>

    @POST("Api/Post/GetAll")
    fun getAllPost(@Body param:JsonParameters): Call<ArrayList<ResponsePost>>


    @GET("Api/PostComment/GetByPostId")
    fun getPostById(
        @Query(ApiParameters.ID) id: Int,
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.LAST_DATE) lastDate: String,
        @Query(ApiParameters.DETAILS) details: Boolean

    ): Call<ArrayList<ResponseComments>>



    @GET("Api/Login/Registration")
    fun registration(
        @Query(ApiParameters.USERNAME) username: String,
        @Query(ApiParameters.EMAIL) email: String,
        @Query(ApiParameters.PASSWORD) password: String,
        @Query(ApiParameters.DOB) dob: String,
        @Query(ApiParameters.TYPE) type: Int,
        @Query(ApiParameters.LANG) lang: String

    ): Call<String>



    @GET("Api/Login/ResetPassword")
    fun resetPassword(
        @Query(ApiParameters.USERNAME) username: String,
        @Query(ApiParameters.LANG) lang: String

    ): Call<String>


    @POST("Api/PostComment/Save")
    fun sendComment(@Body param:JsonParameters): Call<ResponseSaveComment>


    @GET("Api/PostLike/Save")
    fun setLike(
        @Query(ApiParameters.POST_ID) id: Int
   ): Call<Void>


    @GET("Api/UserProfile/{Id}")
    fun getUserProfile(
        @Path(value = "Id", encoded = true) id: Int
    ): Call<ResponseUserInfos>


    @GET("Api/UserProfile/UpdatePassword")
    fun updatePassword(
        @Query(ApiParameters.OLD_PASSWORD) old: String,
        @Query(ApiParameters.NEW_PASSWORD) newpass: String
    ): Call<String>


    @GET("Api/GeographicalLookup/GetAll")
    fun getLocationSubLocation(
    ): Call<ArrayList<ResponseLocations>>


    @GET("Api/GeographicalLookup/GetAllLocations")
    fun getLocations(
    ): Call<ArrayList<ResponseLocations>>


    @GET("Api/GeneralLookup/GetAll")
    fun getGeneralLookup(
    ): Call<ArrayList<ResponseLocations>>

    @GET("Api/GeneralLookup/parent/{id}")
    fun getGeneralLookupParentById(
        @Path(
            value = "id",
            encoded = true
        ) id: Int
    ): Call<ArrayList<ResponseLocations>>


    @POST("Api/UserProfile/UpdateUser")
    fun updateUser(@Body param:RequestUser): Call<Void>


    @GET("Api/UserProfile/UpdateUserName")
    fun updateUsernameBio(
        @Query(ApiParameters.USERNAME) username: String,
        @Query(ApiParameters.BIO) bio: String,
        @Query(ApiParameters.USER_ID) userId: Int

    ): Call<Void>







    @GET("Api/UserProfile/GetUserFollowers")
    fun getFollowers( @Query(ApiParameters.PAGE_USER_ID) id: Int): Call<ArrayList<ResponseFollowers>>



    @DELETE("Api/PostComment/{id}")
     fun deleteComment(@Path(value = "id", encoded = true) id: Int): Call<ResponseMessage>

    @GET("Api/PostComment/GetAllReplies")
    fun getAllReplies(@Query(ApiParameters.COMMENT_ID) CommentId: Int): Call<ArrayList<ResponseSubCommrent>>


/*    @GET("Api/EventResource/event/{id}")
    fun getEventResources(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<ResponseEventFundRes>>*/



/*    @GET("Api/EventFunding/event/{id}")
    fun getEventFunds(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<ResponseEventFundRes>>*/


    @GET("Api/EventResource/event/GetEventResources_SYNC")
    fun getEventResources(
        @Query(ApiParameters.POST_ID) id: Int
    ): Call<ArrayList<ResponseEventFundRes>>


    @GET("Api/EventFunding/event/GetEventFunding_SYNC")
    fun getEventFunds(
        @Query(ApiParameters.POST_ID) id: Int
    ): Call<ArrayList<ResponseEventFundRes>>

    @POST("Api/EventSponsor/SaveEventResourceSponsor")
    fun saveResourcesSponsor(@Query(ApiParameters.EVENT_RESOURCES_ID) id: Int): Call<Boolean>

    @POST("Api/EventSponsor/SaveEventFundingSponsor")
    fun saveFundingSponsor(@Query(ApiParameters.EVENT_FUNDING_ID) id: Int): Call<Boolean>


    @DELETE("Api/EventSponsor/DeleteEventResourceSponsor")
    fun deleteResourcesSponsor(@Query(ApiParameters.EVENT_RESOURCES_ID) id: Int): Call<Boolean>

    @DELETE("Api/EventSponsor/DeleteEventFundingSponsor")
    fun deleteFundingSponsor(@Query(ApiParameters.EVENT_FUNDING_ID) id: Int): Call<Boolean>

    @GET("Api/PostType/GetAll")
    fun getPostTypes( @Query(ApiParameters.DIV_TYPE) type: Int): Call<ArrayList<ResponsePostType>>


    @GET("Api/Category/GetAll")
    fun getCategories(): Call<ArrayList<ResponseCategory>>


    @GET("Api/UserProfile/GetAll")
    fun getAllUsers(): Call<ArrayList<ResponseUser>>

    @GET("Api/Post/PostCategories/{PostId}")
    fun getCategoriesById(
        @Path(value = "PostId", encoded = true) PostId: Int
    ): Call<ArrayList<ResponseCategory>>



    @POST("Api/Post/PostCategories/{PostId}")
    fun sendverification(
        @Path(value = "PostId", encoded = true) PostId: Int,
        @Body param:ArrayList<RequestCategories>
    ): Call<String>

    @POST("Api/Post/Save")
    fun savePost(@Body param:RequestPost): Call<Any>



    @Multipart
    @POST("Api/Post/Save")
    fun savePost(
        @Part(ApiParameters.JSON) id: RequestBody,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<ResponsePost>


    @Multipart
    @POST("Api/Event/Save")
    fun saveEvent(
        @Part(ApiParameters.JSON) id: RequestBody,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<Void>


    @DELETE("Api/Post/{id}")
    fun deletePost(@Path(value = "id", encoded = true) id: Int): Call<Void>



    @GET("Api/MediaFile/GetByUserId")
    fun getProfileGallery( @Query(ApiParameters.USER_ID) id: Int): Call<ArrayList<ResponseMedia>>

    @GET("Api/UserProfile/UpdateProfileImage")
    fun updateProfileImage(@Query(ApiParameters.FILE_NAME) filename: String, @Query(ApiParameters.USER_ID) id: Int): Call<String>

    @GET("Api/UserProfile/UpdateCoverImage")
    fun updateCoverImage(@Query(ApiParameters.FILE_NAME) filename: String, @Query(ApiParameters.USER_ID) id: Int): Call<Void>



    @Multipart
    @POST("Api/UploadFile/PostProfileImage")
    fun uploadProfileImage(
        @Part(ApiParameters.PAGE_USER_ID) id: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<String>


    @GET("Api/EventDate/event/{id}")
    fun getEventDates(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<Dates>>


    @GET("Api/EventFunding/event/{id}")
    fun getEventFunding(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<Funding>>


    @GET("Api/EventOrganizer/event/{id}")
    fun getOrganizers(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<ResponseUser>>


    @GET("Api/EventPartner/event/{id}")
    fun getPartners(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<ResponseUser>>



    @GET("Api/EventResource/event/{id}")
    fun getResources(
        @Path(value = "id", encoded = true) id: Int
    ): Call<ArrayList<Resources>>


    @GET("Api/UserNotification/GetAll")
    fun getNotifications(): Call<ArrayList<ResponseNotification>>


    @POST("Api/UserNotification/SetNotificationSeen")
    fun setSeenNotification( @Query(ApiParameters.ID) id: Int): Call<ResponseNotification>

    @GET("Api/Post/{Id}")
    fun getPostById(
        @Path(value = "Id", encoded = true) id: Int
    ): Call<ResponsePost>


    @GET("Api/UserFollower/Save")
    fun follow( @Query(ApiParameters.USER_ID) id: Int): Call<Boolean>


    @GET("Api/Message/GetAllHeader")
    fun getAllHeader(): Call<ArrayList<ResponseUser>>


    @GET("Api/Message/Get")
    fun getChats(
        @Query(ApiParameters.RECEIVER_ID) id: Int,
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.TAKE) take: Int,
        @Query(ApiParameters.MAX_ID) maxId: Int
    ): Call<ArrayList<ResponseChat>>


    @POST("Api/Message/Save")
    fun sendChat(
        @Body param:ResponseChat
    ):  Call<ResponseChat>



    @GET("Api/TeamMessage/Get")
    fun getTeamChat(
        @Query(ApiParameters.TEAM_ID) id: Int,
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.TAKE) take: Int,
        @Query(ApiParameters.MAX_ID) maxId: Int
    ): Call<ArrayList<ResponseChat>>


    @POST("Api/TeamMessage/Save")
    fun sendTeamChat(
        @Body param:ResponseChat
    ):  Call<ResponseChat>



    @GET("Api/Course/GetAll")
    fun getAllCources(): Call<ArrayList<ResponseCourse>>

    @GET("Api/Publication/GetAll")
    fun getPublication(): Call<ArrayList<ResponsePublication>>


    @GET("Api/Course/GetOnlineCourses")
    fun getOnlineCourses(@Query(ApiParameters.TEXT) text: String,
                         @Query(ApiParameters.SORT) sort: String): Call<ArrayList<ResponseOnlineCourses>>

    @GET("Api/Publication/GetOnlinePublications")
    fun getOnlinePublications(@Query(ApiParameters.TEXT) text: String,
                              @Query(ApiParameters.SORT) sort: String): Call<ArrayList<ResponseOnlinePublication>>




    @GET("Api/Course/GetOnlineCourseDetails")
    fun getOnlineCoursesDetails(@Query(ApiParameters.COURSE_ID) course_id: Int): Call<ArrayList<ResponseOnlineCourseDetail>>


    @GET("Api/Lessons/GetOnlineLessonDetails")
    fun getOnlineLessonDetails(@Query(ApiParameters.COURSE_ID) course_id: Int,@Query(ApiParameters.LESSON_ID) lesson_id: Int): Call<ResponseOnlineCourseDetail>


    @FormUrlEncoded
    @POST("Api/UserLessonCompletion/AddUserLessonCompletion")
    fun saveLessonCompeted(
        @Field(ApiParameters.LESSON_ID) lessonId: Int,
        @Field(ApiParameters.USER_ID) userId: Int,
        @Field(ApiParameters.COMPLETED) completed: Boolean

    ): Call<Boolean>



    @GET("Api/UserLessonCompletion/IsUserLessonCompleted")
    fun checkLessonCompleted(
        @Query(ApiParameters.LESSON_ID) lessonId: Int,
        @Query(ApiParameters.USER_ID) userId: Int

    ): Call<Boolean>



    @GET("Api/Course/GetCourseRegisteredUsers")
    fun getRegisteredUsers   (@Query(ApiParameters.COURSE_ID) course_id: Int,
                              @Query(ApiParameters.TAKE) take: Int,
                              @Query(ApiParameters.SKIP) skip: Int
                              ): Call<ArrayList<ResponseCourseRegisteredUsers>>

    @GET("Api/Course/GetCourseReviews")
    fun getCourseReviews(@Query(ApiParameters.COURSE_ID) course_id: Int,
                         @Query(ApiParameters.TAKE) take: Int,
                         @Query(ApiParameters.SKIP) skip: Int): Call<ArrayList<ResponseReview>>



    @GET("Api/Lessons/GetByCourseId/{CourseId}")
    fun getCourseLessons(
        @Path(value = "CourseId", encoded = true) CourseId: Int
    ): Call<ArrayList<ResponseLesson>>


    @GET("Api/MediaFile/GetByLessonId")
    fun getLessonMedia(@Query(ApiParameters.LESSON_ID) lessonId: Int): Call<ArrayList<ResponseLessonMedia>>


    @GET("Api/Course/CheckRegisteredUser")
    fun checkCourseRegistered(@Query(ApiParameters.COURSE_ID) courseId: Int): Call<Int>


    @POST("Api/Course/UserCourseRegistration")
    fun saveCourseRegistration(@Query(ApiParameters.COURSE_ID) courseId: Int, @Query(ApiParameters.ACTION) Action: String): Call<Boolean>


    @GET("Api/Search/Search")
    fun search(
        @Query(ApiParameters.KEY) key: String,
        @Query(ApiParameters.ALL) all: Boolean)
            : Call<ArrayList<ResponseSearch>>


    //<editor-fold desc="Teams">
    @GET("Api/Team/GetMyTeams")
    fun getMyTeams(@Query(ApiParameters.SEARCH_KEY) searchKey: String): Call<ArrayList<TeamStartup>>

    @GET("Api/Team/GetAllTeams")
    fun getAllTeams(
        @Query(ApiParameters.SEARCH_KEY) searchKey: String,
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.TAKE) take: Int,
        @Query(ApiParameters.MAX_ID) maxId: Int
    ): Call<ArrayList<TeamStartup>>

    @POST("Api/Team/Save")
    fun saveTeam(@Body param: TeamStartup): Call<Int>

    @GET("Api/TeamMember/team/{Id}")
    fun getTeamMembers(@Path(value = "Id", encoded = true) id: Int): Call<ArrayList<TeamStartupMember>>



    @GET("Api/TeamMember/GetTeamMembers")
    fun getTaskTeamMembers(@Query(ApiParameters.TEAM_ID) id: Int): Call<ArrayList<ResponseUser>>


    @POST("Api/TeamMember/Save")
    fun saveTeamMember(@Body param: TeamStartupMember): Call<Void>



    @DELETE("Api/TeamMember/{id}")
    fun deleteTeamMember(@Path(value = "id", encoded = true) id: Int): Call<Void>
    //</editor-fold>


    @GET("Api/Team/JoinTeam")
    fun joinTeam(
        @Query(ApiParameters.TEAM_ID) ID: Int
    ): Call<Boolean>

    @GET("Api/TeamRequest/GetAll")
    fun getAllTeamRequests(
        @Query(ApiParameters.TEAM_ID) ID: Int
    ): Call<ArrayList<ResponseTeamRequest>>

    @POST("Api/TeamRequest/AcceptTeamRequests")
    fun acceptTeamRequest(
        @Query(ApiParameters.ID) id: Int,
        @Query(ApiParameters.APPROVED) approved: Boolean,
        @Query(ApiParameters.REJECTED) rejected: Boolean
    ): Call<Boolean>



    @GET("Api/Project/GetAll")
    fun getTeamProjects(
        @Query(ApiParameters.LANG) key: String,
        @Query(ApiParameters.TEAM_ID) id: Int
    ): Call<ArrayList<ResponseTeamProject>>

    @POST("Api/Project/Save")
    fun saveProject(@Body param: ResponseTeamProject): Call<ResponseTeamProject>


    @GET("Api/Project/GetById")
    fun getById(
        @Query(ApiParameters.ID) id: Int,
        @Query(ApiParameters.LANG) lang: String,
        @Query(ApiParameters.TEAM_ID) teamId: Int
    ): Call<ResponseTeamProject>

    @DELETE("Api/Project/DeleteProject")
    fun deleteProject(
        @Query(ApiParameters.PROJECT_ID) project_id:Int,
        @Query(ApiParameters.TEAM_ID) team_id:Int
    ): Call<Boolean>

    @GET("Api/ProjectMember/GetProjectMembers")
    fun getProjectMembers(
        @Query(ApiParameters.PROJECT_ID) id: Int
    ): Call<ArrayList<TeamStartupMember>>
    //</editor-fold>

    @GET("Api/TeamFile/GetAll")
    fun getTeamFiles(
        @Query(ApiParameters.TEAM_ID) id: Int
    ): Call<ArrayList<ResponseTeamFile>>


    @DELETE("Api/TeamFile/{id}")
    fun deleteFile(@Path(value = "id", encoded = true) id: Int): Call<Void>


    @Multipart
    @POST("Api/TeamFile/Save")
    fun saveFile(
        @Part(ApiParameters.JSON) id: RequestBody,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<Void>


    //<editor-fold desc="Projects">
    @GET("Api/Task/GetAll")
    fun getTeamTasks(
        @Query(ApiParameters.TEAM_ID) id: Int
    ): Call<ArrayList<ResponseTeamTask>>


    @DELETE("Api/Task/DeleteTask")
    fun deleteTask(
        @Query(ApiParameters.TASK_ID) task_id:Int,
        @Query(ApiParameters.TEAM_ID) team_id:Int
    ): Call<Boolean>




    @DELETE("Api/Team/{id}")
    fun deleteTeam(@Path(value = "id", encoded = true) id: Int): Call<Void>

    @POST("Api/Task/Save")
    fun saveTask(@Body param: ResponseTeamTask): Call<Int?>

    @POST("Api/TaskAssignedUsers/Save")
    fun saveTaskAssignedUser(
        @Query(ApiParameters.TASK_ID) taskId: Int,
        @Query(ApiParameters.USER_IDS) userIds: String
    ): Call<Void>

    @POST("Api/ProjectMember/Save")
    fun saveProjectMember(
        @Query(ApiParameters.PORJECT_ID) projectId: Int,
        @Query(ApiParameters.USER_IDS) userIds: String
    ): Call<Boolean>

    @GET("Api/Task/GetById")
    fun getTeamTaskById(
        @Query(ApiParameters.ID) id: Int,
        @Query(ApiParameters.TEAM_ID) teamId: Int
    ): Call<ResponseTeamTask>

    @GET("Api/TaskAssignedUsers/GetTaskMembersIds")
    fun getTaskMembersIds(
        @Query(ApiParameters.TASK_ID) id: Int
    ): Call<String>
    //</editor-fold>


    @GET("Api/TaskAssignedUsers/GetTaskMembers")
    fun getTaskMembers(
        @Query(ApiParameters.TASK_ID) id: Int
    ): Call<ArrayList<ResponseUser>>


    @GET("Api/ProjectMember/GetProjectMembers")
    fun getPopupProjectMembers(
        @Query(ApiParameters.PROJECT_ID) id: Int
    ): Call<ArrayList<ResponseUser>>

    //<editor-fold desc="User Info">
    @GET("Api/UserEducation/user/{${ApiParameters.ID}}")
    fun getUserEducation(
        @Path(ApiParameters.ID) id: Int
    ): Call<ArrayList<UserEducation>>

    @POST("Api/UserEducation/Save")
    fun postUserEducation(
        @Body param: UserEducation
    ): Call<Any?>

    @DELETE("Api/UserEducation/{id}")
    fun deleteUserEducation(
        @Path(value = "id", encoded = true) id: Int
    ): Call<Void>


    @GET("Api/UserWork/user/{${ApiParameters.ID}}")
    fun getUserWork(
        @Path(ApiParameters.ID) id: Int
    ): Call<ArrayList<UserWork>>

    @POST("Api/UserWork/Save")
    fun postUserWork(
        @Body param: UserWork
    ): Call<Any?>

    @DELETE("Api/UserWork/{id}")
    fun deleteUserWork(
        @Path("id", encoded = true) id: Int
    ): Call<Void>


    @GET("Api/UserExtraCertification/user/{${ApiParameters.ID}}")
    fun getUserExtraCertification(
        @Path(ApiParameters.ID) id: Int
    ): Call<ArrayList<UserExtraCertification>>

    @POST("Api/UserExtraCertification/Save")
    fun postUserExtraCertification(
        @Body param: UserExtraCertification
    ): Call<Any?>

    @DELETE("Api/UserExtraCertification/{id}")
    fun deleteUserExtraCertification(
        @Path("id", encoded = true) id: Int
    ): Call<Void>
    //</editor-fold>

    @GET("Api/EventParticipant/event/{id}")
    fun getEventParticipant(
        @Path(value = "id", encoded = true) CourseId: Int
    ): Call<ArrayList<ResponseParticipant>>


    @POST("Api/EventParticipant/Participate")
    fun participateToEvent( @Query(ApiParameters.EVENT_ID) id: Int): Call<Int>


    @GET("Api/PointRule/GetUserPoints")
    fun getUserPoints(
        @Query(ApiParameters.USER_ID) id: Int
    ): Call<ArrayList<ResponsePoint>>

    @GET("Api/PostLike/Get")
    fun getLikers(
        @Query(ApiParameters.ID) id: Int
    ): Call<ArrayList<ResponseFollowers>>



    @GET("Api/EventResource/event/GetEventResources_SYNC")
    fun getEventResources_synk(
        @Query(ApiParameters.ID) id: Int
    ): Call<ArrayList<ResponseFollowers>>


    @POST("Api/Notification/SaveDevice")
    fun saveDevice(
        @Body param:ResponseDevice
    ): Call<ResponseDevice>



    @POST("Api/PostReport/Save")
    fun postReport(
        @Body param:ResponseSendReport
    ): Call<ResponseSendReport>


    @GET("Api/Notification/GetAllMobileConfig")
    fun getMobileConfigurations(): Call<ArrayList<ResponseConfiguration>>


    @GET("Api/Task/GetTasksForPlan")
    fun getTaskForPlan(
        @Query(ApiParameters.TEAM_ID) TeamId: Int,
        @Query(ApiParameters.PROJECT_ID) ProjectId: Int
    ): Call<ArrayList<ResponseConfiguration>>


    @GET("Api/TeamMember/GetUserTeams")
    fun getUserTeams(): Call<ArrayList<ResponseUserTeam>>



    @GET("Api/Startup/GetAll")
    fun getAllStartup(
        @Query(ApiParameters.SEARCH_KEY) searchKey: String,
        @Query(ApiParameters.SKIP) skip: Int,
        @Query(ApiParameters.TAKE) take: Int,
        @Query(ApiParameters.MAX_ID) maxId: Int
    ): Call<ArrayList<TeamStartup>>



    @POST("Api/Startup/Save")
    fun saveStartup(@Body param: TeamStartup): Call<Int>


    @DELETE("Api/Startup/{id}")
    fun deleteStartup(@Path(value = "id", encoded = true) id: Int): Call<Void>


    @GET("Api/StartupMember/Startup/{id}")
    fun getStartupMembers(@Path(value = "id", encoded = true) id: Int): Call<ArrayList<TeamStartupMember>>

    @POST("Api/StartupMember/Save")
    fun saveStartupMember(@Body param: TeamStartupMember): Call<Void>


    @DELETE("Api/StartupMember/{id}")
    fun deleteStartupMember(@Path(value = "id", encoded = true) id: Int): Call<Void>



    @GET("Api/Publication/ViewOnlinePublication")
    fun getPublicationDetails(@Query(ApiParameters.ID) id: Int): Call<ResponseCoursePublication>


    @FormUrlEncoded
    @POST("Api/Course/SaveCourseReviews")
    fun saveCourseReview(
        @Field(ApiParameters.ID) id: Int,
        @Field(ApiParameters.COURSE_ID) courseId: Int,
        @Field(ApiParameters.USER_ID) userid: Int,
        @Field(ApiParameters.RATING) rating: Int,
        @Field(ApiParameters.DELETED) deleted: Boolean,
        @Field(ApiParameters.TEXT) text: String,
        @Field(ApiParameters.DATE) date: String
        ): Call<RequestReview>


    @GET("Api/Publication/GetOnlinePublicationsByCourseId")
    fun getPublicationByCourseId(@Query(ApiParameters.COURSE_ID) id: Int): Call<ResponseCoursePublication>



    @GET("Api/UserProfile/GetById")
    fun getUserDetails(@Query(ApiParameters.ID) id: Int): Call<ResponseStartupProfile>




    @GET("Api/UserProduct/user/{id}")
    fun getStartupProducts(@Path(value = "id", encoded = true) id: Int): Call<ArrayList<ResponseStartupProduct>>


    @Multipart
    @POST("Api/UserProduct/SaveWithFile")
    fun saveStartupProduct(
        @Part(ApiParameters.JSON) id: RequestBody,
        @Part file: ArrayList<MultipartBody.Part>
    ): Call<Void>

    @GET("Api/UserBranch/user/{id}")
    fun getStartupBranches(@Path(value = "id", encoded = true) id: Int): Call<ArrayList<ResponseBranch>>

    @POST("Api/UserBranch/Save")
    fun saveStartupBranch(@Body param: ResponseBranch): Call<ResponseBranch>

   // @DELETE("Api/UserBranch/{id}")
    @DELETE("Api/UserBranch/DeleteAsync")
    fun deleteStartupBranch(@Query(ApiParameters.ID) task_id:Int): Call<Void>


    @GET("Api/UserOpeningHour/user/{id}")
    fun getStartupOpeningHour(@Path(value = "id", encoded = true) id: Int): Call<ArrayList<ResponseOpeningHour>>

    @POST("Api/UserOpeningHour/Save")
    fun saveStartupOpenningHour(@Body param: ResponseOpeningHour): Call<ResponseOpeningHour>

    @DELETE("Api/UserOpeningHour/{id}")
    fun deleteStartupOpeningHour(@Path(value = "id", encoded = true) id: Int): Call<Void>


    @GET("Api/StartupDetail/GetAll")
    fun getStartupPartners(@Query(ApiParameters.STARTUP_ID) id: Int): Call<ArrayList<ResponseUser>>


    @Multipart
    @POST("Api/StartupDetail/Save")
    fun saveStartupdetails(
        @Part(ApiParameters.JSON) id: RequestBody,
        @Part file: MultipartBody.Part
    ): Call<ResponseUser>

    @Multipart
    @POST("Api/StartupDetail/Save")
    fun saveStartupdetails(
        @Part(ApiParameters.JSON) id: RequestBody
    ): Call<ResponseUser>



    @DELETE("Api/StartupDetail/DeleteAsync")
    fun deleteStartupUser(@Query(ApiParameters.id) id:Int): Call<Void>


    @GET("Api/StartupReview/GetStartupReviews")
    fun getStartupReviews(@Query(ApiParameters.STARTUP_ID) id: Int,
                          @Query(ApiParameters.TAKE) take: Int,
                          @Query(ApiParameters.SKIP) skip: Int
                          ): Call<ArrayList<ResponseReview>>

    @DELETE("Api/StartupReview/DeleteStartupReview")
    fun deleteStartupReview(@Query(ApiParameters.ID) id:Int): Call<Void>



    @FormUrlEncoded
    @POST("Api/StartupReview/SaveStartupReviews")
    fun saveStartupReview(
        @Field(ApiParameters.ID) id: Int,
        @Field(ApiParameters.STARTUP_ID) startupId: Int,
        @Field(ApiParameters.TEXT) text: String,
        @Field(ApiParameters.RATING) rating: Int,
        @Field(ApiParameters.DATE) date: String,
        @Field(ApiParameters.DELETED) deleted: Boolean


    ): Call<RequestReview>


    @GET("Api/StartupSocialMediaLink/GetAllSocialMedia")
    fun getStartupAllSocialMedias(): Call<ArrayList<ResponseSocialMedia>>


    @GET("Api/StartupSocialMediaLink/GetAll")
    fun getStartupSocialMedias(@Query(ApiParameters.STARTUP_ID) startupId: Int): Call<ArrayList<ResponseSocialMedia>>

    @DELETE("Api/UserProduct/DeleteAsync")
    fun deleteStartupProduct(@Query(ApiParameters.id) id:Int): Call<Void>

    @DELETE("Api/StartupSocialMediaLink/DeleteAsync")
    fun deleteStartupProfileLink(@Query(ApiParameters.id) id:Int): Call<Void>


    @FormUrlEncoded
    @POST("Api/UserProfile/UpdateUser")
    fun updateStartupProfile(
        @Field(ApiParameters.ID) id: Int,
        @Field(ApiParameters.BIO) bio: String,
        @Field(ApiParameters.CITY) city: Int,
        @Field(ApiParameters.LOCATION) location: Int,
        @Field(ApiParameters.PHONE_NUMBER) phone: String,
        @Field(ApiParameters.SHOW_TO_PUBLIC) show_to_public: Boolean,
        @Field(ApiParameters.USERNAME) username: String,

        @Field(ApiParameters.DATE_OF_BIRTH) dob: String,
        @Field(ApiParameters.SUB_TYPE) subType: Int,
        @Field(ApiParameters.WEBSITE) website: String,
        @Field(ApiParameters.WORK_EXPERIENCE) exp: Int,
        @Field(ApiParameters.OVERVIEW) overView: String,
        @Field(ApiParameters.INDUSTRY) industry: Int,
        @Field(ApiParameters.FULL_ADDRESS) fullAddress: String,
        @Field(ApiParameters.COMPANY_SIZE) companuSize: Int,
        @Field(ApiParameters.SPECIALITIES) specialities: Int
    ): Call<Void>



    @FormUrlEncoded
    @POST("Api/StartupSocialMediaLink/Save")
    fun saveSocialMedia(
        @Field(ApiParameters.LINK) link: String,
        @Field(ApiParameters.SOCIAL_MEDIA_ID) SocialMediaId: Int,
        @Field(ApiParameters.STARTUP_ID) startupid: Int

    ): Call<Void>



    @DELETE("Api/Course/DeleteCourseReviews")
    fun deleteCourseReview(@Query(ApiParameters.ID) id:Int): Call<Boolean>


    @POST("Api/Team/ChangePrivacy")
    fun changePrivacy(@Query(ApiParameters.TEAM_ID) teamId: Int,@Query(ApiParameters.PUBLIC) public: Boolean): Call<Void>

/*    @POST("Api/UserProfile/UpdateUserName")
    fun updateUserNameBio(@Query(ApiParameters.USERNAME) username: String,
                          @Query(ApiParameters.BIO) bio: String,
                          @Query(ApiParameters.USER_ID) userId: String): Call<Void>*/

}
