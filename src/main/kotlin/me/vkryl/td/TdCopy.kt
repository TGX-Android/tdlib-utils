/*
 * This file is a part of tdlib-utils
 * Copyright © Vyacheslav Krylov (slavone@protonmail.ch) 2014
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

import org.drinkless.tdlib.TdApi.*

fun ChatPermissions.copyTo (dst: ChatPermissions) {
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
      this.canAddWebPagePreviews,
      this.canChangeInfo,
      this.canInviteUsers,
      this.canPinMessages,
      this.canCreateTopics
    )
  }
  dst.canSendBasicMessages = this.canSendBasicMessages
  dst.canSendAudios = this.canSendAudios
  dst.canSendDocuments = this.canSendDocuments
  dst.canSendPhotos = this.canSendPhotos
  dst.canSendVideos = this.canSendVideos
  dst.canSendVideoNotes = this.canSendVideoNotes
  dst.canSendVoiceNotes = this.canSendVoiceNotes
  dst.canSendPolls = this.canSendPolls
  dst.canSendOtherMessages = this.canSendOtherMessages
  dst.canAddWebPagePreviews = this.canAddWebPagePreviews
  dst.canChangeInfo = this.canChangeInfo
  dst.canInviteUsers = this.canInviteUsers
  dst.canPinMessages = this.canPinMessages
  dst.canCreateTopics = this.canCreateTopics
}

fun ChatPosition.copyTo (dst: ChatPosition) {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    ChatPosition(
      this.list,
      this.order,
      this.isPinned,
      this.source
    )
  }
  dst.list = this.list
  dst.order = this.order
  dst.isPinned = this.isPinned
  dst.source = this.source
}

fun File.copyTo (dst: File): Boolean {
  val hasChanges = !this.equalsTo(dst, false)

  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    LocalFile(
      this.local.path,
      this.local.canBeDownloaded,
      this.local.canBeDeleted,
      this.local.isDownloadingActive,
      this.local.isDownloadingCompleted,
      this.local.downloadOffset,
      this.local.downloadedPrefixSize,
      this.local.downloadedSize
    )
  }
  dst.local.path = this.local.path
  dst.local.canBeDownloaded = this.local.canBeDownloaded
  dst.local.canBeDeleted = this.local.canBeDeleted
  dst.local.isDownloadingActive = this.local.isDownloadingActive
  dst.local.isDownloadingCompleted = this.local.isDownloadingCompleted
  dst.local.downloadOffset = this.local.downloadOffset
  dst.local.downloadedPrefixSize = this.local.downloadedPrefixSize
  dst.local.downloadedSize = this.local.downloadedSize

  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    RemoteFile(
      this.remote.id,
      this.remote.uniqueId,
      this.remote.isUploadingActive,
      this.remote.isUploadingCompleted,
      this.remote.uploadedSize
    )
  }
  dst.remote.id = this.remote.id
  dst.remote.uniqueId = this.remote.uniqueId
  dst.remote.isUploadingActive = this.remote.isUploadingActive
  dst.remote.isUploadingCompleted = this.remote.isUploadingCompleted
  dst.remote.uploadedSize = this.remote.uploadedSize

  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    File(
      this.id,
      this.size,
      this.expectedSize,
      this.local,
      this.remote
    )
  }
  // dst.id = this.id
  dst.size = this.size
  dst.expectedSize = this.expectedSize

  return hasChanges
}

fun User.copyTo (dst: User) {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    User(
      this.id,
      this.firstName,
      this.lastName,
      this.usernames,
      this.phoneNumber,
      this.status,
      this.profilePhoto,
      this.accentColorId,
      this.backgroundCustomEmojiId,
      this.profileAccentColorId,
      this.profileBackgroundCustomEmojiId,
      this.emojiStatus,
      this.isContact,
      this.isMutualContact,
      this.isCloseFriend,
      this.isVerified,
      this.isPremium,
      this.isSupport,
      this.restrictionReason,
      this.isScam,
      this.isFake,
      this.hasActiveStories,
      this.hasUnreadActiveStories,
      this.restrictsNewChats,
      this.haveAccess,
      this.type,
      this.languageCode,
      this.addedToAttachmentMenu
    )
  }
  dst.firstName = this.firstName
  dst.lastName = this.lastName
  dst.usernames = this.usernames
  dst.phoneNumber = this.phoneNumber
  dst.status = this.status
  dst.profilePhoto = this.profilePhoto
  dst.accentColorId = this.accentColorId
  dst.backgroundCustomEmojiId = this.backgroundCustomEmojiId
  dst.profileAccentColorId = this.profileAccentColorId
  dst.profileBackgroundCustomEmojiId = this.profileBackgroundCustomEmojiId
  dst.emojiStatus = this.emojiStatus
  dst.isContact = this.isContact
  dst.isMutualContact = this.isMutualContact
  dst.isCloseFriend = this.isCloseFriend
  dst.isVerified = this.isVerified
  dst.isPremium = this.isPremium
  dst.isSupport = this.isSupport
  dst.restrictionReason = this.restrictionReason
  dst.isScam = this.isScam
  dst.isFake = this.isFake
  dst.hasActiveStories = this.hasActiveStories
  dst.hasUnreadActiveStories = this.hasUnreadActiveStories
  dst.restrictsNewChats = this.restrictsNewChats
  dst.haveAccess = this.haveAccess
  dst.type = this.type
  dst.languageCode = this.languageCode
  dst.addedToAttachmentMenu = this.addedToAttachmentMenu
}

fun Message.copyTo (dst: Message) {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    Message(
      this.id,
      this.senderId,
      this.chatId,
      this.sendingState,
      this.schedulingState,
      this.isOutgoing,
      this.isPinned,
      this.canBeEdited,
      this.canBeForwarded,
      this.canBeRepliedInAnotherChat,
      this.canBeSaved,
      this.canBeDeletedOnlyForSelf,
      this.canBeDeletedForAllUsers,
      this.canGetAddedReactions,
      this.canGetStatistics,
      this.canGetMessageThread,
      this.canGetReadDate,
      this.canGetViewers,
      this.canGetMediaTimestampLinks,
      this.canReportReactions,
      this.hasTimestampedMedia,
      this.isChannelPost,
      this.isTopicMessage,
      this.containsUnreadMention,
      this.date,
      this.editDate,
      this.forwardInfo,
      this.importInfo,
      this.interactionInfo,
      this.unreadReactions,
      this.replyTo,
      this.messageThreadId,
      this.savedMessagesTopicId,
      this.selfDestructType,
      this.selfDestructIn,
      this.autoDeleteIn,
      this.viaBotUserId,
      this.senderBoostCount,
      this.authorSignature,
      this.mediaAlbumId,
      this.restrictionReason,
      this.content,
      this.replyMarkup
    )
  }
  dst.id = this.id
  dst.senderId = this.senderId
  dst.chatId = this.chatId
  dst.sendingState = this.sendingState
  dst.schedulingState = this.schedulingState
  dst.isOutgoing = this.isOutgoing
  dst.isPinned = this.isPinned
  dst.canBeEdited = this.canBeEdited
  dst.canBeForwarded = this.canBeForwarded
  dst.canBeRepliedInAnotherChat = this.canBeRepliedInAnotherChat
  dst.canBeSaved = this.canBeSaved
  dst.canBeDeletedOnlyForSelf = this.canBeDeletedOnlyForSelf
  dst.canBeDeletedForAllUsers = this.canBeDeletedForAllUsers
  dst.canGetAddedReactions = this.canGetAddedReactions
  dst.canGetStatistics = this.canGetStatistics
  dst.canGetMessageThread = this.canGetMessageThread
  dst.canGetReadDate = this.canGetReadDate
  dst.canGetViewers = this.canGetViewers
  dst.canGetMediaTimestampLinks = this.canGetMediaTimestampLinks
  dst.canReportReactions = this.canReportReactions;
  dst.hasTimestampedMedia = this.hasTimestampedMedia
  dst.isChannelPost = this.isChannelPost
  dst.isTopicMessage = this.isTopicMessage
  dst.containsUnreadMention = this.containsUnreadMention
  dst.date = this.date
  dst.editDate = this.editDate
  dst.forwardInfo = this.forwardInfo
  dst.interactionInfo = this.interactionInfo
  dst.unreadReactions = this.unreadReactions
  dst.replyTo = this.replyTo
  dst.messageThreadId = this.messageThreadId
  dst.savedMessagesTopicId = this.savedMessagesTopicId
  dst.selfDestructType = this.selfDestructType
  dst.selfDestructIn = this.selfDestructIn
  dst.autoDeleteIn = this.autoDeleteIn
  dst.viaBotUserId = this.viaBotUserId
  dst.senderBoostCount = this.senderBoostCount
  dst.authorSignature = this.authorSignature
  dst.mediaAlbumId = this.mediaAlbumId
  dst.restrictionReason = this.restrictionReason
  dst.content = this.content
  dst.replyMarkup = this.replyMarkup
}

fun LinkPreviewOptions?.copyTo (dst: LinkPreviewOptions) {
  if (COMPILE_CHECK) {
    LinkPreviewOptions(
      false,
      "",
      false,
      false,
      false
    )
  }
  dst.isDisabled = this?.isDisabled ?: false
  dst.url = this?.url ?: ""
  dst.forceSmallMedia = this?.forceSmallMedia ?: false
  dst.forceLargeMedia = this?.forceLargeMedia ?: false
  dst.showAboveText = this?.showAboveText ?: false
}

fun AccentColor.copyTo (dst: AccentColor) {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    AccentColor(
      this.id,
      this.builtInAccentColorId,
      this.lightThemeColors,
      this.darkThemeColors,
      this.minChannelChatBoostLevel
    )
  }
  dst.id = this.id
  dst.builtInAccentColorId = this.builtInAccentColorId
  dst.lightThemeColors = this.lightThemeColors
  dst.darkThemeColors = this.darkThemeColors
  dst.minChannelChatBoostLevel = this.minChannelChatBoostLevel
}

fun File?.copyOf (): File? {
  return this?.let {
    File(
      this.id,
      this.size,
      this.expectedSize,
      this.local.copyOf(),
      this.remote.copyOf())
  }
}

fun LocalFile?.copyOf (): LocalFile? {
  return this?.let {
    LocalFile(
      this.path,
      this.canBeDownloaded,
      this.canBeDeleted,
      this.isDownloadingActive,
      this.isDownloadingCompleted,
      this.downloadOffset,
      this.downloadedPrefixSize,
      this.downloadedSize
    )
  }
}

fun RemoteFile?.copyOf (): RemoteFile? {
  return this?.let {
    RemoteFile(
      this.id,
      this.uniqueId,
      this.isUploadingActive,
      this.isUploadingCompleted,
      this.uploadedSize
    )
  }
}

fun Message?.copyOf (): Message? {
  return this?.let {
    Message(
      this.id,
      this.senderId,
      this.chatId,
      this.sendingState,
      this.schedulingState,
      this.isOutgoing,
      this.isPinned,
      this.canBeEdited,
      this.canBeForwarded,
      this.canBeRepliedInAnotherChat,
      this.canBeSaved,
      this.canBeDeletedOnlyForSelf,
      this.canBeDeletedForAllUsers,
      this.canGetAddedReactions,
      this.canGetStatistics,
      this.canGetMessageThread,
      this.canGetReadDate,
      this.canGetViewers,
      this.canGetMediaTimestampLinks,
      this.canReportReactions,
      this.hasTimestampedMedia,
      this.isChannelPost,
      this.isTopicMessage,
      this.containsUnreadMention,
      this.date,
      this.editDate,
      this.forwardInfo,
      this.importInfo,
      this.interactionInfo,
      this.unreadReactions,
      this.replyTo,
      this.messageThreadId,
      this.savedMessagesTopicId,
      this.selfDestructType,
      this.selfDestructIn,
      this.autoDeleteIn,
      this.viaBotUserId,
      this.senderBoostCount,
      this.authorSignature,
      this.mediaAlbumId,
      this.restrictionReason,
      this.content,
      this.replyMarkup
    )
  }
}

fun Chat?.copyOf (): Chat? {
  return this?.let {
    Chat(
      this.id,
      this.type,
      this.title,
      this.photo,
      this.accentColorId,
      this.backgroundCustomEmojiId,
      this.profileAccentColorId,
      this.profileBackgroundCustomEmojiId,
      this.permissions,
      this.lastMessage,
      if (this.positions != null) this.positions.copyOf() else null,
      this.messageSenderId,
      this.blockList,
      this.hasProtectedContent,
      this.isTranslatable,
      this.isMarkedAsUnread,
      this.viewAsTopics,
      this.hasScheduledMessages,
      this.canBeDeletedOnlyForSelf,
      this.canBeDeletedForAllUsers,
      this.canBeReported,
      this.defaultDisableNotification,
      this.unreadCount,
      this.lastReadInboxMessageId,
      this.lastReadOutboxMessageId,
      this.unreadMentionCount,
      this.unreadReactionCount,
      this.notificationSettings,
      this.availableReactions,
      this.messageAutoDeleteTime,
      this.emojiStatus,
      this.background,
      this.themeName,
      this.actionBar,
      this.videoChat,
      this.pendingJoinRequests,
      this.replyMarkupMessageId,
      this.draftMessage,
      this.clientData
    )
  }
}

fun ChatPermissions?.copyOf (): ChatPermissions? {
  return this?.let {
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
      this.canAddWebPagePreviews,
      this.canChangeInfo,
      this.canInviteUsers,
      this.canPinMessages,
      this.canCreateTopics
    )
  }
}

fun StickerSetInfo?.copyOf (): StickerSetInfo? {
  return this?.let {
    StickerSetInfo(
      this.id,
      this.title,
      this.name,
      this.thumbnail,
      this.thumbnailOutline,
      this.isInstalled,
      this.isArchived,
      this.isOfficial,
      this.stickerFormat,
      this.stickerType,
      this.needsRepainting,
      this.isAllowedAsChatEmojiStatus,
      this.isViewed,
      this.size,
      this.covers
    )
  }
}

fun StickerSet?.copyOf (): StickerSet? {
  return this?.let {
    StickerSet(
      this.id,
      this.title,
      this.name,
      this.thumbnail,
      this.thumbnailOutline,
      this.isInstalled,
      this.isArchived,
      this.isOfficial,
      this.stickerFormat,
      this.stickerType,
      this.needsRepainting,
      this.isAllowedAsChatEmojiStatus,
      this.isViewed,
      this.stickers,
      this.emojis
    )
  }
}

fun ChatAdministratorRights?.copyOf (): ChatAdministratorRights? {
  return this?.let {
    ChatAdministratorRights(
      this.canManageChat,
      this.canChangeInfo,
      this.canPostMessages,
      this.canEditMessages,
      this.canDeleteMessages,
      this.canInviteUsers,
      this.canRestrictMembers,
      this.canPinMessages,
      this.canManageTopics,
      this.canPromoteMembers,
      this.canManageVideoChats,
      this.canPostStories,
      this.canEditStories,
      this.canDeleteStories,
      this.isAnonymous
    )
  }
}

fun ChatMemberStatus?.copyOf (): ChatMemberStatus? {
  return this?.let {
    when (constructor) {
      ChatMemberStatusCreator.CONSTRUCTOR -> ChatMemberStatusCreator(
        (this as ChatMemberStatusCreator).customTitle,
        this.isAnonymous,
        this.isMember
      )
      ChatMemberStatusAdministrator.CONSTRUCTOR -> ChatMemberStatusAdministrator(
        (this as ChatMemberStatusAdministrator).customTitle,
        this.canBeEdited,
        this.rights.copyOf()
      )
      ChatMemberStatusBanned.CONSTRUCTOR -> ChatMemberStatusBanned((this as ChatMemberStatusBanned).bannedUntilDate)
      ChatMemberStatusLeft.CONSTRUCTOR -> ChatMemberStatusLeft()
      ChatMemberStatusMember.CONSTRUCTOR -> ChatMemberStatusMember()
      ChatMemberStatusRestricted.CONSTRUCTOR -> ChatMemberStatusRestricted(
        (this as ChatMemberStatusRestricted).isMember,
        this.restrictedUntilDate,
        this.permissions.copyOf()
      )
      else -> {
        assertChatMemberStatus_33fc5755()
        throw unsupported(this)
      }
    }
  }
}

fun LinkPreviewOptions?.copyOf (): LinkPreviewOptions {
  return this?.let {
    LinkPreviewOptions(
      this.isDisabled,
      this.url,
      this.forceSmallMedia,
      this.forceLargeMedia,
      this.showAboveText
    )
  } ?: LinkPreviewOptions(
    false,
    "",
    false,
    false,
    false
  )
}

fun WebPage?.copyOf (): WebPage? {
  return this?.let {
    WebPage(
      this.url,
      this.displayUrl,
      this.type,
      this.siteName,
      this.title,
      this.description,
      this.photo,
      this.embedUrl,
      this.embedType,
      this.embedWidth,
      this.embedHeight,
      this.duration,
      this.author,
      this.hasLargeMedia,
      this.showLargeMedia,
      this.skipConfirmation,
      this.showAboveText,
      this.animation,
      this.audio,
      this.document,
      this.sticker,
      this.video,
      this.videoNote,
      this.voiceNote,
      this.storySenderChatId,
      this.storyId,
      this.instantViewVersion
    )
  }
}

fun SearchChatMessages.copyOf (): SearchChatMessages = SearchChatMessages(
  this.chatId,
  this.query,
  this.senderId,
  this.fromMessageId,
  this.offset,
  this.limit,
  this.filter,
  this.messageThreadId,
  this.savedMessagesTopicId
)

fun SearchSecretMessages.copyOf (): SearchSecretMessages = SearchSecretMessages(
  this.chatId,
  this.query,
  this.offset,
  this.limit,
  this.filter
)