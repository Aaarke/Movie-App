package com.example.mvvmbaseproject.ui.base.ui.base

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.ui.base.BaseActivity

abstract class BaseFragment<V : BaseViewModel<*>> : Fragment() {
    private var mViewModel: V? = null
    abstract val viewModel: V

    private lateinit var mRootView:View
    var baseActivity: BaseActivity<*>? = null
        private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mRootView= inflater.inflate(R.layout.base_fragment, container, false)
        return mRootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    interface Callback {
        fun onFragmentAttached()

        fun onFragmentDetached(tag: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            val activity = context as BaseActivity<*>?
            this.baseActivity = activity
            activity!!.onFragmentAttached()
        }
    }

    override fun onDetach() {
        baseActivity = null
        super.onDetach()
    }





}
