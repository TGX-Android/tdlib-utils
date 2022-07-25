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

@file:JvmName("JSON")

package me.vkryl.td

import me.vkryl.core.isEmpty
import me.vkryl.core.parseInt
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi.*

fun parse (json: String?): JsonValue? {
  if (isEmpty(json)) {
    return null
  }
  val result = Client.execute(GetJsonValue(json))
  return if (result is JsonValue) {
    result
  } else {
    null
  }
}

@Suppress("UNCHECKED_CAST")
fun toValue (value: Any?): JsonValue {
  return when (value) {
    is JsonValue -> value
    null -> JsonValueNull()
    is Number -> JsonValueNumber(value.toDouble())
    is String -> JsonValueString(value)
    is Boolean -> JsonValueBoolean(value)
    is Array<*> -> JsonValueArray(value.map { toValue(it) }.toTypedArray())
    is ArrayList<*> -> JsonValueArray(value.map { toValue(it) }.toTypedArray())
    is Map<*, *> -> toObject(value as Map<String, Any?>)
    else -> error("Unsupported type: ${value.javaClass.name}")
  }
}

fun toObject (members: List<JsonObjectMember>): JsonValueObject = JsonValueObject(members.toTypedArray())
fun toObject (members: Map<String, Any?>): JsonValueObject = toObject(members.map { JsonObjectMember(it.key, toValue(it.value)) })

fun asString (json: JsonValue?): String? = if (json is JsonValueString) json.value else null
fun asInt (json: JsonValue?): Int {
  return when (json) {
    is JsonValueNumber -> json.value.toInt()
    is JsonValueString -> parseInt(json.value)
    else -> 0
  }
}
fun asMap (json: JsonValue?): Map<String, JsonValue>? {
  if (json !is JsonValueObject)
    return null
  val map = HashMap<String, JsonValue>(json.members.size)
  for (member in json.members) {
    map[member.key] = member.value
  }
  return map
}

fun stringify (obj: JsonValue): String? {
  val result = Client.execute(GetJsonString(obj));
  return if (result is Text) {
    result.text
  } else {
    null
  }
}
fun stringify (members: List<JsonObjectMember>): String? = stringify(toObject(members))