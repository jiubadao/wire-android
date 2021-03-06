/**
  * Wire
  * Copyright (C) 2017 Wire Swiss GmbH
  *
  * This program is free software: you can redistribute it and/or modify
  * it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or
  * (at your option) any later version.
  *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  * GNU General Public License for more details.
  *
  * You should have received a copy of the GNU General Public License
  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
  */
package com.waz.zclient.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.waz.model.{UserData, UserId}
import com.waz.service.ZMessaging
import com.waz.threading.Threading
import com.waz.utils.events.Signal
import com.waz.zclient.common.views.ChatheadView
import com.waz.zclient.ui.text.TypefaceTextView
import com.waz.zclient.views.pickuser.{ContactListItemTextView, UserRowView}
import com.waz.zclient.{R, ViewHelper}

class SearchResultUserRowView(val context: Context, val attrs: AttributeSet, val defStyleAttr: Int) extends FrameLayout(context, attrs, defStyleAttr) with UserRowView with ViewHelper {
  def this(context: Context, attrs: AttributeSet) = this(context, attrs, 0)
  def this(context: Context) = this(context, null)

  inflate(R.layout.list_row_pickuser_searchuser, this, addToParent = true)

  private val chathead = findById[ChatheadView](R.id.cv_pickuser__searchuser_chathead)
  private val contactListItemTextView = findById[ContactListItemTextView](R.id.clitv__contactlist__user__text_view)
  private val guestLabel = findById[TypefaceTextView](R.id.guest_indicator)
  private var showContactInfo: Boolean = false
  private val userId = Signal[UserId]()

  private val isGuest = for{
    z <- inject[Signal[ZMessaging]]
    uId <- userId
    isGuest <- z.teams.isGuest(uId)
  } yield isGuest

  var userData = Option.empty[UserData]

  def setUser(userData: UserData): Unit = {
    this.userData = Some(userData)
    userId ! userData.id
    contactListItemTextView.setUser(userData, showContactInfo)
    chathead.setUserId(userData.id)
  }

  def setShowContatctInfo(showContactInfo: Boolean): Unit = {
    this.showContactInfo = showContactInfo
  }

  def getUser = userData.map(_.id)

  def onClicked(): Unit = {
    setSelected(!isSelected)
  }

  override def setSelected(selected: Boolean): Unit = {
    super.setSelected(selected)
    chathead.setSelected(selected)
  }

  def applyDarkTheme(): Unit = {
    contactListItemTextView.applyDarkTheme()
  }

  isGuest.on(Threading.Ui) { guest =>
    guestLabel.setVisibility(if (guest) View.VISIBLE else View.GONE)
  }
}
