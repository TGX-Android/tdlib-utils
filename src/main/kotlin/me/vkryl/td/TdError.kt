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

fun isDatabaseBrokenError (message: String): Boolean {
  return message.contains("Wrong key or database is corrupted") ||
    message.contains("SQL logic error or missing database") ||
    message.contains("database disk image is malformed") ||
    message.contains("file is encrypted or is not a database") ||
    message.contains("unsupported file format")
}

fun isDiskFullError (message: String): Boolean {
  return message.contains("PosixError : No space left on device") ||
    message.contains("database or disk is full")
}

fun isExternalError (message: String): Boolean {
  return isDatabaseBrokenError(message) ||
    isDiskFullError(message) ||
    message.contains("I/O error")
}

fun normalizeError (t: Throwable): Throwable {
  // TODO?
  return t
}

fun buildErrorMessage(prefix: String?, message: String?, clientCount: Long, stripPotentiallyPrivateData: Boolean): String? {
  val b = StringBuilder()
  if (prefix != null) {
    b.append(prefix)
  }
  if (clientCount > 0 && !stripPotentiallyPrivateData) {
    if (b.isNotEmpty()) {
      b.append(" ")
    }
    b.append("(").append(clientCount).append(")")
  }
  if (!message.isNullOrEmpty()) {
    if (b.isNotEmpty()) {
      b.append(": ")
    }
    if (stripPotentiallyPrivateData) {
      b.append(stripPrivateData(message))
    } else {
      b.append(message)
    }
  }
  return b.toString()
}

fun stripPrivateData (sourceMessage: String?): String? {
  var message = sourceMessage
  if (message.isNullOrEmpty()) return message
  var newStart = 0
  var tagStart = -1
  for (i in message.indices) {
    val c = message[i]
    if (c == '[') {
      tagStart = i
    } else if (c == ']' && tagStart != -1) {
      newStart = i + 1
      tagStart = -1
    } else if (tagStart == -1) {
      newStart = i
      if (!Character.isWhitespace(c)) {
        break
      }
    }
  }
  if (newStart > 0) {
    message = message.substring(newStart)
  }
  val b = StringBuilder(message.length)
  var quoteOpenIndex = -1
  var prevChar = 0.toChar()
  for (i in message.indices) {
    val c = message[i]
    if (c == '"' && prevChar != '\\') {
      if (quoteOpenIndex == -1) {
        quoteOpenIndex = i
      } else {
        b.append("STRING")
      }
    } else if (quoteOpenIndex == -1) {
      if (c == '@') {
        b.append("AT")
      } else if (c != '`') {
        b.append(c)
      }
    }
    prevChar = c
  }
  return b.toString().replace("[0-9]+".toRegex(), "X")
}

fun findSourceFileAndLineNumber (message: String?): StackTraceElement? {
  // sample message: [ 0][t 7][1663524892.910522937][StickersManager.cpp:327][#3][!Td]  Check `Unreacheable` failed
  if (message.isNullOrEmpty()) return null
  var tagCount = 0
  var tagStart = -1
  val regex = Regex("^[A-Za-z_0-9.]+[^.]\$")
  for (i in message.indices) {
    val c = message[i]
    if (c == '[') {
      tagStart = i
    } else if (c == ']') {
      if (tagStart != -1) {
        /*switch (tagCount) {
         case 0: break; // [ 0] — logging level
         case 1: break; // [t 7] — thread id
         case 2: break; // [1663524892.910522937] — time
         case 3: break; // [StickersManager.cpp:327] - file & line
         case 4: break; // [#3] — instance id
         case 5: break; // [!Td]
       }*/if (tagCount == 3) {
          // Extract everything between `[` and `]`
          var fileName = message.substring(tagStart + 1, i)
          var splitIndex = fileName.indexOf(':')
          var lineNumber = 0
          if (splitIndex != -1) {
            val lineNumberStr = fileName.substring(splitIndex + 1)
            fileName = fileName.substring(0, splitIndex)
            try {
              lineNumber = lineNumberStr.toInt()
            } catch (ignored: NumberFormatException) {
            }
          }
          if (fileName.matches(regex)) {
            splitIndex = fileName.indexOf('.')
            val declaringClass = fileName.substring(0, splitIndex)
            return StackTraceElement("libtdjni.so", declaringClass, "$declaringClass.java", lineNumber)
          }
          return null
        }
        tagStart = -1
        tagCount++
      }
    } else if (tagStart == -1) {
      if (!Character.isWhitespace(c)) {
        break
      }
    }
  }
  return null
}