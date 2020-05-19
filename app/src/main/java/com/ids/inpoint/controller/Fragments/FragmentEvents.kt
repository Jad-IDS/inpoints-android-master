package com.ids.inpoint.controller.Fragments




import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

import com.ids.inpoint.R
import com.ids.inpoint.controller.Activities.ActivityHome
import com.ids.inpoint.controller.Adapters.AdapterEvents

import com.ids.inpoint.controller.Adapters.AdapterSpinner
import com.ids.inpoint.controller.Adapters.RVOnItemClickListener.RVOnItemClickListener
import com.ids.inpoint.model.*

import com.ids.inpoint.utils.AppConstants
import com.ids.inpoint.utils.AppHelper
import com.ids.inpoint.utils.AppHelper.Companion.share
import kotlinx.android.synthetic.main.fragment_events.*
import kotlinx.android.synthetic.main.fragment_fragment_news_feed.*
import kotlinx.android.synthetic.main.fragment_fragment_profile.*
import kotlinx.android.synthetic.main.fragment_image.*
import kotlinx.android.synthetic.main.fragment_post.*
import kotlinx.android.synthetic.main.toolbar.*


class FragmentEvents : Fragment() ,RVOnItemClickListener {
    lateinit var adapterSelect: AdapterSpinner
    lateinit var adapterSort: AdapterSpinner
    var arraySelect= java.util.ArrayList<ItemSpinner>()
    var arraySort= java.util.ArrayList<ItemSpinner>()

    private var isSortClicked=false
    private var isSelectClicked=false
    private var showMore=false

    lateinit var adapterEvents: AdapterEvents
    var arrayFeeds= java.util.ArrayList<News_feed>()
    var arrayComments= java.util.ArrayList<comments>()
    var arrayComments2= java.util.ArrayList<comments>()
    var arrayComments3= java.util.ArrayList<comments>()

    var arrayResources= java.util.ArrayList<Resources>()

    var newsFeed:News_feed?=null
    var newsFeed2:News_feed?=null
    var newsFeed3:News_feed?=null
    lateinit var imageFilePath: String
    var comment1:comments?=null
    var comment2:comments?=null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment_events, container, false)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){

        setSelectSpinner()
        setSortSpinner()

        linearSelectedSort.setOnClickListener{
            if(!isSelectClicked){
                rvSelect.visibility=View.VISIBLE
                isSelectClicked=true
            }else{
                rvSelect.visibility=View.GONE
                isSelectClicked=false
            }
        }

        linearSortCategory.setOnClickListener{
            if(!isSortClicked){
                rvCategory.visibility=View.VISIBLE
                isSortClicked=true
            }else{
                rvCategory.visibility=View.GONE
                isSortClicked=false
            }
        }


        icSearch.setOnClickListener{
            svSearch.requestFocus()
            var imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(svSearch, InputMethodManager.SHOW_IMPLICIT)

        }
        
       // setDemoData()
       try{ setEvents()}catch (e:Exception){}
        

    }

    private fun setEvents(){
        adapterEvents= AdapterEvents(arrayFeeds,this)
        val glm = GridLayoutManager(activity, 1)
        rvEvents.adapter=adapterEvents
        rvEvents.layoutManager=glm
    }

    private fun setSelectSpinner(){

        arraySelect.add(ItemSpinner(1,"select 1"))
        arraySelect.add(ItemSpinner(2,"select 2"))
        adapterSelect= AdapterSpinner(arraySelect,this,AppConstants.SPINNER_EVENT_SELECT)
        val glm = GridLayoutManager(activity, 1)
        rvSelect.adapter=adapterSelect
        rvSelect.layoutManager=glm
    }


    private fun setSortSpinner(){

        arraySort.add(ItemSpinner(1,"category 1"))
        arraySort.add(ItemSpinner(2,"category 2"))
        arraySort.add(ItemSpinner(3,"category 3"))
        adapterSort= AdapterSpinner(arraySort,this,AppConstants.SPINNER_EVENT_SORT)
        val glm = GridLayoutManager(activity, 1)
        rvCategory.adapter=adapterSort
        rvCategory.layoutManager=glm
    }











    override fun onItemClicked(view: View, position: Int) {
        if(view.id==R.id.IdSpinnerSelect){
            tvSelectedSelect.text = adapterSelect.itemSpinner[position].name
            rvSelect.visibility=View.GONE
            isSelectClicked=false
        }else if(view.id==R.id.IdSpinnerSort){
            tvSelectedSortCategory.text = adapterSort.itemSpinner[position].name
            rvCategory.visibility=View.GONE
            isSortClicked=false
        }
        else if(view.id== R.id.linearDots){
            arrayFeeds[position].settingsViewVisible=!arrayFeeds[position].settingsViewVisible!!
            adapterEvents.notifyDataSetChanged()
        }else if(view.id==R.id.ivImagePost){
            try{(activity!! as ActivityHome).showImage(adapterEvents.newsFeeds[position].imagePostUrl.toString())}catch (e:Exception){ }
        }else if (view.id==R.id.linearLike){
            arrayFeeds[position].isLiked=!arrayFeeds[position].isLiked!!
            adapterEvents.notifyDataSetChanged()
        }else if(view.id==R.id.linearComment){
            arrayFeeds[position].showComments=true
            adapterEvents.notifyDataSetChanged()
        }else if(view.id==R.id.ivSend){
            var etComment: EditText = rvNewsFeed.findViewHolderForAdapterPosition(position)!!.itemView.findViewById(com.ids.inpoint.R.id.etComment) as EditText
            comment1= comments("1",
                "Dany Soleiman",
                "https://www.eharmony.co.uk/dating-advice/wp-content/uploads/2018/06/datingprofile2-900x600.jpg",
                etComment.text.toString(),
                "now",0,false,false, arrayListOf())

            if(position==0){
                arrayComments.add(comment1!!)
            }
            else if(position==1)
            {
                arrayComments2.add(comment1!!)
            }
            else if(position==2){
                arrayComments3.add(comment1!!)
            }


            adapterEvents.notifyDataSetChanged()
            etComment.setText("")
            // Toast.makeText(activity,"aaaaaa",Toast.LENGTH_LONG).show()
        }else if(view.id==R.id.linearShare){
            share(this!!.activity!!,arrayFeeds[position].text_post.toString(),arrayFeeds[position].text_post.toString())
        }else if(view.id==R.id.linearShow){
            arrayFeeds[position].isShowMore=!arrayFeeds[position].isShowMore!!
            adapterEvents.notifyDataSetChanged()
        }
    }
}
