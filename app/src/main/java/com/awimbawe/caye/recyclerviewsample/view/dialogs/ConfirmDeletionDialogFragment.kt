package com.awimbawe.caye.recyclerviewsample.view.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import com.awimbawe.caye.recyclerviewsample.R
import android.content.DialogInterface
import android.support.v7.app.AlertDialog
import com.awimbawe.caye.recyclerviewsample.MainActivity
import com.awimbawe.caye.recyclerviewsample.model.entity.Item
import com.awimbawe.caye.recyclerviewsample.view.adapters.ItemAdapter


/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [ConfirmDeletionDialogFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [ConfirmDeletionDialogFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class ConfirmDeletionDialogFragment: DialogFragment() {

    var adapter : ItemAdapter? = null
    var item : Item? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the Builder class for convenient dialog construction
        val builder = AlertDialog.Builder(context as MainActivity)
        builder.setMessage(R.string.dialog_confirm_deletion)
            .setPositiveButton(R.string.delete, DialogInterface.OnClickListener { dialog, id ->
                if(item != null) adapter?.deleteItem(item!!)
            })
            .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener { dialog, id ->
                dismiss()
            })
        // Create the AlertDialog object and return it
        return builder.create()
    }
}
