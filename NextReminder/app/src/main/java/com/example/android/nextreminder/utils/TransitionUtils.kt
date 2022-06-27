package com.example.android.nextreminder.utils

import android.app.Activity
import android.app.ActivityOptions
import android.util.Pair
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.android.nextreminder.R
import com.example.android.nextreminder.data.SimilarDTO
import com.example.android.nextreminder.ui.detail.DetailActivity

/**
 * Add a screen transition animation with shared elements (title, type icon, description)
 *
 * https://developer.android.com/training/transitions/start-activity#start-with-element
 */
fun openDetailWithSharedElementTransition(item: SimilarDTO, itemRoot: ConstraintLayout, activity: Activity) {
    // Set the destination screen
    val intent = DetailActivity.newIntent(activity, item)

    val titleTag = activity.getString(R.string.transition_item_title)
    val typeTag = activity.getString(R.string.transition_item_type)
    val descriptionTag = activity.getString(R.string.transition_item_description)

    // Retrieve each views using a tag
    // For simplicity, we use the same string for the tag as the transition name
    // (android:tag and android:transitionName, set on each view in XML)
    // The same string is also used in the transition destination screen XML
    val titleView = itemRoot.findViewWithTag<View>(titleTag)
    val typeView = itemRoot.findViewWithTag<View>(typeTag)
    val descriptionView = itemRoot.findViewWithTag<View>(descriptionTag)

    // Add multiple shared elements
    // https://developer.android.com/training/transitions/start-activity#start-with-multiple
    val option = ActivityOptions.makeSceneTransitionAnimation(
        activity,
        Pair.create(titleView, titleTag),
        Pair.create(typeView, typeTag),
        Pair.create(descriptionView, descriptionTag)
    )
    activity.startActivity(intent, option.toBundle())
}