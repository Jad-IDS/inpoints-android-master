package com.ids.inpoint.controller.Activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.ids.inpoint.R
import com.ids.inpoint.controller.Adapters.AdapterCustomSpinner
import com.ids.inpoint.controller.Adapters.AdapterMultiSelectSpinnerImageText
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.controller.Base.AppCompactBase
import com.ids.inpoint.controller.MyApplication
import com.ids.inpoint.model.ItemSpinner
import com.ids.inpoint.model.ItemSpinnerTextImage
import com.ids.inpoint.model.ResponseTeamProject
import com.ids.inpoint.model.TeamStartupMember
import com.ids.inpoint.model.response.ResponseLocations
import com.ids.inpoint.model.response.ResponseTeamTask
import com.ids.inpoint.model.response.ResponseUser
import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.RetrofitClientAuth
import com.ids.inpoint.utils.RetrofitInterface
import kotlinx.android.synthetic.main.activity_team_task.*
import kotlinx.android.synthetic.main.loading.*
import kotlinx.android.synthetic.main.toolbar_general.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class ActivityTeamTask : AppCompactBase(), RVOnItemClickListener {

    private lateinit var adapterTaskStatuses: AdapterCustomSpinner
    private var arrayTaskStatuses: ArrayList<ItemSpinner> = arrayListOf()

    private lateinit var adapterPredecessors: AdapterCustomSpinner
    private var arrayPredecessors: ArrayList<ItemSpinner> = arrayListOf()

    private lateinit var adapterProjects: AdapterCustomSpinner
    private var arrayProjects: ArrayList<ItemSpinner> = arrayListOf()

    private lateinit var adapterMembers: AdapterMultiSelectSpinnerImageText
    private var arrayMembers: ArrayList<ItemSpinnerTextImage> = arrayListOf()

    private lateinit var dateListener: DatePickerDialog.OnDateSetListener
    private var activeCalendar = 0
    private lateinit var startDateCalendar: Calendar
    private lateinit var dueDateCalendar: Calendar
    private lateinit var endDateCalendar: Calendar

    private var dialog: Dialog? = null
    private var rvVerifyCategories: RecyclerView? = null
    private var btVerify: LinearLayout? = null

    var selectedProjectId: Int? = null
    private var task: ResponseTeamTask = ResponseTeamTask()

    private var gotStatuses = false
    private var gotTasks = false
    private var gotProjects = false
    private var gotMembers = false
    private var waitingProjectMembers=false

    private var selectedTaskId: Int = 0
    private var count=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_task)

        init()
    }

    private fun init() {
        supportActionBar!!.hide()
        btBack.setOnClickListener { super.onBackPressed() }

        startDateCalendar = Calendar.getInstance()
        dueDateCalendar = Calendar.getInstance()
        endDateCalendar = Calendar.getInstance()

        selectedTaskId = intent.getIntExtra(AppConstants.TASK_ID, 0)

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

        spProjects.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (count>=3) {
                    arrayMembers.clear()
                    adapterProjects.notifyDataSetChanged()
                    tvAssignees.setText("")

                }
                Log.wtf("count",count++.toString())
                count++

                if (position == 0) {
                    Log.wtf("sp_project","call_team_member")
                    selectedProjectId = null
                    getTeamMembers(MyApplication.selectedStartupTeam.id!!, false,0)
               }
                else {
                    Log.wtf("sp_project","call_project_member")
                    selectedProjectId = arrayProjects[position].id!!
                    getProjectMembers(selectedProjectId!!, false,0)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spStatus.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                try {
                    task.status = arrayTaskStatuses[position].id
                } catch (ex: java.lang.Exception) {
                    var t = ex
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spPredecessors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    task.predecessor = null
                } else {
                    try {
                        task.predecessor = arrayPredecessors[position].id
                    } catch (ex: java.lang.Exception) {
                        var t = ex
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btSave.setOnClickListener {
            if (etName.text.trim().isEmpty() ||
                etSummary.text.trim().isEmpty() ||
                tvStartDate.text.trim().isEmpty() ||
                tvDueDate.text.isEmpty() ||
                tvAssignees.text.isEmpty()
            ) {
                AppHelper.createDialog(this, getString(R.string.fill_required_fields))
            } else {
                task.deleted = false
                task.notes = etNote.text.toString()
                task.name = etName.text.toString()
                task.summary = etSummary.text.toString()
                task.startDate = tvStartDate.text.toString()
                task.endDate = tvEndDate.text.toString()
                task.dueDate = tvDueDate.text.toString()
                task.teamId = MyApplication.selectedStartupTeam.id
                task.projectId = selectedProjectId
                task.estimatedHours =
                    if (tvEstimatedHours.text.isEmpty()) 0 else tvEstimatedHours.text.toString().toInt()
                saveTask()
            }
        }

        getProjectStatuses()
        getTeamTasks()
        getTeamProjects()
        //getTeamMembers(MyApplication.selectedStartupTeam.id!!)

        if (selectedTaskId != 0) {
            tvTitle.text = getString(R.string.edit_task)
            fillEditData(selectedTaskId)
        }
    }

    private fun fillEditData(selectedTaskId: Int) {
        if (selectedTaskId != 0 && gotMembers && gotProjects && gotStatuses && gotTasks &&!waitingProjectMembers) {
            getTask(selectedTaskId)
          //  getTaskMembers(selectedTaskId)
        }
    }

    private fun getProjectStatuses() {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getGeneralLookupParentById(
                AppConstants.TASK_STATUS
            )?.enqueue(object : Callback<ArrayList<ResponseLocations>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseLocations>>,
                    response: Response<ArrayList<ResponseLocations>>
                ) {
                    try {
                        onProjectStatusesRetrieved(response.body())
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

    private fun onProjectStatusesRetrieved(body: java.util.ArrayList<ResponseLocations>?) {
        arrayTaskStatuses.clear()

        //arrayTaskStatuses.add(ItemSpinner(id = 0, name = getString(R.string.none)))

        body?.forEach {
            arrayTaskStatuses.add(
                ItemSpinner(
                    id = it.id,
                    name = if (MyApplication.languageCode == AppConstants.LANG_ENGLISH) it.valueEn else it.valueAr
                )
            )
        }

        adapterTaskStatuses =
            AdapterCustomSpinner(this, arrayTaskStatuses, AppConstants.SPINNER_TYPE_TEXT)

        spStatus.adapter = adapterTaskStatuses
        loading.visibility = View.GONE

        if (gotStatuses == false) {
            gotStatuses = true
            fillEditData(selectedTaskId)
        }
    }

    private fun getTeamTasks() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamTasks(
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamTask>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseTeamTask>>,
                    response: Response<ArrayList<ResponseTeamTask>>
                ) {
                    try {
                        onTeamTaskRetrieved(response.body())

                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseTeamTask>>,
                    throwable: Throwable
                ) {
                }
            })
    }

    private fun onTeamTaskRetrieved(body: ArrayList<ResponseTeamTask>?) {
        arrayPredecessors.clear()

        arrayPredecessors.add(ItemSpinner(id = 0, name = getString(R.string.none)))

        body?.forEach {
            arrayPredecessors.add(
                ItemSpinner(
                    id = it.id,
                    name = it.name
                )
            )
        }

        adapterPredecessors =
            AdapterCustomSpinner(this, arrayPredecessors, AppConstants.SPINNER_TYPE_TEXT)

        spPredecessors.adapter = adapterPredecessors
        loading.visibility = View.GONE

        if (gotTasks == false) {
            gotTasks = true
            fillEditData(selectedTaskId)
        }
    }

    private fun getTeamProjects() {
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamProjects(
                MyApplication.languageCode,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ArrayList<ResponseTeamProject>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseTeamProject>>,
                    response: Response<ArrayList<ResponseTeamProject>>
                ) {
                    try {
                        onProjectsRetrieved(response.body())

                    } catch (E: Exception) {
                    }
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseTeamProject>>,
                    throwable: Throwable
                ) {
                }
            })
    }

    private fun onProjectsRetrieved(body: ArrayList<ResponseTeamProject>?) {
        arrayProjects.clear()

        arrayProjects.add(ItemSpinner(id = 0, name = getString(R.string.none)))

        body?.forEach {
            arrayProjects.add(
                ItemSpinner(
                    id = it.id,
                    name = it.name
                )
            )
        }

        adapterProjects =
            AdapterCustomSpinner(this, arrayProjects, AppConstants.SPINNER_TYPE_TEXT)

        spProjects.adapter = adapterProjects
        loading.visibility = View.GONE

        if (gotProjects == false) {
            gotProjects = true
            fillEditData(selectedTaskId)
        }
    }

    private fun getTeamMembers(id: Int, setMembers: Boolean = false,taskId:Int) {
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTaskTeamMembers(id)?.enqueue(object : Callback<ArrayList<ResponseUser>> {
                override fun onResponse(
                    call: Call<ArrayList<ResponseUser>>,
                    response: Response<ArrayList<ResponseUser>>
                ) {
                    try {
                        onTeamMembersReceived(response.body()!!, setMembers)
                        if(taskId!=0)
                            getTaskMembers(selectedTaskId)

                    } catch (E: Exception) {
                    }
                    loading.visibility = View.GONE;
                }

                override fun onFailure(
                    call: Call<ArrayList<ResponseUser>>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE;
                    Toast.makeText(this@ActivityTeamTask, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getProjectMembers(id: Int, setMembers: Boolean = false,taskId:Int) {
        waitingProjectMembers=true
        loading.visibility = View.VISIBLE;
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getProjectMembers(id)?.enqueue(object : Callback<ArrayList<TeamStartupMember>> {
                override fun onResponse(
                    call: Call<ArrayList<TeamStartupMember>>,
                    response: Response<ArrayList<TeamStartupMember>>
                ) {
                    waitingProjectMembers=false
                    try {

                        onProjectMemberReceived(response.body()!!, setMembers)
                        if(taskId!=0)
                            getTaskMembers(selectedTaskId)
                    } catch (E: Exception) {
                    }
                    loading.visibility = View.GONE;
                }

                override fun onFailure(
                    call: Call<ArrayList<TeamStartupMember>>,
                    throwable: Throwable
                ) {
                    waitingProjectMembers=false
                    loading.visibility = View.GONE;
                    Toast.makeText(this@ActivityTeamTask, "failed", Toast.LENGTH_LONG).show()
                }
            })
    }



    private fun onProjectMemberReceived( response: ArrayList<TeamStartupMember>, setMembers: Boolean = false) {
        arrayMembers.clear()

        response.forEach {
            arrayMembers.add(
                ItemSpinnerTextImage(
                    id = it.userId,
                    text = it.UserName,
                    image = it.image
                )
            )
        }
        loading.visibility = View.GONE

        if (setMembers) {
            setMembers()
        }

        if (gotMembers == false) {
            gotMembers = true
            fillEditData(selectedTaskId)
        }
    }



    private fun onTeamMembersReceived(response: ArrayList<ResponseUser>,setMembers: Boolean = false) {
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

        if (gotMembers == false) {
            gotMembers = true
            fillEditData(selectedTaskId)
        }
    }

    private fun getTaskMembers(selectedTaskId: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTaskMembersIds(
                selectedTaskId
            )?.enqueue(object : Callback<String> {
                override fun onResponse(
                    call: Call<String>,
                    response: Response<String>
                ) {
                    try {
                        var x = response.body()!!.split(",")
                        x.forEach {
                            arrayMembers.single { it2 ->
                                it2.id == it.toInt()
                            }.isSelected = true
                        }

                        tvAssignees.text =arrayMembers.filter { q -> q.isSelected }.map { it.text }.joinToString()

                        try{adapterMembers.notifyDataSetChanged()}catch (e:java.lang.Exception){}

                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<String>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun getTask(id: Int) {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.getTeamTaskById(
                id,
                MyApplication.selectedStartupTeam.id!!
            )?.enqueue(object : Callback<ResponseTeamTask> {
                override fun onResponse(
                    call: Call<ResponseTeamTask>,
                    response: Response<ResponseTeamTask>
                ) {
                    try {
                        onTaskRetrieved(response.body()!!)
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<ResponseTeamTask>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
    }

    private fun onTaskRetrieved(body: ResponseTeamTask?) {
        if (body != null) {
            task = body

            try {
                etName.setText(task.name)
            } catch (ex: Exception) {
            }

            try {
                etSummary.setText(task.summary)
            } catch (ex: Exception) {
            }

            try {
                var d = AppHelper.dateEvent.parse(task.startDate)
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
                var d = AppHelper.dateEvent.parse(task.endDate)
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
                var d = AppHelper.dateEvent.parse(task.dueDate)
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
                etNote.setText(task.notes)
            } catch (ex: Exception) {
            }

            try {
                tvEstimatedHours.setText(task.estimatedHours.toString())
            } catch (ex: Exception) {
            }

            try {
                selectedProjectId = task.projectId

                if (selectedProjectId != null) {
                    var item = arrayProjects.single {
                        it.id == task.projectId
                    }
                    spProjects.setSelection(arrayProjects.indexOf(item))
                }

            } catch (ex: Exception) {
            }

            try {

                if (task.predecessor != null) {
                    var item = arrayPredecessors.single {
                        it.id == task.predecessor
                    }
                    spPredecessors.setSelection(arrayPredecessors.indexOf(item))
                }
            } catch (ex: Exception) {
            }
            if(task.projectId!=0&&task.projectId!=null){
                getProjectMembers(task.projectId!!,false,selectedTaskId)
            }else
                getTeamMembers(MyApplication.selectedStartupTeam.id!!, false,selectedTaskId)


            try {

                if (task.status != null) {
                    var item = arrayTaskStatuses.single {
                        it.id == task.status
                    }
                    spStatus.setSelection(arrayTaskStatuses.indexOf(item))
                }
            } catch (ex: Exception) {
            }
        }
    }

    private fun saveTask() {
        loading.visibility = View.VISIBLE
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveTask(
                task
            )?.enqueue(object : Callback<Int?> {
                override fun onResponse(
                    call: Call<Int?>,
                    response: Response<Int?>
                ) {
                    try {
                        if (response.code() == 200) {
                            saveTaskMembers(response.body()!!)
                        } else {
                            loading.visibility = View.GONE
                            AppHelper.createDialog(
                                this@ActivityTeamTask,
                                getString(R.string.task_creation_failed)
                            )
                        }

                        loading.visibility = View.GONE
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<Int?>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                    AppHelper.createDialog(
                        this@ActivityTeamTask,
                        getString(R.string.task_creation_failed)
                    )
                }
            })
    }

    private fun saveTaskMembers(selectedTaskId: Int) {
        loading.visibility = View.VISIBLE

        var users = arrayMembers.filter { it.isSelected }.map { it.id }.joinToString(",")
        RetrofitClientAuth.client?.create(RetrofitInterface::class.java)
            ?.saveTaskAssignedUser(
                selectedTaskId,
                users

            )?.enqueue(object : Callback<Void> {
                override fun onResponse(
                    call: Call<Void>,
                    response: Response<Void>
                ) {
                    try {
                        btBack.callOnClick()
                        loading.visibility = View.GONE
                    } catch (e: java.lang.Exception) {
                        loading.visibility = View.GONE
                    }
                }

                override fun onFailure(
                    call: Call<Void>,
                    throwable: Throwable
                ) {
                    loading.visibility = View.GONE
                }
            })
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
           if (selectedProjectId == null) {
                getTeamMembers(MyApplication.selectedStartupTeam.id!!, true,0)
            } else {
                getProjectMembers(selectedProjectId!!, true,0)
            }
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

    override fun onItemClicked(view: View, position: Int) {}

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
}
