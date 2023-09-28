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

@file:JvmName("TdConstants")

package me.vkryl.td

const val MAX_USERNAME_LENGTH = 32
const val MAX_NAME_LENGTH = 64
const val MAX_CHANNEL_DESCRIPTION_LENGTH = 255
const val MAX_CHAT_TITLE_LENGTH = 128
const val MAX_POLL_QUESTION_LENGTH = 255
const val MAX_POLL_OPTION_LENGTH = 100
const val MAX_POLL_OPTION_COUNT = 10
const val MAX_CUSTOM_TITLE_LENGTH = 16
const val MAX_QUIZ_EXPLANATION_LENGTH = 200
const val DEFAULT_CODE_LENGTH = 5
const val MAX_MESSAGE_GROUP_SIZE = 10
const val ANIMATED_STICKER_MIME_TYPE = "application/x-tgsticker"
const val BACKGROUND_PATTERN_MIME_TYPE = "application/x-tgwallpattern"
const val TELEGRAM_ACCOUNT_ID = 777000L
const val TELEGRAM_CHANNEL_BOT_ACCOUNT_ID = 136817688L
const val TELEGRAM_REPLIES_BOT_ACCOUNT_ID = 1271266957L
const val TELEGRAM_ANIMATED_EMOJI_STICKER_SET_ID = 1258816259751983L
const val TELEGRAM_BOT_FATHER_ACCOUNT_ID = 93372553L
const val TELEGRAM_BOT_FATHER_USERNAME = "BotFather"
const val IV_PREVIEW_USERNAME = "iv"
const val CHAT_PERMISSIONS_COUNT = 13
const val MAX_CHAT_INVITE_LINK_USER_COUNT = 99999
const val MAX_CUSTOM_EMOJI_COUNT_PER_REQUEST = 200
const val MAX_MESSAGE_ENTITY_COUNT = 100
const val MAX_MESSAGE_CUSTOM_EMOJI_COUNT = 100
@JvmField val SLOW_MODE_OPTIONS = intArrayOf(0, 10, 30, 60, 300, 900, 3600)
@JvmField val CHAT_TTL_OPTIONS = intArrayOf(0, 86400, 604800, 2678400)
@JvmField val TME_HOSTS = arrayOf("t.me", "tx.me", "telegram.me", "telegram.dog")
@JvmField val FRAGMENT_HOST = "fragment.com"
@JvmField val TELEGRAM_HOSTS = arrayOf("telegram.org", "contest.com", "fragment.com")
@JvmField val TELEGRAPH_HOSTS = arrayOf("telegra.ph", "graph.org", "te.legra.ph")

internal const val COMPILE_CHECK = false