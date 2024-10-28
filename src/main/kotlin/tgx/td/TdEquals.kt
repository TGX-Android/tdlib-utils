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
      this.canAddLinkPreviews,
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
    this.canAddLinkPreviews == b.canAddLinkPreviews &&
    this.canChangeInfo == b.canChangeInfo &&
    this.canInviteUsers == b.canInviteUsers &&
    this.canPinMessages == b.canPinMessages &&
    this.canCreateTopics == b.canCreateTopics
  )
}

@OptIn(ExperimentalContracts::class)
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

fun Array<PollOption>.equalsTo (b: Array<PollOption>, onlyTextContent: Boolean = false): Boolean {
  if (this === b) return true
  if (this.size != b.size) return false
  this.forEachIndexed { index, option ->
    val bOption = b[index]
    if (!option.equalsTo(bOption, onlyTextContent)) {
      return false
    }
  }
  return true
}

@OptIn(ExperimentalContracts::class)
fun PollType.equalsTo(b: PollType): Boolean {
  return when {
    this === b -> true
    this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      PollTypeRegular.CONSTRUCTOR -> {
        require(this is PollTypeRegular && b is PollTypeRegular)
        this.allowMultipleAnswers == b.allowMultipleAnswers
      }
      PollTypeQuiz.CONSTRUCTOR -> {
        require(this is PollTypeQuiz && b is PollTypeQuiz)
        this.explanation.equalsTo(b.explanation) &&
        this.correctOptionId == b.correctOptionId
      }
      else -> {
        assertPollType_324514f9()
        throw unsupported(this)
      }
    }
  }
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

fun CanSendMessageToUserResult?.equalsTo(b: CanSendMessageToUserResult?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      CanSendMessageToUserResultUserRestrictsNewChats.CONSTRUCTOR,
      CanSendMessageToUserResultUserIsDeleted.CONSTRUCTOR,
      CanSendMessageToUserResultOk.CONSTRUCTOR -> true
      else -> {
        assertCanSendMessageToUserResult_3ce8a048()
        throw unsupported(this)
      }
    }
  }
}

fun ChatActionBar?.equalsTo(b: ChatActionBar?): Boolean {
  return when {
    this === b -> true
    this === null || b === null || this.constructor != b.constructor -> false
    else -> when (this.constructor) {
      ChatActionBarReportSpam.CONSTRUCTOR -> {
        require(this is ChatActionBarReportSpam && b is ChatActionBarReportSpam)
        if (COMPILE_CHECK) {
          ChatActionBarReportSpam(
            this.canUnarchive
          )
        }
        this.canUnarchive == b.canUnarchive
      }
      ChatActionBarReportAddBlock.CONSTRUCTOR -> {
        require(this is ChatActionBarReportAddBlock && b is ChatActionBarReportAddBlock)
        if (COMPILE_CHECK) {
          ChatActionBarReportAddBlock(
            this.canUnarchive
          )
        }
        this.canUnarchive == b.canUnarchive
      }
      ChatActionBarJoinRequest.CONSTRUCTOR -> {
        require(this is ChatActionBarJoinRequest && b is ChatActionBarJoinRequest)
        if (COMPILE_CHECK) {
          ChatActionBarJoinRequest(
            this.title,
            this.isChannel,
            this.requestDate
          )
        }
        this.title == b.title &&
        this.isChannel == b.isChannel &&
        this.requestDate == b.requestDate
      }
      ChatActionBarAddContact.CONSTRUCTOR,
      ChatActionBarSharePhoneNumber.CONSTRUCTOR,
      ChatActionBarInviteMembers.CONSTRUCTOR -> true
      else -> {
        assertChatActionBar_eedc82ed()
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
      TextEntityTypeExpandableBlockQuote.CONSTRUCTOR,
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
        assertTextEntityType_56c1e709()
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
      UserPrivacySettingRuleAllowPremiumUsers.CONSTRUCTOR,
      UserPrivacySettingRuleAllowContacts.CONSTRUCTOR,
      UserPrivacySettingRuleRestrictAll.CONSTRUCTOR,
      UserPrivacySettingRuleRestrictContacts.CONSTRUCTOR -> true
      else -> {
        assertUserPrivacySettingRule_c58ead3c()
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
        if (COMPILE_CHECK) {
          InternalLinkTypeAuthenticationCode(
            this.code
          )
        }
        this.code == b.code
      }
      InternalLinkTypeBotStart.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotStart && b is InternalLinkTypeBotStart)
        if (COMPILE_CHECK) {
          InternalLinkTypeBotStart(
            this.botUsername,
            this.startParameter,
            this.autostart
          )
        }
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.autostart == b.autostart
      }
      InternalLinkTypeBotStartInGroup.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotStartInGroup && b is InternalLinkTypeBotStartInGroup)
        if (COMPILE_CHECK) {
          InternalLinkTypeBotStartInGroup(
            this.botUsername,
            this.startParameter,
            this.administratorRights
          )
        }
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.administratorRights.equalsTo(b.administratorRights)
      }
      InternalLinkTypeBotAddToChannel.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBotAddToChannel && b is InternalLinkTypeBotAddToChannel)
        if (COMPILE_CHECK) {
          InternalLinkTypeBotAddToChannel(
            this.botUsername,
            this.administratorRights
          )
        }
        this.botUsername == b.botUsername &&
        this.administratorRights.equalsTo(b.administratorRights)
      }
      InternalLinkTypeAttachmentMenuBot.CONSTRUCTOR -> {
        require(this is InternalLinkTypeAttachmentMenuBot && b is InternalLinkTypeAttachmentMenuBot)
        if (COMPILE_CHECK) {
          InternalLinkTypeAttachmentMenuBot(
            this.targetChat,
            this.botUsername,
            this.url
          )
        }
        this.targetChat.equalsTo(b.targetChat) &&
        this.botUsername == b.botUsername &&
        this.url == b.url
      }
      InternalLinkTypeBackground.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBackground && b is InternalLinkTypeBackground)
        if (COMPILE_CHECK) {
          InternalLinkTypeBackground(
            this.backgroundName
          )
        }
        this.backgroundName == b.backgroundName
      }
      InternalLinkTypeChatFolderInvite.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatFolderInvite && b is InternalLinkTypeChatFolderInvite)
        if (COMPILE_CHECK) {
          InternalLinkTypeChatFolderInvite(
            this.inviteLink
          )
        }
        this.inviteLink == b.inviteLink
      }
      InternalLinkTypeChatInvite.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatInvite && b is InternalLinkTypeChatInvite)
        if (COMPILE_CHECK) {
          InternalLinkTypeChatInvite(
            this.inviteLink
          )
        }
        this.inviteLink == b.inviteLink
      }
      InternalLinkTypeGame.CONSTRUCTOR -> {
        require(this is InternalLinkTypeGame && b is InternalLinkTypeGame)
        if (COMPILE_CHECK) {
          InternalLinkTypeGame(
            this.botUsername,
            this.gameShortName
          )
        }
        this.botUsername == b.botUsername &&
        this.gameShortName == b.gameShortName
      }
      InternalLinkTypeInstantView.CONSTRUCTOR -> {
        require(this is InternalLinkTypeInstantView && b is InternalLinkTypeInstantView)
        if (COMPILE_CHECK) {
          InternalLinkTypeInstantView(
            this.url,
            this.fallbackUrl
          )
        }
        this.url == b.url &&
        this.fallbackUrl == b.fallbackUrl
      }
      InternalLinkTypeInvoice.CONSTRUCTOR -> {
        require(this is InternalLinkTypeInvoice && b is InternalLinkTypeInvoice)
        if (COMPILE_CHECK) {
          InternalLinkTypeInvoice(
            this.invoiceName
          )
        }
        this.invoiceName == b.invoiceName
      }
      InternalLinkTypeLanguagePack.CONSTRUCTOR -> {
        require(this is InternalLinkTypeLanguagePack && b is InternalLinkTypeLanguagePack)
        if (COMPILE_CHECK) {
          InternalLinkTypeLanguagePack(
            this.languagePackId
          )
        }
        this.languagePackId == b.languagePackId
      }
      InternalLinkTypeMessage.CONSTRUCTOR -> {
        require(this is InternalLinkTypeMessage && b is InternalLinkTypeMessage)
        if (COMPILE_CHECK) {
          InternalLinkTypeMessage(
            this.url
          )
        }
        this.url == b.url
      }
      InternalLinkTypeMessageDraft.CONSTRUCTOR -> {
        require(this is InternalLinkTypeMessageDraft && b is InternalLinkTypeMessageDraft)
        if (COMPILE_CHECK) {
          InternalLinkTypeMessageDraft(
            this.text,
            this.containsLink
          )
        }
        this.containsLink == b.containsLink &&
        this.text.equalsTo(b.text)
      }
      InternalLinkTypePremiumFeatures.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumFeatures && b is InternalLinkTypePremiumFeatures)
        if (COMPILE_CHECK) {
          InternalLinkTypePremiumFeatures(
            this.referrer
          )
        }
        this.referrer == b.referrer
      }
      InternalLinkTypePremiumGiftCode.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumGiftCode && b is InternalLinkTypePremiumGiftCode)
        if (COMPILE_CHECK) {
          InternalLinkTypePremiumGiftCode(
            this.code
          )
        }
        this.code == b.code
      }
      InternalLinkTypeUserToken.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUserToken && b is InternalLinkTypeUserToken)
        if (COMPILE_CHECK) {
          InternalLinkTypeUserToken(
            this.token
          )
        }
        this.token == b.token
      }
      InternalLinkTypeStickerSet.CONSTRUCTOR -> {
        require(this is InternalLinkTypeStickerSet && b is InternalLinkTypeStickerSet)
        if (COMPILE_CHECK) {
          InternalLinkTypeStickerSet(
            this.stickerSetName,
            this.expectCustomEmoji
          )
        }
        this.stickerSetName == b.stickerSetName &&
        this.expectCustomEmoji == b.expectCustomEmoji
      }
      InternalLinkTypePassportDataRequest.CONSTRUCTOR -> {
        require(this is InternalLinkTypePassportDataRequest && b is InternalLinkTypePassportDataRequest)
        if (COMPILE_CHECK) {
          InternalLinkTypePassportDataRequest(
            this.botUserId,
            this.scope,
            this.publicKey,
            this.nonce,
            this.callbackUrl
          )
        }
        this.botUserId == b.botUserId &&
        this.scope == b.scope &&
        this.publicKey == b.publicKey &&
        this.nonce == b.nonce &&
        this.callbackUrl == b.callbackUrl
      }
      InternalLinkTypePhoneNumberConfirmation.CONSTRUCTOR -> {
        require(this is InternalLinkTypePhoneNumberConfirmation && b is InternalLinkTypePhoneNumberConfirmation)
        if (COMPILE_CHECK) {
          InternalLinkTypePhoneNumberConfirmation(
            this.hash,
            this.phoneNumber
          )
        }
        this.hash == b.hash &&
        this.phoneNumber == b.phoneNumber
      }
      InternalLinkTypeProxy.CONSTRUCTOR -> {
        require(this is InternalLinkTypeProxy && b is InternalLinkTypeProxy)
        if (COMPILE_CHECK) {
          InternalLinkTypeProxy(
            this.server,
            this.port,
            this.type
          )
        }
        this.server == b.server &&
        this.port == b.port &&
        this.type.equalsTo(b.type)
      }
      InternalLinkTypePublicChat.CONSTRUCTOR -> {
        require(this is InternalLinkTypePublicChat && b is InternalLinkTypePublicChat)
        if (COMPILE_CHECK) {
          InternalLinkTypePublicChat(
            this.chatUsername,
            this.draftText,
            this.openProfile
          )
        }
        this.chatUsername == b.chatUsername &&
        this.draftText == b.draftText &&
        this.chatUsername == b.chatUsername
      }
      InternalLinkTypeTheme.CONSTRUCTOR -> {
        require(this is InternalLinkTypeTheme && b is InternalLinkTypeTheme)
        if (COMPILE_CHECK) {
          InternalLinkTypeTheme(
            this.themeName
          )
        }
        this.themeName == b.themeName
      }
      InternalLinkTypeUnknownDeepLink.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUnknownDeepLink && b is InternalLinkTypeUnknownDeepLink)
        if (COMPILE_CHECK) {
          InternalLinkTypeUnknownDeepLink(
            this.link
          )
        }
        this.link == b.link
      }
      InternalLinkTypeUserPhoneNumber.CONSTRUCTOR -> {
        require(this is InternalLinkTypeUserPhoneNumber && b is InternalLinkTypeUserPhoneNumber)
        if (COMPILE_CHECK) {
          InternalLinkTypeUserPhoneNumber(
            this.phoneNumber,
            this.draftText,
            this.openProfile
          )
        }
        this.phoneNumber == b.phoneNumber &&
        this.draftText == b.draftText &&
        this.openProfile == b.openProfile
      }
      InternalLinkTypeVideoChat.CONSTRUCTOR -> {
        require(this is InternalLinkTypeVideoChat && b is InternalLinkTypeVideoChat)
        if (COMPILE_CHECK) {
          InternalLinkTypeVideoChat(
            this.chatUsername,
            this.inviteHash,
            this.isLiveStream
          )
        }
        this.chatUsername == b.chatUsername &&
        this.isLiveStream == b.isLiveStream &&
        this.inviteHash == b.inviteHash
      }
      InternalLinkTypeWebApp.CONSTRUCTOR -> {
        require(this is InternalLinkTypeWebApp && b is InternalLinkTypeWebApp)
        if (COMPILE_CHECK) {
          InternalLinkTypeWebApp(
            this.botUsername,
            this.webAppShortName,
            this.startParameter,
            this.isCompact
          )
        }
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.webAppShortName == b.webAppShortName &&
        this.isCompact == b.isCompact
      }
      InternalLinkTypeChatBoost.CONSTRUCTOR -> {
        require(this is InternalLinkTypeChatBoost && b is InternalLinkTypeChatBoost)
        if (COMPILE_CHECK) {
          InternalLinkTypeChatBoost(
            this.url
          )
        }
        this.url == b.url
      }
      InternalLinkTypeMainWebApp.CONSTRUCTOR -> {
        require(this is InternalLinkTypeMainWebApp && b is InternalLinkTypeMainWebApp)
        if (COMPILE_CHECK) {
          InternalLinkTypeMainWebApp(
            this.botUsername,
            this.startParameter,
            this.isCompact
          )
        }
        this.botUsername == b.botUsername &&
        this.startParameter == b.startParameter &&
        this.isCompact == b.isCompact
      }
      InternalLinkTypeStory.CONSTRUCTOR -> {
        require(this is InternalLinkTypeStory && b is InternalLinkTypeStory)
        if (COMPILE_CHECK) {
          InternalLinkTypeStory(
            this.storySenderUsername,
            this.storyId
          )
        }
        this.storySenderUsername == b.storySenderUsername && this.storyId == b.storyId
      }
      InternalLinkTypePremiumGift.CONSTRUCTOR -> {
        require(this is InternalLinkTypePremiumGift && b is InternalLinkTypePremiumGift)
        if (COMPILE_CHECK) {
          InternalLinkTypePremiumGift(
            this.referrer
          )
        }
        this.referrer == b.referrer
      }
      InternalLinkTypeBuyStars.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBuyStars && b is InternalLinkTypeBuyStars)
        if (COMPILE_CHECK) {
          InternalLinkTypeBuyStars(
            this.starCount,
            this.purpose
          )
        }
        this.starCount == b.starCount &&
        this.purpose == b.purpose
      }
      InternalLinkTypeBusinessChat.CONSTRUCTOR -> {
        require(this is InternalLinkTypeBusinessChat && b is InternalLinkTypeBusinessChat)
        if (COMPILE_CHECK) {
          InternalLinkTypeBusinessChat(
            this.linkName
          )
        }
        this.linkName == b.linkName
      }
      else -> {
        assertInternalLinkType_ff0c4471()
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
      InlineKeyboardButtonTypeCopyText.CONSTRUCTOR -> {
        require(this is InlineKeyboardButtonTypeCopyText && b is InlineKeyboardButtonTypeCopyText)
        this.text == b.text
      }
      InlineKeyboardButtonTypeCallbackGame.CONSTRUCTOR,
      InlineKeyboardButtonTypeBuy.CONSTRUCTOR -> true
      else -> {
        assertInlineKeyboardButtonType_4c981aa8()
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

fun LinkPreviewType?.equalsTo(b: LinkPreviewType?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        LinkPreviewTypeAlbum.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeAlbum && b is LinkPreviewTypeAlbum)
          TODO()
        }
        LinkPreviewTypeAnimation.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeAnimation && b is LinkPreviewTypeAnimation)
          TODO()
        }
        LinkPreviewTypeApp.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeApp && b is LinkPreviewTypeApp)
          TODO()
        }
        LinkPreviewTypeArticle.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeArticle && b is LinkPreviewTypeArticle)
          TODO()
        }
        LinkPreviewTypeAudio.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeAudio && b is LinkPreviewTypeAudio)
          TODO()
        }
        LinkPreviewTypeBackground.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeBackground && b is LinkPreviewTypeBackground)
          TODO()
        }
        LinkPreviewTypeChannelBoost.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeChannelBoost && b is LinkPreviewTypeChannelBoost)
          TODO()
        }
        LinkPreviewTypeChat.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeChat && b is LinkPreviewTypeChat)
          TODO()
        }
        LinkPreviewTypeDocument.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeDocument && b is LinkPreviewTypeDocument)
          TODO()
        }
        LinkPreviewTypeEmbeddedAnimationPlayer.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeEmbeddedAnimationPlayer && b is LinkPreviewTypeEmbeddedAnimationPlayer)
          TODO()
        }
        LinkPreviewTypeEmbeddedAudioPlayer.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeEmbeddedAudioPlayer && b is LinkPreviewTypeEmbeddedAudioPlayer)
          TODO()
        }
        LinkPreviewTypeEmbeddedVideoPlayer.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeEmbeddedVideoPlayer && b is LinkPreviewTypeEmbeddedVideoPlayer)
          TODO()
        }
        LinkPreviewTypeInvoice.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeInvoice && b is LinkPreviewTypeInvoice)
          TODO()
        }
        LinkPreviewTypeMessage.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeMessage && b is LinkPreviewTypeMessage)
          TODO()
        }
        LinkPreviewTypePhoto.CONSTRUCTOR -> {
          require(this is LinkPreviewTypePhoto && b is LinkPreviewTypePhoto)
          TODO()
        }
        LinkPreviewTypePremiumGiftCode.CONSTRUCTOR -> {
          require(this is LinkPreviewTypePremiumGiftCode && b is LinkPreviewTypePremiumGiftCode)
          TODO()
        }
        LinkPreviewTypeShareableChatFolder.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeShareableChatFolder && b is LinkPreviewTypeShareableChatFolder)
          TODO()
        }
        LinkPreviewTypeSticker.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeSticker && b is LinkPreviewTypeSticker)
          TODO()
        }
        LinkPreviewTypeStickerSet.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeStickerSet && b is LinkPreviewTypeStickerSet)
          TODO()
        }
        LinkPreviewTypeStory.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeStory && b is LinkPreviewTypeStory)
          TODO()
        }
        LinkPreviewTypeSupergroupBoost.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeSupergroupBoost && b is LinkPreviewTypeSupergroupBoost)
          TODO()
        }
        LinkPreviewTypeTheme.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeTheme && b is LinkPreviewTypeTheme)
          TODO()
        }
        LinkPreviewTypeUnsupported.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeUnsupported && b is LinkPreviewTypeUnsupported)
          TODO()
        }
        LinkPreviewTypeUser.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeUser && b is LinkPreviewTypeUser)
          TODO()
        }
        LinkPreviewTypeVideo.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeVideo && b is LinkPreviewTypeVideo)
          TODO()
        }
        LinkPreviewTypeVideoChat.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeVideoChat && b is LinkPreviewTypeVideoChat)
          TODO()
        }
        LinkPreviewTypeVideoNote.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeVideoNote && b is LinkPreviewTypeVideoNote)
          TODO()
        }
        LinkPreviewTypeVoiceNote.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeVoiceNote && b is LinkPreviewTypeVoiceNote)
          TODO()
        }
        LinkPreviewTypeWebApp.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeWebApp && b is LinkPreviewTypeWebApp)
          TODO()
        }
        LinkPreviewTypeExternalAudio.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeExternalAudio && b is LinkPreviewTypeExternalAudio)
          TODO()
        }
        LinkPreviewTypeExternalVideo.CONSTRUCTOR -> {
          require(this is LinkPreviewTypeExternalVideo && b is LinkPreviewTypeExternalVideo)
          TODO()
        }
        else -> {
          assertLinkPreviewType_eb86a63d()
          throw unsupported(this)
        }
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
        ReactionTypePaid.CONSTRUCTOR -> {
          require(this is ReactionTypePaid && b is ReactionTypePaid)
          true
        }
        else -> {
          assertReactionType_43844388()
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
      SuggestedActionExtendPremium.CONSTRUCTOR -> {
        require(this is SuggestedActionExtendPremium && b is SuggestedActionExtendPremium)
        if (COMPILE_CHECK) {
          SuggestedActionExtendPremium(this.managePremiumSubscriptionUrl)
        }
        this.managePremiumSubscriptionUrl == b.managePremiumSubscriptionUrl
      }
      SuggestedActionEnableArchiveAndMuteNewChats.CONSTRUCTOR,
      SuggestedActionCheckPassword.CONSTRUCTOR,
      SuggestedActionCheckPhoneNumber.CONSTRUCTOR,
      SuggestedActionViewChecksHint.CONSTRUCTOR,
      SuggestedActionUpgradePremium.CONSTRUCTOR,
      SuggestedActionRestorePremium.CONSTRUCTOR,
      SuggestedActionSubscribeToAnnualPremium.CONSTRUCTOR,
      SuggestedActionGiftPremiumForChristmas.CONSTRUCTOR,
      SuggestedActionSetBirthdate.CONSTRUCTOR,
      SuggestedActionExtendStarSubscriptions.CONSTRUCTOR -> true
      else -> {
        assertSuggestedAction_5c4efa90()
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

fun OptionValue?.equalsTo(b: OptionValue?): Boolean {
  return when {
    this === b -> true
    this == null || b == null || this.constructor != b.constructor -> false
    else -> {
      when (this.constructor) {
        OptionValueEmpty.CONSTRUCTOR -> true
        OptionValueBoolean.CONSTRUCTOR -> {
          require(this is OptionValueBoolean && b is OptionValueBoolean)
          if (COMPILE_CHECK) {
            OptionValueBoolean(this.value)
          }
          this.value == b.value
        }
        OptionValueInteger.CONSTRUCTOR -> {
          require(this is OptionValueInteger && b is OptionValueInteger)
          if (COMPILE_CHECK) {
            OptionValueInteger(this.value)
          }
          this.value == b.value
        }
        OptionValueString.CONSTRUCTOR -> {
          require(this is OptionValueString && b is OptionValueString)
          if (COMPILE_CHECK) {
            OptionValueString(this.value)
          }
          this.value == value
        }
        else -> {
          assertOptionValue_710db1a4()
          throw unsupported(this)
        }
      }
    }
  }
}