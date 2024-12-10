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

import me.vkryl.core.equalsOrBothEmpty
import org.drinkless.tdlib.TdApi.*
import java.util.*
import kotlin.contracts.ExperimentalContracts

// Core comparison tools

internal fun String?.equalsOrEmpty(b: String?): Boolean =
  (this.isNullOrEmpty() && b.isNullOrEmpty()) || this == b

internal fun <T : Object> T?.safeEqualsTo(b: T?, equalsTo: T.(T) -> Boolean): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> this.equalsTo(b)
  }
}

internal fun IntArray?.contentEqualsOrEmpty(other: IntArray?): Boolean =
  this.contentEquals(other) || ((this?.size ?: 0) == 0 && (other?.size ?: 0) == 0)

internal fun LongArray?.contentEqualsOrEmpty(other: LongArray?): Boolean =
  this.contentEquals(other) || ((this?.size ?: 0) == 0 && (other?.size ?: 0) == 0)

internal fun BooleanArray?.contentEqualsOrEmpty(other: BooleanArray?): Boolean =
  this.contentEquals(other) || ((this?.size ?: 0) == 0 && (other?.size ?: 0) == 0)

internal fun ShortArray?.contentEqualsOrEmpty(other: ShortArray?): Boolean =
  this.contentEquals(other) || ((this?.size ?: 0) == 0 && (other?.size ?: 0) == 0)

internal fun ByteArray?.contentEqualsOrEmpty(other: ByteArray?): Boolean =
  this.contentEquals(other) || ((this?.size ?: 0) == 0 && (other?.size ?: 0) == 0)

internal fun Array<String?>?.contentEqualsOrEmpty(other: Array<String?>?): Boolean =
  this.contentEqualsOrEmpty(other, String?::equalsOrEmpty)

internal fun <T> Array<T>?.contentEqualsOrEmpty(other: Array<T>?, contentEqual: T.(T) -> Boolean): Boolean {
  return when {
    this === other || (this.isNullOrEmpty() && other.isNullOrEmpty()) -> true
    this == null || other == null || this.size != other.size -> false
    else -> {
      this.forEachIndexed { index, obj ->
        val b = other[index]
        if (!obj.contentEqual(b)) {
          return false
        }
      }
      true
    }
  }
}

// Array methods

@JvmOverloads
fun Array<PollOption>?.equalsTo (other: Array<PollOption>?, onlyTextContent: Boolean = false): Boolean = this.contentEqualsOrEmpty(other) { b ->
  this.equalsTo(b, onlyTextContent)
}

@JvmOverloads
fun Array<TextEntity>?.equalsTo(other: Array<TextEntity>?, ignoreDefaultEntities: Boolean = false): Boolean = when {
  this === other -> true
  ignoreDefaultEntities -> this.findEssential().equalsTo(other.findEssential())
  else -> this.contentEqualsOrEmpty(other, TextEntity::equalsTo)
}

// Methods below rely on non-default behavior.

@OptIn(ExperimentalContracts::class)
@JvmOverloads
fun PollOption.equalsTo(b: PollOption, onlyTextContent: Boolean = false): Boolean {
  if (COMPILE_CHECK) {
    PollOption(
      this.text,
      this.voterCount,
      this.votePercentage,
      this.isChosen,
      this.isBeingChosen
    )
  }
  return this.text.equalsTo(b.text) && (onlyTextContent || (
    this.voterCount == b.voterCount &&
    this.votePercentage == b.votePercentage &&
    this.isChosen == b.isChosen &&
    this.isBeingChosen == b.isBeingChosen
  ))
}

@OptIn(ExperimentalContracts::class)
fun Poll.equalsTo(b: Poll, onlyTextContent: Boolean = false): Boolean {
  if (COMPILE_CHECK) {
    Poll(
      this.id,
      this.question,
      this.options,
      this.totalVoterCount,
      this.recentVoterIds,
      this.isAnonymous,
      this.type,
      this.openPeriod,
      this.closeDate,
      this.isClosed
    )
  }
  return this.question.equalsTo(b.question) &&
    this.options.equalsTo(b.options, onlyTextContent) && (onlyTextContent || (
    this.id == b.id &&
    this.totalVoterCount == b.totalVoterCount &&
    this.recentVoterIds.contentEquals(b.recentVoterIds) &&
    this.isAnonymous == b.isAnonymous &&
    this.type.equalsTo(b.type) &&
    this.openPeriod == b.openPeriod &&
    this.closeDate == b.closeDate &&
    this.isClosed == b.isClosed
  ))
}

fun ChatPermissions.equalsTo(old: ChatPermissions, defaultPermissions: ChatPermissions): Boolean {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    ChatPermissions(
      this.canSendBasicMessages,
      this.canSendAudios,
      this.canSendDocuments,
      this.canSendPhotos,
      this.canSendVideos,
      this.canSendVideoNotes,
      this.canSendVoiceNotes,
      this.canSendPolls,
      this.canSendOtherMessages,
      this.canAddLinkPreviews,
      this.canChangeInfo,
      this.canInviteUsers,
      this.canPinMessages,
      this.canCreateTopics
    )
  }
  return (this === old) || (
    (this.canSendBasicMessages == old.canSendBasicMessages || !this.canSendBasicMessages && !defaultPermissions.canSendBasicMessages) &&
    (this.canSendAudios == old.canSendAudios || !this.canSendAudios && !defaultPermissions.canSendAudios) &&
    (this.canSendDocuments == old.canSendDocuments || !this.canSendDocuments && !defaultPermissions.canSendDocuments) &&
    (this.canSendPhotos == old.canSendPhotos || !this.canSendPhotos && !defaultPermissions.canSendPhotos) &&
    (this.canSendVideos == old.canSendVideos || !this.canSendVideos && !defaultPermissions.canSendVideos) &&
    (this.canSendVideoNotes == old.canSendVideoNotes || !this.canSendVideoNotes && !defaultPermissions.canSendVideoNotes) &&
    (this.canSendVoiceNotes == old.canSendVoiceNotes || !this.canSendVoiceNotes && !defaultPermissions.canSendVoiceNotes) &&
    (this.canSendPolls == old.canSendPolls || !this.canSendPolls && !defaultPermissions.canSendPolls) &&
    (this.canSendOtherMessages == old.canSendOtherMessages || !this.canSendOtherMessages && !defaultPermissions.canSendOtherMessages) &&
    (this.canAddLinkPreviews == old.canAddLinkPreviews || !this.canAddLinkPreviews && !defaultPermissions.canAddLinkPreviews) &&
    (this.canChangeInfo == old.canChangeInfo || !this.canChangeInfo && !defaultPermissions.canChangeInfo) &&
    (this.canInviteUsers == old.canInviteUsers || !this.canInviteUsers && !defaultPermissions.canInviteUsers) &&
    (this.canPinMessages == old.canPinMessages || !this.canPinMessages && !defaultPermissions.canPinMessages) &&
    (this.canCreateTopics == old.canCreateTopics || !this.canCreateTopics && !defaultPermissions.canCreateTopics)
  )
}

@ExperimentalContracts
@JvmOverloads
fun FormattedText?.equalsTo(b: FormattedText?, ignoreDefaultEntities: Boolean = false): Boolean {
  return when {
    this === b -> true
    this.isEmpty() -> b.isEmpty()
    else -> {
      !b.isEmpty() &&
      this.text.equalsOrBothEmpty(b.text) &&
      this.entities.equalsTo(b.entities, ignoreDefaultEntities)
    }
  }
}

@ExperimentalContracts
@JvmOverloads
fun InputTextQuote?.equalsTo(b: InputTextQuote?, ignoreDefaultEntities: Boolean = false): Boolean {
  return when {
    this === b -> true
    this.isEmpty() -> b.isEmpty()
    else -> {
      !b.isEmpty() &&
      this.text.equalsTo(b.text, ignoreDefaultEntities) &&
      this.position == b.position
    }
  }
}

@JvmOverloads fun File?.equalsTo(b: File?, onlyIdentifier: Boolean = true): Boolean {
  return when {
    this === b -> true
    this === null || b === null -> false
    onlyIdentifier -> this.id == b.id
    else -> {
      this.id == b.id &&
      this.size == b.size &&
      this.expectedSize == b.expectedSize &&
      this.local.equalsTo(b.local) &&
      this.remote.equalsTo(b.remote)
    }
  }
}

fun ChatEventLogFilters?.equalsTo(b: ChatEventLogFilters?): Boolean {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    requireNotNull(this)
    ChatEventLogFilters(
      this.messageEdits,
      this.messageDeletions,
      this.messagePins,
      this.memberJoins,
      this.memberLeaves,
      this.memberInvites,
      this.memberPromotions,
      this.memberRestrictions,
      this.infoChanges,
      this.settingChanges,
      this.inviteLinkChanges,
      this.videoChatChanges,
      this.forumChanges,
      this.subscriptionExtensions
    )
  }
  return (
    (this?.messageEdits ?: false) == (b?.messageEdits ?: false) &&
    (this?.messageDeletions ?: false) == (b?.messageDeletions ?: false) &&
    (this?.messagePins ?: false) == (b?.messagePins ?: false) &&
    (this?.memberJoins ?: false) == (b?.memberJoins ?: false) &&
    (this?.memberLeaves ?: false) == (b?.memberLeaves ?: false) &&
    (this?.memberInvites ?: false) == (b?.memberInvites ?: false) &&
    (this?.memberPromotions ?: false) == (b?.memberPromotions ?: false) &&
    (this?.memberRestrictions ?: false) == (b?.memberRestrictions ?: false) &&
    (this?.infoChanges ?: false) == (b?.infoChanges ?: false) &&
    (this?.settingChanges ?: false) == (b?.settingChanges ?: false) &&
    (this?.inviteLinkChanges ?: false) == (b?.inviteLinkChanges ?: false) &&
    (this?.videoChatChanges ?: false) == (b?.videoChatChanges ?: false) &&
    (this?.forumChanges ?: false) == (b?.forumChanges ?: false) &&
    (this?.subscriptionExtensions ?: false) == (b?.subscriptionExtensions ?: false)
  )
}

@JvmOverloads fun Minithumbnail?.equalsTo(b: Minithumbnail?, checkJpegBytes: Boolean = false): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      (!checkJpegBytes || this.data.contentEquals(b.data))
    }
  }
}

fun Array<PhotoSize>?.equalsTo(b: Array<PhotoSize>?): Boolean {
  return when {
    this === b -> true
    this.isNullOrEmpty() || b.isNullOrEmpty() -> this.isNullOrEmpty() == b.isNullOrEmpty()
    this.contentEqualsOrEmpty(b, PhotoSize::equalsTo) -> true
    else -> {
      val minArray = if (this.size <= b.size) this else b
      val maxArray = if (this.size <= b.size) b else this
      val map = HashMap<String, PhotoSize>(minArray.size)
      for (size in minArray) {
        map[size.type] = size
      }
      for (size in maxArray) {
        val other = map[size.type]
        if (other != null && !other.equalsTo(size)) {
          return false
        }
      }
      true
    }
  }
}

@JvmOverloads fun VoiceNote?.equalsTo(b: VoiceNote?, checkWaveformBytes: Boolean = false): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.duration == b.duration &&
      this.mimeType.equalsOrBothEmpty(b.mimeType) &&
      this.voice.equalsTo(b.voice) &&
      (if (checkWaveformBytes)
        this.waveform.contentEqualsOrEmpty(b.waveform)
      else
        (this.waveform?.size ?: 0) == (b.waveform?.size ?: 0)
      )
    }
  }
}

@ExperimentalContracts
@JvmOverloads fun DraftMessage?.equalsTo(b: DraftMessage?, ignoreDate: Boolean = false): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      (this.date == b.date || ignoreDate) && this.replyTo.equalsTo(b.replyTo) && this.inputMessageText.equalsTo(b.inputMessageText)
    }
  }
}

@ExperimentalContracts
fun InputMessageContent?.equalsTo(b: InputMessageContent?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      InputMessageText.CONSTRUCTOR -> {
        require(this is InputMessageText && b is InputMessageText)
        this.text.equalsTo(b.text) &&
        this.clearDraft == b.clearDraft &&
        this.linkPreviewOptions.equalsTo(b.linkPreviewOptions)
      }
      else -> TODO(this.toString())
    }
  }
}

fun LinkPreviewOptions?.equalsTo(b: LinkPreviewOptions?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> {
      val any = (this ?: b)!!
      !any.isDisabled &&
      any.url.isNullOrEmpty() &&
      !any.forceLargeMedia &&
      !any.forceSmallMedia &&
      !any.showAboveText
    }
    else -> {
      this.isDisabled == b.isDisabled &&
      this.url == b.url &&
      this.forceLargeMedia == b.forceLargeMedia &&
      this.forceSmallMedia == b.forceSmallMedia &&
      this.showAboveText == b.showAboveText
    }
  }
}

@ExperimentalContracts
fun InputMessageReplyTo?.equalsTo(b: InputMessageReplyTo?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      InputMessageReplyToMessage.CONSTRUCTOR -> {
        require(this is InputMessageReplyToMessage && b is InputMessageReplyToMessage)
        if (COMPILE_CHECK) {
          InputMessageReplyToMessage(
            this.messageId,
            this.quote
          )
        }
        this.messageId == b.messageId &&
        this.quote.equalsTo(b.quote, true)
      }
      InputMessageReplyToExternalMessage.CONSTRUCTOR -> {
        require(this is InputMessageReplyToExternalMessage && b is InputMessageReplyToExternalMessage)
        if (COMPILE_CHECK) {
          InputMessageReplyToExternalMessage(
            this.chatId,
            this.messageId,
            this.quote
          )
        }
        this.chatId == b.chatId &&
        this.messageId == b.messageId &&
        this.quote.equalsTo(b.quote, true)
      }
      InputMessageReplyToStory.CONSTRUCTOR -> {
        require(this is InputMessageReplyToStory && b is InputMessageReplyToStory)
        if (COMPILE_CHECK) {
          InputMessageReplyToStory(
            this.storySenderChatId,
            this.storyId
          )
        }
        this.storySenderChatId == b.storySenderChatId &&
        this.storyId == b.storyId
      }
      else -> {
        assertMessageReplyTo_699c5345()
        throw unsupported(this)
      }
    }
  }
}

@ExperimentalContracts
@JvmOverloads
fun LinkPreview?.equalsTo(b: LinkPreview?, ignoreMetadata: Boolean = false): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      if (COMPILE_CHECK) {
        LinkPreview(
          this.url,
          this.displayUrl,
          this.siteName,
          this.title,
          this.description,
          this.author,
          this.type,
          this.hasLargeMedia,
          this.showLargeMedia,
          this.showMediaAboveDescription,
          this.skipConfirmation,
          this.showAboveText,
          this.instantViewVersion
        )
      }
      this.url.equalsOrBothEmpty(b.url) &&
      this.displayUrl.equalsOrBothEmpty(b.displayUrl) &&
      this.siteName.equalsOrBothEmpty(b.siteName) &&
      this.title.equalsOrBothEmpty(b.title) &&
      this.description.equalsTo(b.description) &&
      this.author.equalsOrBothEmpty(b.author) &&
      this.type.equalsTo(b.type) &&
      (ignoreMetadata || (
        this.hasLargeMedia == b.hasLargeMedia &&
        this.showLargeMedia == b.showLargeMedia &&
        this.showMediaAboveDescription == b.showMediaAboveDescription &&
        this.skipConfirmation == b.skipConfirmation &&
        this.showAboveText == b.showAboveText
      )) &&
      this.instantViewVersion == b.instantViewVersion
    }
  }
}

fun BackgroundType?.equalsTo(b: BackgroundType?, ignoreSettings: Boolean = true): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        BackgroundTypeWallpaper.CONSTRUCTOR -> {
          require(this is BackgroundTypeWallpaper && b is BackgroundTypeWallpaper)
          ignoreSettings || (this.isBlurred == b.isBlurred && this.isMoving == b.isMoving)
        }
        BackgroundTypeFill.CONSTRUCTOR -> {
          require(this is BackgroundTypeFill && b is BackgroundTypeFill)
          this.fill.equalsTo(b.fill)
        }
        BackgroundTypePattern.CONSTRUCTOR -> {
          require(this is BackgroundTypePattern && b is BackgroundTypePattern)
          this.fill.equalsTo(b.fill) && (ignoreSettings || (this.intensity == b.intensity && this.isMoving == b.isMoving))
        }
        BackgroundTypeChatTheme.CONSTRUCTOR -> {
          require(this is BackgroundTypeChatTheme && b is BackgroundTypeChatTheme)
          this.themeName == b.themeName
        }
        else -> {
          assertBackgroundType_eedb1e16()
          throw unsupported(this)
        }
      }
    }
  }
}

fun MessageReplyTo?.equalsTo(chatId: Long, messageId: Long): Boolean {
  return if (this != null && this.constructor == MessageReplyToMessage.CONSTRUCTOR) {
    require(this is MessageReplyToMessage)
    this.chatId == chatId && this.messageId == messageId
  } else {
    false
  }
}

@JvmOverloads
fun MessageImportInfo?.equalsTo(b: MessageImportInfo?, compareDate: Boolean = true): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> this.senderName.equalsOrBothEmpty(b.senderName) && (!compareDate || this.date == b.date)
  }
}

@JvmOverloads fun MessageOrigin?.equalsTo(b: MessageOrigin?, checkMessageId: Boolean = true): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      MessageOriginUser.CONSTRUCTOR -> {
        require(this is MessageOriginUser && b is MessageOriginUser)
        this.senderUserId == b.senderUserId
      }
      MessageOriginHiddenUser.CONSTRUCTOR -> {
        require(this is MessageOriginHiddenUser && b is MessageOriginHiddenUser)
        this.senderName == b.senderName
      }
      MessageOriginChat.CONSTRUCTOR -> {
        require(this is MessageOriginChat && b is MessageOriginChat)
        this.senderChatId == b.senderChatId &&
        this.authorSignature == b.authorSignature
      }
      MessageOriginChannel.CONSTRUCTOR -> {
        require(this is MessageOriginChannel && b is MessageOriginChannel)
        this.chatId == b.chatId &&
        (!checkMessageId || this.messageId == b.messageId) &&
        this.authorSignature == b.authorSignature
      }
      else -> {
        assertMessageOrigin_f2224a59()
        throw unsupported(this)
      }
    }
  }
}

@JvmOverloads fun ForwardSource?.equalsTo(b: ForwardSource?, checkMetadata: Boolean = true): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      if (COMPILE_CHECK) {
        ForwardSource(
          this.chatId,
          this.messageId,
          this.senderId,
          this.senderName,
          this.date,
          this.isOutgoing
        )
      }
      this.chatId == b.chatId &&
      this.messageId == b.messageId &&
      this.senderId.equalsTo(b.senderId) &&
      this.senderName.equalsOrBothEmpty(b.senderName) &&
      (!checkMetadata || (
        this.date == b.date &&
        this.isOutgoing == b.isOutgoing
      ))
    }
  }
}