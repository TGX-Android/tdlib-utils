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
fun RichText?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  if (this == null) return true
  return when (this.constructor) {
    RichTextPlain.CONSTRUCTOR -> (this as RichTextPlain).text.isNullOrEmpty()
    RichTextIcon.CONSTRUCTOR, RichTextAnchor.CONSTRUCTOR -> false
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
    RichTexts.CONSTRUCTOR -> {
      for (childText in (this as RichTexts).texts) {
        if (!childText.isEmpty()) return false
      }
      return true
    }
    else -> TODO(this.toString())
  }
}

@ExperimentalContracts
fun DraftMessage?.isEmpty (): Boolean {
  contract {
    returns(false) implies (this@isEmpty != null)
  }
  return (this == null) || (
    this.replyToMessageId == 0L &&
    (this.inputMessageText as InputMessageText).text.isEmpty()
  )
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