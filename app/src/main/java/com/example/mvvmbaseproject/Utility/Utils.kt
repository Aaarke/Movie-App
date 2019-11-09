package com.example.mvvmbaseproject.Utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Build
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mvvmbaseproject.R
import com.example.mvvmbaseproject.Utility.Codes.SnackBarType.Companion.SUCCESS
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson

class Utils {

    /**
     * Method to init toast to the User
     */
    @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    companion object {
        private const val EMPTY_STRING = ""
        const val REQUEST_BODY = "Request Body :: "
        private val TAG = "Utils"
        fun snackBar(context: Context, message: String, view: View) {
            val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            val tv: TextView = snackbar.view.findViewById(com.google.android.material.R.id.snackbar_text)
            tv.gravity = Gravity.CENTER_HORIZONTAL
            tv.setTextColor(ContextCompat.getColor(context, R.color.white))
            tv.maxLines = 5
            snackbar.view.setBackgroundColor(ContextCompat.getColor(context, R.color.snackbar_bg_color_failure))
            snackbar.show()
        }


        fun snackBar(activity: AppCompatActivity, message: String, snackBarType: Int) {
            snackBar(activity, message, activity.window.decorView, snackBarType)
        }

        fun snackBar(activity: AppCompatActivity, message: String) {
            try {
                snackBar(activity, message, activity.window.decorView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        fun snackBar(activity: AppCompatActivity?, message: String, view: View, snackBarType: Int) {
            try {
                if (activity == null) {
                    return
                }

                activity.runOnUiThread {
                    val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    val view = snackbar.view
                    val tv = view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                    tv.gravity = Gravity.CENTER_HORIZONTAL
                    tv.setTextColor(ContextCompat.getColor(activity, R.color.white))
                    view.setBackgroundColor(
                        ContextCompat.getColor(
                            activity,
                            if (snackBarType == SUCCESS) R.color.snackbar_bg_color_success else R.color.snackbar_bg_color
                        )
                    )

                    snackbar.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }


        /**
         * Method used to hide keyboard if outside touched.
         *
         * @param activity
         */

        fun hideSoftKeyboard(activity: AppCompatActivity) {

            val focusedView = activity.currentFocus ?: return

            val windowToken = focusedView.windowToken ?: return

            val inputMethodManager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(windowToken, 0)


        }


        fun hideSoftKeyboard(activity: AppCompatActivity, view: View) {
            try {

                val inputMethodManager = activity.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)

            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        fun hideKeyboard(context: Context, editText: EditText?) {

            if (editText != null) {
                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(editText.windowToken, 0)
            } else {
                (context as AppCompatActivity).window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
            }
        }



        /**
         * Method to set same OnClickListener on multiple views
         *
         * @param listener
         * @param views
         */
        fun setOnClickListener(listener: View.OnClickListener, vararg views: View) {

            for (view in views) {
                view.setOnClickListener(listener)
            }
        }


        /**
         * Method to set same Visibility for Multiple Views
         *
         * @param visibility
         * @param views
         */
        fun setVisibility(visibility: Int, vararg views: View) {
            for (view in views) {
                view.visibility = visibility
            }
        }



        fun logRequestBody(nay: Any) {
            try {
                Log.i(TAG, REQUEST_BODY + Gson().toJson(nay))
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }


        /**
         * Method to check for empty [EditText]s
         *
         * @param editText
         * @return
         */
        fun isEmpty(editText: EditText): Boolean {

            return isEmpty(editText, EMPTY_STRING)
        }

        fun isEmpty(list: List<*>?): Boolean {
            return list == null || list.isEmpty()
        }


        fun isEmpty(mString: String?): Boolean {
            return mString == null || mString.isEmpty()
        }

        private fun isEmpty(editText: EditText, message: String): Boolean {

            if (get(editText).isEmpty()) {
                setErrorOn(editText, message)
                return true
            }

            return false
        }

        /**
         * Method to extract the Text from TextView
         *
         * @param textView
         * @return
         */
        fun get(textView: TextView): String {
            return textView.text.toString()
        }

        /**
         * Method to extract the Text from TextView
         *
         * @param editText
         * @return
         */
        fun get(editText: EditText?): String {
            return editText?.text?.toString()?.trim { it <= ' ' } ?: EMPTY_STRING
        }

        /**
         * Method to extract the Text from TextView
         *
         * @param editTexts
         * @return
         */
        fun get(vararg editTexts: EditText): String {
            return get(null, *editTexts)
        }

        /**
         * Method to extract the Text from TextView
         *
         * @param editTexts
         * @return
         */
        private fun get(join: String?, vararg editTexts: EditText): String {
            var join = join

            val text = StringBuilder(EMPTY_STRING)

            if (join == null) {
                join = EMPTY_STRING
            }

            for (editText in editTexts) {
                text.append(join).append(get(editText))
            }

            return text.toString().replaceFirst(join.toRegex(), EMPTY_STRING)
        }

        fun setErrorOn(editText: EditText, message: String) {
            editText.requestFocus()
        }


        /**
         * Method to set errors on the fields
         *
         * @param editText
         */
        private fun setErrorOn(editText: EditText) {

            setErrorOn(editText, EMPTY_STRING)
        }





        fun setStatusBarColor(activity: AppCompatActivity, color: Int) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val window = activity.window
                window.statusBarColor = color
            }
        }

        @Suppress("DEPRECATION")
        fun internetCheck(context: Context): Boolean {
            val net: Boolean

            try {
                val ConMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                net =
                    ConMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).state == NetworkInfo.State.CONNECTED || ConMgr.getNetworkInfo(
                        ConnectivityManager.TYPE_MOBILE
                    ).state == NetworkInfo.State.CONNECTED
            } catch (e: Exception) {
                e.printStackTrace()
                return false
            }

            return net

        }


        fun toFirstLetterUppercase(string: String?): String {
            return string!!.toUpperCase()[0] + string.substring(1, string.length)
        }

        /**
         * Method to convert the value from
         * density pixels to pixels
         *
         * @param dpValue
         * @return
         */
        fun dpToPx(context: Context, dpValue: Float): Int {

            return TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValue,
                context.resources.displayMetrics
            ).toInt()
        }


    }
}