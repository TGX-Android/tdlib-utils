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

import org.drinkless.tdlib.TdApi.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@ExperimentalContracts
fun FormattedText?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return this == null || this.text.isNullOrEmpty()
}

@ExperimentalContracts
fun ChatFolderName?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return this == null || this.text.isEmpty()
}

@ExperimentalContracts
fun RichText?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  if (this == null) return true
  return when (this.constructor) {
    RichTextPlain.CONSTRUCTOR -> (this as RichTextPlain).text.isNullOrEmpty()
    RichTextIcon.CONSTRUCTOR, RichTextAnchor.CONSTRUCTOR, RichTextCustomEmoji.CONSTRUCTOR -> false
    RichTextBold.CONSTRUCTOR -> (this as RichTextBold).text.isEmpty()
    RichTextAnchorLink.CONSTRUCTOR -> (this as RichTextAnchorLink).text.isEmpty()
    RichTextEmailAddress.CONSTRUCTOR -> (this as RichTextEmailAddress).text.isEmpty()
    RichTextFixed.CONSTRUCTOR -> (this as RichTextFixed).text.isEmpty()
    RichTextItalic.CONSTRUCTOR -> (this as RichTextItalic).text.isEmpty()
    RichTextMarked.CONSTRUCTOR -> (this as RichTextMarked).text.isEmpty()
    RichTextPhoneNumber.CONSTRUCTOR -> (this as RichTextPhoneNumber).text.isEmpty()
    RichTextReference.CONSTRUCTOR -> (this as RichTextReference).text.isEmpty()
    RichTextStrikethrough.CONSTRUCTOR -> (this as RichTextStrikethrough).text.isEmpty()
    RichTextSubscript.CONSTRUCTOR -> (this as RichTextSubscript).text.isEmpty()
    RichTextSuperscript.CONSTRUCTOR -> (this as RichTextSuperscript).text.isEmpty()
    RichTextUnderline.CONSTRUCTOR -> (this as RichTextUnderline).text.isEmpty()
    RichTextUrl.CONSTRUCTOR -> (this as RichTextUrl).text.isEmpty()
    RichTextBankCardNumber.CONSTRUCTOR -> (this as RichTextBankCardNumber).text.isEmpty()
    RichTextBotCommand.CONSTRUCTOR -> (this as RichTextBotCommand).text.isEmpty()
    RichTextCashtag.CONSTRUCTOR -> (this as RichTextCashtag).text.isEmpty()
    RichTextDateTime.CONSTRUCTOR -> (this as RichTextDateTime).text.isEmpty()
    RichTextHashtag.CONSTRUCTOR -> (this as RichTextHashtag).text.isEmpty()
    RichTextMathematicalExpression.CONSTRUCTOR -> (this as RichTextMathematicalExpression).expression.isEmpty()
    RichTextMention.CONSTRUCTOR -> (this as RichTextMention).text.isEmpty()
    RichTextMentionName.CONSTRUCTOR -> (this as RichTextMentionName).text.isEmpty()
    RichTextReferenceLink.CONSTRUCTOR -> (this as RichTextReferenceLink).text.isEmpty()
    RichTextSpoiler.CONSTRUCTOR -> (this as RichTextSpoiler).text.isEmpty()
    RichTextDiff.CONSTRUCTOR -> {
      require(this is RichTextDiff)
      this.text.isEmpty() && this.oldText.isEmpty()
    }
    RichTexts.CONSTRUCTOR -> {
      for (childText in (this as RichTexts).texts) {
        if (!childText.isEmpty()) return false
      }
      true
    }
    else -> {
      assertRichText_d57ed958()
      throw unsupported(this)
    }
  }
}

@ExperimentalContracts
fun DraftMessage?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return (this == null) || (this.replyTo == null && this.content.let {
    when (it.constructor) {
      DraftMessageContentText.CONSTRUCTOR -> {
        require(it is DraftMessageContentText)
        it.text.isEmpty()
      }
      DraftMessageContentRichMessage.CONSTRUCTOR -> {
        require(it is DraftMessageContentRichMessage)
        it.message.blocks.isNullOrEmpty()
      }
      DraftMessageContentVideoNote.CONSTRUCTOR -> {
        require(it is DraftMessageContentVideoNote)
        it.filePath.isEmpty()
      }
      DraftMessageContentVoiceNote.CONSTRUCTOR -> {
        require(it is DraftMessageContentVoiceNote)
        it.filePath.isEmpty()
      }
      else -> {
        assertDraftMessageContent_b637f166()
        throw unsupported(this.content)
      }
    }
  })
}

@ExperimentalContracts
fun TextQuote?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return (this == null) || this.text.isEmpty()
}

@ExperimentalContracts
fun ChatFolderIcon?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return this?.name.isNullOrEmpty()
}

@ExperimentalContracts
fun InputTextQuote?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return (this == null) || this.text.isEmpty()
}

@ExperimentalContracts
fun StatisticalValue?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return (this == null) || (this.value == this.previousValue && this.value == 0.0)
}

fun AvailableReactions?.isEmpty (): Boolean {
  return this == null || (this.topReactions.isEmpty() && this.popularReactions.isEmpty() && this.recentReactions.isEmpty())
}

@JvmOverloads
fun Usernames?.isEmpty (checkDisabled: Boolean = false): Boolean {
  // ignored: this.editableUsername.isNullOrEmpty()
  return this == null || (this.activeUsernames.isNullOrEmpty() && (!checkDisabled || this.disabledUsernames.isNullOrEmpty()))
}

fun MessageReactions?.isEmpty (): Boolean {
  return this == null || this.reactions.isNullOrEmpty()
}