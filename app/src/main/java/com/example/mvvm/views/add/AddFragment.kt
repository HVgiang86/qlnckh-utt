package com.example.mvvm.views.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm.R
import com.example.mvvm.base.BGType
import com.example.mvvm.base.BaseFragment
import com.example.mvvm.databinding.FragmentAddBinding
import com.example.mvvm.domain.AppState
import com.example.mvvm.domain.Document
import com.example.mvvm.domain.Project
import com.example.mvvm.domain.ProjectState
import com.example.mvvm.domain.Researcher
import com.example.mvvm.domain.ResearcherReport
import com.example.mvvm.utils.ext.genId
import com.example.mvvm.utils.ext.gone
import com.example.mvvm.utils.ext.visible
import com.example.mvvm.utils.formatDateToDDMMYYYY
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.nareshchocha.filepickerlibrary.models.DocumentFilePickerConfig
import com.nareshchocha.filepickerlibrary.ui.FilePicker
import com.nareshchocha.filepickerlibrary.utilities.appConst.Const
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import java.io.File
import java.util.Date

@AndroidEntryPoint
class AddFragment : BaseFragment<FragmentAddBinding, AddViewModel>() {
    override val viewModel: AddViewModel by viewModels()

    private val files = mutableMapOf<String, String>()

    private val documentAdapter by lazy {
        ItemAdapter(onClickAdd = {
            pickFile()
        }, onClickRemove = { item ->
            when (item) {
                Item.AddItem -> TODO()
                is Item.DocumentItem -> viewModel.removeDocument(item.document)
                is Item.ResearcherItem -> TODO()
                is Item.SuperVisorItem -> TODO()
                is Item.TitleItem -> TODO()
            }
        })
    }

    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val uri = it.data?.data!!
            val filePath = it.data?.getStringExtra(Const.BundleExtras.FILE_PATH)

            if (filePath == null) {
                uploadFileSelected("document_file", uri)
            } else {
                val file = File(filePath)
                val fileName = file.name
                Timber.d("fileName $fileName")
                uploadFileSelected(fileName, file.toUri())
            }
            Timber.d("uri $uri")
        }
    }

    override fun inflateViewBinding(inflater: LayoutInflater): FragmentAddBinding {
        return FragmentAddBinding.inflate(inflater)
    }

    override fun initialize() {
        setUp()
    }

    private fun pickFile() {
        val pickerConfig = DocumentFilePickerConfig(
            popUpIcon = R.drawable.ic_attach_file, // DrawableRes Id
            popUpText = "File Media",
            allowMultiple = false, // set Multiple pick file
            maxFiles = 0, // max files working only in android latest version
            mMimeTypes = listOf("*/*"), // added Multiple MimeTypes
            askPermissionTitle = null, // set Permission ask Title
            askPermissionMessage = null, // set Permission ask Message
            settingPermissionTitle = null, // set Permission setting Title
            settingPermissionMessage = null, // set Permission setting Messag
        )

        val intent = FilePicker.Builder(requireContext()).setPopUpConfig().addPickDocumentFile(pickerConfig).build()

        launcher.launch(intent)
    }

    private fun addDocument() {
        files.forEach { (t, u) ->
            viewModel.addDocument(Document(t, u, Date().toGMTString()))
        }
    }

    private fun uploadFileSelected(fileName: String, uri: Uri? = null) {
        // Handle the selected images here
        showLoading()

        if (uri == null) return

        try {
            Firebase.storage.maxChunkUploadRetry = 10000
            Firebase.storage.maxUploadRetryTimeMillis = 10000
            Firebase.storage.maxDownloadRetryTimeMillis = 10000

            val storage = Firebase.storage
            val storageRef = storage.reference

            // Create a child reference
            // imagesRef now points to "images"
            val docsRef: StorageReference = storageRef.child("docs")

            val spaceRef = docsRef.child(uri.lastPathSegment ?: "image${System.currentTimeMillis()}")

            val uploadTask = spaceRef.putFile(uri)
            uploadTask.addOnFailureListener {
                it.printStackTrace()
                Timber.d("Upload fail")
                showLoading()
            }

            uploadTask.addOnSuccessListener {
                spaceRef.downloadUrl.addOnSuccessListener {
                    Timber.d("File URL: $it")
                    files[fileName] = it.toString()
                    documentAdapter.setItems(listOf(Item.DocumentItem(Document(fileName, it.toString(), Date().toGMTString()))), fileName)
                    showLoading()
                }.addOnFailureListener {
                    it.printStackTrace()
                    showLoading()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setUp() {
        val type = arguments?.getInt(KEY_TYPE, 0)
        val projectId: Long? = arguments?.getLong(KEY_PROJECT_ID, 0L)
        setupUI(type ?: 0)

        setupObserver()

        viewBinding.btnSave.setOnClickListener {
            when (type) {
                TYPE_ADD_PROJECT -> {
                    onClickAddProject()
                }

                TYPE_ADD_REPORT -> {
                    if (projectId != null && projectId != 0L) {
                        onClickAddReport(projectId)
                    }
                }
            }
        }

        viewBinding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        requirePermission()
    }

    private fun setupObserver() {
        viewModel.documents.observe(this) {
            documentAdapter.setItems(
                it.map { document ->
                    Item.DocumentItem(document)
                },
                "Documents",
            )
        }
    }

    private fun onClickAddReport(projectId: Long) {
        addDocument()

        val title = viewBinding.edtTitle.text.toString()
        val content = viewBinding.edtContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            showMessage("Invalid input!", BGType.BG_TYPE_ERROR)
            return
        }

        val report = ResearcherReport(
            title,
            Date(),
            content = content,
            file = viewModel.documents.value,
            supervisorComments = emptyList(),
            reporter = null,
        )
        viewModel.addReport(report, projectId)
        showMessage("Add report successfully!", BGType.BG_TYPE_SUCCESS)
        requireActivity().onBackPressed()
    }

    private fun onClickAddProject() {
        addDocument()

        val title = viewBinding.edtTitle.text.toString()
        val content = viewBinding.edtContent.text.toString()
        if (title.isEmpty() || content.isEmpty()) {
            showMessage("Invalid input!", BGType.BG_TYPE_ERROR)
            return
        }

        var documents = ArrayList<Document>()
        if (viewModel.documents.value != null) {
            documents = viewModel.documents.value as ArrayList<Document>
        }

        val project = Project(
            genId(),
            title,
            content,
            ProjectState.PROPOSED,
            researcher = emptyList(),
            supervisor = null,
            documents = documents,
            reports = emptyList(),
            score = null,
        )
        viewModel.addProject(project)
        showMessage("Add project successfully!", BGType.BG_TYPE_SUCCESS)
        requireActivity().onBackPressed()
    }

    @SuppressLint("SetTextI18n")
    private fun setupUI(type: Int) {
        viewBinding.rcvDocument.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(requireContext())
            documentAdapter.setItems(listOf(), "Documents")
        }

        viewBinding.tvDate.text = formatDateToDDMMYYYY(Date())

        when (type) {
            TYPE_ADD_PROJECT -> {
                viewBinding.apply {
                    tvToolbar.text = "Propose New Project"
                    edtTitle.hint = "Tên Project"
                    edtContent.hint = "Description"
                    layoutDate.gone()
                    rcvSupervisor.visible()
                    rcvResearcher.visible()
                    rcvDocument.visible()
                }
            }

            TYPE_ADD_REPORT -> {
                viewBinding.apply {
                    tvToolbar.text = "New Report"
                    edtTitle.hint = "Title"
                    edtContent.hint = "Content"
                    layoutDate.visible()
                    rcvSupervisor.gone()
                    rcvResearcher.gone()
                    rcvDocument.visible()
                }
            }
        }
    }

    private fun requirePermission() {
        Dexter.withActivity(requireActivity()).withPermissions(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                // check if all permissions are granted
                if (report.areAllPermissionsGranted()) {
                    // do you work now
                }

                // check for permanent denial of any permission
                if (report.isAnyPermissionPermanentlyDenied) {
                    // permission is denied permenantly, navigate user to app settings
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken,
            ) {
                token.continuePermissionRequest()
            }
        }).onSameThread().check()
    }

    companion object {
        const val KEY_TYPE = "type"
        const val KEY_PROJECT_ID = "project_id"
        const val TYPE_ADD_PROJECT = 1
        const val TYPE_ADD_REPORT = 2
        fun newInstance(type: Int, projectId: Long?) = AddFragment().apply {
            arguments = Bundle().apply {
                putInt(KEY_TYPE, type)
                projectId?.let { putLong(KEY_PROJECT_ID, it) }
            }
        }
    }
}
