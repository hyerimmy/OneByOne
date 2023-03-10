package com.example.onebyone.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.onebyone.R
import com.example.onebyone.ConsumeTypeAdapter
import com.example.onebyone.listener.ConsumeTypeClickListener
import com.example.onebyone.listener.DialogListener
import com.example.onebyone.AddItem
import com.example.onebyone.ConsumeItem
import com.example.onebyone.util.toDigit
import com.google.android.flexbox.*

class ReviseFragment : DialogFragment() {
    enum class TYPE_REVISE {
        ADD, UPDATE, DELETE
    }

    private var mType = TYPE_REVISE.ADD.name

    private var mItem: AddItem? = null
    private var mPosition = -1

    var mDialogListener: DialogListener? = null
    private var rcvConsumeType: RecyclerView? = null
    private var mResId = -1

    companion object {
        @JvmStatic
        fun getInstance() = ReviseFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_revise, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            mType = it.getString("type")!!
            mItem = it.getParcelable("item")
            mPosition = it.getInt("position", -1)
        }

        with(view) {
            val etName = findViewById<EditText>(R.id.et_name)
            val etPrice = findViewById<EditText>(R.id.et_price)
            val ivRevise = findViewById<ImageView>(R.id.iv_revise)
            val ivDelete = findViewById<ImageView>(R.id.iv_delete)

            mItem?.let {
                etName.setText(it.title)
                etPrice.setText(it.price.toString())
            }

            ivRevise.setOnClickListener {
                if (mResId < 0) {
                    Toast.makeText(requireContext(), "?????? ????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etName.text.toString().isNullOrBlank()) {
                    Toast.makeText(requireContext(), "???????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                if (etPrice.text.toString().isNullOrBlank()) {
                    Toast.makeText(requireContext(), "????????? ??????????????????.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val mPrice = try {
                    etPrice.text.toString().replace(",", "").toInt()
                } catch (e: Exception) {
                    -1
                }

                when(mType){
                    TYPE_REVISE.ADD.name->{
                        mDialogListener!!.onAdd(AddItem(
                            resourceLabelId = mResId,
                            title = etName.text.toString(),
                            price = etPrice.text.toString().toDigit(),
                            category = toCategoryText(mResId).toString()
                        ))

                        Log.d("yeonji mResId",mResId.toString())
                        Log.d("yeonji toCategoryText(mResId).toString()",toCategoryText(mResId).toString())
                    }
                    TYPE_REVISE.UPDATE.name->{
                        mDialogListener!!.onUpdate(mPosition, AddItem(
                            resourceLabelId = mResId,
                            title = etName.text.toString(),
                            price = mPrice,
                            category = toCategoryText(mResId).toString()
                        ))
                        Log.d("yeonji mResId",mResId.toString())
                        Log.d("yeonji toCategoryText(mResId).toString()",toCategoryText(mResId).toString())
                    }
                }
                dismiss()
            }

            ivDelete.setOnClickListener {
                mDialogListener!!.onDelete(mPosition)
                dismiss()
            }

            rcvConsumeType = findViewById(R.id.rcv_consume_type)

            rcvConsumeType?.apply {
                layoutManager = FlexboxLayoutManager(context).apply {
                    justifyContent = JustifyContent.CENTER
                    alignItems = AlignItems.CENTER
                    flexDirection = FlexDirection.ROW
                    flexWrap = FlexWrap.WRAP
                }

                adapter = ConsumeTypeAdapter(
                    listOf(
                        ConsumeItem(R.drawable.cb_revise_food, "??????"),
                        ConsumeItem(R.drawable.cb_revise_clothing, "??????/??????"),
                        ConsumeItem(R.drawable.cb_revise_necessity, "?????????"),
                        ConsumeItem(R.drawable.cb_revise_cosmetics, "?????????"),
                        ConsumeItem(R.drawable.cb_revise_appliances, "??????"),
                        ConsumeItem(R.drawable.cb_revise_health, "??????/??????"),
                        ConsumeItem(R.drawable.cb_revise_homedeco, "?????????"),
                        ConsumeItem(R.drawable.cb_revise_etc, "??????")
                    )
                ).apply {
                    setListener(object : ConsumeTypeClickListener {
                        override fun onSelect(data: List<ConsumeItem>, position: Int) {
                            when (data[position].resId) {
                                R.drawable.cb_revise_food -> mResId = R.drawable.cb_add_food
                                R.drawable.cb_revise_clothing -> mResId = R.drawable.cb_add_clothing
                                R.drawable.cb_revise_necessity -> mResId = R.drawable.cb_add_necessity
                                R.drawable.cb_revise_cosmetics -> mResId = R.drawable.cb_add_cosmetics
                                R.drawable.cb_revise_appliances -> mResId = R.drawable.cb_add_appliances
                                R.drawable.cb_revise_health -> mResId = R.drawable.cb_add_health
                                R.drawable.cb_revise_homedeco -> mResId = R.drawable.cb_add_homedeco
                                R.drawable.cb_revise_etc -> mResId = R.drawable.cb_add_etc
                            }
                        }
                    })
                }
            }
        }
    }

    fun toCategoryText(resourceLabelId: Int) : String{
        var result : String = ""
        when (resourceLabelId) {
            R.drawable.cb_add_food -> result = "??????"
            R.drawable.cb_add_clothing -> result = "??????/??????"
            R.drawable.cb_add_necessity -> result = "?????????"
            R.drawable.cb_add_cosmetics -> result = "?????????"
            R.drawable.cb_add_appliances -> result =  "??????"
            R.drawable.cb_add_health -> result =  "??????/??????"
            R.drawable.cb_add_homedeco -> result = "?????????"
            R.drawable.cb_add_etc -> result = "??????"

            R.drawable.cb_revise_food -> result =  "??????"
            R.drawable.cb_revise_clothing -> result =  "??????/??????"
            R.drawable.cb_revise_necessity -> result =  "?????????"
            R.drawable.cb_revise_cosmetics -> result =  "?????????"
            R.drawable.cb_revise_appliances -> result =  "??????"
            R.drawable.cb_revise_health -> result =  "??????/??????"
            R.drawable.cb_revise_homedeco -> result =  "?????????"
            R.drawable.cb_revise_etc -> result =  "??????"
        }
        return result
    }
}