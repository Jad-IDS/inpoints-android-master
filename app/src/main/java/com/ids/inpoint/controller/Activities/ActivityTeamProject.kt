package com.ids.inpoint.controller.Activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterMultiSelectSpinnerImageText
import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.ItemSpinnerTextImage
import com.ids.inpoint.model.ResponseTeamProject
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.model.response.ResponseLocations
import com.ids.inpoint.model.response.ResponseUser
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_team_project.*
import kotlinx.android.synthetic.main.activity_team_project.btSave
import kotlinx.android.synthetic.main.activity_team_project.etName
import kotlinx.android.synthetic.main.activity_team_project.etSummary
import kotlinx.android.synthetic.main.activity_team_project.tvAssignees
import kotlinx.android.synthetic.main.activity_team_project.tvDueDate
import kotlinx.android.synthetic.main.activity_team_project.tvEndDate
import kotlinx.android.synthetic.main.activity_team_project.tvStartDate
import kotlinx.android.synthetic.main.activity_team_project.tvTitle

import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar_general.*
import java.util.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class ActivityTeamProject : AppCompactBase(), RVOnItemClickListener {

    private lateinit var dateListener: DatePickerDialog.OnDateSetListener

    private var activeCalendar = 0

    private lateinit var startDateCalendar: Calendar
    private lateinit var dueDateCalendar: Calendar
    private lateinit var endDateCalendar: Calendar

    private lateinit var typesAdapter: AdapterSpinner
    private var postTypes: ArrayList<ItemSpinner> = ArrayList()

    private lateinit var project: ResponseTeamProject
    private var selectedTypeId: Int? = null

    private var dialog: Dialog? = null
    private var rvVerifyCategories: RecyclerView? = null
    private var btVerify: LinearLayout? = null
    private lateinit var adapterMembers: AdapterMultiSelectSpinnerImageText
    private var arrayMembers: java.util.ArrayList<ItemSpinnerTextImage> = arrayListOf()
    private var gotMembers = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_project)

        init()
    }

    private fun init() {
        supportActionBar!!.hide()
        btBack.setOnClickListener { super.onBackPressed() }

        startDateCalendar = Calendar.getInstance()
        dueDateCalendar = Calendar.getInstance()
        endDateCalendar = Calendar.getInstance()

        getProjectTypes()

        var projectId = intent.getIntExtra(AppConstants.PROJECT_ID, 0)

        if (projectId == 0) {
            project = ResponseTeamProject()
        } else {
            tvTitle.text = getString(R.string.edit_project)
            getProject(projectId)
        }

        dateListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

            when (activeCalendar) {
                0 ->
                    fillDate(year, monthOfYear, dayOfMonth, startDateCalendar, tvStartDate)
                1 ->
                    fillDate(year, monthOfYear, dayOfMonth, dueDateCalendar, tvDueDate)
                2 ->
                    fillDate(year, monthOfYear, dayOfMonth, endDateCalendar, tvEndDate)
            }
        }

        tvStartDate.setOnClickListener {
            activeCalendar = 0
            DatePickerDialog(
                this, dateListener, startDateCalendar.get(Calendar.YEAR), startDateCalendar.get(
                    Calendar.MONTH
                ), startDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tvDueDate.setOnClickListener {
            activeCalendar = 1
            DatePickerDialog(
                this, dateListener, dueDateCalendar.get(Calendar.YEAR), dueDateCalendar.get(
                    Calendar.MONTH
                ), dueDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        tvEndDate.setOnClickListener {
            activeCalendar = 2
            DatePickerDialog(
                this, dateListener, endDateCalendar.get(Calendar.YEAR), endDateCalendar.get(
                    Calendar.MONTH
                ), endDateCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        llAssignees.setOnClickListener {
            showPopupMembers()
        }


        llType.setOnClickListener {
            if (llTypes.visibility == View.VISIBLE) {
                llTypes.visibility = View.GONE
            } else {
                llTypes.visibility = View.VISIBLE
            }
        }

        btSave.setOnClickListener {
            if (etName.text.trim().isEmpty() ||
                etSummary.text.trim().isEmpty() ||
                tvStartDate.text.trim().isEmpty() ||
                tvDueDate.text.isEmpty()
            ) {
                AppHelper.createDialog(this, getString(R.string.fill_required_fields))
            } else {
                project.isDeleted = false
                project.client = etClient.text.toString()
                project.name = etName.text.toString()
                project.summary = etSummary.text.toString()
                project.startDate = tvStartDate.text.toString()
                project.endDate = tvEndDate.text.toString()
                project.dueDate = tvDueDate.text.toString()
                project.code = etCode.text.toString()
                project.teamId = MyApplication.selectedStartupTeam.id
                project.userId = MyApplication.userLoginInfo.id
                project.type = selectedTypeId

                saveProject(project)
            }
        }
    }

    private fun fillDate(
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int,
        activeCalendar: Calendar,
        activeTextView: TextView
    ) {
        activeCalendar.set(Calendar.YEAR, year)
        activeCalendar.set(Calendar.MONTH, monthOfYear)
        activeCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        activeTextView!!.text = AppHelper.dateEvent.format(activeCalendar.time)
    }

    private fun getProjectTypes() {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getGeneralLookupParentById(
                AppConstants.PROJECT_TYPE
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseLocations>>,
                    response: Response<ArrayList<ResponseLocations>>
                ) {
                    try {
                        onProjectTypesRetrieved(response.body())
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseLocations>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun onProjectTypesRetrieved(response: ArrayList<ResponseLocations>?) {

        postTypes.clear()
        for (i in response!!.indices) {
            if(MyApplication.languageCode==AppConstants.LANG_ENGLISH)
               postTypes.add(ItemSpinner(response[i].id, response[i].valueEn))
            else
               postTypes.add(ItemSpinner(response[i].id, response[i].valueAr))
        }

        typesAdapter = AdapterSpinner(postTypes, this, AppConstants.SPINNER_EVENT_SORT)
        val glm = GridLayoutManager(this, 1)
        rvTypes.adapter = typesAdapter
        rvTypes.layoutManager = glm
        loading.visibility = View.GONE
    }

    private fun saveProject(project: ResponseTeamProject) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveProject(
                project
            )?.enqueue(object : Callback<ResponseTeamProject> {
                override fun onResponse(
                    call: Call<ResponseTeamProject>,
                    response: Response<ResponseTeamProject>
                ) {
                    try {
                        saveProjectMembers(response.body()!!.id!!)
                        loading.visibility = View.GONE
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTeamProject>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                    AppHelper.createDialog(
                        this@ActivityTeamProject,
                        getString(R.string.project_creation_failed)
                    )
                }
            })
    }


    private fun saveProjectMembers(selectedTaskId: Int) {
        loading.visibility = View.VISIBLE

        var users = arrayMembers.filter { it.isSelected }.map { it.id }.joinToString(",")
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveProjectMember(
                selectedTaskId,
                users

            )?.enqueue(object : Callback<Boolean> {
                override fun onResponse(
                    call: Call<Boolean>,
                    response: Response<Boolean>
                ) {
                    try {
                        btBack.callOnClick()
                        loading.visibility = View.GONE
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<Boolean>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }


    private fun getProject(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getById(
                id,
                MyApplication.languageCode,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ResponseTeamProject> {
                override fun onResponse(
                    call: Call<ResponseTeamProject>,
                    response: Response<ResponseTeamProject>
                ) {
                    try {
                        onProjectRetrieved(response.body())
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTeamProject>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun onProjectRetrieved(body: ResponseTeamProject?) {
        if (body != null) {
            project = body
            try {
                etClient.setText(project.client)
            } catch (ex: Exception) {
            }

            try {
                etName.setText(project.name)
            } catch (ex: Exception) {
            }

            try {
                etSummary.setText(project.summary)
            } catch (ex: Exception) {
            }

            try {
                var d = AppHelper.dateEvent.parse(project.startDate)
                startDateCalendar.time = d
                fillDate(
                    startDateCalendar.get(Calendar.YEAR),
                    startDateCalendar.get(Calendar.MONTH),
                    startDateCalendar.get(Calendar.DAY_OF_MONTH),
                    startDateCalendar,
                    tvStartDate
                )
            } catch (ex: Exception) {
            }

            try {
                var d = AppHelper.dateEvent.parse(project.endDate)
                endDateCalendar.time = d
                fillDate(
                    endDateCalendar.get(Calendar.YEAR),
                    endDateCalendar.get(Calendar.MONTH),
                    endDateCalendar.get(Calendar.DAY_OF_MONTH),
                    endDateCalendar,
                    tvEndDate
                )
            } catch (ex: Exception) {
            }

            try {
                var d = AppHelper.dateEvent.parse(project.dueDate)
                dueDateCalendar.time = d
                fillDate(
                    dueDateCalendar.get(Calendar.YEAR),
                    dueDateCalendar.get(Calendar.MONTH),
                    dueDateCalendar.get(Calendar.DAY_OF_MONTH),
                    dueDateCalendar,
                    tvDueDate
                )
            } catch (ex: Exception) {
            }

            try {
                etCode.setText(project.code)
            } catch (ex: Exception) {
            }

            try {
                selectedTypeId = project.type
                tvTypes.text = postTypes.single { itemSpinner -> itemSpinner.id == selectedTypeId!! }.name
            } catch (ex: Exception) {
            }
        }
    }

    override fun onItemClicked(view: View, position: Int) {
        selectedTypeId = postTypes[position].id
        tvTypes.text = postTypes[position].name
        llTypes.visibility = View.GONE
    }




    private fun showPopupMembers() {

        dialog = Dialog(this, R.style.dialogWithoutTitle)
        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(false)
        dialog!!.setContentView(R.layout.popup_verify)
        dialog!!.setCancelable(true)
        rvVerifyCategories = dialog!!.findViewById<View>(R.id.rvVerifyCategories) as RecyclerView
        var tvSubmit = dialog!!.findViewById<View>(R.id.tvSubmit) as TextView
        var tvSelectTitle = dialog!!.findViewById<View>(R.id.tvSelectTitle) as TextView
        tvSelectTitle.text = getString(R.string.select_members)
        tvSubmit.text = getString(R.string.dialog_ok)
        btVerify = dialog!!.findViewById<View>(R.id.btVerify) as LinearLayout

      if (arrayMembers.size!=0) {
            setMembers()
        } else {

                getTeamMembers(MyApplication.selectedStartupTeam.id!!, true)
        }

        btVerify!!.setOnClickListener {
            tvAssignees.text =
                arrayMembers.filter { q -> q.isSelected }.map { it.text }.joinToString()
            dialog!!.dismiss()
        }

        dialog!!.show();
    }

    private fun setMembers() {
        adapterMembers = AdapterMultiSelectSpinnerImageText(arrayMembers, this)
        val glm = GridLayoutManager(this, 1)
        rvVerifyCategories!!.adapter = adapterMembers
        rvVerifyCategories!!.layoutManager = glm
    }

    private fun getTeamMembers(id: Int, setMembers: Boolean = false) {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTaskTeamMembers(id)?.enqueue(object : Callback<java.util.ArrayList<ResponseUser>> {
                override fun onResponse(
                    call: Call<java.util.ArrayList<ResponseUser>>,
                    response: Response<java.util.ArrayList<ResponseUser>>
                ) {
                    try {
                        onTeamMembersReceived(response.body()!!, setMembers)
                    } catch (E: Exception) {
                    }
                    loading.visibility = View.GONE;
                }

                override fun onFailure(
                    call: Call<java.util.ArrayList<ResponseUser>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE;
                    Toast.makeText(this@ActivityTeamProject, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }



    private fun onTeamMembersReceived(
        response: java.util.ArrayList<ResponseUser>,
        setMembers: Boolean = false
    ) {
        arrayMembers.clear()

//        arrayProjects.add(ItemSpinner(id = 0, name = getString(R.string.project)))

        response.forEach {
            arrayMembers.add(
                ItemSpinnerTextImage(
                    id = it.id,
                    text = it.userName,
                    image = it.image
                )
            )
        }
        loading.visibility = View.GONE

        if (setMembers) {
            setMembers()
        }

     /*   if (gotMembers == false) {
            gotMembers = true
            fillEditData(selectedTaskId)
        }*/
    }

}
