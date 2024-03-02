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

import me.vkryl.core.equalsOrBothEmpty
import org.drinkless.tdlib.TdApi.*
import java.util.*
import kotlin.contracts.ExperimentalContracts

fun ChatList?.equalsTo(b: ChatList?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatListMain.CONSTRUCTOR, ChatListArchive.CONSTRUCTOR -> true
      ChatListFolder.CONSTRUCTOR -> (this as ChatListFolder).chatFolderId == (b as ChatListFolder).chatFolderId
      else -> {
        assertChatList_db6c93ab()
        throw unsupported(this)
      }
    }
  }
}

fun ChatAdministratorRights?.equalsTo(b: ChatAdministratorRights?): Boolean {
  return when {
    this === b -> true
    this === null || b === null -> false
    else -> {
      if (COMPILE_CHECK) {
        // Cause compilation error when any field in TdApi changes
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
      this.canManageChat == b.canManageChat &&
      this.canChangeInfo == b.canChangeInfo &&
      this.canPostMessages == b.canPostMessages &&
      this.canEditMessages == b.canEditMessages &&
      this.canDeleteMessages == b.canDeleteMessages &&
      this.canInviteUsers == b.canInviteUsers &&
      this.canRestrictMembers == b.canRestrictMembers &&
      this.canPinMessages == b.canPinMessages &&
      this.canManageTopics == b.canManageTopics &&
      this.canPromoteMembers == b.canPromoteMembers &&
      this.canManageVideoChats == b.canManageVideoChats &&
      this.canPostStories == b.canPostStories &&
      this.canEditStories == b.canEditStories &&
      this.canDeleteStories == b.canDeleteStories &&
      this.isAnonymous == b.isAnonymous
    }
  }
}

fun ChatMemberStatus.equalsTo(b: ChatMemberStatus): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatMemberStatusMember.CONSTRUCTOR,
      ChatMemberStatusLeft.CONSTRUCTOR -> true

      ChatMemberStatusCreator.CONSTRUCTOR -> {
        require(this is ChatMemberStatusCreator && b is ChatMemberStatusCreator)
        if (COMPILE_CHECK) {
          // Cause compilation error when any field in TdApi changes
          ChatMemberStatusCreator(
            this.customTitle,
            this.isAnonymous,
            this.isMember
          )
        }
        this.isMember == b.isMember &&
        this.isAnonymous == b.isAnonymous &&
        this.customTitle.equalsOrBothEmpty(b.customTitle)
      }

      ChatMemberStatusBanned.CONSTRUCTOR -> {
        require(this is ChatMemberStatusBanned && b is ChatMemberStatusBanned)
        if (COMPILE_CHECK) {
          // Cause compilation error when any field in TdApi changes
          ChatMemberStatusBanned(this.bannedUntilDate)
        }
        this.bannedUntilDate == b.bannedUntilDate
      }

      ChatMemberStatusAdministrator.CONSTRUCTOR -> {
        require(this is ChatMemberStatusAdministrator && b is ChatMemberStatusAdministrator)
        if (COMPILE_CHECK) {
          // Cause compilation error when any field in TdApi changes
          ChatMemberStatusAdministrator(
            this.customTitle,
            this.canBeEdited,
            this.rights
          )
        }
        this.rights.equalsTo(b.rights) &&
        this.customTitle.equalsOrBothEmpty(b.customTitle)
        // ignored: this.canBeEdited == b.canBeEdited
      }

      ChatMemberStatusRestricted.CONSTRUCTOR -> {
        require(this is ChatMemberStatusRestricted && b is ChatMemberStatusRestricted)
        if (COMPILE_CHECK) {
          // Cause compilation error when any field in TdApi changes
          ChatMemberStatusRestricted(
            this.isMember,
            this.restrictedUntilDate,
            this.permissions
          )
        }
        this.isMember == b.isMember &&
        this.restrictedUntilDate == b.restrictedUntilDate &&
        this.permissions.equalsTo(b.permissions)
      }

      else -> {
        assertChatMemberStatus_33fc5755()
        throw unsupported(this)
      }
    }
  }
}

fun ChatPermissions.equalsTo(b: ChatPermissions): Boolean {
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
  return (this === b) || (
    this.canSendBasicMessages == b.canSendBasicMessages &&
    this.canSendAudios == b.canSendAudios &&
    this.canSendDocuments == b.canSendDocuments &&
    this.canSendPhotos == b.canSendPhotos &&
    this.canSendVideos == b.canSendVideos &&
    this.canSendVideoNotes == b.canSendVideoNotes &&
    this.canSendVoiceNotes == b.canSendVoiceNotes &&
    this.canSendPolls == b.canSendPolls &&
    this.canSendOtherMessages == b.canSendOtherMessages &&
    this.canAddWebPagePreviews == b.canAddWebPagePreviews &&
    this.canChangeInfo == b.canChangeInfo &&
    this.canInviteUsers == b.canInviteUsers &&
    this.canPinMessages == b.canPinMessages &&
    this.canCreateTopics == b.canCreateTopics
  )
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
      this.canAddWebPagePreviews,
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
    (this.canAddWebPagePreviews == old.canAddWebPagePreviews || !this.canAddWebPagePreviews && !defaultPermissions.canAddWebPagePreviews) &&
    (this.canChangeInfo == old.canChangeInfo || !this.canChangeInfo && !defaultPermissions.canChangeInfo) &&
    (this.canInviteUsers == old.canInviteUsers || !this.canInviteUsers && !defaultPermissions.canInviteUsers) &&
    (this.canPinMessages == old.canPinMessages || !this.canPinMessages && !defaultPermissions.canPinMessages) &&
    (this.canCreateTopics == old.canCreateTopics || !this.canCreateTopics && !defaultPermissions.canCreateTopics)
  )
}

fun ChatActionBar?.equalsTo(b: ChatActionBar?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatActionBarReportSpam.CONSTRUCTOR -> {
        require(this is ChatActionBarReportSpam && b is ChatActionBarReportSpam)
        this.canUnarchive == b.canUnarchive
      }
      ChatActionBarReportAddBlock.CONSTRUCTOR -> {
        require(this is ChatActionBarReportAddBlock && b is ChatActionBarReportAddBlock)
        this.canUnarchive == b.canUnarchive && this.distance == b.distance
      }
      ChatActionBarJoinRequest.CONSTRUCTOR -> {
        require(this is ChatActionBarJoinRequest && b is ChatActionBarJoinRequest)
        this.title == b.title &&
        this.isChannel == b.isChannel &&
        this.requestDate == b.requestDate
      }
      ChatActionBarReportUnrelatedLocation.CONSTRUCTOR,
      ChatActionBarAddContact.CONSTRUCTOR,
      ChatActionBarSharePhoneNumber.CONSTRUCTOR,
      ChatActionBarInviteMembers.CONSTRUCTOR -> true
      else -> {
        assertChatActionBar_9b96400f()
        throw unsupported(this)
      }
    }
  }
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

@JvmOverloads
fun Array<TextEntity>?.equalsTo(b: Array<TextEntity>?, ignoreDefaultEntities: Boolean = false): Boolean {
  if (this === b) return true
  if (ignoreDefaultEntities) {
    return this.findEssential().equalsTo(b.findEssential())
  }
  if (this.isNullOrEmpty() != b.isNullOrEmpty()) {
    return false
  }
  if (this != null && b != null) {
    if (this.size != b.size) {
      return false
    }
    for (i in this.indices) {
      if (!this[i].equalsTo(b[i])) {
        return false
      }
    }
  }
  return true
}

fun TextEntity?.equalsTo(b: TextEntity?): Boolean {
  return (this === b) || (
    this !== null && b !== null &&
    this.offset == b.offset &&
    this.length == b.length && this.type.equalsTo(b.type)
  )
}

fun TextEntityType?.equalsTo(b: TextEntityType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      TextEntityTypeMentionName.CONSTRUCTOR -> {
        require(this is TextEntityTypeMentionName && b is TextEntityTypeMentionName)
        this.userId == b.userId
      }
      TextEntityTypeTextUrl.CONSTRUCTOR -> {
        require(this is TextEntityTypeTextUrl && b is TextEntityTypeTextUrl)
        this.url.equalsOrBothEmpty(b.url)
      }
      TextEntityTypePreCode.CONSTRUCTOR -> {
        require(this is TextEntityTypePreCode && b is TextEntityTypePreCode)
        this.language.equalsOrBothEmpty(b.language)
      }
      TextEntityTypeMediaTimestamp.CONSTRUCTOR -> {
        require(this is TextEntityTypeMediaTimestamp && b is TextEntityTypeMediaTimestamp)
        this.mediaTimestamp == b.mediaTimestamp
      }
      TextEntityTypeCustomEmoji.CONSTRUCTOR -> {
        require(this is TextEntityTypeCustomEmoji && b is TextEntityTypeCustomEmoji)
        this.customEmojiId == b.customEmojiId
      }
      TextEntityTypeBlockQuote.CONSTRUCTOR,
      TextEntityTypeBankCardNumber.CONSTRUCTOR,
      TextEntityTypeBold.CONSTRUCTOR,
      TextEntityTypeSpoiler.CONSTRUCTOR,
      TextEntityTypeBotCommand.CONSTRUCTOR,
      TextEntityTypeCashtag.CONSTRUCTOR,
      TextEntityTypeCode.CONSTRUCTOR,
      TextEntityTypeEmailAddress.CONSTRUCTOR,
      TextEntityTypeHashtag.CONSTRUCTOR,
      TextEntityTypeItalic.CONSTRUCTOR,
      TextEntityTypeMention.CONSTRUCTOR,
      TextEntityTypePhoneNumber.CONSTRUCTOR,
      TextEntityTypePre.CONSTRUCTOR,
      TextEntityTypeStrikethrough.CONSTRUCTOR,
      TextEntityTypeUnderline.CONSTRUCTOR,
      TextEntityTypeUrl.CONSTRUCTOR ->
        true
      else -> {
        assertTextEntityType_91234a79()
        throw unsupported(this)
      }
    }
  }
}

fun ChatAction.equalsTo(b: ChatAction): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatActionTyping.CONSTRUCTOR,
      ChatActionRecordingVideo.CONSTRUCTOR,
      ChatActionRecordingVoiceNote.CONSTRUCTOR,
      ChatActionChoosingLocation.CONSTRUCTOR,
      ChatActionChoosingContact.CONSTRUCTOR,
      ChatActionChoosingSticker.CONSTRUCTOR,
      ChatActionStartPlayingGame.CONSTRUCTOR,
      ChatActionRecordingVideoNote.CONSTRUCTOR,
      ChatActionCancel.CONSTRUCTOR -> true
      ChatActionUploadingVideo.CONSTRUCTOR -> {
        require(this is ChatActionUploadingVideo && b is ChatActionUploadingVideo)
        this.progress == b.progress
      }
      ChatActionWatchingAnimations.CONSTRUCTOR -> {
        require(this is ChatActionWatchingAnimations && b is ChatActionWatchingAnimations)
        this.emoji == b.emoji
      }
      ChatActionUploadingVoiceNote.CONSTRUCTOR -> {
        require(this is ChatActionUploadingVoiceNote && b is ChatActionUploadingVoiceNote)
        this.progress == b.progress
      }
      ChatActionUploadingPhoto.CONSTRUCTOR -> {
        require(this is ChatActionUploadingPhoto && b is ChatActionUploadingPhoto)
        this.progress == b.progress
      }
      ChatActionUploadingDocument.CONSTRUCTOR -> {
        require(this is ChatActionUploadingDocument && b is ChatActionUploadingDocument)
        this.progress == b.progress
      }
      ChatActionUploadingVideoNote.CONSTRUCTOR -> {
        require(this is ChatActionUploadingVideoNote && b is ChatActionUploadingVideoNote)
        this.progress == b.progress
      }
      else -> {
        assertChatAction_e6a90de7()
        throw unsupported(this)
      }
    }
  }
}

fun ChatSource?.equalsTo(b: ChatSource?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatSourceMtprotoProxy.CONSTRUCTOR -> true
      ChatSourcePublicServiceAnnouncement.CONSTRUCTOR -> {
        require(this is ChatSourcePublicServiceAnnouncement && b is ChatSourcePublicServiceAnnouncement)
        this.type.equalsOrBothEmpty(b.type) && this.text.equalsOrBothEmpty(b.text)
      }
      else -> {
        assertChatSource_12b21238()
        throw unsupported(this)
      }
    }
  }
}

fun UserStatus?.equalsTo(b: UserStatus?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      UserStatusEmpty.CONSTRUCTOR,
      UserStatusLastMonth.CONSTRUCTOR,
      UserStatusLastWeek.CONSTRUCTOR,
      UserStatusRecently.CONSTRUCTOR -> true
      UserStatusOnline.CONSTRUCTOR -> {
        require(this is UserStatusOnline && b is UserStatusOnline)
        this.expires == b.expires
      }
      UserStatusOffline.CONSTRUCTOR -> {
        require(this is UserStatusOffline && b is UserStatusOffline)
        this.wasOnline == b.wasOnline
      }
      else -> {
        assertUserStatus_6492acaf()
        throw unsupported(this)
      }
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

fun LocalFile?.equalsTo(b: LocalFile?): Boolean {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    requireNotNull(this)
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
  return when {
    this === b -> true
    this === null || b === null -> false
    else -> {
      this.downloadedSize == b.downloadedSize &&
      this.downloadOffset == b.downloadOffset &&
      this.downloadedPrefixSize == b.downloadedPrefixSize &&
      this.isDownloadingActive == b.isDownloadingActive &&
      this.isDownloadingCompleted == b.isDownloadingCompleted &&
      this.canBeDeleted == b.canBeDeleted &&
      this.canBeDownloaded == b.canBeDownloaded &&
      this.path.equalsOrBothEmpty(b.path)
    }
  }
}

fun RemoteFile?.equalsTo(b: RemoteFile?): Boolean {
  if (COMPILE_CHECK) {
    // Cause compilation error when any field in TdApi changes
    requireNotNull(this)
    RemoteFile(
      this.id,
      this.uniqueId,
      this.isUploadingActive,
      this.isUploadingCompleted,
      this.uploadedSize
    )
  }
  return when {
    this === b -> true
    this === null || b === null -> false
    else -> {
      this.uploadedSize == b.uploadedSize &&
      this.isUploadingActive == b.isUploadingActive &&
      this.isUploadingCompleted == b.isUploadingCompleted &&
      this.id.equalsOrBothEmpty(b.id) &&
      this.uniqueId.equalsOrBothEmpty(b.uniqueId)
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
      this.forumChanges
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
    (this?.forumChanges ?: false) == (b?.forumChanges ?: false)
  )
}

fun UserPrivacySettingRule.equalsTo(b: UserPrivacySettingRule): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      UserPrivacySettingRuleRestrictChatMembers.CONSTRUCTOR -> {
        require(this is UserPrivacySettingRuleRestrictChatMembers && b is UserPrivacySettingRuleRestrictChatMembers)
        Arrays.equals(this.chatIds, b.chatIds)
      }
      UserPrivacySettingRuleAllowChatMembers.CONSTRUCTOR -> {
        require(this is UserPrivacySettingRuleAllowChatMembers && b is UserPrivacySettingRuleAllowChatMembers)
        Arrays.equals(this.chatIds, b.chatIds)
      }
      UserPrivacySettingRuleAllowUsers.CONSTRUCTOR -> {
        require(this is UserPrivacySettingRuleAllowUsers && b is UserPrivacySettingRuleAllowUsers)
        Arrays.equals(this.userIds, b.userIds)
      }
      UserPrivacySettingRuleRestrictUsers.CONSTRUCTOR -> {
        require(this is UserPrivacySettingRuleRestrictUsers && b is UserPrivacySettingRuleRestrictUsers)
        Arrays.equals(this.userIds, b.userIds)
      }
      UserPrivacySettingRuleAllowAll.CONSTRUCTOR,
      UserPrivacySettingRuleAllowContacts.CONSTRUCTOR,
      UserPrivacySettingRuleRestrictAll.CONSTRUCTOR,
      UserPrivacySettingRuleRestrictContacts.CONSTRUCTOR -> true
      else -> {
        assertUserPrivacySettingRule_9d021ccd()
        throw unsupported(this)
      }
    }
  }
}

fun ProxyType.equalsTo(b: ProxyType): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ProxyTypeSocks5.CONSTRUCTOR -> {
        require(this is ProxyTypeSocks5 && b is ProxyTypeSocks5)
        this.username == b.username &&
        this.password == b.password
      }
      ProxyTypeHttp.CONSTRUCTOR -> {
        require(this is ProxyTypeHttp && b is ProxyTypeHttp)
        this.username == b.username &&
        this.password == b.password &&
        this.httpOnly == b.httpOnly
      }
      ProxyTypeMtproto.CONSTRUCTOR -> {
        require(this is ProxyTypeMtproto && b is ProxyTypeMtproto)
        this.secret == b.secret
      }
      else -> {
        assertProxyType_bc1a1076()
        throw unsupported(this)
      }
    }
  }
}

@OptIn(ExperimentalContracts::class)
fun InternalLinkType.equalsTo(b: InternalLinkType): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      InternalLinkTypeActiveSessions.CONSTRUCTOR,
      InternalLinkTypeChangePhoneNumber.CONSTRUCTOR,
      InternalLinkTypeDefaultMessageAutoDeleteTimerSettings.CONSTRUCTOR,
      InternalLinkTypeEditProfileSettings.CONSTRUCTOR,
      InternalLinkTypeLanguageSettings.CONSTRUCTOR,
      InternalLinkTypeSettings.CONSTRUCTOR,
      InternalLinkTypeRestorePurchases.CONSTRUCTOR,
      InternalLinkTypeThemeSettings.CONSTRUCTOR,
      InternalLinkTypeUnsupportedProxy.CONSTRUCTOR,
      InternalLinkTypeQrCodeAuthentication.CONSTRUCTOR,
      InternalLinkTypePrivacyAndSecuritySettings.CONSTRUCTOR,
      InternalLinkTypeChatFolderSettings.CONSTRUCTOR ->
        true
      InternalLinkTypeAuthenticationCode.CONSTRUCTOR -> {
        require(this is InternalLinkTypeAuthenticationCode && b is InternalLinkTypeAuthenticationCode)
        this.code == b.code
      }
      InternalLinkTypeBotStart.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotStart && b is InternalLinkTypeBotStart)
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.autostart == b.autostart
      }
      InternalLinkTypeBotStartInGroup.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotStartInGroup && b is InternalLinkTypeBotStartInGroup)
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.administratorRights.equalsTo(b.administratorRights)
      }
      InternalLinkTypeBotAddToChannel.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotAddToChannel && b is InternalLinkTypeBotAddToChannel)
        this.botUsername == b.botUsername &&
        this.administratorRights.equalsTo(b.administratorRights)
      }
      InternalLinkTypeAttachmentMenuBot.CONSTRUCTOR -> {
        require(this is InternalLinkTypeAttachmentMenuBot && b is InternalLinkTypeAttachmentMenuBot)
        this.botUsername == b.botUsername &&
        this.url == b.url &&
        this.targetChat.equalsTo(b.targetChat)
      }
      InternalLinkTypeBackground.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBackground && b is InternalLinkTypeBackground)
        this.backgroundName == b.backgroundName
      }
      InternalLinkTypeChatFolderInvite.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatFolderInvite && b is InternalLinkTypeChatFolderInvite)
        this.inviteLink == b.inviteLink
      }
      InternalLinkTypeChatInvite.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatInvite && b is InternalLinkTypeChatInvite)
        this.inviteLink == b.inviteLink
      }
      InternalLinkTypeGame.CONSTRUCTOR -> {
        require(this is InternalLinkTypeGame && b is InternalLinkTypeGame)
        this.botUsername == b.botUsername &&
        this.gameShortName == b.gameShortName
      }
      InternalLinkTypeInstantView.CONSTRUCTOR -> {
        require(this is InternalLinkTypeInstantView && b is InternalLinkTypeInstantView)
        this.url == b.url &&
        this.fallbackUrl == b.fallbackUrl
      }
      InternalLinkTypeInvoice.CONSTRUCTOR -> {
        require(this is InternalLinkTypeInvoice && b is InternalLinkTypeInvoice)
        this.invoiceName == b.invoiceName
      }
      InternalLinkTypeLanguagePack.CONSTRUCTOR -> {
        require(this is InternalLinkTypeLanguagePack && b is InternalLinkTypeLanguagePack)
        this.languagePackId == b.languagePackId
      }
      InternalLinkTypeMessage.CONSTRUCTOR -> {
        require(this is InternalLinkTypeMessage && b is InternalLinkTypeMessage)
        this.url == b.url
      }
      InternalLinkTypeMessageDraft.CONSTRUCTOR -> {
        require(this is InternalLinkTypeMessageDraft && b is InternalLinkTypeMessageDraft)
        this.containsLink == b.containsLink &&
        this.text.equalsTo(b.text)
      }
      InternalLinkTypePremiumFeatures.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumFeatures && b is InternalLinkTypePremiumFeatures)
        this.referrer == b.referrer
      }
      InternalLinkTypePremiumGiftCode.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumGiftCode && b is InternalLinkTypePremiumGiftCode)
        this.code == b.code
      }
      InternalLinkTypeUserToken.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUserToken && b is InternalLinkTypeUserToken)
        this.token == b.token
      }
      InternalLinkTypeStickerSet.CONSTRUCTOR -> {
        require(this is InternalLinkTypeStickerSet && b is InternalLinkTypeStickerSet)
        this.stickerSetName == b.stickerSetName &&
        this.expectCustomEmoji == b.expectCustomEmoji
      }
      InternalLinkTypePassportDataRequest.CONSTRUCTOR -> {
        require(this is InternalLinkTypePassportDataRequest && b is InternalLinkTypePassportDataRequest)
        this.botUserId == b.botUserId &&
        this.scope == b.scope &&
        this.publicKey == b.publicKey &&
        this.nonce == b.nonce &&
        this.callbackUrl == b.callbackUrl
      }
      InternalLinkTypePhoneNumberConfirmation.CONSTRUCTOR -> {
        require(this is InternalLinkTypePhoneNumberConfirmation && b is InternalLinkTypePhoneNumberConfirmation)
        this.hash == b.hash &&
        this.phoneNumber == b.phoneNumber
      }
      InternalLinkTypeProxy.CONSTRUCTOR -> {
        require(this is InternalLinkTypeProxy && b is InternalLinkTypeProxy)
        this.server == b.server &&
        this.port == b.port &&
        this.type.equalsTo(b.type)
      }
      InternalLinkTypePublicChat.CONSTRUCTOR -> {
        require(this is InternalLinkTypePublicChat && b is InternalLinkTypePublicChat)
        this.chatUsername == b.chatUsername
      }
      InternalLinkTypeTheme.CONSTRUCTOR -> {
        require(this is InternalLinkTypeTheme && b is InternalLinkTypeTheme)
        this.themeName == b.themeName
      }
      InternalLinkTypeUnknownDeepLink.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUnknownDeepLink && b is InternalLinkTypeUnknownDeepLink)
        this.link == b.link
      }
      InternalLinkTypeUserPhoneNumber.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUserPhoneNumber && b is InternalLinkTypeUserPhoneNumber)
        this.phoneNumber == b.phoneNumber
      }
      InternalLinkTypeVideoChat.CONSTRUCTOR -> {
        require(this is InternalLinkTypeVideoChat && b is InternalLinkTypeVideoChat)
        this.chatUsername == b.chatUsername &&
        this.isLiveStream == b.isLiveStream &&
        this.inviteHash == b.inviteHash
      }
      InternalLinkTypeWebApp.CONSTRUCTOR -> {
        require(this is InternalLinkTypeWebApp && b is InternalLinkTypeWebApp)
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.webAppShortName == b.webAppShortName
      }
      InternalLinkTypeChatBoost.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatBoost && b is InternalLinkTypeChatBoost)
        this.url == b.url
      }
      InternalLinkTypeSideMenuBot.CONSTRUCTOR -> {
        require(this is InternalLinkTypeSideMenuBot && b is InternalLinkTypeSideMenuBot)
        this.url == b.url &&
        this.botUsername == b.botUsername
      }
      InternalLinkTypeStory.CONSTRUCTOR -> {
        require(this is InternalLinkTypeStory && b is InternalLinkTypeStory)
        this.storySenderUsername == b.storySenderUsername && this.storyId == b.storyId
      }
      InternalLinkTypePremiumGift.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumGift && b is InternalLinkTypePremiumGift)
        this.referrer == b.referrer
      }
      else -> {
        assertInternalLinkType_18c73626()
        throw unsupported(this)
      }
    }
  }
}

fun TargetChat.equalsTo(b: TargetChat): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      TargetChatCurrent.CONSTRUCTOR -> true
      TargetChatChosen.CONSTRUCTOR -> {
        require(this is TargetChatChosen && b is TargetChatChosen)
        this.allowUserChats == b.allowUserChats &&
        this.allowBotChats == b.allowBotChats &&
        this.allowGroupChats == b.allowGroupChats &&
        this.allowChannelChats == b.allowChannelChats
      }
      TargetChatInternalLink.CONSTRUCTOR -> {
        require(this is TargetChatInternalLink && b is TargetChatInternalLink)
        this.link.equalsTo(b.link)
      }
      else -> {
        assertTargetChat_75ff347c()
        throw unsupported(this)
      }
    }
  }
}

fun InlineKeyboardButton.equalsTo(b: InlineKeyboardButton): Boolean {
  return (this === b) || (this.text == b.text && this.type.equalsTo(b.type))
}

fun InlineKeyboardButtonType.equalsTo(b: InlineKeyboardButtonType): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      InlineKeyboardButtonTypeUrl.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeUrl && b is InlineKeyboardButtonTypeUrl)
        this.url == b.url
      }
      InlineKeyboardButtonTypeLoginUrl.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeLoginUrl && b is InlineKeyboardButtonTypeLoginUrl)
        this.id == b.id && this.url == b.url && this.forwardText == b.forwardText
      }
      InlineKeyboardButtonTypeCallback.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeCallback && b is InlineKeyboardButtonTypeCallback)
        Arrays.equals(this.data, b.data)
      }
      InlineKeyboardButtonTypeCallbackWithPassword.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeCallbackWithPassword && b is InlineKeyboardButtonTypeCallbackWithPassword)
        Arrays.equals(this.data, b.data)
      }
      InlineKeyboardButtonTypeSwitchInline.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeSwitchInline && b is InlineKeyboardButtonTypeSwitchInline)
        this.query == b.query && this.targetChat.equalsTo(b.targetChat)
      }
      InlineKeyboardButtonTypeUser.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeUser && b is InlineKeyboardButtonTypeUser)
        this.userId == b.userId
      }
      InlineKeyboardButtonTypeWebApp.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeWebApp && b is InlineKeyboardButtonTypeWebApp)
        this.url == b.url
      }
      InlineKeyboardButtonTypeCallbackGame.CONSTRUCTOR,
      InlineKeyboardButtonTypeBuy.CONSTRUCTOR -> true
      else -> {
        assertInlineKeyboardButtonType_4cba1cc1()
        throw unsupported(this)
      }
    }
  }
}

fun KeyboardButton.equalsTo(b: KeyboardButton): Boolean {
  return (this === b) || (this.text == b.text && this.type.equalsTo(b.type))
}

fun KeyboardButtonType.equalsTo(b: KeyboardButtonType): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      KeyboardButtonTypeRequestPoll.CONSTRUCTOR -> {
        require(this is KeyboardButtonTypeRequestPoll && b is KeyboardButtonTypeRequestPoll)
        this.forceQuiz == b.forceQuiz &&
        this.forceRegular == b.forceRegular
      }
      KeyboardButtonTypeRequestUsers.CONSTRUCTOR -> {
        require(this is KeyboardButtonTypeRequestUsers && b is KeyboardButtonTypeRequestUsers)
        this.id == b.id &&
        this.restrictUserIsBot == b.restrictUserIsBot &&
        this.restrictUserIsPremium == b.restrictUserIsPremium &&
        this.userIsPremium == b.userIsPremium &&
        this.maxQuantity == b.maxQuantity
      }
      KeyboardButtonTypeRequestChat.CONSTRUCTOR -> {
        require(this is KeyboardButtonTypeRequestChat && b is KeyboardButtonTypeRequestChat)
        this.id == b.id &&
        this.chatIsChannel == b.chatIsChannel &&
        this.restrictChatIsForum == b.restrictChatIsForum &&
        this.chatIsForum == b.chatIsForum &&
        this.restrictChatHasUsername == b.restrictChatHasUsername &&
        this.chatIsCreated == b.chatIsCreated &&
        this.userAdministratorRights.equalsTo(b.userAdministratorRights) &&
        this.botAdministratorRights.equalsTo(b.botAdministratorRights) &&
        this.botIsMember == b.botIsMember
      }
      KeyboardButtonTypeWebApp.CONSTRUCTOR -> {
        require(this is KeyboardButtonTypeWebApp && b is KeyboardButtonTypeWebApp)
        this.url == b.url
      }
      KeyboardButtonTypeText.CONSTRUCTOR,
      KeyboardButtonTypeRequestPhoneNumber.CONSTRUCTOR,
      KeyboardButtonTypeRequestLocation.CONSTRUCTOR -> true
      else -> {
        assertKeyboardButtonType_b955a01e()
        throw unsupported(this)
      }
    }
  }
}

fun ReplyMarkup?.equalsTo(b: ReplyMarkup?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ReplyMarkupRemoveKeyboard.CONSTRUCTOR -> {
        require(this is ReplyMarkupRemoveKeyboard && b is ReplyMarkupRemoveKeyboard)
        this.isPersonal == b.isPersonal
      }
      ReplyMarkupForceReply.CONSTRUCTOR -> {
        require(this is ReplyMarkupForceReply && b is ReplyMarkupForceReply)
        this.isPersonal == b.isPersonal
      }
      ReplyMarkupShowKeyboard.CONSTRUCTOR -> {
        require(this is ReplyMarkupShowKeyboard && b is ReplyMarkupShowKeyboard)
        if (this.isPersonal != b.isPersonal ||
          this.oneTime != b.oneTime ||
          this.resizeKeyboard != b.resizeKeyboard ||
          (this.rows?.size ?: 0) != (b.rows?.size)) {
          return false
        }
        this.rows?.let {
          for (i in this.rows.indices) {
            if ((this.rows[i]?.size ?: 0) != (b.rows[i]?.size ?: 0)) {
              return false
            }
            this.rows[i]?.let {
              for (j in this.rows[i].indices) {
                if (!this.rows[i][j].equalsTo(b.rows[i][j])) {
                  return false
                }
              }
            }
          }
        }
        true
      }
      ReplyMarkupInlineKeyboard.CONSTRUCTOR -> {
        require(this is ReplyMarkupInlineKeyboard && b is ReplyMarkupInlineKeyboard)
        if ((this.rows?.size ?: 0) != (b.rows?.size ?: 0)) {
          return false
        }
        this.rows?.let {
          for (i in this.rows.indices) {
            if ((this.rows[i]?.size ?: 0) != (b.rows[i]?.size ?: 0)) {
              return false
            }
            this.rows[i]?.let {
              for (j in this.rows[i].indices) {
                if (!this.rows[i][j].equalsTo(b.rows[i][j])) {
                  return false
                }
              }
            }
          }
        }
        true
      }
      else -> {
        assertReplyMarkup_d6ebcdbe()
        throw unsupported(this)
      }
    }
  }
}

fun PhotoSize?.equalsTo(b: PhotoSize?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      this.type == b.type &&
      this.photo.equalsTo(b.photo)
    }
  }
}

@JvmOverloads fun Minithumbnail?.equalsTo(b: Minithumbnail?, checkJpegBytes: Boolean = false): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      (!checkJpegBytes || Arrays.equals(this.data, b.data))
    }
  }
}

fun Thumbnail?.equalsTo(b: Thumbnail?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      this.file.equalsTo(b.file) &&
      this.format.equalsTo(b.format)
    }
  }
}

fun Usernames?.equalsTo(b: Usernames?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.editableUsername == b.editableUsername &&
      Arrays.equals(this.activeUsernames, b.activeUsernames) &&
      Arrays.equals(this.disabledUsernames, b.disabledUsernames)
    }
  }
}

fun ThumbnailFormat.equalsTo(b: ThumbnailFormat): Boolean {
  return this.constructor == b.constructor
}

fun Array<PhotoSize>?.equalsTo(b: Array<PhotoSize>?): Boolean {
  return when {
    this === b -> true
    this.isNullOrEmpty() || b.isNullOrEmpty() -> this.isNullOrEmpty() == b.isNullOrEmpty()
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
      if (checkWaveformBytes) Arrays.equals(this.waveform, b.waveform) else (this.waveform?.size ?: 0) == (b.waveform?.size ?: 0)
    }
  }
}

fun VideoNote?.equalsTo(b: VideoNote?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.duration == b.duration &&
      this.length == b.length &&
      this.video.equalsTo(b.video) &&
      this.thumbnail.equalsTo(b.thumbnail) &&
      this.minithumbnail.equalsTo(b.minithumbnail)
    }
  }
}

fun Video?.equalsTo(b: Video?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      this.duration == b.duration &&
      this.supportsStreaming == b.supportsStreaming &&
      this.hasStickers == b.hasStickers &&
      this.video.equalsTo(b.video) &&
      this.fileName.equalsOrBothEmpty(b.fileName) &&
      this.mimeType.equalsOrBothEmpty(b.mimeType) &&
      this.thumbnail.equalsTo(b.thumbnail) &&
      this.minithumbnail.equalsTo(b.minithumbnail)
    }
  }
}

fun Audio?.equalsTo(b: Audio?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.duration == b.duration &&
      this.title.equalsOrBothEmpty(b.title) &&
      this.performer.equalsOrBothEmpty(b.performer) &&
      this.mimeType.equalsOrBothEmpty(b.mimeType) &&
      this.fileName.equalsOrBothEmpty(b.fileName) &&
      this.audio.equalsTo(b.audio) &&
      this.albumCoverThumbnail.equalsTo(b.albumCoverThumbnail) &&
      this.albumCoverMinithumbnail.equalsTo(b.albumCoverMinithumbnail)
    }
  }
}

fun Photo?.equalsTo(b: Photo?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.hasStickers == b.hasStickers &&
      this.minithumbnail.equalsTo(b.minithumbnail) &&
      this.sizes.equalsTo(b.sizes)
    }
  }
}

fun Animation?.equalsTo(b: Animation?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.width == b.width &&
      this.height == b.height &&
      this.duration == b.duration &&
      this.hasStickers == b.hasStickers &&
      this.animation.equalsTo(b.animation) &&
      this.mimeType.equalsOrBothEmpty(b.mimeType) &&
      this.fileName.equalsOrBothEmpty(b.fileName) &&
      this.minithumbnail.equalsTo(b.minithumbnail) &&
      this.thumbnail.equalsTo(b.thumbnail)
    }
  }
}

fun Document?.equalsTo(b: Document?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.document.equalsTo(b.document) &&
      this.mimeType.equalsOrBothEmpty(b.mimeType) &&
      this.fileName.equalsOrBothEmpty(b.fileName) &&
      this.minithumbnail.equalsTo(b.minithumbnail) &&
      this.thumbnail.equalsTo(b.thumbnail)
    }
  }
}

fun Sticker?.equalsTo(b: Sticker?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.id == id &&
      this.setId == setId &&
      this.width == width &&
      this.height == height &&
      this.emoji.equalsOrBothEmpty(b.emoji) &&
      this.format.equalsTo(b.format) &&
      this.fullType.equalsTo(b.fullType) &&
      this.outline.equalsTo(outline) &&
      this.thumbnail.equalsTo(b.thumbnail) &&
      this.sticker.equalsTo(b.sticker)
    }
  }
}

fun StickerFullType?.equalsTo(b: StickerFullType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      StickerFullTypeRegular.CONSTRUCTOR -> {
        require(this is StickerFullTypeRegular && b is StickerFullTypeRegular)
        this.premiumAnimation.equalsTo(b.premiumAnimation)
      }
      StickerFullTypeMask.CONSTRUCTOR -> {
        require(this is StickerFullTypeMask && b is StickerFullTypeMask)
        this.maskPosition.equalsTo(b.maskPosition)
      }
      StickerFullTypeCustomEmoji.CONSTRUCTOR -> {
        require(this is StickerFullTypeCustomEmoji && b is StickerFullTypeCustomEmoji)
        this.customEmojiId == b.customEmojiId &&
        this.needsRepainting == b.needsRepainting
      }
      else -> {
        assertStickerFullType_466eed9d()
        throw unsupported(this)
      }
    }
  }
}

fun StickerFormat?.equalsTo(b: StickerFormat?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      StickerFormatWebp.CONSTRUCTOR,
      StickerFormatTgs.CONSTRUCTOR,
      StickerFormatWebm.CONSTRUCTOR -> true
      else -> {
        assertStickerFormat_4fea4648()
        throw unsupported(this)
      }
    }
  }
}

fun StickerType?.equalsTo(b: StickerType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      StickerTypeRegular.CONSTRUCTOR,
      StickerTypeMask.CONSTRUCTOR,
      StickerTypeCustomEmoji.CONSTRUCTOR -> true
      else -> {
        assertStickerType_cc811bb7()
        throw unsupported(this)
      }
    }
  }
}

fun Array<ClosedVectorPath>?.equalsTo(b: Array<ClosedVectorPath>?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.size != b.size -> false
    else -> {
      this.forEachIndexed { index, path ->
        if (!path.equalsTo(b[index])) {
          return false
        }
      }
      true
    }
  }
}

fun ClosedVectorPath?.equalsTo(b: ClosedVectorPath?): Boolean {
  return this === b || this?.commands.equalsTo(b?.commands)
}

fun Array<VectorPathCommand>?.equalsTo(b: Array<VectorPathCommand>?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.size != b.size -> false
    else -> {
      this.forEachIndexed { index, command ->
        if (!command.equalsTo(b[index])) {
          return false
        }
      }
      true
    }
  }
}

fun VectorPathCommand?.equalsTo(b: VectorPathCommand?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> when(this.constructor) {
      VectorPathCommandLine.CONSTRUCTOR -> (this as VectorPathCommandLine).endPoint.equalsTo((b as VectorPathCommandLine).endPoint)
      VectorPathCommandCubicBezierCurve.CONSTRUCTOR -> {
        (this as VectorPathCommandCubicBezierCurve).endPoint.equalsTo((b as VectorPathCommandCubicBezierCurve).endPoint) &&
          this.startControlPoint.equalsTo(b.startControlPoint) &&
          this.endControlPoint.equalsTo(b.endControlPoint)
      }
      else -> {
        assertVectorPathCommand_4e60caf3()
        throw unsupported(this)
      }
    }
  }
}

fun Point?.equalsTo(b: Point?): Boolean {
  return this === b || this?.x == b?.x && this?.y == b?.y
}

fun MaskPosition?.equalsTo(b: MaskPosition?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.scale == b.scale &&
      this.xShift == b.xShift &&
      this.yShift == b.yShift &&
      this.point.equalsTo(b.point)
    }
  }
}

fun MaskPoint?.equalsTo(b: MaskPoint?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      MaskPointForehead.CONSTRUCTOR,
      MaskPointEyes.CONSTRUCTOR,
      MaskPointMouth.CONSTRUCTOR,
      MaskPointChin.CONSTRUCTOR -> true
      else -> {
        assertMaskPoint_40914d4e()
        throw unsupported(this)
      }
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
        this.chatId == b.chatId &&
        this.messageId == b.messageId &&
        this.quote.equalsTo(b.quote, true)
      }
      InputMessageReplyToStory.CONSTRUCTOR -> {
        require(this is InputMessageReplyToStory && b is InputMessageReplyToStory)
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
fun WebPage?.equalsTo(b: WebPage?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.instantViewVersion == b.instantViewVersion &&
      this.duration == b.duration &&
      this.embedWidth == b.embedWidth &&
      this.embedHeight == b.embedHeight &&
      this.embedType.equalsOrBothEmpty(b.embedType) &&
      this.embedUrl.equalsOrBothEmpty(b.embedUrl) &&
      this.type.equalsOrBothEmpty(b.type) &&
      this.url.equalsOrBothEmpty(b.url) &&
      this.displayUrl.equalsOrBothEmpty(b.displayUrl) &&
      this.siteName.equalsOrBothEmpty(b.siteName) &&
      this.title.equalsOrBothEmpty(b.title) &&
      this.description.equalsTo(b.description) &&
      this.author.equalsOrBothEmpty(b.author) &&
      this.photo.equalsTo(b.photo) &&
      this.animation.equalsTo(b.animation) &&
      this.audio.equalsTo(b.audio) &&
      this.document.equalsTo(b.document) &&
      this.video.equalsTo(b.video) &&
      this.videoNote.equalsTo(b.videoNote) &&
      this.voiceNote.equalsTo(b.voiceNote) &&
      this.sticker.equalsTo(b.sticker)
    }
  }
}

fun MessageSender?.equalsTo(b: MessageSender?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        MessageSenderChat.CONSTRUCTOR -> (this as MessageSenderChat).chatId == (b as MessageSenderChat).chatId
        MessageSenderUser.CONSTRUCTOR -> (this as MessageSenderUser).userId == (b as MessageSenderUser).userId
        else -> {
          assertMessageSender_439d4c9c()
          throw unsupported(this)
        }
      }
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

fun BackgroundFill?.equalsTo(b: BackgroundFill?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        BackgroundFillSolid.CONSTRUCTOR -> {
          require(this is BackgroundFillSolid && b is BackgroundFillSolid)
          this.color == b.color
        }
        BackgroundFillGradient.CONSTRUCTOR -> {
          require(this is BackgroundFillGradient && b is BackgroundFillGradient)
          this.topColor == b.topColor && this.bottomColor == b.bottomColor && this.rotationAngle == b.rotationAngle
        }
        BackgroundFillFreeformGradient.CONSTRUCTOR -> {
          require(this is BackgroundFillFreeformGradient && b is BackgroundFillFreeformGradient)
          Arrays.equals(this.colors, b.colors)
        }
        else -> {
          assertBackgroundFill_6086fe10()
          throw unsupported(this)
        }
      }
    }
  }
}

fun ReactionType?.equalsTo(b: ReactionType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        ReactionTypeEmoji.CONSTRUCTOR -> {
          require(this is ReactionTypeEmoji && b is ReactionTypeEmoji)
          this.emoji == b.emoji
        }
        ReactionTypeCustomEmoji.CONSTRUCTOR -> {
          require(this is ReactionTypeCustomEmoji && b is ReactionTypeCustomEmoji)
          this.customEmojiId == b.customEmojiId
        }
        else -> {
          assertReactionType_7dcca074()
          throw unsupported(this)
        }
      }
    }
  }
}

fun ChatFolderIcon?.equalsTo(b: ChatFolderIcon?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> this.name.equalsOrBothEmpty(b.name)
  }
}

fun DeviceToken?.equalsTo(b: DeviceToken?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        DeviceTokenFirebaseCloudMessaging.CONSTRUCTOR -> {
          require(this is DeviceTokenFirebaseCloudMessaging && b is DeviceTokenFirebaseCloudMessaging)
          this.token == b.token && this.encrypt == b.encrypt
        }
        DeviceTokenApplePush.CONSTRUCTOR -> {
          require(this is DeviceTokenApplePush && b is DeviceTokenApplePush)
          this.deviceToken == b.deviceToken && this.isAppSandbox == b.isAppSandbox
        }
        DeviceTokenApplePushVoIP.CONSTRUCTOR -> {
          require(this is DeviceTokenApplePushVoIP && b is DeviceTokenApplePushVoIP)
          this.deviceToken == b.deviceToken && this.isAppSandbox == b.isAppSandbox && this.encrypt == b.encrypt
        }
        DeviceTokenWindowsPush.CONSTRUCTOR -> {
          require(this is DeviceTokenWindowsPush && b is DeviceTokenWindowsPush)
          this.accessToken == b.accessToken
        }
        DeviceTokenMicrosoftPushVoIP.CONSTRUCTOR -> {
          require(this is DeviceTokenMicrosoftPushVoIP && b is DeviceTokenMicrosoftPushVoIP)
          this.channelUri == b.channelUri
        }
        DeviceTokenWebPush.CONSTRUCTOR -> {
          require(this is DeviceTokenWebPush && b is DeviceTokenWebPush)
          this.endpoint == b.endpoint && this.authBase64url == b.authBase64url && this.p256dhBase64url == b.p256dhBase64url
        }
        DeviceTokenSimplePush.CONSTRUCTOR -> {
          require(this is DeviceTokenSimplePush && b is DeviceTokenSimplePush)
          this.endpoint == b.endpoint
        }
        DeviceTokenUbuntuPush.CONSTRUCTOR -> {
          require(this is DeviceTokenUbuntuPush && b is DeviceTokenUbuntuPush)
          this.token == b.token
        }
        DeviceTokenBlackBerryPush.CONSTRUCTOR -> {
          require(this is DeviceTokenBlackBerryPush && b is DeviceTokenBlackBerryPush)
          this.token == b.token
        }
        DeviceTokenTizenPush.CONSTRUCTOR -> {
          require(this is DeviceTokenTizenPush && b is DeviceTokenTizenPush)
          this.regId == b.regId
        }
        else -> {
          assertDeviceToken_de4a4f61()
          throw unsupported(this)
        }
      }
    }
  }
}

fun StoryList?.equalsTo(b: StoryList?): Boolean {
  return this === b || (this != null && b != null && this.constructor == b.constructor)
}

fun MessageSelfDestructType?.equalsTo(b: MessageSelfDestructType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        MessageSelfDestructTypeImmediately.CONSTRUCTOR -> true
        MessageSelfDestructTypeTimer.CONSTRUCTOR -> {
          require(this is MessageSelfDestructTypeTimer && b is MessageSelfDestructTypeTimer)
          this.selfDestructTime == b.selfDestructTime
        }
        else -> {
          assertMessageSelfDestructType_58882d8c()
          throw unsupported(this)
        }
      }
    }
  }
}

fun MessageReplyTo?.equalsTo(b: MessageReplyTo?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        MessageReplyToMessage.CONSTRUCTOR -> {
          require(this is MessageReplyToMessage && b is MessageReplyToMessage)
          this.chatId == b.chatId && this.messageId == b.messageId
        }
        MessageReplyToStory.CONSTRUCTOR -> {
          require(this is MessageReplyToStory && b is MessageReplyToStory)
          this.storySenderChatId == b.storySenderChatId && this.storyId == b.storyId
        }
        else -> throw unsupported(this)
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

fun AccentColor?.equalsTo(b: AccentColor?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.id == b.id &&
      this.lightThemeColors.contentEquals(b.lightThemeColors) &&
      this.darkThemeColors.contentEquals(b.darkThemeColors)
    }
  }
}

fun ProfileAccentColor?.equalsTo(b: ProfileAccentColor?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.id == b.id &&
      this.lightThemeColors.equalsTo(b.lightThemeColors) &&
      this.darkThemeColors.equalsTo(b.darkThemeColors)
    }
  }
}

fun ProfileAccentColors?.equalsTo(b: ProfileAccentColors?): Boolean {
  return when {
    this === b -> true
    this == null || b == null -> false
    else -> {
      this.paletteColors.contentEquals(b.paletteColors) &&
      this.backgroundColors.contentEquals(b.backgroundColors) &&
      this.storyColors.contentEquals(b.storyColors)
    }
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

fun SuggestedAction?.equalsTo(b: SuggestedAction?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      SuggestedActionConvertToBroadcastGroup.CONSTRUCTOR -> {
        require(this is SuggestedActionConvertToBroadcastGroup && b is SuggestedActionConvertToBroadcastGroup)
        if (COMPILE_CHECK) {
          SuggestedActionConvertToBroadcastGroup(this.supergroupId)
        }
        this.supergroupId == b.supergroupId
      }
      SuggestedActionSetPassword.CONSTRUCTOR -> {
        require(this is SuggestedActionSetPassword && b is SuggestedActionSetPassword)
        if (COMPILE_CHECK) {
          SuggestedActionSetPassword(this.authorizationDelay)
        }
        this.authorizationDelay == b.authorizationDelay
      }
      SuggestedActionEnableArchiveAndMuteNewChats.CONSTRUCTOR,
      SuggestedActionCheckPassword.CONSTRUCTOR,
      SuggestedActionCheckPhoneNumber.CONSTRUCTOR,
      SuggestedActionViewChecksHint.CONSTRUCTOR,
      SuggestedActionUpgradePremium.CONSTRUCTOR,
      SuggestedActionRestorePremium.CONSTRUCTOR,
      SuggestedActionSubscribeToAnnualPremium.CONSTRUCTOR,
      SuggestedActionGiftPremiumForChristmas.CONSTRUCTOR -> true
      else -> {
        assertSuggestedAction_45d3774f()
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