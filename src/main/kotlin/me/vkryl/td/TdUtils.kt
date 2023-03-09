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

import android.graphics.Path
import me.vkryl.core.UTF_8
import me.vkryl.core.limit
import me.vkryl.core.wrapHttps
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.*
import kotlin.contracts.ExperimentalContracts
import kotlin.math.max
import kotlin.math.min

fun LanguagePackInfo?.isLocal (): Boolean = this?.id!!.startsWith("X")
fun LanguagePackInfo?.isBeta (): Boolean = this?.isBeta ?: false
fun LanguagePackInfo?.isInstalled (): Boolean {
  return this?.let {
    !this.isOfficial && (this.isInstalled || this.isLocal())
  } ?: false
}

fun File?.getId (): Int = this?.id ?: 0

private fun stringOption (optionName: String): String? {
  val result = Client.execute(GetOption(optionName))
  return if (result is OptionValueString) {
    result.value
  } else {
    null
  }
}

fun tdlibVersion (): String? = stringOption("version")
fun tdlibCommitHashFull (): String? = stringOption("commit_hash")
fun tdlibCommitHash (): String? = tdlibCommitHashFull().limit(7)

fun String?.findEntities (predicate: (TextEntity) -> Boolean): Array<TextEntity>? {
  this?.isNotEmpty().let {
    val result = Client.execute(GetTextEntities(this))
    if (result is TextEntities && result.entities.isNotEmpty()) {
      return result.entities.filter(predicate).toTypedArray()
    }
  }
  return null
}

fun String?.findEntities (): Array<TextEntity>? {
  this?.isNotEmpty().let {
    val result = Client.execute(GetTextEntities(this))
    if (result is TextEntities && result.entities.isNotEmpty()) {
      return result.entities
    }
  }
  return null
}

fun FormattedText.addDefaultEntities () {
  when {
    this.entities.isNullOrEmpty() -> {
      this.entities = this.text.findEntities()
    }
    else -> {
      val entities = ArrayList<TextEntity>()
      var startIndex = 0
      for (entity in this.entities) {
        if (entity.offset > startIndex) {
          val found = this.text.substring(startIndex, entity.offset).findEntities()
          found?.let {
            for (foundEntity in found) {
              foundEntity.offset += startIndex
            }
            entities.addAll(found)
          }
          startIndex = entity.offset + entity.length
        }
      }
      if (startIndex < this.text.length) {
        val found = this.text.substring(startIndex).findEntities()
        found?.let {
          for (foundEntity in found) {
            foundEntity.offset += startIndex
          }
          entities.addAll(found)
        }
      }
      if (entities.isNotEmpty()) {
        entities.addAll(this.entities)
        entities.sort()
        this.entities = entities.toTypedArray()
      }
    }
  }
}

fun concat(vararg texts: FormattedText): FormattedText? {
  if (texts.isNullOrEmpty())
    return null
  var result: FormattedText? = null
  for (text in texts) {
    result = result?.concat(text) ?: text
  }
  return result
}

fun FormattedText.concat(text: FormattedText): FormattedText {
  val count1 = (this.entities?.size ?: 0)
  val count2 = (text.entities?.size ?: 0)
  val entities = when (count2) {
    0 -> this.entities
    else -> Array<TextEntity>(count1 + count2) { index ->
      when {
        index >= count1 -> {
          val entity = text.entities[index - count1]
          TextEntity(entity.offset + this.text.length, entity.length, entity.type)
        }
        else -> this.entities[index]
      }
    }
  }
  return FormattedText(this.text + text.text, entities)
}

@ExperimentalContracts
fun FormattedText?.trim (): FormattedText? {
  if (this == null || this.isEmpty())
    return this

  var startIndex = 0
  var endIndex = this.text.length - 1
  var startFound = false

  while (startIndex <= endIndex) {
    val index = if (!startFound) startIndex else endIndex
    val match = Character.isWhitespace(this.text[index])

    if (!startFound) {
      if (!match)
        startFound = true
      else
        startIndex += 1
    } else {
      if (!match)
        break
      else
        endIndex -= 1
    }
  }
  endIndex++

  return if (startIndex == 0 && endIndex == this.text.length) {
    this
  } else {
    this.substring(startIndex, endIndex)
  }
}

@JvmOverloads
fun FormattedText.ellipsize(maxCodePointCount: Int, ellipsis: String = "…"): FormattedText {
  var end = 0
  var codePointCount = 0
  while (end < this.text.length) {
    val codePoint = this.text.codePointAt(end)
    end += Character.charCount(codePoint)
    codePointCount++
    if (codePointCount == maxCodePointCount) {
      // Reached the limit.
      if (!this.entities.isNullOrEmpty()) {
        for (entity in this.entities) {
          if (entity.offset >= end)
            break
          if (entity.offset + entity.length < end)
            continue
          if (entity.offset < end && entity.offset + entity.length > end &&
              entity.type.constructor == TextEntityTypeCustomEmoji.CONSTRUCTOR) {
            // Make sure TextEntityTypeCustomEmoji doesn't get ellipsized in the middle
            end = entity.offset
            break
          }
        }
      }
      val result = this.substring(0, end)
      result.text += ellipsis
      return result
    }
  }
  return this
}

@JvmOverloads
fun FormattedText.substring(start: Int, end: Int = this.text.length): FormattedText {
  val entities = if (!this.entities.isNullOrEmpty()) {
    val resultLength = end - start
    var list : MutableList<TextEntity>? = null
    for (entity in entities) {
      if (entity.offset + entity.length <= start || entity.offset >= end) {
        continue
      }
      if (list == null) {
        list = ArrayList()
      }
      var offset = entity.offset - start
      var length = entity.length
      if (offset < 0) {
        length += offset
        offset = 0
      }
      if (offset + length > resultLength) {
        length -= (offset + length) - resultLength
      }
      list.add(TextEntity(offset, length, entity.type))
    }
    list?.toTypedArray()
  } else {
    emptyArray()
  }
  return FormattedText(this.text.substring(start, end), entities)
}

fun TextEntityType?.isEssential (): Boolean {
  return when (this?.constructor) {
    TextEntityTypeBankCardNumber.CONSTRUCTOR,
    TextEntityTypeHashtag.CONSTRUCTOR,
    TextEntityTypeCashtag.CONSTRUCTOR,
    TextEntityTypeMention.CONSTRUCTOR,
    TextEntityTypeUrl.CONSTRUCTOR,
    TextEntityTypeMediaTimestamp.CONSTRUCTOR -> false
    null -> false
    else -> true
  }
}

fun Array<TextEntity>?.findEssential (): Array<TextEntity>? {
  return when {
    this.isNullOrEmpty() -> this
    else -> {
      var filtered : MutableList<TextEntity>? = null
      var essentialCount = 0
      var rudimentaryCount = 0
      for ((index, entity) in this.withIndex()) {
        if (entity.type.isEssential()) {
          essentialCount++
          if (filtered == null && rudimentaryCount > 0) {
            filtered = ArrayList()
            for (i in 0 until index) {
              if (this[i].type.isEssential()) {
                filtered.add(this[i])
              }
            }
          }
          filtered?.add(entity)
        } else {
          rudimentaryCount++
          if (filtered == null && essentialCount > 0) {
            filtered = ArrayList()
            for (i in 0 until index) {
              if (this[i].type.isEssential()) {
                filtered.add(this[i])
              }
            }
          }
        }
      }
      when {
        filtered != null -> filtered.toTypedArray()
        rudimentaryCount == this.size -> null
        else -> this
      }
    }
  }
}

@ExperimentalContracts
fun FormattedText.parseMarkdown (): Boolean {
  val result = Client.execute(ParseMarkdown(this))
  return if (result is FormattedText && !result.equalsTo(this, true)) {
    this.text = result.text
    this.entities = result.entities
    true
  } else {
    false
  }
}

fun FormattedText?.hasLinks (): Boolean {
  if (this?.entities != null) {
    for (entity in this.entities) {
      when (entity.type.constructor) {
        TextEntityTypeUrl.CONSTRUCTOR,
        TextEntityTypeTextUrl.CONSTRUCTOR,
        TextEntityTypeEmailAddress.CONSTRUCTOR -> return true
      }
    }
  }
  return false
}

fun FormattedText?.getText (): String? {
  return if (this != null && !this.text.isNullOrEmpty()) {
    this.text
  } else {
    null
  }
}

fun MessageContent.isSecret (): Boolean {
  return when (this.constructor) {
    MessageAnimation.CONSTRUCTOR -> (this as MessageAnimation).isSecret
    MessagePhoto.CONSTRUCTOR -> (this as MessagePhoto).isSecret
    MessageVideo.CONSTRUCTOR -> (this as MessageVideo).isSecret
    MessageVideoNote.CONSTRUCTOR -> (this as MessageVideoNote).isSecret
    else -> false
  }
}

fun MessageSender?.getSenderUserId (): Long {
  return if (this?.constructor == MessageSenderUser.CONSTRUCTOR) {
    (this as MessageSenderUser).userId
  } else {
    0
  }
}

fun Message?.getSenderUserId (): Long {
  return this?.senderId.getSenderUserId()
}

@JvmOverloads
fun Message?.getMessageAuthorId(allowForward: Boolean = true): Long {
  if (this == null)
    return 0
  if (allowForward) {
    val forwardInfo = this.forwardInfo
    if (forwardInfo != null) {
      return when (forwardInfo.origin.constructor) {
        MessageForwardOriginUser.CONSTRUCTOR -> {
          fromUserId((forwardInfo.origin as MessageForwardOriginUser).senderUserId)
        }
        MessageForwardOriginChannel.CONSTRUCTOR -> {
          (forwardInfo.origin as MessageForwardOriginChannel).chatId
        }
        MessageForwardOriginChat.CONSTRUCTOR -> {
          (forwardInfo.origin as MessageForwardOriginChat).senderChatId
        }
        MessageForwardOriginHiddenUser.CONSTRUCTOR,
        MessageForwardOriginMessageImport.CONSTRUCTOR -> {
          0
        }
        else -> {
          TODO(forwardInfo.origin.toString())
        }
      }
    }
  }
  return when (this.senderId.constructor) {
    MessageSenderUser.CONSTRUCTOR -> fromUserId((this.senderId as MessageSenderUser).userId)
    MessageSenderChat.CONSTRUCTOR -> (this.senderId as MessageSenderChat).chatId
    else -> TODO(this.senderId.toString())
  }
}

fun MessageSender?.getSenderId (): Long {
  return if (this != null) {
    when (this.constructor) {
      MessageSenderUser.CONSTRUCTOR -> fromUserId((this as MessageSenderUser).userId)
      MessageSenderChat.CONSTRUCTOR -> (this as MessageSenderChat).chatId
      else -> TODO(this.toString())
    }
  } else {
    0
  }
}

fun Message?.getSenderId (): Long {
  return this?.senderId.getSenderId()
}

fun Sticker?.customEmojiId (): Long {
  return if (this != null && this.fullType.constructor == StickerFullTypeCustomEmoji.CONSTRUCTOR) {
    (this.fullType as StickerFullTypeCustomEmoji).customEmojiId
  } else {
    0
  }
}

fun AuthenticationCodeType.codeLength (fallbackCodeLength: Int): Int {
  return when (this.constructor) {
    AuthenticationCodeTypeTelegramMessage.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeTelegramMessage).length
    AuthenticationCodeTypeSms.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeSms).length
    AuthenticationCodeTypeFragment.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeFragment).length
    AuthenticationCodeTypeFirebaseAndroid.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeFirebaseAndroid).length
    AuthenticationCodeTypeFirebaseIos.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeFirebaseIos).length
    AuthenticationCodeTypeCall.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeCall).length
    AuthenticationCodeTypeMissedCall.CONSTRUCTOR ->
      (this as AuthenticationCodeTypeMissedCall).length
    AuthenticationCodeTypeFlashCall.CONSTRUCTOR ->
      fallbackCodeLength
    else ->
      TODO(this.toString())
  }
}
fun MessageContent?.textOrCaption (): FormattedText? {
  return when (this?.constructor) {
    MessageText.CONSTRUCTOR -> (this as MessageText).text
    MessageAnimatedEmoji.CONSTRUCTOR -> {
      require(this is MessageAnimatedEmoji)
      val customEmojiId = this.animatedEmoji.sticker.customEmojiId()
      if (customEmojiId != 0L) {
        FormattedText(this.emoji, arrayOf(
          TextEntity(0, this.emoji.length, TextEntityTypeCustomEmoji(customEmojiId))
        ))
      } else {
        FormattedText(this.emoji, arrayOf())
      }
    }
    MessagePhoto.CONSTRUCTOR -> (this as MessagePhoto).caption
    MessageVideo.CONSTRUCTOR -> (this as MessageVideo).caption
    MessageDocument.CONSTRUCTOR -> (this as MessageDocument).caption
    MessageAnimation.CONSTRUCTOR -> (this as MessageAnimation).caption
    MessageVoiceNote.CONSTRUCTOR -> (this as MessageVoiceNote).caption
    MessageAudio.CONSTRUCTOR -> (this as MessageAudio).caption
    else -> null
  }
}

fun Array<PhotoSize>.findSmallest (): PhotoSize? {
  return if (this.isNotEmpty()) {
    var photoSize: PhotoSize = this[0]
    for (i in 1 until this.size) {
      val it = this[i]
      if (it.width < photoSize.width || it.height < photoSize.height) {
        photoSize = it
      }
    }
    photoSize
  } else {
    null
  }
}

fun Photo?.findSmallest (): PhotoSize? = this?.sizes?.findSmallest()

fun Array<PhotoSize>.findBiggest (): PhotoSize? {
  return if (this.isNotEmpty()) {
    var photoSize: PhotoSize = this[0]
    for (i in 1 until this.size) {
      val it = this[i]
      if (it.width > photoSize.width || it.height > photoSize.height) {
        photoSize = it
      }
    }
    photoSize
  } else {
    null
  }
}

fun Photo?.findBiggest (): PhotoSize? = this?.sizes?.findBiggest()

fun MessageContent?.getTargetFileId (): Int {
  return when (this?.constructor) {
    MessageVideo.CONSTRUCTOR -> (this as MessageVideo).video.video.id
    MessageDocument.CONSTRUCTOR -> (this as MessageDocument).document.document.id
    MessageAnimation.CONSTRUCTOR -> (this as MessageAnimation).animation.animation.id
    MessageSticker.CONSTRUCTOR -> (this as MessageSticker).sticker.sticker.id
    MessageAudio.CONSTRUCTOR -> (this as MessageAudio).audio.audio.id
    MessageVideoNote.CONSTRUCTOR -> (this as MessageVideoNote).videoNote.video.id
    MessageVoiceNote.CONSTRUCTOR -> (this as MessageVoiceNote).voiceNote.voice.id
    MessagePhoto.CONSTRUCTOR -> {
      require(this is MessagePhoto)
      this.photo.sizes.findBiggest()?.photo?.id ?: 0
    }
    else -> 0
  }
}

fun Message?.matchesFilter(filter: SearchMessagesFilter?): Boolean {
  if (this == null || filter == null) {
    return true
  }
  return when (filter.constructor) {
    SearchMessagesFilterAnimation.CONSTRUCTOR -> this.content.constructor == MessageAnimation.CONSTRUCTOR
    SearchMessagesFilterDocument.CONSTRUCTOR -> this.content.constructor == MessageDocument.CONSTRUCTOR
    SearchMessagesFilterVoiceNote.CONSTRUCTOR -> this.content.constructor == MessageVoiceNote.CONSTRUCTOR
    SearchMessagesFilterVideo.CONSTRUCTOR -> this.content.constructor == MessageVideo.CONSTRUCTOR
    SearchMessagesFilterPhoto.CONSTRUCTOR -> this.content.constructor == MessagePhoto.CONSTRUCTOR
    SearchMessagesFilterAudio.CONSTRUCTOR -> this.content.constructor == MessageAudio.CONSTRUCTOR
    SearchMessagesFilterVideoNote.CONSTRUCTOR -> this.content.constructor == MessageVideoNote.CONSTRUCTOR
    SearchMessagesFilterChatPhoto.CONSTRUCTOR -> this.content.constructor == MessageChatChangePhoto.CONSTRUCTOR
    SearchMessagesFilterFailedToSend.CONSTRUCTOR -> this.sendingState?.constructor == MessageSendingStateFailed.CONSTRUCTOR
    SearchMessagesFilterPinned.CONSTRUCTOR -> this.isPinned
    SearchMessagesFilterUnreadMention.CONSTRUCTOR -> this.containsUnreadMention
    SearchMessagesFilterPhotoAndVideo.CONSTRUCTOR -> when (this.content.constructor) {
      MessagePhoto.CONSTRUCTOR, MessageVideo.CONSTRUCTOR -> true
      else -> false
    }
    SearchMessagesFilterVoiceAndVideoNote.CONSTRUCTOR -> when (this.content.constructor) {
      MessageVideoNote.CONSTRUCTOR, MessageVoiceNote.CONSTRUCTOR -> true
      else -> false
    }
    SearchMessagesFilterUrl.CONSTRUCTOR -> {
      (this.content.constructor == MessageText.CONSTRUCTOR && (this.content as MessageText).webPage != null) || this.content.textOrCaption().hasLinks()
    }
    SearchMessagesFilterEmpty.CONSTRUCTOR -> true
    SearchMessagesFilterMention.CONSTRUCTOR -> false // TODO
    else -> TODO(filter.toString())
  }
}

fun RichText.findReference(name: String): RichText? {
  return when (this.constructor) {
    RichTextPlain.CONSTRUCTOR, RichTextIcon.CONSTRUCTOR, RichTextAnchor.CONSTRUCTOR -> null
    RichTexts.CONSTRUCTOR -> {
      val texts = this as RichTexts
      if (texts.texts.size == 2 && texts.texts[0].constructor == RichTextAnchor.CONSTRUCTOR && (texts.texts[0] as RichTextAnchor).name == name) {
        texts.texts[1]
      } else {
        for (text in texts.texts) {
          val found = text.findReference(name)
          if (found != null)
            return found
        }
        null
      }
    }
    RichTextAnchorLink.CONSTRUCTOR -> (this as RichTextAnchorLink).text.findReference(name)
    RichTextBold.CONSTRUCTOR -> (this as RichTextBold).text.findReference(name)
    RichTextItalic.CONSTRUCTOR -> (this as RichTextItalic).text.findReference(name)
    RichTextUnderline.CONSTRUCTOR -> (this as RichTextUnderline).text.findReference(name)
    RichTextStrikethrough.CONSTRUCTOR -> (this as RichTextStrikethrough).text.findReference(name)
    RichTextFixed.CONSTRUCTOR -> (this as RichTextFixed).text.findReference(name)
    RichTextUrl.CONSTRUCTOR -> (this as RichTextUrl).text.findReference(name)
    RichTextEmailAddress.CONSTRUCTOR -> (this as RichTextEmailAddress).text.findReference(name)
    RichTextSubscript.CONSTRUCTOR -> (this as RichTextSubscript).text.findReference(name)
    RichTextSuperscript.CONSTRUCTOR -> (this as RichTextSuperscript).text.findReference(name)
    RichTextMarked.CONSTRUCTOR -> (this as RichTextMarked).text.findReference(name)
    RichTextPhoneNumber.CONSTRUCTOR -> (this as RichTextPhoneNumber).text.findReference(name)
    RichTextReference.CONSTRUCTOR -> (this as RichTextReference).text.findReference(name)
    else -> {
      TODO(this.toString())
    }
  }
}

fun PageBlock.findReference(name: String): RichText? {
  return when (this.constructor) {
    PageBlockAnchor.CONSTRUCTOR, PageBlockChatLink.CONSTRUCTOR, PageBlockDivider.CONSTRUCTOR -> null
    PageBlockCover.CONSTRUCTOR -> {
      (this as PageBlockCover).cover.findReference(name)
    }
    PageBlockAuthorDate.CONSTRUCTOR -> {
      (this as PageBlockAuthorDate).author.findReference(name)
    }
    PageBlockKicker.CONSTRUCTOR -> {
      (this as PageBlockKicker).kicker.findReference(name)
    }
    PageBlockTitle.CONSTRUCTOR -> {
      (this as PageBlockTitle).title.findReference(name)
    }
    PageBlockSubtitle.CONSTRUCTOR -> {
      (this as PageBlockSubtitle).subtitle.findReference(name)
    }
    PageBlockParagraph.CONSTRUCTOR -> {
      (this as PageBlockParagraph).text.findReference(name)
    }
    PageBlockPreformatted.CONSTRUCTOR -> {
      (this as PageBlockPreformatted).text.findReference(name)
    }
    PageBlockHeader.CONSTRUCTOR -> {
      (this as PageBlockHeader).header.findReference(name)
    }
    PageBlockSubheader.CONSTRUCTOR -> {
      (this as PageBlockSubheader).subheader.findReference(name)
    }
    PageBlockRelatedArticles.CONSTRUCTOR -> {
      (this as PageBlockRelatedArticles).header.findReference(name)
    }
    PageBlockFooter.CONSTRUCTOR -> {
      (this as PageBlockFooter).footer.findReference(name)
    }
    PageBlockAnimation.CONSTRUCTOR -> {
      val caption = (this as PageBlockAnimation).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockAudio.CONSTRUCTOR -> {
      val caption = (this as PageBlockAudio).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockVideo.CONSTRUCTOR -> {
      val caption = (this as PageBlockVideo).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockMap.CONSTRUCTOR -> {
      val caption = (this as PageBlockMap).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockPhoto.CONSTRUCTOR -> {
      val caption = (this as PageBlockPhoto).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockVoiceNote.CONSTRUCTOR -> {
      val caption = (this as PageBlockVoiceNote).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockEmbedded.CONSTRUCTOR -> {
      val caption = (this as PageBlockEmbedded).caption
      caption.text.findReference(name) ?: caption.credit.findReference(name)
    }
    PageBlockBlockQuote.CONSTRUCTOR -> {
      val quote = this as PageBlockBlockQuote
      quote.text.findReference(name) ?: quote.credit.findReference(name)
    }
    PageBlockPullQuote.CONSTRUCTOR -> {
      val quote = this as PageBlockPullQuote
      quote.text.findReference(name) ?: quote.credit.findReference(name)
    }
    PageBlockCollage.CONSTRUCTOR -> {
      val collage = this as PageBlockCollage
      for (pageBlock in collage.pageBlocks) {
        val reference = pageBlock.findReference(name)
        if (reference != null)
          return reference
      }
      collage.caption.text.findReference(name) ?: collage.caption.credit.findReference(name)
    }
    PageBlockSlideshow.CONSTRUCTOR -> {
      val slideshow = this as PageBlockSlideshow
      for (pageBlock in slideshow.pageBlocks) {
        val reference = pageBlock.findReference(name)
        if (reference != null)
          return reference
      }
      slideshow.caption.text.findReference(name) ?: slideshow.caption.credit.findReference(name)
    }
    PageBlockDetails.CONSTRUCTOR -> {
      val details = this as PageBlockDetails
      details.header.findReference(name) ?: let {
        for (pageBlock in details.pageBlocks) {
          val reference = pageBlock.findReference(name)
          if (reference != null)
            return reference
        }
        null
      }
    }
    PageBlockEmbeddedPost.CONSTRUCTOR -> {
      val post = this as PageBlockEmbeddedPost
      for (pageBlock in post.pageBlocks) {
        val reference = pageBlock.findReference(name)
        if (reference != null)
          return reference
      }
      post.caption.text.findReference(name) ?: post.caption.credit.findReference(name)
    }
    PageBlockList.CONSTRUCTOR -> {
      for (pageBlockListItem in (this as PageBlockList).items) {
        for (pageBlock in pageBlockListItem.pageBlocks) {
          val reference = pageBlock.findReference(name)
          if (reference != null)
            return reference
        }
      }
      null
    }
    PageBlockTable.CONSTRUCTOR -> {
      val table = this as PageBlockTable
      for (row in table.cells) {
        for (cell in row) {
          val reference = cell.text?.findReference(name)
          if (reference != null)
            return reference
        }
      }
      table.caption.findReference(name)
    }
    else -> TODO(this.toString())
  }
}

fun WebPageInstantView.findReference(name: String?): RichText? {
  if (name.isNullOrEmpty())
    return null
  for (pageBlock in this.pageBlocks) {
    val reference = pageBlock.findReference(name)
    if (reference != null)
      return reference
  }
  return null
}

fun StickerSet.toStickerSetInfo (): StickerSetInfo {
  return StickerSetInfo(
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
    this.isViewed,
    this.stickers.size,
    this.stickers
  )
}

fun Usernames?.primaryUsername (): String? {
  return if (this != null) {
    if (this.activeUsernames.isNotEmpty() && this.activeUsernames[0].isNotEmpty()) {
      this.activeUsernames[0]
    } else if (this.editableUsername != null && this.editableUsername.isNotEmpty()) {
      this.editableUsername
    } else {
      null
    }
  } else {
    null
  }
}

@JvmOverloads fun Usernames?.hasUsername (checkDisabled: Boolean = false): Boolean = !this.isEmpty(checkDisabled)

fun Supergroup?.primaryUsername (): String? = this?.usernames.primaryUsername()
fun User?.primaryUsername (): String? = this?.usernames.primaryUsername()

fun Supergroup?.hasUsername (): Boolean = this?.usernames.hasUsername()
fun User?.hasUsername (): Boolean = this?.usernames.hasUsername()

@JvmOverloads fun Usernames?.findUsername (username: String, allowDisabled: Boolean = false): Boolean {
  if (username.isNotEmpty() && this != null) {
    for (activeUsername in this.activeUsernames) {
      if (activeUsername.equals(username, ignoreCase = true)) {
        return true
      }
    }
    if (this.editableUsername.isNotEmpty() && this.editableUsername.equals(username, ignoreCase = true)) {
      return true
    }
    if (allowDisabled) {
      for (disabledUsername in this.disabledUsernames) {
        if (disabledUsername.equals(username, ignoreCase = true)) {
          return true
        }
      }
    }
  }
  return false
}

@JvmOverloads fun Supergroup?.findUsername (username: String, allowDisabled: Boolean = false): Boolean = this?.usernames.findUsername(username, allowDisabled)
@JvmOverloads fun User?.findUsername (username: String, allowDisabled: Boolean = false): Boolean = this?.usernames.findUsername(username, allowDisabled)

@JvmOverloads fun Usernames?.findUsernameByPrefix (usernamePrefix: String, allowDisabled: Boolean = false): Boolean {
  if (usernamePrefix.isNotEmpty() && this != null) {
    for (activeUsername in this.activeUsernames) {
      if (activeUsername.startsWith(usernamePrefix, ignoreCase = true)) {
        return true
      }
    }
    if (this.editableUsername.isNotEmpty() && this.editableUsername.startsWith(usernamePrefix, ignoreCase = true)) {
      return true
    }
    if (allowDisabled) {
      for (disabledUsername in this.disabledUsernames) {
        if (disabledUsername.startsWith(usernamePrefix, ignoreCase = true)) {
          return true
        }
      }
    }
  }
  return false
}

@JvmOverloads fun User?.findUsernameByPrefix (usernamePrefix: String, allowDisabled: Boolean = false): Boolean = this?.usernames.findUsernameByPrefix(usernamePrefix, allowDisabled)
@JvmOverloads fun Supergroup?.findUsernameByPrefix (usernamePrefix: String, allowDisabled: Boolean = false): Boolean = this?.usernames.findUsernameByPrefix(usernamePrefix, allowDisabled)

@JvmOverloads
fun buildOutline(sticker: Sticker?, targetWidth: Float, targetHeight: Float = targetWidth, out: Path? = null): Path? {
  return if (sticker != null) {
    buildOutline(sticker.outline, sticker.width, sticker.height, targetWidth, targetHeight, out)
  } else {
    out
  }
}

@JvmOverloads
fun buildOutline(contours: Array<ClosedVectorPath>?, sourceWidth: Int, sourceHeight: Int, targetWidth: Float, targetHeight: Float, out: Path? = null): Path? {
  if (contours.isNullOrEmpty()) return out
  val ratio = if (targetWidth == -1.0f && targetHeight == -1.0f) {
    1.0f
  } else {
    min(targetWidth / sourceWidth.toFloat(), targetHeight / sourceHeight.toFloat())
  }
  return when {
    ratio <= 0.0f -> out
    ratio == 1.0f -> buildOutline(contours, ratio, out = out)
    else -> {
      val displayWidth = sourceWidth.toFloat() * ratio
      val displayHeight = sourceHeight.toFloat() * ratio
      val offsetX = targetWidth / 2.0f - displayWidth / 2.0f
      val offsetY = targetHeight / 2.0f - displayHeight / 2.0f
      buildOutline(contours, ratio, offsetX, offsetY, out)
    }
  }
}

@JvmOverloads
fun buildOutline(contours: Array<ClosedVectorPath>?, ratio: Float, offsetX: Float = 0.0f, offsetY: Float = 0.0f, out: Path? = null): Path? {
  if (contours.isNullOrEmpty() || ratio <= 0) return out
  var path = out
  for (contour in contours) {
    if (contour.commands.isNullOrEmpty()) {
      continue
    }
    if (path == null) {
      path = Path()
    }
    val lastCommand = contour.commands[contour.commands.size - 1]
    val endPoint = when (lastCommand.constructor) {
      VectorPathCommandLine.CONSTRUCTOR -> (lastCommand as VectorPathCommandLine).endPoint
      VectorPathCommandCubicBezierCurve.CONSTRUCTOR -> (lastCommand as VectorPathCommandCubicBezierCurve).endPoint
      else -> TODO(lastCommand.toString())
    }
    path.moveTo(
      (endPoint.x * ratio).toFloat() + offsetX,
      (endPoint.y * ratio).toFloat() + offsetY
    )
    for (command in contour.commands) {
      when (command.constructor) {
        VectorPathCommandLine.CONSTRUCTOR -> {
          val line = command as VectorPathCommandLine
          path.lineTo(
            (line.endPoint.x * ratio).toFloat() + offsetX,
            (line.endPoint.y * ratio).toFloat() + offsetY
          )
        }
        VectorPathCommandCubicBezierCurve.CONSTRUCTOR -> {
          val curve = command as VectorPathCommandCubicBezierCurve
          path.cubicTo(
            (curve.startControlPoint.x * ratio).toFloat() + offsetX,
            (curve.startControlPoint.y * ratio).toFloat() + offsetY,
            (curve.endControlPoint.x * ratio).toFloat() + offsetX,
            (curve.endControlPoint.y * ratio).toFloat() + offsetY,
            (curve.endPoint.x * ratio).toFloat() + offsetX,
            (curve.endPoint.y * ratio).toFloat() + offsetY
          )
        }
        else -> TODO(command.toString())
      }
    }
  }
  return path
}

fun ChatMemberStatus?.isAnonymous (): Boolean {
  return when (this?.constructor) {
    ChatMemberStatusCreator.CONSTRUCTOR -> (this as ChatMemberStatusCreator).isAnonymous
    ChatMemberStatusAdministrator.CONSTRUCTOR -> (this as ChatMemberStatusAdministrator).rights.isAnonymous
    else -> false
  }
}

fun StickerFormat?.isAnimated (): Boolean {
  return when (this?.constructor) {
    StickerFormatTgs.CONSTRUCTOR,
    StickerFormatWebm.CONSTRUCTOR -> true
    StickerFormatWebp.CONSTRUCTOR -> false
    else -> TODO(this.toString())
  }
}

@JvmOverloads fun MessageReplyInfo?.hasUnread (lastGlobalReadInboxMessageId: Long = 0): Boolean {
  return this != null && this.lastMessageId != 0L && this.lastMessageId > max(lastGlobalReadInboxMessageId, this.lastReadInboxMessageId)
}

fun String.substring(entity: TextEntity?): String? {
  return entity?.let {
    this.substring(it.offset, it.offset + it.length)
  }
}

fun CharSequence.subSequence(entity: TextEntity?): CharSequence? {
  return entity?.let {
    this.subSequence(it.offset, it.offset + it.length)
  }
}

@kotlin.ExperimentalStdlibApi
fun FormattedText?.findUrl(lookupUrl: String, returnAny: Boolean): String? {
  if (this?.entities?.isNullOrEmpty() ?: return null)
    return null
  val lookupUri = wrapHttps(lookupUrl) ?: return null
  var count = 0
  var url: String? = null
  for (entity in this.entities) {
    url = when (entity.type.constructor) {
      TextEntityTypeUrl.CONSTRUCTOR -> {
        this.text.substring(entity)
      }
      TextEntityTypeTextUrl.CONSTRUCTOR -> {
        (entity.type as TextEntityTypeTextUrl).url
      }
      else -> continue
    }
    count++
    val uri = wrapHttps(url)
    if (uri != null && uri.buildUpon().fragment(null).build() == lookupUri) {
      return url
    }
  }
  return when {
    !returnAny -> lookupUrl
    count == 1 -> url
    else -> null
  }
}

fun ChatMemberStatus.getCustomTitle (): String? {
  return when (this.constructor) {
    ChatMemberStatusCreator.CONSTRUCTOR -> (this as ChatMemberStatusCreator).customTitle
    ChatMemberStatusAdministrator.CONSTRUCTOR -> (this as ChatMemberStatusAdministrator).customTitle
    else -> null
  }
}

fun ChatType.matchesScope(scope: NotificationSettingsScope): Boolean {
  return when (scope.constructor) {
    NotificationSettingsScopePrivateChats.CONSTRUCTOR -> this.constructor == ChatTypePrivate.CONSTRUCTOR || this.constructor == ChatTypeSecret.CONSTRUCTOR
    NotificationSettingsScopeGroupChats.CONSTRUCTOR -> this.constructor == ChatTypeBasicGroup.CONSTRUCTOR || (this.constructor == ChatTypeSupergroup.CONSTRUCTOR && !(this as ChatTypeSupergroup).isChannel)
    NotificationSettingsScopeChannelChats.CONSTRUCTOR -> this.constructor == ChatTypeSupergroup.CONSTRUCTOR && (this as ChatTypeSupergroup).isChannel
    else -> TODO(scope.toString())
  }
}

fun StickerFullType.toType (): StickerType {
  return when (this.constructor) {
    StickerFullTypeRegular.CONSTRUCTOR -> StickerTypeRegular()
    StickerFullTypeMask.CONSTRUCTOR -> StickerTypeMask()
    StickerFullTypeCustomEmoji.CONSTRUCTOR -> StickerTypeCustomEmoji()
    else -> TODO(this.toString())
  }
}

fun StickerFullType?.isPremium (): Boolean {
  return this != null && this.constructor == StickerFullTypeRegular.CONSTRUCTOR && (this as StickerFullTypeRegular).premiumAnimation != null
}

fun Sticker?.isPremium (): Boolean = this?.fullType.isPremium()

fun ChatPermissions.count (): Int {
  var count = 0
  if (this.canSendBasicMessages) count++
  if (this.canSendAudios) count++
  if (this.canSendDocuments) count++
  if (this.canSendPhotos) count++
  if (this.canSendVideos) count++
  if (this.canSendVideoNotes) count++
  if (this.canSendVoiceNotes) count++
  if (this.canSendPolls) count++
  if (this.canSendOtherMessages) count++
  if (this.canAddWebPagePreviews) count++
  if (this.canChangeInfo) count++
  if (this.canInviteUsers) count++
  if (this.canPinMessages) count++
  return count
}

fun ChatInviteLink.isTemporary (): Boolean = this.expirationDate != 0 || this.memberCount != 0 || this.memberLimit != 0

fun InlineKeyboardButtonTypeCallbackWithPassword.isBotOwnershipTransfer (): Boolean = try {
  String(this.data, UTF_8).matches(Regex("^bots/[0-9]+/trsf/.+$"))
} catch (e: IllegalArgumentException) {
  false
}

fun requiresPremiumSubscription (setting: UserPrivacySetting): Boolean {
  return setting.constructor == UserPrivacySettingAllowPrivateVoiceAndVideoNoteMessages.CONSTRUCTOR
}

fun PushMessageContent.getText (): String? {
  return when (this.constructor) {
    PushMessageContentText.CONSTRUCTOR ->
      (this as PushMessageContentText).text
    PushMessageContentAnimation.CONSTRUCTOR ->
      (this as PushMessageContentAnimation).caption
    PushMessageContentPhoto.CONSTRUCTOR ->
      (this as PushMessageContentPhoto).caption
    PushMessageContentVideo.CONSTRUCTOR ->
      (this as PushMessageContentVideo).caption
    // server doesn't send caption for these types, even though they have it
    PushMessageContentVoiceNote.CONSTRUCTOR,
    PushMessageContentAudio.CONSTRUCTOR,
    PushMessageContentDocument.CONSTRUCTOR ->
      null
    // types below do not have text or caption
    PushMessageContentHidden.CONSTRUCTOR,
    PushMessageContentContact.CONSTRUCTOR,
    PushMessageContentContactRegistered.CONSTRUCTOR,
    PushMessageContentGame.CONSTRUCTOR,
    PushMessageContentGameScore.CONSTRUCTOR,
    PushMessageContentInvoice.CONSTRUCTOR,
    PushMessageContentLocation.CONSTRUCTOR,
    PushMessageContentPoll.CONSTRUCTOR,
    PushMessageContentScreenshotTaken.CONSTRUCTOR,
    PushMessageContentSticker.CONSTRUCTOR,
    PushMessageContentVideoNote.CONSTRUCTOR,
    PushMessageContentBasicGroupChatCreate.CONSTRUCTOR,
    PushMessageContentChatAddMembers.CONSTRUCTOR,
    PushMessageContentChatChangePhoto.CONSTRUCTOR,
    PushMessageContentChatChangeTitle.CONSTRUCTOR,
    PushMessageContentChatSetTheme.CONSTRUCTOR,
    PushMessageContentChatDeleteMember.CONSTRUCTOR,
    PushMessageContentChatJoinByLink.CONSTRUCTOR,
    PushMessageContentChatJoinByRequest.CONSTRUCTOR,
    PushMessageContentRecurringPayment.CONSTRUCTOR,
    PushMessageContentMessageForwards.CONSTRUCTOR,
    PushMessageContentMediaAlbum.CONSTRUCTOR ->
      null
    // unsupported
    else ->
      TODO(this.toString())
  }
}

fun PushMessageContent.isPinned (): Boolean {
  return when (this.constructor) {
    PushMessageContentHidden.CONSTRUCTOR ->
      (this as PushMessageContentHidden).isPinned
    PushMessageContentAnimation.CONSTRUCTOR ->
      (this as PushMessageContentAnimation).isPinned
    PushMessageContentAudio.CONSTRUCTOR ->
      (this as PushMessageContentAudio).isPinned
    PushMessageContentContact.CONSTRUCTOR ->
      (this as PushMessageContentContact).isPinned
    PushMessageContentDocument.CONSTRUCTOR ->
      (this as PushMessageContentDocument).isPinned
    PushMessageContentGame.CONSTRUCTOR ->
      (this as PushMessageContentGame).isPinned
    PushMessageContentGameScore.CONSTRUCTOR ->
      (this as PushMessageContentGameScore).isPinned
    PushMessageContentInvoice.CONSTRUCTOR ->
      (this as PushMessageContentInvoice).isPinned
    PushMessageContentLocation.CONSTRUCTOR ->
      (this as PushMessageContentLocation).isPinned
    PushMessageContentPhoto.CONSTRUCTOR ->
      (this as PushMessageContentPhoto).isPinned
    PushMessageContentPoll.CONSTRUCTOR ->
      (this as PushMessageContentPoll).isPinned
    PushMessageContentSticker.CONSTRUCTOR ->
      (this as PushMessageContentSticker).isPinned
    PushMessageContentText.CONSTRUCTOR ->
      (this as PushMessageContentText).isPinned
    PushMessageContentVideo.CONSTRUCTOR ->
      (this as PushMessageContentVideo).isPinned
    PushMessageContentVideoNote.CONSTRUCTOR ->
      (this as PushMessageContentVideoNote).isPinned
    PushMessageContentVoiceNote.CONSTRUCTOR ->
      (this as PushMessageContentVoiceNote).isPinned
    // do not have `isPinned` field:
    PushMessageContentContactRegistered.CONSTRUCTOR,
    PushMessageContentScreenshotTaken.CONSTRUCTOR,
    PushMessageContentBasicGroupChatCreate.CONSTRUCTOR,
    PushMessageContentChatAddMembers.CONSTRUCTOR,
    PushMessageContentChatChangePhoto.CONSTRUCTOR,
    PushMessageContentChatChangeTitle.CONSTRUCTOR,
    PushMessageContentChatSetTheme.CONSTRUCTOR,
    PushMessageContentChatDeleteMember.CONSTRUCTOR,
    PushMessageContentChatJoinByLink.CONSTRUCTOR,
    PushMessageContentChatJoinByRequest.CONSTRUCTOR,
    PushMessageContentRecurringPayment.CONSTRUCTOR,
    PushMessageContentMessageForwards.CONSTRUCTOR,
    PushMessageContentMediaAlbum.CONSTRUCTOR ->
      false
    // unsupported
    else ->
      TODO(this.toString())
  }
}

fun AvailableReactions.isAvailable (reactionType: ReactionType): Boolean {
  if (isEmpty())
    return false
  for (availableReactionType in topReactions) {
    if (reactionType.equalsTo(availableReactionType.type))
      return true
  }
  for (availableReactionType in recentReactions) {
    if (reactionType.equalsTo(availableReactionType.type))
      return true
  }
  for (availableReactionType in popularReactions) {
    if (reactionType.equalsTo(availableReactionType.type))
      return true
  }
  return false
}

@JvmOverloads
fun newSendOptions (disableNotification: Boolean = false,
                    fromBackground: Boolean = false,
                    protectContent: Boolean = false,
                    updateOrderOfInstalledStickerSets: Boolean = false,
                    schedulingState: MessageSchedulingState? = null): MessageSendOptions {
  return MessageSendOptions(
    disableNotification,
    fromBackground,
    protectContent,
    updateOrderOfInstalledStickerSets,
    schedulingState,
    0
  )
}

@JvmOverloads
fun newSendOptions (options: MessageSendOptions?,
                    forceDisableNotifications: Boolean = false,
                    forceUpdateOrderOfInstalledStickerSets: Boolean = false,
                    updatedSchedulingState: MessageSchedulingState? = null): MessageSendOptions {
  return if (options != null) {
    newSendOptions(
      disableNotification = forceDisableNotifications || options.disableNotification,
      fromBackground = options.fromBackground,
      protectContent = options.protectContent,
      updateOrderOfInstalledStickerSets = forceUpdateOrderOfInstalledStickerSets || options.updateOrderOfInstalledStickerSets,
      schedulingState = updatedSchedulingState ?: options.schedulingState
    )
  } else {
    newSendOptions(
      disableNotification = forceDisableNotifications,
      fromBackground = false,
      protectContent = false,
      updateOrderOfInstalledStickerSets = forceUpdateOrderOfInstalledStickerSets,
      schedulingState = updatedSchedulingState
    )
  }
}

fun newSendOptions (options: MessageSendOptions?,
                    updatedSchedulingState: MessageSchedulingState?): MessageSendOptions =
  newSendOptions(
    options = options,
    forceDisableNotifications = false,
    forceUpdateOrderOfInstalledStickerSets = false,
    updatedSchedulingState = updatedSchedulingState
  )

fun newSendOptions (forceDisableNotification: Boolean, schedulingState: MessageSchedulingState?): MessageSendOptions =
  newSendOptions(
    options = null,
    forceDisableNotifications = forceDisableNotification,
    forceUpdateOrderOfInstalledStickerSets = false,
    updatedSchedulingState = schedulingState
  )

fun newSendOptions (schedulingState: MessageSchedulingState): MessageSendOptions =
  newSendOptions(
    options = null,
    forceDisableNotifications = false,
    forceUpdateOrderOfInstalledStickerSets = false,
    updatedSchedulingState = schedulingState
  )