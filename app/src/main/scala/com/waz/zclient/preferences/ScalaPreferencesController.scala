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
package com.waz.zclient.preferences

import com.waz.content.UserPreferences
import com.waz.service.ZMessaging
import com.waz.utils.events.{EventContext, Signal}
import com.waz.zclient.{Injectable, Injector}

//So that java can access the new preferences from the SE
class ScalaPreferencesController(implicit inj: Injector, ec: EventContext) extends Injectable {

  private val zms = inject[Signal[ZMessaging]]
  private val userPrefs = zms.map(_.userPrefs)

  private var _sendButtonEnabled = false
  userPrefs.flatMap(_.preference(UserPreferences.SendButtonEnabled).signal){ _sendButtonEnabled = _ }

  private var _analyticsEnabled = false
  userPrefs.flatMap(_.preference(UserPreferences.AnalyticsEnabled).signal){ _analyticsEnabled = _ }

  def isSendButtonEnabled: Boolean = _sendButtonEnabled

  def isAnalyticsEnabled: Boolean = _analyticsEnabled
}
