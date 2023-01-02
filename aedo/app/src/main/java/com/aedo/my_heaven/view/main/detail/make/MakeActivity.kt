package com.aedo.my_heaven.view.main.detail.make

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import com.aedo.my_heaven.R
import com.aedo.my_heaven.api.APIService
import com.aedo.my_heaven.api.ApiUtils
import com.aedo.my_heaven.databinding.ActivityMakeBinding
import com.aedo.my_heaven.model.restapi.base.*
import com.aedo.my_heaven.util.`object`.Constant.ALBUM_REQUEST_CODE
import com.aedo.my_heaven.util.`object`.Constant.ONE_PERMISSION_REQUEST_CODE
import com.aedo.my_heaven.util.base.BaseActivity
import com.aedo.my_heaven.util.base.MyApplication
import com.aedo.my_heaven.util.base.MyApplication.Companion.prefs
import com.aedo.my_heaven.util.dialog.CustomDialogFragment
import com.aedo.my_heaven.util.file.FileUtil
import com.aedo.my_heaven.util.log.LLog
import com.aedo.my_heaven.util.log.LLog.TAG
import com.aedo.my_heaven.view.main.MainActivity
import com.bumptech.glide.Glide
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDate
import java.util.*


class MakeActivity : BaseActivity() {
    private lateinit var mBinding: ActivityMakeBinding
    private lateinit var apiServices: APIService
    private var mViewModel: MakeViewModel? = null
    private val fileUtil = FileUtil()
    private var files4: MutableList<Uri> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_make)
        mBinding.activity = this@MakeActivity
        apiServices = ApiUtils.apiService
        mBinding.viewModel = mViewModel

        val onlyDate: LocalDate = LocalDate.now()
        mBinding.tvMakeData.text = onlyDate.toString()
        mBinding.imgPake.clipToOutline = true

        inStatusBar()
        makeTop()
        setupSpinnerHandler()
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.setIsMainNoticeViewed(false)
    }

    private fun setupSpinnerHandler() {
        mBinding.makeTxSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.spinnerText.text = mBinding.makeTxSpinner.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        mBinding.makeTxSpinnerInfor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mBinding.spinnerInfoTextTt.text = mBinding.makeTxSpinnerInfor.getItemAtPosition(position).toString()
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
    }

    private fun makeTop() {
        val person = resources.getStringArray(R.array.spinner_relationship)
        val place = resources.getStringArray(R.array.spinner_place)

        val perAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, person)
        val placeAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, place)

        mBinding.makeTxSpinner.adapter = perAdapter
        mBinding.makeTxSpinnerInfor.adapter = placeAdapter

    }

    fun onOkClick(v: View) {
        val spinner_text = mBinding.spinnerText.text.toString()
        val make_person = mBinding.makeTxName.text.toString()
        val make_phone = mBinding.makeTxPhone.text.toString()
        val place = mBinding.spinnerInfoTextTt.text.toString()
        val deceased_name = mBinding.makeTxPersonName.text.toString()
        val deceased_age = mBinding.makeTxAge.text.toString()
        val eod_date = mBinding.eodText.text.toString()
        val eod_time = mBinding.eodTextTime.text.toString()
        val coffin_date = mBinding.coffinText.text.toString()
        val coffin_time = mBinding.coffinTextTime.text.toString()
        val dofp_date = mBinding.dofpText.text.toString()
        val dofp_time = mBinding.dofpTextTime.text.toString()
        val buried = mBinding.makeTxPlace.text.toString()
        val word = mBinding.makeTxTex.text.toString()

        when {
            spinner_text.isEmpty() -> {
                mBinding.spinnerText.error = "미입력"
            }
            make_person.isEmpty() -> {
                mBinding.makeTxName.error = "대미입력"
            }
            make_phone.isEmpty() -> {
                mBinding.makeTxPhone.error = "미입력"
            }
            place.isEmpty() -> {
                mBinding.spinnerInfoTextTt.error="미입력"
            }
            deceased_name.isEmpty() -> {
                mBinding.makeTxPersonName.error = "미입력"
            }
            deceased_age.isEmpty() -> {
                mBinding.makeTxAge.error = "미입력"
            }
            eod_date.isEmpty() -> {
                mBinding.eodText.error = "미입력"
            }
            eod_time.isEmpty() -> {
                mBinding.eodTextTime.error = "미입력"
            }
            coffin_date.isEmpty() -> {
                mBinding.coffinText.error = "미입력"
            }
            coffin_time.isEmpty() -> {
                mBinding.coffinTextTime.error = "미입력"
            }
            dofp_date.isEmpty() -> {
                mBinding.dofpText.error = "미입력"
            }
            dofp_time.isEmpty() -> {
                mBinding.dofpTextTime.error = "미입력"
            }
            word.isEmpty() -> {
                mBinding.makeTxTex.error = "미입력"
            }
            else -> {
                val dlg: AlertDialog.Builder = AlertDialog.Builder(
                    this,
                    android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar_MinWidth
                )
                dlg.setTitle("부고 등록") //제목
                dlg.setMessage("부고를 등록 하시겠습니까?")
                dlg.setPositiveButton("확인") { dialog, which ->
                    testAPI()
                    dialog.dismiss()
                }
                dlg.setNegativeButton("취소") { dialog, which ->
                    dialog.dismiss()
                }
                dlg.show()
            }
        }
    }

    private fun testAPI() {
        val img: MutableList<MultipartBody.Part?> =  ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let { Log.i("img", it) }
            img.add(prepareFilePart("img", uri))
        }

        val resident = Resident(mBinding.spinnerText.text.toString(), mBinding.makeTxName.text.toString(),mBinding.makeTxPhone.text.toString())
        val deceased = Deceased(mBinding.makeTxPersonName.text.toString(), mBinding.makeTxAge.text.toString())
        val place = Place( mBinding.spinnerInfoTextTt.text.toString(), mBinding.spinnerInfoTextNumber.text.toString())
        val eod = Eod(mBinding.eodText.text.toString(), mBinding.eodTextTime.text.toString())
        val coffin = Coffin(mBinding.coffinText.text.toString(), mBinding.coffinTextTime.text.toString())
        val dofp = Dofp(mBinding.dofpText.text.toString(), mBinding.dofpTextTime.text.toString())

        val buried = mBinding.makeTxPlace.text.toString()
        val word = mBinding.makeTxTex.text.toString()
        val created =mBinding.tvMakeData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()

        requestHashMap["relation"] = resident.relation!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["residentName"] = resident.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["residentphone"] = resident.phone!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["deceasedName"] = deceased.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["deceasedAge"] = deceased.age!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["place_name"] = place.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["place_number"] = place.number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["eod_date"] = eod.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["eod_time"] = eod.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["coffin_date"] = coffin.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["coffin_time"] = coffin.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["dofp_date"] = dofp.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["dofp_time"] = dofp.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["buried"] = buried.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["word"] = word.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["created"] =created.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.e("만들기 API")
        apiServices.getImgCreate(prefs.myaccesstoken,img,requestHashMap).enqueue(object : Callback<CreateModel> {
            override fun onResponse(call: Call<CreateModel>, response: Response<CreateModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"testAPI  API SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(TAG,"testAPI  API ERROR -> ${response.errorBody()}")
                    testOtherAPI()
                }
            }

            override fun onFailure(call: Call<CreateModel>, t: Throwable) {
                Log.d(TAG,"testAPI Fail -> $t")
                Toast.makeText(this@MakeActivity,"다시 시도해 주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun testOtherAPI() {
        val img: MutableList<MultipartBody.Part?> =  ArrayList()
        for (uri:Uri in files4) {
            uri.path?.let { Log.i("img", it) }
            img.add(prepareFilePart("img", uri))
        }

        val resident = Resident(mBinding.spinnerText.text.toString(), mBinding.makeTxName.text.toString(),mBinding.makeTxPhone.text.toString())
        val deceased = Deceased(mBinding.makeTxPersonName.text.toString(), mBinding.makeTxAge.text.toString())
        val place = Place( mBinding.spinnerInfoTextTt.text.toString(), mBinding.spinnerInfoTextNumber.text.toString())
        val eod = Eod(mBinding.eodText.text.toString(), mBinding.eodTextTime.text.toString())
        val coffin = Coffin(mBinding.coffinText.text.toString(), mBinding.coffinTextTime.text.toString())
        val dofp = Dofp(mBinding.dofpText.text.toString(), mBinding.dofpTextTime.text.toString())

        val buried = mBinding.makeTxPlace.text.toString()
        val word = mBinding.makeTxTex.text.toString()
        val created =mBinding.tvMakeData.text.toString()
        val requestHashMap : HashMap<String, RequestBody> = HashMap()

        requestHashMap["relation"] = resident.relation!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["residentName"] = resident.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["residentphone"] = resident.phone!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["deceasedName"] = deceased.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["deceasedAge"] = deceased.age!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["place_name"] = place.name!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["place_number"] = place.number!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["eod_date"] = eod.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["eod_time"] = eod.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["coffin_date"] = coffin.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["coffin_time"] = coffin.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["dofp_date"] = dofp.date!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["dofp_time"] = dofp.time!!.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        requestHashMap["buried"] = buried.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["word"] = word.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        requestHashMap["created"] =created.toRequestBody("multipart/form-data".toMediaTypeOrNull())

        LLog.e("만들기_두번째 API")
        apiServices.getImgCreate(prefs.newaccesstoken,img,requestHashMap).enqueue(object : Callback<CreateModel> {
            override fun onResponse(call: Call<CreateModel>, response: Response<CreateModel>) {
                val result = response.body()
                if(response.isSuccessful&& result!= null) {
                    Log.d(TAG,"testAPI Second API SUCCESS -> $result")
                    moveList()
                }
                else {
                    Log.d(TAG,"testAPI Second API ERROR -> ${response.errorBody()}")
                    Log.d(TAG,"testAPI Second API ERROR2 -> ${response.message()}")
                    Log.d(TAG,"부고 등록 토큰 -> ${prefs.newaccesstoken}")
                    Log.d(TAG,"부고 등록 이미지 -> $img")
                    Log.d(TAG,"부고 등록 바디 -> $requestHashMap")
                }
            }

            override fun onFailure(call: Call<CreateModel>, t: Throwable) {
                Log.d(TAG,"testAPI Second Fail -> $t")
                Toast.makeText(this@MakeActivity,"다시 시도해 주세요",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun prepareFilePart(partName: String, fileUri: Uri): MultipartBody.Part {
        val file = File(fileUri.path!!)
        Log.i("here is error", file.absolutePath)
        val requestFile: RequestBody = file
            .asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }

    fun onBackClick(v: View){
        val dialog = CustomDialogFragment()
        val btn= arrayOf("계속","나가기") //여기에서 만약에 버튼이 하나라면 btn=arrayOf("취소")만 적으면 된다.
        dialog.arguments= bundleOf(
            "titleContext" to "기록을 중지하고 나갈까요?",
            "bodyContext" to "나가면 기록 중인 내용이 사라져요.",
            "btnData" to btn
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
            override fun onClickNo() {
                //취소버튼을 눌렀을 때 처리할 곳
            }
            override fun onClickYes() {
                moveMain()
            }
        })
        dialog.show(this.supportFragmentManager, "CustomDialog")
    }

    @SuppressLint("SetTextI18n")
    fun onEodDataClick(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.eodText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onEodTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.eodTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    fun onCoffinDataClick(v: View) {
        var dateString = ""
        val cal = Calendar.getInstance() //캘린더 뷰
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.coffinText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onCoffinTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.coffinTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    @SuppressLint("SetTextI18n")
    fun onDofpDataClick(v: View) {
        var dateString = ""

        val cal = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            dateString = "${year}년 ${month+1}월 ${dayOfMonth}일"
            mBinding.dofpText.text = "$dateString "
        }
        DatePickerDialog(this, dateSetListener, cal.get(Calendar.YEAR),cal.get(Calendar.MONTH),cal.get(Calendar.DAY_OF_MONTH)).show()
    }

    fun onDofpTimeClick(v:View) {
        var timeString = ""
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            timeString = "${hour}:${minute}"
            mBinding.dofpTextTime.text = timeString
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    fun onPickClick(v: View) {
        requestPermission()
    }

    private fun requestPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
            -> {
                getAlbum()
            }

            shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
            -> {
                Toast.makeText(this,"사진 접근 권한 동의를 해주세요",Toast.LENGTH_SHORT).show()
            }

            else -> requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),ONE_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            ONE_PERMISSION_REQUEST_CODE -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getAlbum()
            } else {
                Toast.makeText(this, "사진권한 동의를 해주세요.", Toast.LENGTH_SHORT).show()
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun getAlbum() {
        val intent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        intent.type = "image/*"
        startActivityForResult(intent,ALBUM_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( resultCode == Activity.RESULT_OK) {
            if (requestCode == ALBUM_REQUEST_CODE) {
                try {
                    val img = data?.data
                    mBinding.imgPake.setImageURI(img)
                    val imgPath = img.let {
                        fileUtil.getPath(this, it!!)
                    }
                    files4.add(Uri.parse(imgPath))
                    if (imgPath != null) {
                        Log.e("image", imgPath)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onBackPressed() {
        val dialog = CustomDialogFragment()
        val btn= arrayOf("계속","나가기") //여기에서 만약에 버튼이 하나라면 btn=arrayOf("취소")만 적으면 된다.
        dialog.arguments= bundleOf(
            "titleContext" to "기록을 중지하고 나갈까요?",
            "bodyContext" to "나가면 기록 중인 내용이 사라져요.",
            "btnData" to btn
        )
        dialog.setButtonClickListener(object: CustomDialogFragment.OnButtonClickListener {
            override fun onClickNo() {
                //취소버튼을 눌렀을 때 처리할 곳
            }
            override fun onClickYes() {
                moveMain()
            }
        })
        dialog.show(this.supportFragmentManager, "CustomDialog")
    }
}