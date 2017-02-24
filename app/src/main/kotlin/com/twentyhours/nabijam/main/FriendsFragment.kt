package com.twentyhours.nabijam.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.twentyhours.nabijam.R

/**
 * Created by soonhyung on 2/5/17.
 */
class FriendsFragment : Fragment() {
  fun newInstance(): FriendsFragment {
    return FriendsFragment()
  }

  override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    return inflater?.inflate(R.layout.content_main, container, false)
  }

  override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val textView = view?.findViewById(R.id.title) as TextView
    textView.text = "Friends"
  }
}