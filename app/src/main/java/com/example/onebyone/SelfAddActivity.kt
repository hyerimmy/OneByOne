package com.example.onebyone

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.onebyone.listener.DialogListener
import com.example.onebyone.listener.ItemClickListener
import kotlinx.android.synthetic.main.activity_camera_add.iv_next
import kotlinx.android.synthetic.main.activity_camera_add.recycler

class SelfAddActivity : AppCompatActivity() {
    private val mUpdateRequestCode = 200
    private val mAddRequestCode = 300
    private val items by lazy {
        intent?.getParcelableArrayListExtra<AddItem>("items")
    }

    private var mAdapter: AddRecyclerAdapter? = null
    private val mListener = object: DialogListener{
        override fun onAdd(item: AddItem) {
            mAdapter?.addItem(item)
        }

        override fun onDelete(position: Int) {
            mAdapter?.deleteItem(position)
        }

        override fun onUpdate(position: Int, item: AddItem) {
            mAdapter?.updateItem(item, position)
        }
    }

    private val ivAddItem by lazy {
        findViewById<ImageView>(R.id.iv_add_item)
    }

    private val ivBack by lazy {
        findViewById<ImageView>(R.id.iv_back)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_add)

        mAdapter = AddRecyclerAdapter(items ?: arrayListOf(), true)
        mAdapter!!.setListener(object : ItemClickListener {
            override fun onClick(dataList: List<AddItem>, position: Int) {
                ReviseFragment.getInstance().run {
                    arguments = Bundle().apply {
                        putParcelable("item", dataList[position])
                        putInt("position", position)
                    }
                    mDialogListener = mListener
                    show(supportFragmentManager, "")
                }
            }
        })

        with(recycler) {
            adapter = mAdapter
            setHasFixedSize(true)
        }

        iv_next.setOnClickListener {
            Intent(this, SelfAddActivity2::class.java).apply {
                putParcelableArrayListExtra("items", mAdapter!!.getList())
                startActivity(this)
            }
        }

        ivAddItem.setOnClickListener {
            AddFragment.getInstance().run{
                mDialogListener = mListener
                show(supportFragmentManager, "")
            }
        }

        ivBack.setOnClickListener {
            finish()
        }
    }
}