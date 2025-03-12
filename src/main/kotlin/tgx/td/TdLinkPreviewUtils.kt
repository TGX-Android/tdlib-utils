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


fun LinkPreviewType.getDuration (): Int {
  return when (this.constructor) {
    LinkPreviewTypeAudio.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAudio)
      this.audio.duration
    }
    LinkPreviewTypeEmbeddedAnimationPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAnimationPlayer)
      this.duration
    }
    LinkPreviewTypeEmbeddedAudioPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAudioPlayer)
      this.duration
    }
    LinkPreviewTypeEmbeddedVideoPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedVideoPlayer)
      this.duration
    }
    LinkPreviewTypeVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideo)
      this.video.duration
    }
    LinkPreviewTypeExternalAudio.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeExternalAudio)
      this.duration
    }
    LinkPreviewTypeExternalVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeExternalVideo)
      this.duration
    }
    LinkPreviewTypeAlbum.CONSTRUCTOR,
    LinkPreviewTypeAnimation.CONSTRUCTOR,
    LinkPreviewTypeApp.CONSTRUCTOR,
    LinkPreviewTypeArticle.CONSTRUCTOR,
    LinkPreviewTypeBackground.CONSTRUCTOR,
    LinkPreviewTypeChannelBoost.CONSTRUCTOR,
    LinkPreviewTypeChat.CONSTRUCTOR,
    LinkPreviewTypeDocument.CONSTRUCTOR,
    LinkPreviewTypeInvoice.CONSTRUCTOR,
    LinkPreviewTypeMessage.CONSTRUCTOR,
    LinkPreviewTypePhoto.CONSTRUCTOR,
    LinkPreviewTypePremiumGiftCode.CONSTRUCTOR,
    LinkPreviewTypeShareableChatFolder.CONSTRUCTOR,
    LinkPreviewTypeSticker.CONSTRUCTOR,
    LinkPreviewTypeStickerSet.CONSTRUCTOR,
    LinkPreviewTypeStory.CONSTRUCTOR,
    LinkPreviewTypeSupergroupBoost.CONSTRUCTOR,
    LinkPreviewTypeTheme.CONSTRUCTOR,
    LinkPreviewTypeUnsupported.CONSTRUCTOR,
    LinkPreviewTypeUser.CONSTRUCTOR,
    LinkPreviewTypeUpgradedGift.CONSTRUCTOR,
    LinkPreviewTypeVideoChat.CONSTRUCTOR,
    LinkPreviewTypeVideoNote.CONSTRUCTOR,
    LinkPreviewTypeVoiceNote.CONSTRUCTOR,
    LinkPreviewTypeWebApp.CONSTRUCTOR -> 0
    else -> {
      assertLinkPreviewType_e4d80559()
      throw unsupported(this)
    }
  }
}

fun LinkPreviewType.getAnimation (): Animation? {
  return when (this.constructor) {
    LinkPreviewTypeAnimation.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAnimation)
      this.animation
    }
    else -> {
      assertLinkPreviewType_e4d80559()
      null
    }
  }
}

fun LinkPreviewType.getVideo (): Video? {
  return when (this.constructor) {
    LinkPreviewTypeVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideo)
      this.video
    }
    LinkPreviewTypeAlbum.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAlbum)
      this.media.firstOrNull()?.takeIf {
        it.constructor == LinkPreviewAlbumMediaVideo.CONSTRUCTOR
      }?.let {
        require(it is LinkPreviewAlbumMediaVideo)
        it.video
      }
    }
    else -> {
      assertLinkPreviewType_e4d80559()
      null
    }
  }
}

fun LinkPreviewType.getDocument (): Document? {
  return this.takeIf {
    it.constructor == LinkPreviewTypeDocument.CONSTRUCTOR
  }?.let {
    require(it is LinkPreviewTypeDocument)
    it.document
  }
}

fun LinkPreviewType.getAudio (): Audio? {
  return this.takeIf {
    it.constructor == LinkPreviewTypeAudio.CONSTRUCTOR
  }?.let {
    require(it is LinkPreviewTypeAudio)
    it.audio
  }
}

fun LinkPreviewType.getVoiceNote (): VoiceNote? {
  return this.takeIf {
    it.constructor == LinkPreviewTypeVoiceNote.CONSTRUCTOR
  }?.let {
    require(it is LinkPreviewTypeVoiceNote)
    it.voiceNote
  }
}

fun LinkPreviewType.getSticker (): Sticker? {
  return when (this.constructor) {
    LinkPreviewTypeSticker.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSticker)
      this.sticker
    }
    LinkPreviewTypeUpgradedGift.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUpgradedGift)
      this.gift.symbol.sticker
    }
    else -> {
      assertLinkPreviewType_e4d80559()
      null
    }
  }
}

fun LinkPreviewType.hasPhoto (): Boolean {
  return this.getPhoto() != null
}

fun LinkPreviewType.getPhoto (): Photo? {
  return when (this.constructor) {
    LinkPreviewTypeEmbeddedAnimationPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAnimationPlayer)
      this.thumbnail
    }
    LinkPreviewTypeEmbeddedAudioPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAudioPlayer)
      this.thumbnail
    }
    LinkPreviewTypeEmbeddedVideoPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedVideoPlayer)
      this.thumbnail
    }
    LinkPreviewTypeAlbum.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAlbum)
      this.media.firstOrNull()?.takeIf {
        it.constructor == LinkPreviewAlbumMediaPhoto.CONSTRUCTOR
      }?.let {
        require(it is LinkPreviewAlbumMediaPhoto)
        it.photo
      }
    }
    LinkPreviewTypeApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeApp)
      this.photo
    }
    LinkPreviewTypeArticle.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeArticle)
      this.photo
    }
    LinkPreviewTypeChannelBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChannelBoost)
      this.photo.toPhoto()
    }
    LinkPreviewTypeChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChat)
      this.photo.toPhoto()
    }
    LinkPreviewTypeSupergroupBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSupergroupBoost)
      this.photo.toPhoto()
    }
    LinkPreviewTypeUser.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUser)
      this.photo.toPhoto()
    }
    LinkPreviewTypeVideoChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideoChat)
      this.photo.toPhoto()
    }
    LinkPreviewTypePhoto.CONSTRUCTOR -> {
      require(this is LinkPreviewTypePhoto)
      this.photo
    }
    LinkPreviewTypeWebApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeWebApp)
      this.photo
    }
    LinkPreviewTypeAnimation.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAnimation)
      this.animation.thumbnail.toPhoto(this.animation.minithumbnail)
    }
    LinkPreviewTypeVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideo)
      this.cover ?: this.video.thumbnail.toPhoto(this.video.minithumbnail)
    }
    LinkPreviewTypeAudio.CONSTRUCTOR,
    LinkPreviewTypeBackground.CONSTRUCTOR,
    LinkPreviewTypeDocument.CONSTRUCTOR,
    LinkPreviewTypeInvoice.CONSTRUCTOR,
    LinkPreviewTypeMessage.CONSTRUCTOR,
    LinkPreviewTypePremiumGiftCode.CONSTRUCTOR,
    LinkPreviewTypeShareableChatFolder.CONSTRUCTOR,
    LinkPreviewTypeSticker.CONSTRUCTOR,
    LinkPreviewTypeStickerSet.CONSTRUCTOR,
    LinkPreviewTypeStory.CONSTRUCTOR,
    LinkPreviewTypeTheme.CONSTRUCTOR,
    LinkPreviewTypeUnsupported.CONSTRUCTOR,
    LinkPreviewTypeUpgradedGift.CONSTRUCTOR,
    LinkPreviewTypeVideoNote.CONSTRUCTOR,
    LinkPreviewTypeVoiceNote.CONSTRUCTOR,
    LinkPreviewTypeExternalAudio.CONSTRUCTOR,
    LinkPreviewTypeExternalVideo.CONSTRUCTOR -> null
    else -> {
      assertLinkPreviewType_e4d80559()
      throw unsupported(this)
    }
  }
}

fun LinkPreviewType.getThumbnail (): Thumbnail? {
  return when (this.constructor) {
    LinkPreviewTypeAudio.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAudio)
      this.audio?.albumCoverThumbnail
    }
    LinkPreviewTypeEmbeddedAnimationPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAnimationPlayer)
      this.thumbnail.toThumbnail()
    }
    LinkPreviewTypeEmbeddedAudioPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAudioPlayer)
      this.thumbnail.toThumbnail()
    }
    LinkPreviewTypeEmbeddedVideoPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedVideoPlayer)
      this.thumbnail.toThumbnail()
    }
    LinkPreviewTypeVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideo)
      this.video?.thumbnail
    }
    LinkPreviewTypeAlbum.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAlbum)
      this.media.firstOrNull()?.let {
        when (it.constructor) {
          LinkPreviewAlbumMediaPhoto.CONSTRUCTOR -> {
            require(it is LinkPreviewAlbumMediaPhoto)
            it.photo.toThumbnail()
          }
          LinkPreviewAlbumMediaVideo.CONSTRUCTOR -> {
            require(it is LinkPreviewAlbumMediaVideo)
            it.video.thumbnail
          }
          else -> {
            assertLinkPreviewAlbumMedia_8c33c943()
            throw unsupported(it)
          }
        }
      }
    }
    LinkPreviewTypeAnimation.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAnimation)
      this.animation.thumbnail
    }
    LinkPreviewTypeApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeApp)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeArticle.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeArticle)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeBackground.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeBackground)
      null
    }
    LinkPreviewTypeChannelBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChannelBoost)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChat)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeDocument.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeDocument)
      this.document.thumbnail
    }
    LinkPreviewTypeInvoice.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeInvoice)
      null
    }
    LinkPreviewTypeMessage.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeMessage)
      null
    }
    LinkPreviewTypePhoto.CONSTRUCTOR -> {
      require(this is LinkPreviewTypePhoto)
      this.photo.toThumbnail()
    }
    LinkPreviewTypePremiumGiftCode.CONSTRUCTOR -> {
      require(this is LinkPreviewTypePremiumGiftCode)
      null
    }
    LinkPreviewTypeShareableChatFolder.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeShareableChatFolder)
      null
    }
    LinkPreviewTypeSticker.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSticker)
      this.sticker.thumbnail
    }
    LinkPreviewTypeStickerSet.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeStickerSet)
      this.stickers.firstOrNull()?.thumbnail
    }
    LinkPreviewTypeStory.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeStory)
      null
    }
    LinkPreviewTypeSupergroupBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSupergroupBoost)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeTheme.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeTheme)
      null
    }
    LinkPreviewTypeUnsupported.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUnsupported)
      null
    }
    LinkPreviewTypeUpgradedGift.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUpgradedGift)
      this.gift.symbol.sticker.toThumbnail()
    }
    LinkPreviewTypeUser.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUser)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeVideoChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideoChat)
      this.photo.toThumbnail()
    }
    LinkPreviewTypeVideoNote.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideoNote)
      this.videoNote.thumbnail
    }
    LinkPreviewTypeVoiceNote.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVoiceNote)
      null
    }
    LinkPreviewTypeWebApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeWebApp)
      null
    }
    LinkPreviewTypeExternalAudio.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeExternalAudio)
      null
    }
    LinkPreviewTypeExternalVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeExternalVideo)
      null
    }
    else -> {
      assertLinkPreviewType_e4d80559()
      throw unsupported(this)
    }
  }
}

/*fun LinkPreviewType.getVideo (): Video? {
  return when (this.constructor) {
    LinkPreviewTypeAudio.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAudio)
      null
    }
    LinkPreviewTypeEmbeddedAnimationPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAnimationPlayer)
      null
    }
    LinkPreviewTypeEmbeddedAudioPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedAudioPlayer)
      null
    }
    LinkPreviewTypeEmbeddedVideoPlayer.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeEmbeddedVideoPlayer)
      null
    }
    LinkPreviewTypeVideo.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideo)
      null
    }
     LinkPreviewTypeAlbum.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAlbum)
      null
    }
     LinkPreviewTypeAnimation.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeAnimation)
      null
    }
     LinkPreviewTypeApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeApp)
      null
    }
     LinkPreviewTypeArticle.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeArticle)
      null
    }
     LinkPreviewTypeBackground.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeBackground)
      null
    }
     LinkPreviewTypeChannelBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChannelBoost)
      null
    }
     LinkPreviewTypeChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeChat)
      null
    }
     LinkPreviewTypeDocument.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeDocument)
      null
    }
     LinkPreviewTypeInvoice.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeInvoice)
      null
    }
     LinkPreviewTypeMessage.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeMessage)
      null
    }
     LinkPreviewTypePhoto.CONSTRUCTOR -> {
      require(this is LinkPreviewTypePhoto)
      null
    }
     LinkPreviewTypePremiumGiftCode.CONSTRUCTOR -> {
      require(this is LinkPreviewTypePremiumGiftCode)
      null
    }
     LinkPreviewTypeShareableChatFolder.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeShareableChatFolder)
      null
    }
     LinkPreviewTypeSticker.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSticker)
      null
    }
     LinkPreviewTypeStickerSet.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeStickerSet)
      null
    }
     LinkPreviewTypeStory.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeStory)
      null
    }
     LinkPreviewTypeSupergroupBoost.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeSupergroupBoost)
      null
    }
     LinkPreviewTypeTheme.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeTheme)
      null
    }
     LinkPreviewTypeUnsupported.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUnsupported)
      null
    }
     LinkPreviewTypeUser.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeUser)
      null
    }
     LinkPreviewTypeVideoChat.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideoChat)
      null
    }
     LinkPreviewTypeVideoNote.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVideoNote)
      null
    }
     LinkPreviewTypeVoiceNote.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeVoiceNote)
      null
    }
    LinkPreviewTypeWebApp.CONSTRUCTOR -> {
      require(this is LinkPreviewTypeWebApp)
      null
    }
    else -> {
      assertLinkPreviewType_fa8b177c()
      throw unsupported(this)
    }
  }
}*/
