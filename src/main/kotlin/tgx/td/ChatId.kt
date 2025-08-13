/*
 * This file is a part of tdlib-utils
 * Copyright © 2014 (tgx-android@pm.me)
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

@file:JvmName("ChatId")

package tgx.td

import org.drinkless.tdlib.TdApi.*

// the last (1 << 31) - 1 identifiers will be used for secret chat dialog identifiers
const val MAX_CHANNEL_ID = 1000000000000L - (1L shl 31)
const val MIN_MONOFORUM_CHANNEL_ID = 1000000000000L + (1L shl 31) + 1
const val MAX_MONOFORUM_CHANNEL_ID = 3000000000000L

const val MAX_USER_ID = (1L shl 40) - 1
const val MAX_GROUP_ID = 999999999999L
const val ZERO_SECRET_CHAT_ID = -2000000000000L
const val ZERO_CHANNEL_ID = -1000000000000L

@ChatType.Constructors
@JvmOverloads
fun getType (chatId: Long, validate: Boolean = true): Int {
  return when {
    chatId in 1..MAX_USER_ID -> {
      ChatTypePrivate.CONSTRUCTOR
    }
    chatId < 0 -> when {
      -MAX_GROUP_ID <= chatId -> {
        ChatTypeBasicGroup.CONSTRUCTOR
      }
      ZERO_CHANNEL_ID - MAX_CHANNEL_ID <= chatId && chatId != ZERO_CHANNEL_ID -> {
        ChatTypeSupergroup.CONSTRUCTOR
      }
      ZERO_SECRET_CHAT_ID + Int.MIN_VALUE <= chatId && chatId != ZERO_SECRET_CHAT_ID -> {
        ChatTypeSecret.CONSTRUCTOR
      }
      ZERO_CHANNEL_ID - MAX_MONOFORUM_CHANNEL_ID <= chatId -> {
        ChatTypeSupergroup.CONSTRUCTOR
      }
      else -> {
        if (validate) {
          error(chatId.toString())
        }
        0
      }
    }
    else -> {
      if (validate) {
        error(chatId.toString())
      }
      0
    }
  }
}

fun toUserId (chatId: Long): Long {
  return if (getType(chatId, false) == ChatTypePrivate.CONSTRUCTOR) chatId else 0
}
fun toSecretChatId (chatId: Long): Int {
  return if (getType(chatId, false) == ChatTypeSecret.CONSTRUCTOR) (chatId - ZERO_SECRET_CHAT_ID).toInt() else 0
}
fun toBasicGroupId (chatId: Long): Long {
  return if (getType(chatId, false) == ChatTypeBasicGroup.CONSTRUCTOR) -chatId else 0
}
fun toSupergroupId (chatId: Long): Long {
  return if (getType(chatId, false) == ChatTypeSupergroup.CONSTRUCTOR) (ZERO_CHANNEL_ID - chatId) else 0
}

fun isBasicGroup (chatId: Long): Boolean = toBasicGroupId(chatId) != 0L
fun isSecret (chatId: Long): Boolean = toSecretChatId(chatId) != 0
fun isPrivate (chatId: Long): Boolean = toUserId(chatId) != 0L
fun isSupergroup (chatId: Long): Boolean = toSupergroupId(chatId) != 0L

fun isMonoforumChat (chatId: Long): Boolean {
  return toSupergroupId(chatId) in MIN_MONOFORUM_CHANNEL_ID .. MAX_MONOFORUM_CHANNEL_ID
}

fun isUserChat (chatId: Long): Boolean {
  return when (getType(chatId, false)) {
    ChatTypePrivate.CONSTRUCTOR, ChatTypeSecret.CONSTRUCTOR -> true
    ChatTypeBasicGroup.CONSTRUCTOR, ChatTypeSupergroup.CONSTRUCTOR -> false
    else -> false
  }
}
fun isMultiChat (chatId: Long): Boolean {
  return when (getType(chatId, false)) {
    ChatTypePrivate.CONSTRUCTOR, ChatTypeSecret.CONSTRUCTOR -> false
    ChatTypeBasicGroup.CONSTRUCTOR, ChatTypeSupergroup.CONSTRUCTOR -> true
    else -> false
  }
}

fun fromUserId (userId: Long): Long = userId
fun fromBasicGroupId (basicGroupId: Long): Long = -basicGroupId
fun fromSupergroupId (supergroupId: Long): Long = ZERO_CHANNEL_ID - supergroupId;
fun fromSecretChatId (secretChatId: Int): Long = ZERO_SECRET_CHAT_ID + secretChatId

fun fromSender (senderId: MessageSender): Long = when (senderId.constructor) {
  MessageSenderUser.CONSTRUCTOR -> fromUserId((senderId as MessageSenderUser).userId)
  MessageSenderChat.CONSTRUCTOR -> (senderId as MessageSenderChat).chatId
  else -> {
    assertMessageSender_439d4c9c()
    throw unsupported(senderId)
  }
}

fun toString (chatId: Long): String {
  return when (getType(chatId, false)) {
    ChatTypePrivate.CONSTRUCTOR -> "user#${toUserId(chatId)}"
    ChatTypeSecret.CONSTRUCTOR -> "secret#${toSecretChatId(chatId)}"
    ChatTypeBasicGroup.CONSTRUCTOR -> "group#${toBasicGroupId(chatId)}"
    ChatTypeSupergroup.CONSTRUCTOR -> "supergroup#${toSupergroupId(chatId)}"
    else -> "chat#${chatId}"
  }
}