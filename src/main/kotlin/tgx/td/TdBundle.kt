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

@file:JvmName("Td")
@file:JvmMultifileClass

package tgx.td

import android.os.Bundle
import org.drinkless.tdlib.TdApi.*
import kotlin.contracts.ExperimentalContracts

fun Bundle.put (prefix: String, what: SearchMessagesFilter?) {
  if (what != null) {
    putInt(prefix + "_constructor", what.constructor)
  }
}

fun Bundle.restoreSearchMessagesFilter (prefix: String): SearchMessagesFilter? {
  val constructor = getInt(prefix + "_constructor")
  if (constructor != 0) {
    try {
      return constructSearchMessagesFilter(constructor)
    } catch (_: Exception) {  }
  }
  return null
}

fun Bundle.put (prefix: String, what: MessageSender?) {
  if (what != null) {
    when (what.constructor) {
      MessageSenderUser.CONSTRUCTOR -> {
        require(what is MessageSenderUser)
        putLong(prefix + "_user_id", what.userId)
      }
      MessageSenderChat.CONSTRUCTOR -> {
        require(what is MessageSenderChat)
        putLong(prefix + "_chat_id", what.chatId)
      }
      else -> {
        assertMessageSender_439d4c9c()
        throw unsupported(what)
      }
    }
  }
}

fun Bundle.restoreMessageSender (prefix: String): MessageSender? {
  val chatId = getLong(prefix + "_chat_id")
  val userId = getLong(prefix + "_user_id")
  return when {
    chatId != 0L -> MessageSenderChat(chatId)
    userId != 0L -> MessageSenderUser(userId)
    else -> {
      assertMessageSender_439d4c9c()
      null
    }
  }
}

fun Bundle.put (prefix: String, what: TextEntityType?) {
  if (what != null) {
    putInt(prefix + "_constructor", what.constructor)
    when (what.constructor) {
      TextEntityTypeMentionName.CONSTRUCTOR -> {
        require(what is TextEntityTypeMentionName)
        putLong(prefix + "_user_id", what.userId)
      }
      TextEntityTypeCustomEmoji.CONSTRUCTOR -> {
        require(what is TextEntityTypeCustomEmoji)
        putLong(prefix + "_custom_emoji_id", what.customEmojiId)
      }
      TextEntityTypeTextUrl.CONSTRUCTOR -> {
        require(what is TextEntityTypeTextUrl)
        putString(prefix + "_url", what.url)
      }
      TextEntityTypeMediaTimestamp.CONSTRUCTOR -> {
        require(what is TextEntityTypeMediaTimestamp)
        putInt(prefix + "_media_timestamp", what.mediaTimestamp)
      }
      TextEntityTypePreCode.CONSTRUCTOR -> {
        require(what is TextEntityTypePreCode)
        putString(prefix + "_language", what.language)
      }
      TextEntityTypeMention.CONSTRUCTOR,
      TextEntityTypeHashtag.CONSTRUCTOR,
      TextEntityTypeCashtag.CONSTRUCTOR,
      TextEntityTypeBotCommand.CONSTRUCTOR,
      TextEntityTypeUrl.CONSTRUCTOR,
      TextEntityTypeEmailAddress.CONSTRUCTOR,
      TextEntityTypePhoneNumber.CONSTRUCTOR,
      TextEntityTypeBankCardNumber.CONSTRUCTOR,
      TextEntityTypeBold.CONSTRUCTOR,
      TextEntityTypeItalic.CONSTRUCTOR,
      TextEntityTypeUnderline.CONSTRUCTOR,
      TextEntityTypeStrikethrough.CONSTRUCTOR,
      TextEntityTypeSpoiler.CONSTRUCTOR,
      TextEntityTypeCode.CONSTRUCTOR,
      TextEntityTypePre.CONSTRUCTOR,
      TextEntityTypeBlockQuote.CONSTRUCTOR,
      TextEntityTypeExpandableBlockQuote.CONSTRUCTOR -> {
        // Nothing additional to save
      }
      else -> {
        assertTextEntityType_56c1e709()
        throw unsupported(what)
      }
    }
  }
}

fun Bundle.restoreTextEntityType (prefix: String): TextEntityType? {
  val constructor = getInt(prefix + "_constructor")
  return when (constructor) {
    TextEntityTypeMentionName.CONSTRUCTOR -> {
      val userId = getLong(prefix + "_user_id")
      if (userId != 0L) {
        TextEntityTypeMentionName(userId)
      } else {
        null
      }
    }
    TextEntityTypeCustomEmoji.CONSTRUCTOR -> {
      val customEmojiId = getLong(prefix + "_custom_emoji_id")
      if (customEmojiId != 0L) {
        TextEntityTypeCustomEmoji(customEmojiId)
      } else {
        null
      }
    }
    TextEntityTypeTextUrl.CONSTRUCTOR -> {
      val url = getString(prefix + "_url")
      if (!url.isNullOrEmpty()) {
        TextEntityTypeTextUrl(url)
      } else {
        null
      }
    }
    TextEntityTypeMediaTimestamp.CONSTRUCTOR -> {
      val mediaTimestamp = getInt(prefix + "_media_timestamp", -1)
      if (mediaTimestamp >= 0) {
        TextEntityTypeMediaTimestamp(mediaTimestamp)
      } else {
        null
      }
    }
    TextEntityTypePreCode.CONSTRUCTOR -> {
      val language = getString(prefix + "_language")
      TextEntityTypePreCode(language)
    }
    TextEntityTypeMention.CONSTRUCTOR -> TextEntityTypeMention()
    TextEntityTypeHashtag.CONSTRUCTOR -> TextEntityTypeHashtag()
    TextEntityTypeCashtag.CONSTRUCTOR -> TextEntityTypeCashtag()
    TextEntityTypeBotCommand.CONSTRUCTOR -> TextEntityTypeBotCommand()
    TextEntityTypeUrl.CONSTRUCTOR -> TextEntityTypeUrl()
    TextEntityTypeEmailAddress.CONSTRUCTOR -> TextEntityTypeEmailAddress()
    TextEntityTypePhoneNumber.CONSTRUCTOR -> TextEntityTypePhoneNumber()
    TextEntityTypeBankCardNumber.CONSTRUCTOR -> TextEntityTypeBankCardNumber()
    TextEntityTypeBold.CONSTRUCTOR -> TextEntityTypeBold()
    TextEntityTypeItalic.CONSTRUCTOR -> TextEntityTypeItalic()
    TextEntityTypeUnderline.CONSTRUCTOR -> TextEntityTypeUnderline()
    TextEntityTypeStrikethrough.CONSTRUCTOR -> TextEntityTypeStrikethrough()
    TextEntityTypeSpoiler.CONSTRUCTOR -> TextEntityTypeSpoiler()
    TextEntityTypeCode.CONSTRUCTOR -> TextEntityTypeCode()
    TextEntityTypePre.CONSTRUCTOR -> TextEntityTypePre()
    TextEntityTypeBlockQuote.CONSTRUCTOR -> TextEntityTypeBlockQuote()
    TextEntityTypeExpandableBlockQuote.CONSTRUCTOR -> TextEntityTypeExpandableBlockQuote()
    else -> {
      assertTextEntityType_56c1e709()
      null
    }
  }
}

fun Bundle.put (prefix: String, what: TextEntity?) {
  if (what != null) {
    putInt(prefix + "_offset", what.offset)
    putInt(prefix + "_length", what.length)
    put(prefix + "_type", what.type)
  }
}

fun Bundle.restoreTextEntity (prefix: String): TextEntity? {
  val offset = getInt(prefix + "_offset")
  val length = getInt(prefix + "_length")
  val type = restoreTextEntityType(prefix + "_type")
  return if (type != null && containsKey(prefix + "_offset") && containsKey(prefix + "_length")) {
    TextEntity(offset, length, type)
  } else {
    null
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: ChatFolderName) {
  put(prefix + "_text", what.text)
  putBoolean(prefix + "_animateCustomEmoji", what.animateCustomEmoji)
}

fun Bundle.restoreChatFolderName (prefix: String): ChatFolderName {
  return ChatFolderName(
    restoreFormattedText(prefix + "_text") ?: FormattedText("", arrayOf()),
    getBoolean(prefix + "_animateCustomEmoji", true)
  )
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: FormattedText?) {
  if (!what.isEmpty()) {
    putString(prefix + "_text", what.text)
    if (!what.entities.isNullOrEmpty()) {
      putInt(prefix + "_entityCount", what.entities.size)
      what.entities.forEachIndexed { index, entity ->
        put(prefix + "_entity_" + index, entity)
      }
    }
  }
}

fun Bundle.restoreFormattedText (prefix: String): FormattedText? {
  val text = getString(prefix + "_text")
  return if (!text.isNullOrEmpty()) {
    val entityCount = getInt(prefix + "_entityCount")
    val entities = mutableListOf<TextEntity>()
    for (index in 0 until entityCount) {
      val entity = restoreTextEntity(prefix + "_entity_" + index)
      if (entity != null) {
        entities.add(entity)
      }
    }
    FormattedText(text, entities.toTypedArray())
  } else {
    null
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: InputTextQuote?) {
  if (!what.isEmpty()) {
    put(prefix + "_text", what.text)
    putInt(prefix + "_position", what.position)
  }
}

@ExperimentalContracts
fun Bundle.restoreInputTextQuote (prefix: String): InputTextQuote? {
  val text = restoreFormattedText(prefix + "_text")
  return if (!text.isEmpty()) {
    val position = getInt(prefix + "_position")
    InputTextQuote(text, position)
  } else {
    null
  }
}

fun Bundle.put (prefix: String, what: LinkPreviewOptions?) {
  if (what != null) {
    putBoolean(prefix + "_hasOptions", true)
    putBoolean(prefix + "_isDisabled", what.isDisabled)
    putString(prefix + "_url", what.url)
    putBoolean(prefix + "_forceSmallMedia", what.forceSmallMedia)
    putBoolean(prefix + "_forceLargeMedia", what.forceLargeMedia)
    putBoolean(prefix + "_showAboveText", what.showAboveText)
  }
}

fun Bundle.restoreLinkPreviewOptions (prefix: String): LinkPreviewOptions? {
  return if (getBoolean(prefix + "_hasOptions")) {
    LinkPreviewOptions(
      getBoolean(prefix + "_isDisabled"),
      getString(prefix + "_url"),
      getBoolean(prefix + "_forceSmallMedia"),
      getBoolean(prefix + "_forceLargeMedia"),
      getBoolean(prefix + "_showAboveText")
    )
  } else {
    null
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: InputMessageText?) {
  if (what != null) {
    put(prefix + "_text", what.text)
    put(prefix + "_linkPreviewOptions", what.linkPreviewOptions)
    putBoolean(prefix + "_clearDraft", what.clearDraft)
  }
}

@ExperimentalContracts
fun Bundle.restoreInputMessageText (prefix: String): InputMessageText? {
  val text = restoreFormattedText(prefix + "_text")
  val linkPreviewOptions = restoreLinkPreviewOptions(prefix + "_linkPreviewOptions")
  val clearDraft = getBoolean(prefix + "_clearDraft")
  return if (!text.isEmpty()) {
    InputMessageText(
      text,
      linkPreviewOptions,
      clearDraft
    )
  } else {
    null
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: InputMessageReplyTo?) {
  if (what != null) {
    putInt(prefix + "_constructor", what.constructor)
    when (what.constructor) {
      InputMessageReplyToMessage.CONSTRUCTOR -> {
        require(what is InputMessageReplyToMessage)
        putLong(prefix + "_messageId", what.messageId)
        put(prefix + "_quote", what.quote)
      }
      InputMessageReplyToExternalMessage.CONSTRUCTOR -> {
        require(what is InputMessageReplyToExternalMessage)
        putLong(prefix + "_chatId", what.chatId)
        putLong(prefix + "_messageId", what.messageId)
        put(prefix + "_quote", what.quote)
      }
      InputMessageReplyToStory.CONSTRUCTOR -> {
        require(what is InputMessageReplyToStory)
        putLong(prefix + "_chatId", what.storyPosterChatId)
        putInt(prefix + "_storyId", what.storyId)
      }
      else -> {
        assertInputMessageReplyTo_acef6f3a()
        throw unsupported(what)
      }
    }
  }
}

@ExperimentalContracts
fun Bundle.restoreInputMessageReplyTo (prefix: String): InputMessageReplyTo? {
  val constructor = getInt(prefix + "_constructor")
  return when (constructor) {
    InputMessageReplyToMessage.CONSTRUCTOR -> {
      val messageId = getLong(prefix + "_messageId")
      val quote = restoreInputTextQuote(prefix + "_quote")
      if (messageId != 0L) {
        InputMessageReplyToMessage(messageId, quote)
      } else {
        null
      }
    }
    InputMessageReplyToExternalMessage.CONSTRUCTOR -> {
      val chatId = getLong(prefix + "_chatId")
      val messageId = getLong(prefix + "_messageId")
      val quote = restoreInputTextQuote(prefix + "_quote")
      if (messageId != 0L) {
        InputMessageReplyToExternalMessage(chatId, messageId, quote)
      } else {
        null
      }
    }
    InputMessageReplyToStory.CONSTRUCTOR -> {
      val storySenderChatId = getLong(prefix + "_chatId")
      val storyId = getInt(prefix + "_storyId")
      if (storySenderChatId != 0L && storyId != 0) {
        InputMessageReplyToStory(storySenderChatId, storyId)
      } else {
        null
      }
    }
    else -> {
      assertInputMessageReplyTo_acef6f3a()
      null
    }
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: DraftMessage?) {
  if (what != null) {
    if (COMPILE_CHECK) {
      DraftMessage(
        what.replyTo,
        what.date,
        what.inputMessageText,
        what.effectId
      )
    }
    val inputMessageText = what.inputMessageText as InputMessageText?
    put(prefix + "_replyTo", what.replyTo)
    putInt(prefix + "_date", what.date)
    put(prefix + "_text", inputMessageText)
    putLong(prefix + "_effect", what.effectId)
  }
}

@ExperimentalContracts
fun Bundle.restoreDraftMessage (prefix: String): DraftMessage? {
  val replyTo = restoreInputMessageReplyTo(prefix + "_replyTo")
  val date = getInt(prefix + "_date")
  val inputMessageText = restoreInputMessageText(prefix + "_text")
  val effectId = getLong(prefix + "_effect")
  return if (inputMessageText != null || replyTo != null) {
    DraftMessage(replyTo, date, inputMessageText, effectId)
  } else {
    null
  }
}

@ExperimentalContracts
fun Bundle.put (prefix: String, what: MessageReplyInfo?) {
  if (what != null) {
    if (COMPILE_CHECK) {
      MessageReplyInfo(
        what.replyCount,
        what.recentReplierIds,
        what.lastReadInboxMessageId,
        what.lastReadOutboxMessageId,
        what.lastMessageId
      )
    }
    putInt(prefix + "_replyCount", what.replyCount)
    putLong(prefix + "_lastMessageId", what.lastMessageId)
    putLong(prefix + "_lastReadInboxMessageId", what.lastReadInboxMessageId)
    putLong(prefix + "_lastReadOutboxMessageId", what.lastReadOutboxMessageId)
    if (!what.recentReplierIds.isNullOrEmpty()) {
      putInt(prefix + "_recentReplierIdsLength", what.recentReplierIds.size)
      what.recentReplierIds.forEachIndexed { index, recentReplierId ->
        put(prefix + "_recentReplierId_" + index, recentReplierId)
      }
    }
  }
}

@ExperimentalContracts
fun Bundle.restoreMessageReplyInfo (prefix: String): MessageReplyInfo? {
  val replyCount = getInt(prefix + "_replyCount", -1)
  if (replyCount == -1) {
    return null
  }
  val lastMessageId = getLong(prefix + "_lastMessageId")
  val lastReadInboxMessageId = getLong(prefix + "_lastReadInboxMessageId")
  val lastReadOutboxMessageId = getLong(prefix + "_lastReadOutboxMessageId")
  val recentReplierIdsLength = getInt(prefix + "_recentReplierIdsLength")
  val recentReplierIds = mutableListOf<MessageSender>()
  for (index in 0 until recentReplierIdsLength) {
    val recentReplierId = restoreMessageSender(prefix + "_recentReplierId_" + index)
    if (recentReplierId != null) {
      recentReplierIds.add(recentReplierId)
    }
  }
  return MessageReplyInfo(
    replyCount,
    recentReplierIds.toTypedArray(),
    lastReadInboxMessageId,
    lastReadOutboxMessageId,
    lastMessageId
  )
}