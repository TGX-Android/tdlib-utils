/*
 * This file is a part of tdlib-utils
 * Copyright © Vyacheslav Krylov (slavone@protonmail.ch) 2014-2022
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("ChatPosition")

package me.vkryl.td

import org.drinkless.td.libcore.telegram.TdApi.*

@JvmField val CHAT_LIST_MAIN = ChatListMain()
@JvmField val CHAT_LIST_ARCHIVE = ChatListArchive()

@JvmOverloads fun Chat?.findPosition (chatList: ChatList? = CHAT_LIST_MAIN): ChatPosition? {
  return this?.let {
    val positions = this.positions
    positions.getOrNull(indexOf(positions, chatList))
  }
}

@JvmOverloads fun indexOf (positions: Array<ChatPosition>?, chatList: ChatList? = CHAT_LIST_MAIN, suggestedIndex: Int = -1): Int {
  return positions?.let {
    return if (suggestedIndex in positions.indices && positions[suggestedIndex].list.equalsTo(chatList ?: CHAT_LIST_MAIN)) {
      suggestedIndex
    } else {
      positions.indexOfFirst { position -> position.list.equalsTo(chatList ?: CHAT_LIST_MAIN) }
    }
  } ?: -1
}

fun Chat?.isArchived (): Boolean {
  return this.getOrder(CHAT_LIST_ARCHIVE) != 0L
}
@JvmOverloads fun Chat?.isPinned (chatList: ChatList? = CHAT_LIST_MAIN): Boolean {
  return this.findPosition(chatList)?.isPinned ?: false
}
@JvmOverloads fun Chat?.getOrder (chatList: ChatList? = CHAT_LIST_MAIN): Long {
  return this.findPosition(chatList)?.order ?: 0
}
