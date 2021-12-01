package com.satellite.messenger.ui.profile

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.satellite.messenger.database.Repository
import com.satellite.messenger.database.entity.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import javax.inject.Inject


class ProfileViewModel @Inject constructor(val repository: Repository): ViewModel() {

    // e-mail закрытый для скачивания картинки
    // байты картинки

    private var _bmp: MutableLiveData<Bitmap?>? = MutableLiveData<Bitmap?>()
    val bmp: LiveData<Bitmap?>? get() = _bmp!!

    private var _progress: MutableLiveData<Int>? = MutableLiveData<Int>()
    val progress: LiveData<Int>? get() = _progress

    // место хранения картинки
    // нужно ли подгружать картинку с сервера
    private val ONE_MEGABYTE: Long = 1024 * 1024
    var profile: Profile = Profile(Id=1)

    var email:String = ""

    var sendToInterface = false
    private var tempAbs = ""

    fun getProfile() {

        GlobalScope.launch(Dispatchers.Main) {
            profile = repository.getRoomDatabase().getProfile()

            if (profile.iconUrl.isNotEmpty()) {
                // подкачать с хранилища
                getInternalStorage(profile.iconUrl)
            } else if (profile.state) {
                // подкачать с облака
                getIconInternet(email)
            }
        }

    }

    private fun getInternalStorage(str: String) {

        val imgFile = File(str)
        if (imgFile.exists()) {
            _bmp?.value = BitmapFactory.decodeFile(str)
            sendToInterface = false
        }

    }

    private fun getIconInternet(str: String) {

        val ref: StorageReference = FirebaseStorage.getInstance().reference.child("images").child(str)

        ref.getBytes(ONE_MEGABYTE).addOnSuccessListener {

            _bmp?.value = BitmapFactory.decodeByteArray(it, 0, it.size)

            try {
                val fileCash = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), email)
                val outCash = FileOutputStream(fileCash)
                _bmp!!.value?.compress(Bitmap.CompressFormat.PNG, 100, outCash)
                outCash.flush()
                outCash.close()

                profile.iconUrl = fileCash.toString()
                sendToInterface = true
                updateStorageProfile()
            }
            catch (e: Exception){
                print(e.message)
            }
        }

    }

    fun push(uri: Uri) {

        val ref: StorageReference = FirebaseStorage.getInstance().reference.child("images").child("$email")
        ref.putFile(uri)
            .addOnProgressListener { taskSnapshot ->
                _progress?.value = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                    .totalByteCount).toInt()

                profile.state = true
                updateStateProfile()
            }

    }

    private fun updateStateProfile() {
        GlobalScope.launch(Dispatchers.Main) {
            repository.getRoomDatabase().updateStateProfile(profile.state)
        }
    }

    private fun updateStorageProfile() {
        GlobalScope.launch(Dispatchers.Main) {
            repository.getRoomDatabase().updatePlaceStorageProfile(profile.iconUrl)
        }
    }

    fun getPath(uri: Uri, context: Context) {
        val cursor: Cursor = uri.let { it1 -> context.contentResolver.query(it1, arrayOf(MediaStore.Images.Media.DATA), null, null, null) }!!
        cursor.moveToFirst()
        val columnIndex: Int = cursor.getColumnIndex(arrayOf(MediaStore.Images.Media.DATA)[0])
        tempAbs = cursor.getString(columnIndex)
        profile.iconUrl = tempAbs
        updateStorageProfile()
    }

}