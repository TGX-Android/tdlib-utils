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

@file:JvmName("Td")
@file:JvmMultifileClass

package me.vkryl.td

import org.drinkless.td.libcore.telegram.TdApi.*

private fun ChatList.toSortKey (): Int {
  return when (this.constructor) {
    ChatListMain.CONSTRUCTOR -> 0
    ChatListArchive.CONSTRUCTOR -> 1
    ChatListFilter.CONSTRUCTOR -> 2 + (this as ChatListFilter).chatFilterId
    else -> error(this.toString())
  }
}

fun Array<Chat>.sort (chatList: ChatList? = CHAT_LIST_MAIN) {
  this.sortWith(compareByDescending<Chat> { it.getOrder(chatList) }.thenByDescending { it.id })
}
fun MutableList<Chat>.sort (chatList: ChatList? = CHAT_LIST_MAIN) {
  this.sortWith(compareByDescending<Chat> { it.getOrder(chatList) }.thenByDescending { it.id })
}

fun Array<TextEntity>.sort () {
  this.sortWith(compareBy<TextEntity> {it.offset}.thenByDescending {it.length})
}
fun MutableList<TextEntity>.sort () {
  this.sortWith(compareBy<TextEntity> {it.offset}.thenByDescending {it.length})
}

fun Array<Message>.sort () = this.sortWith(compareBy {it.id})
fun Array<ChatPosition>.sort () = this.sortWith(compareBy {it.list.toSortKey()})

fun Array<Session>.sort () {
  this.sortWith(compareByDescending<Session> { it.isCurrent }.thenByDescending { it.isPasswordPending }.thenByDescending { it.lastActiveDate })
}
fun Array<LanguagePackInfo>.sort (activeLanguagePackId: String) {
  this.sortWith(compareByDescending<LanguagePackInfo> { it.isInstalled() }.thenBy { it.isBeta }.thenByDescending { it.isOfficial }.thenByDescending { !it.isInstalled() && activeLanguagePackId == it.id })
}

fun MutableList<Message>?.sortByViewCount () = this?.filter { it.interactionInfo != null }?.sortedWith(compareByDescending<Message> { it.interactionInfo!!.viewCount }.thenByDescending { it.interactionInfo!!.forwardCount })